package essalud.gcpp.essaludgcppbackend.service.encuesta;

import essalud.gcpp.essaludgcppbackend.dtos.encuesta.EncuestaDto;
import essalud.gcpp.essaludgcppbackend.model.encuesta.*;
import essalud.gcpp.essaludgcppbackend.model.usuario.Usuario;
import essalud.gcpp.essaludgcppbackend.repositories.encuesta.*;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsuarioRep;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EncuestaServ {

    private final UsuarioRep UR;

    private final ModuloRep MR;

    private final CategoriaRep CR;

    private final SubCategoriaRep SCR;

    private final VariableRep VAR;

    private final EncuestaRep ER;
    public EncuestaServ(UsuarioRep UR, ModuloRep MR, CategoriaRep CR, SubCategoriaRep SCR, VariableRep VAR, EncuestaRep ER) {
        this.UR = UR;
        this.MR = MR;
        this.CR = CR;
        this.SCR = SCR;
        this.VAR = VAR;
        this.ER = ER;
    }

    public void registrarEncuesta(EncuestaDto encuestaDto) {
        Usuario user = UR.findById(encuestaDto.getId_user()).orElse(null);
        Modulo modulo = MR.findById(encuestaDto.getId_modulo()).orElse(null);
        Categoria categoria = CR.findById(encuestaDto.getId_categoria()).orElse(null);
        SubCategoria subCategoria = SCR.findById(encuestaDto.getId_subcategoria()).orElse(null);
        Variable variable = VAR.findById(encuestaDto.getId_variable()).orElse(null);

        Encuesta encuesta = new Encuesta();
        encuesta.setCodcas(encuestaDto.getCodcas());
        encuesta.setStatus(encuestaDto.getStatus());
        encuesta.setComment(encuestaDto.getComment());
        encuesta.setDate_creation(LocalDateTime.now());
        encuesta.setUser_encuesta(user);
        encuesta.setModulo_encuesta(modulo);
        encuesta.setCategoria_encuesta(categoria);
        encuesta.setSubcategoria_encuesta(subCategoria);
        encuesta.setVariable_encuesta(variable);
        ER.save(encuesta);
    }

    public List<EncuestaDto> getEncuestas() {
        List<Encuesta> encuestas = ER.findAll();
        List<EncuestaDto> encuestasDto = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();
        for (Encuesta encuesta : encuestas) {
            EncuestaDto encuestaDto = mapper.map(encuesta, EncuestaDto.class);
            encuestaDto.setId_user(encuesta.getUser_encuesta().getId());
            encuestaDto.setId_categoria(encuesta.getCategoria_encuesta().getId());
            encuestaDto.setId_modulo(encuesta.getModulo_encuesta().getId());
            encuestaDto.setId_subcategoria(encuesta.getSubcategoria_encuesta().getId());
            encuestaDto.setId_variable(encuesta.getVariable_encuesta().getId());
            encuestasDto.add(encuestaDto);
        }
        return encuestasDto;
    }

}
