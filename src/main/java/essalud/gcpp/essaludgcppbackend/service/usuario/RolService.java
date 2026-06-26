package essalud.gcpp.essaludgcppbackend.service.usuario;

import essalud.gcpp.essaludgcppbackend.dtos.usuario.RolDto;
import essalud.gcpp.essaludgcppbackend.model.usuario.Rol;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.RolRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RolService {

    private final RolRepository rolRepository;
    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<RolDto> getRols() {
        List<Rol> rols = rolRepository.findAll();
        List<RolDto> rolDtos = new ArrayList<>();
        for (Rol rol : rols) {
            rolDtos.add(rolDto(rol));
        }
        return rolDtos;
    }


    private RolDto rolDto(Rol rol) {
        RolDto rolDto = new RolDto();
        rolDto.setId(rol.getId());
        rolDto.setName(rol.getName());
        rolDto.setDescription(rol.getDescription());
        rolDto.setDate_creation(rol.getDate_creation());
        rolDto.setDate_update(rol.getDate_update());
        rolDto.setFlag(rol.getFlag());
        return rolDto;
    }
}
