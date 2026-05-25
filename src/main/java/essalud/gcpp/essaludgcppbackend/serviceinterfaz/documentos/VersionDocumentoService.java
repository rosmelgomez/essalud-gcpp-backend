package essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.VersionDocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.documentos.AuditoriaDocumentaria;
import essalud.gcpp.essaludgcppbackend.model.documentos.Documento;
import essalud.gcpp.essaludgcppbackend.model.documentos.FlujoAprobacionDocumento;
import essalud.gcpp.essaludgcppbackend.model.documentos.TipoDocumento;
import essalud.gcpp.essaludgcppbackend.model.documentos.VersionDocumento;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.AuditoriaDocumentariaRepository;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.DocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.VersionDocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsersRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VersionDocumentoService {
    private final UsersRepository usersRepository;
    private final DocumentoRepository documentoRepository;
    private final VersionDocumentoRepository versionDocumentoRepository;
    private final FlujoAprobacionDocumentoService fads;
    private final AuditoriaDocumentariaRepository auditoriaDocumentariaRepository;

    public VersionDocumentoService(UsersRepository usersRepository, DocumentoRepository documentoRepository, VersionDocumentoRepository versionDocumentoRepository,  FlujoAprobacionDocumentoService fads, AuditoriaDocumentariaRepository auditoriaDocumentariaRepository) {
        this.usersRepository = usersRepository;
        this.documentoRepository = documentoRepository;
        this.versionDocumentoRepository = versionDocumentoRepository;
        this.fads = fads;
        this.auditoriaDocumentariaRepository = auditoriaDocumentariaRepository;
    }

    public void saveVersionDocumento(VersionDocumentoDto versionDocumentoDto) throws IOException {
       Documento documento = documentoRepository.findById(versionDocumentoDto.getId_documento()).orElse(null);
       Users user = usersRepository.findById(versionDocumentoDto.getId_user_creation()).orElse(null);

       if(documento != null && user != null){
           TipoDocumento tipo = documento.getTipo_documento();
           if (tipo != null) {
               // 1. Validar tamaño máximo
               double fileSizeMb = (double) versionDocumentoDto.getArchivo().getSize() / (1024.0 * 1024.0);
               if (tipo.getMax_size_mb() != null && fileSizeMb > tipo.getMax_size_mb()) {
                   throw new IllegalArgumentException("El archivo excede el tamaño máximo permitido de " + tipo.getMax_size_mb() + " MB.");
               }

               // 2. Validar extensión permitida
               String fileName = versionDocumentoDto.getArchivo().getOriginalFilename();
               if (fileName != null && tipo.getExtensiones_permitidas() != null) {
                   String ext = "";
                   int dotIndex = fileName.lastIndexOf('.');
                   if (dotIndex > 0) {
                       ext = fileName.substring(dotIndex).toLowerCase();
                   }
                   boolean extAllowed = false;
                   String[] allowedExts = tipo.getExtensiones_permitidas().split(",");
                   for (String allowed : allowedExts) {
                       if (allowed.trim().toLowerCase().equals(ext)) {
                           extAllowed = true;
                           break;
                       }
                   }
                   if (!extAllowed) {
                       throw new IllegalArgumentException("La extensión del archivo " + ext + " no está permitida para este tipo de documento (" + tipo.getExtensiones_permitidas() + ").");
                   }
               }
           }

           System.out.println("Bytes: " + versionDocumentoDto.getArchivo().getSize());
           System.out.println("Mb: " + (versionDocumentoDto.getArchivo().getSize() / (1024*1024)));
           VersionDocumento versionDocumento = new VersionDocumento();
           versionDocumento.setNombre(versionDocumentoDto.getNombre());
           versionDocumento.setDescripcion(versionDocumentoDto.getDescripcion());
           versionDocumento.setExtencion(versionDocumentoDto.getArchivo().getContentType());
           versionDocumento.setVersion(versionDocumentoDto.getVersion());
           versionDocumento.setSize((double) (versionDocumentoDto.getArchivo().getSize() / (1024*1024)));

           versionDocumento.setDate_creation(LocalDateTime.now());
           versionDocumento.setDate_update(LocalDateTime.now());
           versionDocumento.setArchivo( versionDocumentoDto.getArchivo().getBytes());
           versionDocumento.setUser_version_documento(user);
           versionDocumento.setDocumento_version_documento(documento);
           versionDocumento = versionDocumentoRepository.save(versionDocumento);

           if(documentoRepository.requiere_aprobacion(documento.getId())){
               this.fads.saveFlujoAprobacionDocumento(versionDocumentoDto.getId_user_aprobador(), versionDocumento);
           }
       }
    }

    public List<VersionDocumentoDto> getAllVersionDocumentos(){
        List<VersionDocumento> versionDocumentos = versionDocumentoRepository.findAll();
        List<VersionDocumentoDto> versionDocumentosDto = new ArrayList<>();
        for (VersionDocumento versionDocumento : versionDocumentos){
            versionDocumentosDto.add(versionDocumentoDto(versionDocumento));
        }
        return versionDocumentosDto;
    }

    public Resource loadResource(Integer id) {
        VersionDocumento data = versionDocumentoRepository.findById(id).orElse(null);
        if (data == null) {
            throw new RuntimeException("documento no encontrado");
        }

        // Validar seguridad de la descarga
        if (!isApproved(data)) {
            // Verificar si el usuario autenticado es creador o aprobador asignado
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String currentUsername = null;
            if (principal instanceof UserDetails) {
                currentUsername = ((UserDetails) principal).getUsername();
            } else if (principal != null) {
                currentUsername = principal.toString();
            }

            boolean isAuthorized = false;
            if (currentUsername != null) {
                if (data.getUser_version_documento() != null && currentUsername.equals(data.getUser_version_documento().getUsername())) {
                    isAuthorized = true;
                } else if (data.getFlujo_aprobaciones() != null) {
                    for (FlujoAprobacionDocumento flujo : data.getFlujo_aprobaciones()) {
                        if (flujo.getUser_flujo_aprobacion() != null && currentUsername.equals(flujo.getUser_flujo_aprobacion().getUsername())) {
                            isAuthorized = true;
                            break;
                        }
                    }
                }
            }

            if (!isAuthorized) {
                throw new AccessDeniedException("La versión del documento no ha sido aprobada y no tiene permisos para descargarla.");
            }
        }

        return new ByteArrayResource(data.getArchivo());
    }

    private boolean isApproved(VersionDocumento version) {
        if (version.getDocumento_version_documento() == null || version.getDocumento_version_documento().getTipo_documento() == null) {
            return true;
        }
        Boolean requiere = version.getDocumento_version_documento().getTipo_documento().getRequiere_aprobacion();
        if (requiere == null || !requiere) {
            return true;
        }
        if (version.getFlujo_aprobaciones() == null || version.getFlujo_aprobaciones().isEmpty()) {
            return false;
        }
        for (FlujoAprobacionDocumento flujo : version.getFlujo_aprobaciones()) {
            if ("Aprobado".equalsIgnoreCase(flujo.getStatus())) {
                return true;
            }
        }
        return false;
    }

    public VersionDocumentoDto getVersionDocumento(Integer id) {
        return versionDocumentoDto(versionDocumentoRepository.getVersion(id));
    }


    public List<VersionDocumentoDto> getVersionByDoc(Integer iddoc){
        List<VersionDocumento> versionDocumentos = versionDocumentoRepository.getVersionByDocumento(iddoc);
        List<VersionDocumentoDto> versionDocumentosDto = new ArrayList<>();
        for (VersionDocumento versionDocumento : versionDocumentos){
            versionDocumentosDto.add(versionDocumentoDto(versionDocumento));
        }
        return versionDocumentosDto;
    }

    public void updateVersionDocumento(VersionDocumentoDto versionDocumentoDto) throws IOException {
        VersionDocumento versionDocumento = versionDocumentoRepository.findById(versionDocumentoDto.getId()).orElse(null);
        if (versionDocumento != null) {
            versionDocumento.setNombre(versionDocumentoDto.getNombre());
            versionDocumento.setDescripcion(versionDocumentoDto.getDescripcion());
            versionDocumento.setExtencion(versionDocumentoDto.getArchivo().getContentType());
            versionDocumento.setVersion(versionDocumentoDto.getVersion());
            versionDocumento.setSize(versionDocumentoDto.getArchivo().getSize());
            versionDocumento.setArchivo(versionDocumentoDto.getArchivo().getBytes());
            versionDocumento.setDate_update(LocalDateTime.now());
            versionDocumentoRepository.save(versionDocumento);
        }
    }

    public List<VersionDocumentoDto> getVersionDocumentosByUser(Integer idUser){
        List<VersionDocumento> versionDocumentos = versionDocumentoRepository.getVersionByUser(idUser);
        List<VersionDocumentoDto> versionDocumentosDto = new ArrayList<>();
        for (VersionDocumento versionDocumento : versionDocumentos){
            versionDocumentosDto.add(versionDocumentoDto(versionDocumento));
        }
        return versionDocumentosDto;
    }

    public List<VersionDocumentoDto> getVersionDocumentosByCodcas(String codcas){
        List<VersionDocumento> versionDocumentos=versionDocumentoRepository.getVersionDocumentoByCodcas(codcas);
        List<VersionDocumentoDto> versionDocumentosDto = new ArrayList<>();
        for (VersionDocumento versionDocumento : versionDocumentos){
            versionDocumentosDto.add(versionDocumentoDto(versionDocumento));
        }
        return versionDocumentosDto;
    }

    public List<VersionDocumentoDto> getVersionDocumentosByCodred(String codred){
        List<VersionDocumento> versionDocumentos = versionDocumentoRepository.getVersionDocumentoByCodred(codred);
        List<VersionDocumentoDto> versionDocumentosDto = new ArrayList<>();
        for (VersionDocumento versionDocumento : versionDocumentos){
            versionDocumentosDto.add(versionDocumentoDto(versionDocumento));
        }
        return versionDocumentosDto;
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
        return dto;
    }


}
