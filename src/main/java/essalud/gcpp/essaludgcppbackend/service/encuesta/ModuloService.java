package essalud.gcpp.essaludgcppbackend.service.encuesta;

import essalud.gcpp.essaludgcppbackend.dtos.encuesta.ModuloDto;
import essalud.gcpp.essaludgcppbackend.model.encuesta.Modulo;
import essalud.gcpp.essaludgcppbackend.repositories.encuesta.ModuloRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModuloService {

    private final ModuloRepository MR;
    public ModuloService(ModuloRepository MR) {
        this.MR = MR;
    }

    public List<ModuloDto> getModulos(){
        List<Modulo> modulos = MR.findAll();
        List<ModuloDto> modulosDto = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (Modulo modulo : modulos) {
            if (modulo.getActive()){
                modulosDto.add(modelMapper.map(modulo, ModuloDto.class));
            }
        }
        return modulosDto;
    }
}
