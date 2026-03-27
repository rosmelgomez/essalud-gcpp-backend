package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.dtos.SolicitudAccesoDto;
import essalud.gcpp.essaludgcppbackend.dtos.UsersDto;
import essalud.gcpp.essaludgcppbackend.model.Rol;
import essalud.gcpp.essaludgcppbackend.model.SolicitudAcceso;
import essalud.gcpp.essaludgcppbackend.model.UserRol;
import essalud.gcpp.essaludgcppbackend.model.Users;
import essalud.gcpp.essaludgcppbackend.repositories.SolicitudAccesoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.UsersRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SolicitudAccesoService {

    private final SolicitudAccesoRepository solicitudAccesoRepository;
    private final UsersRepository usersRepository;

    public SolicitudAccesoService(SolicitudAccesoRepository solicitudAccesoRepository, UsersRepository usersRepository ) {
        this.solicitudAccesoRepository = solicitudAccesoRepository;
        this.usersRepository = usersRepository;
    }

    public void saveSolicitudAcceso(SolicitudAccesoDto solicitudAccesoDto) {
        Users user = usersRepository.findByDni(solicitudAccesoDto.getDni());

        if (user == null) {
            user = new Users();
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode("123456789");

            user.setName(solicitudAccesoDto.getName());
            user.setLastname(solicitudAccesoDto.getLastname());
            user.setEmail(solicitudAccesoDto.getEmail());
            user.setDni(solicitudAccesoDto.getDni());
            user.setUsername(solicitudAccesoDto.getName() + "." + solicitudAccesoDto.getLastname().replace(" ", ""));
            user.setPassword(encodedPassword);
            user.setDate_creation(LocalDateTime.now());
            user.setFlag(false);
            user.setDate_creation(LocalDateTime.now());
            user.setDate_update(LocalDateTime.now());
            user = usersRepository.save(user);
        }

        SolicitudAcceso solicitudAcceso = new SolicitudAcceso();
        solicitudAcceso.setDescripcion(solicitudAccesoDto.getDescripcion());
        solicitudAcceso.setUser_solicitud(user);
        solicitudAcceso.setDate_creation(LocalDateTime.now());
        solicitudAcceso.setDate_update(LocalDateTime.now());
        solicitudAcceso.setStatus(solicitudAccesoDto.getStatus());
        solicitudAccesoRepository.save(solicitudAcceso);
    }


}
