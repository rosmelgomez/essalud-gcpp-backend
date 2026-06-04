package essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.FlujoAprobacionDocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.documentos.FlujoAprobacionDocumento;
import essalud.gcpp.essaludgcppbackend.model.documentos.HistorialAprobacionDocumento;
import essalud.gcpp.essaludgcppbackend.model.documentos.VersionDocumento;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.FlujoAprobacionDocumentoRepository;
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
public class FlujoAprobacionDocumentoService {

    private final FlujoAprobacionDocumentoRepository fadr;
    private final UsersRepository usersRepository;

    private final HistorialAprobacionDocumentoRepository historialAprobacionDocumentoRepository;

    public FlujoAprobacionDocumentoService(FlujoAprobacionDocumentoRepository fadr, UsersRepository usersRepository,
            HistorialAprobacionDocumentoRepository historialAprobacionDocumentoRepository) {
        this.fadr = fadr;
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


    public void saveFlujoAprobacionDocumento(Integer id_user_aprobador, VersionDocumento versionDocumento, String prioridad, String comentarioSolicitud)
            throws IOException {
        Users user = usersRepository.findById(id_user_aprobador).orElse(null);

        if (versionDocumento != null && user != null) {
            FlujoAprobacionDocumento fad = new FlujoAprobacionDocumento();
            fad.setComentario_solicitante(comentarioSolicitud != null ? comentarioSolicitud : "");
            fad.setStatus("Pendiente");
            fad.setPrioridad(prioridad != null ? prioridad : "MEDIA");
            fad.setDate_creation(LocalDateTime.now());
            fad.setDate_update(LocalDateTime.now());
            fad.setFecha_limite(calcularFechaLimite(LocalDateTime.now()));
            fad.setFecha_resolucion(null);
            fad.setUser_flujo_aprobacion(user);
            fad.setVersion_documento_aprobacion(versionDocumento);
            fad = fadr.save(fad);
            saveHistorialAprobacion(fad, "Creado", "Pendiente", "Inicio del flujo de aprobación" + (comentarioSolicitud != null && !comentarioSolicitud.isEmpty() ? ": " + comentarioSolicitud : ""),
                    versionDocumento.getUser_version_documento());
        }
    }

    public List<FlujoAprobacionDocumentoDto> getFlujoAprobacionDocumentos() {
        List<FlujoAprobacionDocumento> lista = fadr.findAll();
        List<FlujoAprobacionDocumentoDto> listaDto = new ArrayList<>();
        for (FlujoAprobacionDocumento f : lista) {
            listaDto.add(flujoAprobacionDocumentoDto(f));
        }
        return listaDto;
    }

    public List<Map<String, Object>> getFlujoAprobacionDocumentosByUser(Integer id_user_aprobador) {
        return fadr.findByUser(id_user_aprobador);
    }

    public void updateFlujoAprobacionDocumento(FlujoAprobacionDocumentoDto fadDto) {
        FlujoAprobacionDocumento fad = fadr.findById(fadDto.getId()).orElse(null);
        if (fad != null) {
            String estadoAnterior = fad.getStatus();
            fad.setComentario_aprobador(fadDto.getComentario());
            fad.setStatus(fadDto.getStatus());
            fad.setDate_update(LocalDateTime.now());
            if ("Aprobado".equalsIgnoreCase(fadDto.getStatus()) || "Rechazado".equalsIgnoreCase(fadDto.getStatus())) {
                fad.setFecha_resolucion(LocalDateTime.now());
            }
            fadr.save(fad);

            // Registrar en el historial
            Object principal = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
            String currentUsername = null;
            if (principal instanceof UserDetails) {
                currentUsername = ((UserDetails) principal).getUsername();
            } else if (principal != null) {
                currentUsername = principal.toString();
            }
            Users userAccion = currentUsername != null ? usersRepository.findByUsername(currentUsername) : null;
            saveHistorialAprobacion(fad, estadoAnterior, fadDto.getStatus(), fadDto.getComentario(), userAccion);
        }
    }

    private void saveHistorialAprobacion(FlujoAprobacionDocumento fad, String estadoAnterior, String estadoNuevo, String comentario, Users userAccion) {
        HistorialAprobacionDocumento hist = HistorialAprobacionDocumento.builder()
                .flujo_aprobacion(fad)
                .estado_anterior(estadoAnterior)
                .estado_nuevo(estadoNuevo)
                .comentario(comentario)
                .fecha_accion(LocalDateTime.now())
                .user_accion(userAccion)
                .build();
        historialAprobacionDocumentoRepository.save(hist);
    }

    public List<java.util.Map<String, Object>> getHistorialByFlujoId(Integer flujoId) {
        List<HistorialAprobacionDocumento> hist = historialAprobacionDocumentoRepository
                .findByFlujoIdOrderByFechaDesc(flujoId);
        List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
        for (HistorialAprobacionDocumento h : hist) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
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

    public List<FlujoAprobacionDocumentoDto> getFlujoByVersionId(Integer versionId) {
        List<FlujoAprobacionDocumento> list = fadr.findByVersionId(versionId);
        List<FlujoAprobacionDocumentoDto> dtoList = new ArrayList<>();
        for (FlujoAprobacionDocumento f : list) {
            dtoList.add(flujoAprobacionDocumentoDto(f));
        }
        return dtoList;
    }


    public void resetFlujoAprobacion(VersionDocumento versionDocumento, Integer idUserAprobador, String prioridad, String comentarioSolicitud) {
        Users aprobador = usersRepository.findById(idUserAprobador).orElse(null);
        if (aprobador == null)
            return;

        List<FlujoAprobacionDocumento> flujos = fadr.findByVersionId(versionDocumento.getId());
        if (flujos != null && !flujos.isEmpty()) {
            FlujoAprobacionDocumento fad = flujos.getFirst();
            String estadoAnterior = fad.getStatus();
            fad.setUser_flujo_aprobacion(aprobador);
            fad.setStatus("Pendiente");
            fad.setPrioridad(prioridad != null ? prioridad : "MEDIA");
            fad.setComentario_solicitante(
                    comentarioSolicitud != null ? comentarioSolicitud : "Versión modificada por el creador");
            fad.setFecha_limite(calcularFechaLimite(LocalDateTime.now()));
            fad.setFecha_resolucion(null);
            fad.setDate_update(LocalDateTime.now());
            fadr.save(fad);

            // Registrar en historial
            Object principal = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
            String currentUsername = null;
            if (principal instanceof UserDetails) {
                currentUsername = ((UserDetails) principal).getUsername();
            } else if (principal != null) {
                currentUsername = principal.toString();
            }
            Users userAccion = currentUsername != null ? usersRepository.findByUsername(currentUsername) : null;

            saveHistorialAprobacion(fad, estadoAnterior, "Pendiente",
                    "Versión modificada por el creador - Solicitud de aprobación regenerada"
                            + (comentarioSolicitud != null && !comentarioSolicitud.isEmpty()
                                    ? ": " + comentarioSolicitud
                                    : ""),
                    userAccion);
        } else {
            try {
                saveFlujoAprobacionDocumento(idUserAprobador, versionDocumento, prioridad, comentarioSolicitud);
            } catch (IOException e) {
                throw new RuntimeException("Error al crear flujo de aprobación en actualización", e);
            }
        }
    }

    private FlujoAprobacionDocumentoDto flujoAprobacionDocumentoDto(FlujoAprobacionDocumento f) {
        FlujoAprobacionDocumentoDto dto = new FlujoAprobacionDocumentoDto();
        dto.setId(f.getId());
        dto.setComentario(f.getComentario_solicitante());
        dto.setComentarioSolicitud(f.getComentario_solicitante());
        dto.setComentarioAprobador(f.getComentario_aprobador());
        dto.setStatus(f.getStatus());
        dto.setPrioridad(f.getPrioridad());
        dto.setDate_creation(f.getDate_creation());
        dto.setDate_update(f.getDate_update());
        dto.setFechaLimite(f.getFecha_limite());
        dto.setFechaResolucion(f.getFecha_resolucion());
        dto.setId_version_documento(f.getVersion_documento_aprobacion().getId());
        if (f.getUser_flujo_aprobacion() != null) {
            dto.setId_user_aprobador(f.getUser_flujo_aprobacion().getId());
        }
        return dto;
    }
}
