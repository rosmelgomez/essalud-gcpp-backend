package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.dtos.SolicitudAccesoDto;
import essalud.gcpp.essaludgcppbackend.model.SolicitudAcceso;
import essalud.gcpp.essaludgcppbackend.model.ResolucionSolicitudAcceso;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.acceso.SolicitudAccesoRep;
import essalud.gcpp.essaludgcppbackend.repositories.acceso.ResolucionSolicitudAccesoRep;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsersRep;
import essalud.gcpp.essaludgcppbackend.service.usuario.UsersServ;
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
public class SolicitudAccesoServ {

    private final SolicitudAccesoRep solicitudAccesoRep;
    private final UsersRep usersRep;
    private final UsersServ usersServ;
    private final ResolucionSolicitudAccesoRep resolucionSolicitudAccesoRep;

    public SolicitudAccesoServ(SolicitudAccesoRep solicitudAccesoRep, UsersRep usersRep, UsersServ usersServ, ResolucionSolicitudAccesoRep resolucionSolicitudAccesoRep) {
        this.solicitudAccesoRep = solicitudAccesoRep;
        this.usersRep = usersRep;
        this.usersServ = usersServ;
        this.resolucionSolicitudAccesoRep = resolucionSolicitudAccesoRep;
    }

    public void saveSolicitudAcceso(SolicitudAccesoDto solicitudAccesoDto) {
        Users user = usersRep.findByDni(solicitudAccesoDto.getDni());

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
            user.setFirst_Login(true);
            user.setDate_creation(LocalDateTime.now());
            user.setDate_update(null);
            user = usersRep.save(user);
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
        solicitudAccesoRep.save(solicitudAcceso);
    }

    public List<Map<String, Object>> getSolicitudAcceso() {
        return solicitudAccesoRep.listSolicitudes();
    }

    @Transactional
    public void updateSolicitudAcceso(SolicitudAccesoDto solicitudAccesoDto) {
        Users user = usersRep.findByDni(solicitudAccesoDto.getDni());
        SolicitudAcceso solicitudAcceso = solicitudAccesoRep.findById(solicitudAccesoDto.getId()).orElse(null);
        if (user != null && solicitudAcceso != null) {
            user.setCodcas(solicitudAccesoDto.getCodcas());
            user.setCodred(solicitudAccesoDto.getCode_red());
            user.setDate_update(LocalDateTime.now());

            // Solo activar al usuario si el estado de la solicitud es APROBADO
            if ("APROBADO".equalsIgnoreCase(solicitudAccesoDto.getStatus())) {
                user.setFlag(true);
                // Asignar el rol en user_rol
                usersServ.updateRolesUserByName(user, List.of(solicitudAccesoDto.getRol_solicitud()));
            } else if ("RECHAZADO".equalsIgnoreCase(solicitudAccesoDto.getStatus())) {
                user.setFlag(false);
            }

            usersRep.save(user);

            solicitudAcceso.setStatus(solicitudAccesoDto.getStatus());
            solicitudAcceso.setRol_solicitud(solicitudAccesoDto.getRol_solicitud());
            solicitudAcceso.setCodcas_solicitud(solicitudAccesoDto.getCodcas());
            solicitudAcceso.setCode_red_solicitud(solicitudAccesoDto.getCode_red());
            solicitudAcceso.setDate_update(LocalDateTime.now());
            solicitudAccesoRep.save(solicitudAcceso);

            // Registrar la resolución de solicitud si se aprueba o rechaza
            if ("APROBADO".equalsIgnoreCase(solicitudAccesoDto.getStatus()) || "RECHAZADO".equalsIgnoreCase(solicitudAccesoDto.getStatus())) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String adminUsername = null;
                if (principal instanceof UserDetails) {
                    adminUsername = ((UserDetails) principal).getUsername();
                } else if (principal != null) {
                    adminUsername = principal.toString();
                }
                Users adminRevisor = adminUsername != null ? usersRep.findByUsername(adminUsername) : null;

                if (adminRevisor != null) {
                    ResolucionSolicitudAcceso rsa = resolucionSolicitudAccesoRep
                            .findBySolicitudAccesoId(solicitudAcceso.getId()).orElse(null);
                    if (rsa == null) {
                        rsa = new ResolucionSolicitudAcceso();
                        rsa.setSolicitudAcceso(solicitudAcceso);
                    }
                    rsa.setUserRevisor(adminRevisor);
                    rsa.setStatus(solicitudAccesoDto.getStatus());
                    rsa.setComentarioRevisor(solicitudAccesoDto.getComentario_revisor());
                    rsa.setDateResolution(LocalDateTime.now());
                    resolucionSolicitudAccesoRep.save(rsa);
                }
            }
        }
    }


}
