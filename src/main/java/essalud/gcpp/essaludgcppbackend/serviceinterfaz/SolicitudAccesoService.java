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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        solicitudAcceso.setCodcas_solicitud(solicitudAccesoDto.getCodcas());
        solicitudAcceso.setCode_red_solicitud(solicitudAccesoDto.getCode_red());
        solicitudAcceso.setRol_solicitud(solicitudAccesoDto.getRol_solicitud());
        solicitudAcceso.setDescripcion(solicitudAccesoDto.getDescripcion());
        solicitudAcceso.setUser_solicitud(user);
        solicitudAcceso.setDate_creation(LocalDateTime.now());
        solicitudAcceso.setDate_update(LocalDateTime.now());
        solicitudAcceso.setStatus("PENDIENTE");
        solicitudAccesoRepository.save(solicitudAcceso);
    }

    public List<Map<String, Object>> getSolicitudAcceso() {
        return solicitudAccesoRepository.listSolicitudes();
    }

    public void updateSolicitudAcceso(SolicitudAccesoDto solicitudAccesoDto) {
        Users user = usersRepository.findByDni(solicitudAccesoDto.getDni());
        SolicitudAcceso solicitudAcceso = solicitudAccesoRepository.findById(solicitudAccesoDto.getId()).orElse(null);
        if (user != null && solicitudAcceso != null) {
            user.setCodcas(solicitudAccesoDto.getCodcas());
            user.setCode_red(solicitudAccesoDto.getCode_red());
            user.setDate_update(LocalDateTime.now());
            user.setFlag(true);
            usersRepository.save(user);
            solicitudAcceso.setStatus(solicitudAccesoDto.getStatus());
            solicitudAcceso.setDate_update(LocalDateTime.now());
            solicitudAccesoRepository.save(solicitudAcceso);
        }
    }
}
