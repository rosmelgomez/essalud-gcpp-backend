package essalud.gcpp.essaludgcppbackend.service.encuesta;

import essalud.gcpp.essaludgcppbackend.dtos.encuesta.CategoriaDto;
import essalud.gcpp.essaludgcppbackend.model.encuesta.Categoria;
import essalud.gcpp.essaludgcppbackend.repositories.encuesta.CategoriaRep;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriaServ {

    private final CategoriaRep CR;

    public CategoriaServ(CategoriaRep CR) {
        this.CR = CR;
    }

    public List<CategoriaDto> findAll() {
        List<Categoria> categorias = CR.findAll();
        List<CategoriaDto> categoriasDto = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Categoria categoria : categorias) {
            if(categoria.getActive()){
                CategoriaDto categoriaDto = modelMapper.map(categoria, CategoriaDto.class);
                categoriaDto.setId_modulo(categoria.getModulo_categoria().getId());
                categoriasDto.add(categoriaDto);
            }
        }
        return categoriasDto;
    }
}
