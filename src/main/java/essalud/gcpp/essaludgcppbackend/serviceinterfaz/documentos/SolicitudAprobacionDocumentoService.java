package essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.SolicitudAprobacionDocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.documentos.SolicitudAprobacionDocumento;
import essalud.gcpp.essaludgcppbackend.model.documentos.ResolucionAprobacionDocumento;
import essalud.gcpp.essaludgcppbackend.model.documentos.HistorialAprobacionDocumento;
import essalud.gcpp.essaludgcppbackend.model.documentos.VersionDocumento;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.SolicitudAprobacionDocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.ResolucionAprobacionDocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.HistorialAprobacionDocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsersRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
public class SolicitudAprobacionDocumentoService {

    private final SolicitudAprobacionDocumentoRepository sadr;
    private final ResolucionAprobacionDocumentoRepository radr;
    private final UsersRepository usersRepository;
    private final HistorialAprobacionDocumentoRepository historialAprobacionDocumentoRepository;

    public SolicitudAprobacionDocumentoService(SolicitudAprobacionDocumentoRepository sadr, ResolucionAprobacionDocumentoRepository radr, UsersRepository usersRepository, HistorialAprobacionDocumentoRepository historialAprobacionDocumentoRepository) {
        this.sadr = sadr;
        this.radr = radr;
        this.usersRepository = usersRepository;
        this.historialAprobacionDocumentoRepository = historialAprobacionDocumentoRepository;
    }

    private LocalDateTime calcularFechaLimite(LocalDateTime fechaInicio) {
        LocalDateTime limite = fechaInicio;
        int diasHabilitados = 0;
        while (diasHabilitados < 3) {
            limite = limite.plusDays(1);
            if (limite.getDayOfWeek() != java.time.DayOfWeek.SATURDAY
                    && limite.getDayOfWeek() != java.time.DayOfWeek.SUNDAY) {
                diasHabilitados++;
            }
        }
        return limite;
    }

    public void saveFlujoAprobacionDocumento(Integer id_user_aprobador, VersionDocumento versionDocumento, String prioridad, String comentarioSolicitud) throws IOException {
        Users aprobador = usersRepository.findById(id_user_aprobador).orElse(null);
        Users solicitante = versionDocumento.getUser_version_documento();

        if (aprobador != null && solicitante != null) {
            SolicitudAprobacionDocumento sad = new SolicitudAprobacionDocumento();
            sad.setComentario_solicitante(comentarioSolicitud != null ? comentarioSolicitud : "");
            sad.setEstado("Pendiente");
            sad.setPrioridad(prioridad != null ? prioridad : "MEDIA");
            sad.setFecha_solicitud(LocalDateTime.now());
            sad.setFecha_limite(calcularFechaLimite(LocalDateTime.now()));
            sad.setUserAprobador(aprobador);
            sad.setUserSolicitante(solicitante);
            sad.setVersionDocumento(versionDocumento);
            sad = sadr.save(sad);
            saveHistorialAprobacion(sad, "Creado", "Pendiente", "Inicio del flujo de aprobación" + (comentarioSolicitud != null && !comentarioSolicitud.isEmpty() ? ": " + comentarioSolicitud : ""), solicitante);
        }
    }

    public void updateFlujoAprobacionDocumento(SolicitudAprobacionDocumentoDto dto) {
        SolicitudAprobacionDocumento sad = sadr.findById(dto.getId()).orElse(null);

        if (sad != null) {
            String estadoAnterior = sad.getEstado();
            sad.setEstado(dto.getStatus());
            sadr.save(sad); /// SolicitudAprobacionDocumentoRepository

            /// Obtener usuario que realiza la acción
            Object principal = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
            String currentUsername = null;
            if (principal instanceof UserDetails) {
                currentUsername = ((UserDetails) principal).getUsername();
            } else if (principal != null) {
                currentUsername = principal.toString();
            }
            Users userAccion = currentUsername != null ? usersRepository.findByUsername(currentUsername) : null;

            // Guardar Resolución Aprobación
            if ("Aprobado".equalsIgnoreCase(dto.getStatus()) || "Rechazado".equalsIgnoreCase(dto.getStatus())) {
                ResolucionAprobacionDocumento rad = ResolucionAprobacionDocumento.builder()
                        .solicitudAprobacion(sad)
                        .userRevisor(userAccion != null ? userAccion : sad.getUserAprobador())
                        .status(dto.getStatus())
                        .comentario_aprobador(dto.getComentario())
                        .fecha_resolucion(LocalDateTime.now())
                        .build();
                radr.save(rad);
            }

            // Registrar en el historial
            saveHistorialAprobacion(sad, estadoAnterior, dto.getStatus(), dto.getComentario(), userAccion);
        }
    }

    private void saveHistorialAprobacion(SolicitudAprobacionDocumento sad, String estadoAnterior, String estadoNuevo, String comentario, Users userAccion) {
        HistorialAprobacionDocumento hist = HistorialAprobacionDocumento.builder()
                .solicitud_aprobacion(sad)
                .estado_anterior(estadoAnterior)
                .estado_nuevo(estadoNuevo)
                .comentario(comentario)
                .fecha_accion(LocalDateTime.now())
                .user_accion(userAccion)
                .build();
        historialAprobacionDocumentoRepository.save(hist);
    }

    public List<SolicitudAprobacionDocumentoDto> getFlujoAprobacionDocumentos() {
        List<SolicitudAprobacionDocumento> lista = sadr.findAll();
        List<SolicitudAprobacionDocumentoDto> listaDto = new ArrayList<>();
        for (SolicitudAprobacionDocumento s : lista) {
            listaDto.add(solicitudAprobacionDocumentoDto(s));
        }
        return listaDto;
    }

    public List<Map<String, Object>> getFlujoAprobacionDocumentosByUser(Integer id_user_aprobador) {
        return sadr.findByUser(id_user_aprobador);
    }

    public List<Map<String, Object>> getHistorialByFlujoId(Integer flujoId) {
        List<HistorialAprobacionDocumento> hist = historialAprobacionDocumentoRepository
                .findByFlujoIdOrderByFechaDesc(flujoId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (HistorialAprobacionDocumento h : hist) {
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", h.getId());
            map.put("estado_anterior", h.getEstado_anterior());
            map.put("estado_nuevo", h.getEstado_nuevo());
            map.put("comentario", h.getComentario());
            map.put("fecha_accion", h.getFecha_accion());
            if (h.getUser_accion() != null) {
                map.put("usuario_nombre", h.getUser_accion().getName() + " " + h.getUser_accion().getLastname());
                map.put("usuario_username", h.getUser_accion().getUsername());
            } else {
                map.put("usuario_nombre", "Sistema");
                map.put("usuario_username", "system");
            }
            result.add(map);
        }
        return result;
    }

    public List<SolicitudAprobacionDocumentoDto> getFlujoByVersionId(Integer versionId) {
        List<SolicitudAprobacionDocumento> list = sadr.findByVersionId(versionId);
        List<SolicitudAprobacionDocumentoDto> dtoList = new ArrayList<>();
        for (SolicitudAprobacionDocumento s : list) {
            dtoList.add(solicitudAprobacionDocumentoDto(s));
        }
        return dtoList;
    }

    public void resetFlujoAprobacion(VersionDocumento versionDocumento, Integer idUserAprobador, String prioridad, String comentarioSolicitud) {
        Users aprobador = usersRepository.findById(idUserAprobador).orElse(null);
        if (aprobador == null)
            return;

        List<SolicitudAprobacionDocumento> solicitudes = sadr.findByVersionId(versionDocumento.getId());
        if (solicitudes != null && !solicitudes.isEmpty()) {
            SolicitudAprobacionDocumento sad = solicitudes.getFirst();
            String estadoAnterior = sad.getEstado();
            sad.setUserAprobador(aprobador);
            sad.setEstado("Pendiente");
            sad.setPrioridad(prioridad != null ? prioridad : "MEDIA");
            sad.setComentario_solicitante(
                    comentarioSolicitud != null ? comentarioSolicitud : "Versión modificada por el creador");
            sad.setFecha_limite(calcularFechaLimite(LocalDateTime.now()));
            sadr.save(sad);

            // Registrar en historial
            Object principal = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
            String currentUsername = null;
            if (principal instanceof UserDetails) {
                currentUsername = ((UserDetails) principal).getUsername();
            } else if (principal != null) {
                currentUsername = principal.toString();
            }
            Users userAccion = currentUsername != null ? usersRepository.findByUsername(currentUsername) : null;

            saveHistorialAprobacion(sad, estadoAnterior, "Pendiente",
                    "Versión modificada por el creador - Solicitud de aprobación regenerada"
                            + (comentarioSolicitud != null && !comentarioSolicitud.isEmpty()
                                    ? ": " + comentarioSolicitud
                                    : ""),
                    userAccion);
        } else {
            try {
                saveFlujoAprobacionDocumento(idUserAprobador, versionDocumento, prioridad, comentarioSolicitud);
            } catch (IOException e) {
                throw new RuntimeException("Error al crear solicitud de aprobación en actualización", e);
            }
        }
    }

    private SolicitudAprobacionDocumentoDto solicitudAprobacionDocumentoDto(SolicitudAprobacionDocumento s) {
        SolicitudAprobacionDocumentoDto dto = new SolicitudAprobacionDocumentoDto();
        dto.setId(s.getId());
        dto.setComentario(s.getComentario_solicitante());
        dto.setComentarioSolicitud(s.getComentario_solicitante());
        dto.setStatus(s.getEstado());
        dto.setPrioridad(s.getPrioridad());
        dto.setDate_creation(s.getFecha_solicitud());
        dto.setDate_update(s.getFecha_solicitud()); // O usar fecha de actualización si existiera
        dto.setFechaLimite(s.getFecha_limite());
        dto.setId_version_documento(s.getVersionDocumento().getId());
        dto.setId_user_solicitante(s.getUserSolicitante().getId());
        if (s.getUserAprobador() != null) {
            dto.setId_user_aprobador(s.getUserAprobador().getId());
        }
        
        // Obtener la última resolución para mapear el comentario del aprobador y fecha resolucion
        if (s.getResoluciones() != null && !s.getResoluciones().isEmpty()) {
            ResolucionAprobacionDocumento ultima = s.getResoluciones().getLast();
            dto.setComentarioAprobador(ultima.getComentario_aprobador());
            dto.setFechaResolucion(ultima.getFecha_resolucion());
        }
        
        return dto;
    }
}
