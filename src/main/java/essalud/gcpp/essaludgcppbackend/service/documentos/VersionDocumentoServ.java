package essalud.gcpp.essaludgcppbackend.service.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.VersionDocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.documentos.Documento;
import essalud.gcpp.essaludgcppbackend.model.documentos.SolicitudAprobacionDocumento;
import essalud.gcpp.essaludgcppbackend.model.documentos.TipoDocumento;
import essalud.gcpp.essaludgcppbackend.model.documentos.VersionDocumento;
import essalud.gcpp.essaludgcppbackend.model.documentos.VersionDocumentoArchivo;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.DocumentoRep;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.VersionDocumentoRep;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.VersionDocumentoArchivoRep;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsersRep;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.AuditoriaDocumentariaRep;
import essalud.gcpp.essaludgcppbackend.model.documentos.AuditoriaDocumentaria;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VersionDocumentoServ {
    private final UsersRep usersRep;
    private final DocumentoRep documentoRep;
    private final VersionDocumentoRep versionDocumentoRep;
    private final VersionDocumentoArchivoRep versionDocumentoArchivoRep;
    private final SolicitudAprobacionDocumentoServ sads;
    private final AuditoriaDocumentariaRep auditoriaDocumentariaRep;

    public VersionDocumentoServ(UsersRep usersRep, DocumentoRep documentoRep,
                                VersionDocumentoRep versionDocumentoRep,
                                VersionDocumentoArchivoRep versionDocumentoArchivoRep,
                                SolicitudAprobacionDocumentoServ sads, AuditoriaDocumentariaRep auditoriaDocumentariaRep) {
        this.usersRep = usersRep;
        this.documentoRep = documentoRep;
        this.versionDocumentoRep = versionDocumentoRep;
        this.versionDocumentoArchivoRep = versionDocumentoArchivoRep;
        this.sads = sads;
        this.auditoriaDocumentariaRep = auditoriaDocumentariaRep;
    }

    private boolean validarRequerimientos(TipoDocumento tipo, VersionDocumentoDto versionDocumentoDto) {
        if (tipo != null) {
            double fileSizeMb = (double) versionDocumentoDto.getArchivo().getSize() / 1048576;
            if (tipo.getMax_size_mb() != null && fileSizeMb > tipo.getMax_size_mb()) {
                return false;
            }

            // Validar extension permitida
            String fileName = versionDocumentoDto.getArchivo().getOriginalFilename();
            if (fileName != null && tipo.getExtensionesPermitidas() != null) {
                String ext = "";
                int dotIndex = fileName.lastIndexOf('.');
                if (dotIndex > 0) {
                    ext = fileName.substring(dotIndex).toLowerCase();
                }
                final String targetExt = ext;
                return tipo.getExtensionesPermitidas().stream()
                        .anyMatch(allowed -> allowed.getNombre().equalsIgnoreCase(targetExt));
            } else {
                return false;
            }
        }
        return true;
    }

    public void saveVersionDocumento(VersionDocumentoDto versionDocumentoDto) throws IOException {
        Documento documento = documentoRep.findById(versionDocumentoDto.getId_documento()).orElse(null);
        Users user = usersRep.findById(versionDocumentoDto.getId_user_creation()).orElse(null);

        if (documento != null && user != null) {
            TipoDocumento tipo = documento.getTipo_documento();

            if (validarRequerimientos(tipo, versionDocumentoDto)) {
                VersionDocumento versionDocumento = new VersionDocumento();
                versionDocumento.setNombre(versionDocumentoDto.getNombre());
                versionDocumento.setDescripcion(versionDocumentoDto.getDescripcion());
                versionDocumento.setExtencion(versionDocumentoDto.getArchivo().getContentType());
                versionDocumento.setVersion(versionDocumentoDto.getVersion());
                versionDocumento.setSize((double) versionDocumentoDto.getArchivo().getSize() / 1048576);
                versionDocumento.setDate_creation(LocalDateTime.now());
                versionDocumento.setDate_update(LocalDateTime.now());
                versionDocumento.setUser_version_documento(user);
                versionDocumento.setDocumento_version_documento(documento);
                versionDocumento.setVisible(versionDocumentoDto.getVisible() != null ? versionDocumentoDto.getVisible() : true);

                /// se guarda el archivo de manera correlacional usando cascada
                VersionDocumentoArchivo vda = new VersionDocumentoArchivo();
                vda.setArchivo(versionDocumentoDto.getArchivo().getBytes());
                vda.setVersionDocumento(versionDocumento);
                versionDocumento.setArchivoVersion(vda);

                versionDocumento = versionDocumentoRep.save(versionDocumento);

                if (documentoRep.requiere_aprobacion(documento.getId())) {
                    this.sads.saveFlujoAprobacionDocumento(
                            versionDocumentoDto.getId_user_aprobador(),
                            versionDocumento,
                            versionDocumentoDto.getPrioridad(),
                            versionDocumentoDto.getComentario_solicitud()
                    );
                }
            }
        }
    }

    public List<VersionDocumentoDto> getAllVersionDocumentos() {
        List<VersionDocumento> versionDocumentos = versionDocumentoRep.findAll();
        List<VersionDocumentoDto> versionDocumentosDto = new ArrayList<>();
        for (VersionDocumento versionDocumento : versionDocumentos) {
            versionDocumentosDto.add(versionDocumentoDto(versionDocumento));
        }
        return versionDocumentosDto;
    }

    public Resource loadResource(Integer id) {
        VersionDocumento data = versionDocumentoRep.findById(id).orElse(null);
        if (data == null) {
            throw new RuntimeException("documento no encontrado");
        }

        Documento documento = data.getDocumento_version_documento();
        if (documento == null) {
            throw new RuntimeException("documento no encontrado");
        }

        // Enforce Security Check
        boolean requiresApproval = documentoRep.requiere_aprobacion(documento.getId());
        if (requiresApproval) {
            List<SolicitudAprobacionDocumento> flujos = data.getSolicitudesAprobacion();
            boolean isApproved = false;
            Integer idUserAprobador = null;
            if (flujos != null && !flujos.isEmpty()) {
                SolicitudAprobacionDocumento flujo = flujos.getFirst();
                if ("Aprobado".equalsIgnoreCase(flujo.getEstado())) {
                    isApproved = true;
                }
                if (flujo.getUserAprobador() != null) {
                    idUserAprobador = flujo.getUserAprobador().getId();
                }
            }

            if (!isApproved) {
                String currentUsername = getCurrentUsername();
                if (currentUsername == null) {
                    throw new org.springframework.security.access.AccessDeniedException(
                            "Acceso Denegado: La versión del documento no está aprobada.");
                }

                Users currentUser = usersRep.findByUsername(currentUsername);
                if (currentUser == null) {
                    throw new org.springframework.security.access.AccessDeniedException(
                            "Acceso Denegado: La versión del documento no está aprobada.");
                }

                boolean isCreator = Objects.equals(data.getUser_version_documento().getId(), currentUser.getId())
                        || (documento.getUser_documento() != null
                                && Objects.equals(documento.getUser_documento().getId(), currentUser.getId()));
                boolean isAssignedApprover = Objects.equals(idUserAprobador, currentUser.getId());

                org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext()
                        .getAuthentication();
                boolean hasAdminPrivilege = false;
                if (auth != null) {
                    hasAdminPrivilege = auth.getAuthorities().stream()
                            .anyMatch(a -> "ADMIN".equalsIgnoreCase(a.getAuthority())
                                    || "ADMIN_RED".equalsIgnoreCase(a.getAuthority()));
                }

                if (!isCreator && !isAssignedApprover && !hasAdminPrivilege) {
                    throw new org.springframework.security.access.AccessDeniedException(
                            "Acceso Denegado: La versión del documento no está aprobada.");
                }
            }
        }

        // Log Auditing
        String currentUsername = getCurrentUsername();
        Users currentUser = currentUsername != null ? usersRep.findByUsername(currentUsername) : null;
        String action = "VISUALIZAR";
        try {
            jakarta.servlet.http.HttpServletRequest request = ((org.springframework.web.context.request.ServletRequestAttributes) org.springframework.web.context.request.RequestContextHolder
                    .currentRequestAttributes())
                    .getRequest();

            if ("true".equalsIgnoreCase(request.getParameter("download"))) {
                action = "DESCARGAR";
            }

            String ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            String userAgent = request.getHeader("User-Agent");

            AuditoriaDocumentaria audit = AuditoriaDocumentaria.builder()
                    .documento(documento)
                    .versionDocumento(data)
                    .user(currentUser)
                    .accion(action)
                    .ip_address(ipAddress != null ? ipAddress : "unknown")
                    .user_agent(userAgent)
                    .fecha_accion(LocalDateTime.now())
                    .build();

            auditoriaDocumentariaRep.save(audit);
        } catch (Exception e) {
            System.err.println("Error al guardar auditoría documental: " + e.getMessage());
        }

        VersionDocumentoArchivo archivo = versionDocumentoArchivoRep.findById(id).orElse(null);
        if (archivo == null) {
            throw new RuntimeException("archivo no encontrado");
        }
        return new ByteArrayResource(archivo.getArchivo());
    }

    public VersionDocumentoDto getVersionDocumento(Integer id) {
        return versionDocumentoRep.getVersion(id);
    }

    public List<VersionDocumentoDto> getVersionByDoc(Integer iddoc) {
        return versionDocumentoRep.getVersionByDocumento(iddoc);
    }

    public void updateVersionDocumento(VersionDocumentoDto versionDocumentoDto) throws IOException {
        VersionDocumento versionDocumento = versionDocumentoRep.findById(versionDocumentoDto.getId())
                .orElse(null);
        if (versionDocumento != null) {
            String currentUsername = getCurrentUsername();
            if (currentUsername == null || versionDocumento.getUser_version_documento() == null
                    || !currentUsername.equals(versionDocumento.getUser_version_documento().getUsername())) {
                throw new org.springframework.security.access.AccessDeniedException(
                        "No tiene permisos para modificar esta versión.");
            }

            versionDocumento.setNombre(versionDocumentoDto.getNombre());
            versionDocumento.setDescripcion(versionDocumentoDto.getDescripcion());
            versionDocumento.setVersion(versionDocumentoDto.getVersion());

            if (versionDocumentoDto.getArchivo() != null) {

                Documento documento = versionDocumento.getDocumento_version_documento();

                if (documento != null) {
                    TipoDocumento tipo = documento.getTipo_documento();
                    if (validarRequerimientos(tipo, versionDocumentoDto)) {
                        versionDocumento.setExtencion(versionDocumentoDto.getArchivo().getContentType());
                        versionDocumento.setSize((double) versionDocumentoDto.getArchivo().getSize() / 1048576);

                        VersionDocumentoArchivo vda = versionDocumento.getArchivoVersion();
                        if (vda == null) {
                            vda = new VersionDocumentoArchivo();
                            vda.setVersionDocumento(versionDocumento);
                            versionDocumento.setArchivoVersion(vda);
                        }
                        vda.setArchivo(versionDocumentoDto.getArchivo().getBytes());
                    }
                }
            }
            if (versionDocumentoDto.getVisible() != null) {
                versionDocumento.setVisible(versionDocumentoDto.getVisible());
            }
            Documento documento = versionDocumento.getDocumento_version_documento();
            if (documento != null && documentoRep.requiere_aprobacion(documento.getId())) {
                this.sads.resetFlujoAprobacion(
                        versionDocumento,
                        versionDocumentoDto.getId_user_aprobador(),
                        versionDocumentoDto.getPrioridad(),
                        versionDocumentoDto.getComentario_solicitud());
            }
            versionDocumento.setDate_update(LocalDateTime.now());
            versionDocumentoRep.save(versionDocumento);
        }
    }

    public void deleteVersionDocumento(Integer id, Integer userId) {
        VersionDocumento version = versionDocumentoRep.findById(id).orElse(null);
        Users user = usersRep.findById(userId).orElse(null);
        if (version != null && user != null) {
            if (!version.getUser_version_documento().getId().equals(userId)) {
                throw new org.springframework.security.access.AccessDeniedException(
                        "No tiene permisos para eliminar esta versión.");
            }
            version.setDate_delete(LocalDateTime.now());
            versionDocumentoRep.save(version);
        }
    }

    public List<VersionDocumentoDto> getVersionDocumentosByUser(Integer idUser) {
        return versionDocumentoRep.getVersionByUser(idUser);
    }

    public List<VersionDocumentoDto> getVersionDocumentosByCodcas(String codcas) {
        return versionDocumentoRep.getVersionDocumentoByCodcas(codcas);
    }

    public List<VersionDocumentoDto> getVersionDocumentosByCodred(String codred) {
        return versionDocumentoRep.getVersionDocumentoByCodred(codred);
    }

    private VersionDocumentoDto versionDocumentoDto(VersionDocumento versionDocumento) {
        VersionDocumentoDto dto = new VersionDocumentoDto();
        dto.setId(versionDocumento.getId());
        dto.setNombre(versionDocumento.getNombre());
        dto.setDescripcion(versionDocumento.getDescripcion());
        dto.setExtencion(versionDocumento.getExtencion());
        dto.setVersion(versionDocumento.getVersion());
        dto.setSize(versionDocumento.getSize());
        dto.setDate_creation(versionDocumento.getDate_creation());
        dto.setDate_update(versionDocumento.getDate_update());
        dto.setId_documento(versionDocumento.getDocumento_version_documento().getId());
        dto.setId_user_creation(versionDocumento.getUser_version_documento().getId());
        dto.setVisible(versionDocumento.getVisible());
        if (versionDocumento.getSolicitudesAprobacion() != null
                && !versionDocumento.getSolicitudesAprobacion().isEmpty()) {
            SolicitudAprobacionDocumento flujo = versionDocumento.getSolicitudesAprobacion().getFirst();
            if (flujo.getUserAprobador() != null) {
                dto.setId_user_aprobador(flujo.getUserAprobador().getId());
            }
        }
        return dto;
    }

    private String getCurrentUsername() {
        Object principal = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication())
                .getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal != null) {
            return principal.toString();
        }
        return null;
    }

}
