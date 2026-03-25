package essalud.gcpp.essaludgcppbackend.serviceinterfaz.encuesta;

import essalud.gcpp.essaludgcppbackend.dtos.encuesta.SubCategoriaDto;
import essalud.gcpp.essaludgcppbackend.model.encuesta.SubCategoria;
import essalud.gcpp.essaludgcppbackend.repositories.encuesta.SubCategoriaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubCategoriaService {

    private final SubCategoriaRepository SCR;
    public SubCategoriaService(SubCategoriaRepository SCR) {
        this.SCR = SCR;
    }

    public List<SubCategoriaDto> getSubCategorias() {
        List<SubCategoria>  subCategorias = SCR.findAll();
        List<SubCategoriaDto> subCategoriasDto = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (SubCategoria subCategoria : subCategorias) {
            if (subCategoria.getActive()){
                SubCategoriaDto subCategoriaDto = modelMapper.map(subCategoria, SubCategoriaDto.class);
                subCategoriaDto.setId_categoria(subCategoria.getCategoria_subcategoria().getId());
                subCategoriasDto.add(subCategoriaDto);
            }
        }
        return subCategoriasDto;
    }

}
