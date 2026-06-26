package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.dtos.SolicitudAccesoDto;
import essalud.gcpp.essaludgcppbackend.model.SolicitudAcceso;
import essalud.gcpp.essaludgcppbackend.model.ResolucionSolicitudAcceso;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.acceso.SolicitudAccesoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.acceso.ResolucionSolicitudAccesoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsersRepository;
import essalud.gcpp.essaludgcppbackend.service.usuario.UsersService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class SolicitudAccesoService {

    private final SolicitudAccesoRepository solicitudAccesoRepository;
    private final UsersRepository usersRepository;
    private final UsersService usersService;
    private final ResolucionSolicitudAccesoRepository resolucionSolicitudAccesoRepository;

    public SolicitudAccesoService(SolicitudAccesoRepository solicitudAccesoRepository, 
                                  UsersRepository usersRepository,
                                  UsersService usersService,
                                  ResolucionSolicitudAccesoRepository resolucionSolicitudAccesoRepository) {
        this.solicitudAccesoRepository = solicitudAccesoRepository;
        this.usersRepository = usersRepository;
        this.usersService = usersService;
        this.resolucionSolicitudAccesoRepository = resolucionSolicitudAccesoRepository;
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
            user.setFirstLogin(true);
            user.setDate_creation(LocalDateTime.now());
            user.setDate_update(null);
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

    @Transactional
    public void updateSolicitudAcceso(SolicitudAccesoDto solicitudAccesoDto) {
        Users user = usersRepository.findByDni(solicitudAccesoDto.getDni());
        SolicitudAcceso solicitudAcceso = solicitudAccesoRepository.findById(solicitudAccesoDto.getId()).orElse(null);
        if (user != null && solicitudAcceso != null) {
            user.setCodcas(solicitudAccesoDto.getCodcas());
            user.setCodred(solicitudAccesoDto.getCode_red());
            user.setDate_update(LocalDateTime.now());

            // Solo activar al usuario si el estado de la solicitud es APROBADO
            if ("APROBADO".equalsIgnoreCase(solicitudAccesoDto.getStatus())) {
                user.setFlag(true);
                // Asignar el rol en user_rol
                usersService.updateRolesUserByName(user, List.of(solicitudAccesoDto.getRol_solicitud()));
            } else if ("RECHAZADO".equalsIgnoreCase(solicitudAccesoDto.getStatus())) {
                user.setFlag(false);
            }

            usersRepository.save(user);

            solicitudAcceso.setStatus(solicitudAccesoDto.getStatus());
            solicitudAcceso.setRol_solicitud(solicitudAccesoDto.getRol_solicitud());
            solicitudAcceso.setCodcas_solicitud(solicitudAccesoDto.getCodcas());
            solicitudAcceso.setCode_red_solicitud(solicitudAccesoDto.getCode_red());
            solicitudAcceso.setDate_update(LocalDateTime.now());
            solicitudAccesoRepository.save(solicitudAcceso);

            // Registrar la resolución de solicitud si se aprueba o rechaza
            if ("APROBADO".equalsIgnoreCase(solicitudAccesoDto.getStatus()) || "RECHAZADO".equalsIgnoreCase(solicitudAccesoDto.getStatus())) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String adminUsername = null;
                if (principal instanceof UserDetails) {
                    adminUsername = ((UserDetails) principal).getUsername();
                } else if (principal != null) {
                    adminUsername = principal.toString();
                }
                Users adminRevisor = adminUsername != null ? usersRepository.findByUsername(adminUsername) : null;

                if (adminRevisor != null) {
                    ResolucionSolicitudAcceso rsa = resolucionSolicitudAccesoRepository
                            .findBySolicitudAccesoId(solicitudAcceso.getId()).orElse(null);
                    if (rsa == null) {
                        rsa = new ResolucionSolicitudAcceso();
                        rsa.setSolicitudAcceso(solicitudAcceso);
                    }
                    rsa.setUserRevisor(adminRevisor);
                    rsa.setStatus(solicitudAccesoDto.getStatus());
                    rsa.setComentarioRevisor(solicitudAccesoDto.getComentario_revisor());
                    rsa.setDateResolution(LocalDateTime.now());
                    resolucionSolicitudAccesoRepository.save(rsa);
                }
            }
        }
    }
}
