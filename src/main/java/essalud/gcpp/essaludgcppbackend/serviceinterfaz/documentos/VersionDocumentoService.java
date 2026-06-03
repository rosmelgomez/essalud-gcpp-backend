package essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.VersionDocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.documentos.Documento;
import essalud.gcpp.essaludgcppbackend.model.documentos.FlujoAprobacionDocumento;
import essalud.gcpp.essaludgcppbackend.model.documentos.TipoDocumento;
import essalud.gcpp.essaludgcppbackend.model.documentos.VersionDocumento;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.DocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.VersionDocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsersRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VersionDocumentoService {
    private final UsersRepository usersRepository;
    private final DocumentoRepository documentoRepository;
    private final VersionDocumentoRepository versionDocumentoRepository;
    private final FlujoAprobacionDocumentoService fads;


    public VersionDocumentoService(UsersRepository usersRepository, DocumentoRepository documentoRepository,
            VersionDocumentoRepository versionDocumentoRepository, FlujoAprobacionDocumentoService fads
         ) {
        this.usersRepository = usersRepository;
        this.documentoRepository = documentoRepository;
        this.versionDocumentoRepository = versionDocumentoRepository;
        this.fads = fads;
    }

    private boolean validarRequerimientos(TipoDocumento tipo,VersionDocumentoDto versionDocumentoDto){

        if (tipo != null) {
            System.out.println("B: "+ (double) versionDocumentoDto.getArchivo().getSize());
            System.out.println("K: "+ (double) versionDocumentoDto.getArchivo().getSize() / 1024);
            System.out.println("M: "+ (double) versionDocumentoDto.getArchivo().getSize() / 1048576);
            System.out.println("G: "+ (double) versionDocumentoDto.getArchivo().getSize() / 1073741824);
            // Validar tamaño maximo
            double fileSizeMb = (double) versionDocumentoDto.getArchivo().getSize() / 1048576;
            if (tipo.getMax_size_mb() != null && fileSizeMb > tipo.getMax_size_mb()) {
                return false;
            }

            // Validar extension permitida
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
                return extAllowed;
            }
            else  {
                return false;
            }
        }
        return true;
    }

    public void saveVersionDocumento(VersionDocumentoDto versionDocumentoDto) throws IOException {
        Documento documento = documentoRepository.findById(versionDocumentoDto.getId_documento()).orElse(null);
        Users user = usersRepository.findById(versionDocumentoDto.getId_user_creation()).orElse(null);

        if (documento != null && user != null) {
            TipoDocumento tipo = documento.getTipo_documento();

            if (validarRequerimientos(tipo, versionDocumentoDto)){
                VersionDocumento versionDocumento = new VersionDocumento();
                versionDocumento.setNombre(versionDocumentoDto.getNombre());
                versionDocumento.setDescripcion(versionDocumentoDto.getDescripcion());
                versionDocumento.setExtencion(versionDocumentoDto.getArchivo().getContentType());
                versionDocumento.setVersion(versionDocumentoDto.getVersion());
                versionDocumento.setSize((double) versionDocumentoDto.getArchivo().getSize() / 1048576);
                versionDocumento.setDate_creation(LocalDateTime.now());
                versionDocumento.setDate_update(LocalDateTime.now());
                versionDocumento.setArchivo(versionDocumentoDto.getArchivo().getBytes());
                versionDocumento.setUser_version_documento(user);
                versionDocumento.setDocumento_version_documento(documento);
                versionDocumento.setVisible(versionDocumentoDto.getVisible() != null ? versionDocumentoDto.getVisible() : true);
                versionDocumento = versionDocumentoRepository.save(versionDocumento);

                if (documentoRepository.requiere_aprobacion(documento.getId())) {
                    this.fads.saveFlujoAprobacionDocumento(versionDocumentoDto.getId_user_aprobador(), versionDocumento);
                }
            }
        }
    }

    public List<VersionDocumentoDto> getAllVersionDocumentos() {
        List<VersionDocumento> versionDocumentos = versionDocumentoRepository.findAll();
        List<VersionDocumentoDto> versionDocumentosDto = new ArrayList<>();
        for (VersionDocumento versionDocumento : versionDocumentos) {
            versionDocumentosDto.add(versionDocumentoDto(versionDocumento));
        }
        return versionDocumentosDto;
    }

    public Resource loadResource(Integer id) {
        VersionDocumento data = versionDocumentoRepository.findById(id).orElse(null);
        if (data == null) {
            throw new RuntimeException("documento no encontrado");
        }

        Documento documento = data.getDocumento_version_documento();
        if (documento == null) {
            throw new RuntimeException("documento no encontrado");
        }

        return new ByteArrayResource(data.getArchivo());
    }

    public VersionDocumentoDto getVersionDocumento(Integer id) {
        return versionDocumentoRepository.getVersion(id);
    }

    public List<VersionDocumentoDto> getVersionByDoc(Integer iddoc) {
        return versionDocumentoRepository.getVersionByDocumento(iddoc);
    }

    public void updateVersionDocumento(VersionDocumentoDto versionDocumentoDto) throws IOException {
        VersionDocumento versionDocumento = versionDocumentoRepository.findById(versionDocumentoDto.getId()).orElse(null);
        if (versionDocumento != null) {
            String currentUsername = getCurrentUsername();
            if (currentUsername == null || versionDocumento.getUser_version_documento() == null || !currentUsername.equals(versionDocumento.getUser_version_documento().getUsername())) {
                throw new org.springframework.security.access.AccessDeniedException("No tiene permisos para modificar esta versión.");
            }

            versionDocumento.setNombre(versionDocumentoDto.getNombre());
            versionDocumento.setDescripcion(versionDocumentoDto.getDescripcion());
            versionDocumento.setVersion(versionDocumentoDto.getVersion());

            if (versionDocumentoDto.getArchivo() != null) {

                Documento documento = versionDocumento.getDocumento_version_documento();

                if (documento != null) {
                    TipoDocumento tipo = documento.getTipo_documento();
                    if(validarRequerimientos(tipo, versionDocumentoDto)){
                        versionDocumento.setExtencion(versionDocumentoDto.getArchivo().getContentType());
                        versionDocumento.setSize((double) versionDocumentoDto.getArchivo().getSize() / 1048576);
                        versionDocumento.setArchivo(versionDocumentoDto.getArchivo().getBytes());
                    }
                }
            }
            if (versionDocumentoDto.getVisible() != null) {
                versionDocumento.setVisible(versionDocumentoDto.getVisible());
            }
            if (documentoRepository.requiere_aprobacion(versionDocumentoDto.getId())) {
                this.fads.saveFlujoAprobacionDocumento(versionDocumentoDto.getId_user_aprobador(), versionDocumento);
            }
            versionDocumento.setDate_update(LocalDateTime.now());
            versionDocumentoRepository.save(versionDocumento);
        }
    }

    public void deleteVersionDocumento(Integer id, Integer userId) {
        VersionDocumento version = versionDocumentoRepository.findById(id).orElse(null);
        Users user = usersRepository.findById(userId).orElse(null);
        if (version != null && user != null) {
            if (!version.getUser_version_documento().getId().equals(userId)) {
                throw new org.springframework.security.access.AccessDeniedException("No tiene permisos para eliminar esta versión.");
            }
            version.setDate_delete(LocalDateTime.now());
            versionDocumentoRepository.save(version);
        }
    }

    public List<VersionDocumentoDto> getVersionDocumentosByUser(Integer idUser) {
        return versionDocumentoRepository.getVersionByUser(idUser);
    }

    public List<VersionDocumentoDto> getVersionDocumentosByCodcas(String codcas) {
        return  versionDocumentoRepository.getVersionDocumentoByCodcas(codcas);
    }

    public List<VersionDocumentoDto> getVersionDocumentosByCodred(String codred) {
        return versionDocumentoRepository.getVersionDocumentoByCodred(codred);
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
        if (versionDocumento.getFlujo_aprobaciones_Version() != null && !versionDocumento.getFlujo_aprobaciones_Version().isEmpty()) {
            FlujoAprobacionDocumento flujo = versionDocumento.getFlujo_aprobaciones_Version().getFirst();
            if (flujo.getUser_flujo_aprobacion() != null) {
                dto.setId_user_aprobador(flujo.getUser_flujo_aprobacion().getId());
            }
        }
        return dto;
    }

    private String getCurrentUsername() {
        Object principal = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal != null) {
            return principal.toString();
        }
        return null;
    }





}
