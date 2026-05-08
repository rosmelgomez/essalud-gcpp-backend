package essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.VersionDocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.documentos.Documento;
import essalud.gcpp.essaludgcppbackend.model.documentos.VersionDocumento;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.DocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.VersionDocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsersRepository;
import org.hibernate.type.descriptor.java.BlobJavaType;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
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

    public VersionDocumentoService(UsersRepository usersRepository, DocumentoRepository documentoRepository, VersionDocumentoRepository versionDocumentoRepository,  FlujoAprobacionDocumentoService fads) {
        this.usersRepository = usersRepository;
        this.documentoRepository = documentoRepository;
        this.versionDocumentoRepository = versionDocumentoRepository;
        this.fads = fads;
    }


    public void saveVersionDocumento(VersionDocumentoDto versionDocumentoDto) throws IOException {
       Documento documento = documentoRepository.findById(versionDocumentoDto.getId_documento()).orElse(null);
       Users user = usersRepository.findById(versionDocumentoDto.getId_user_creation()).orElse(null);

       if(documento != null && user != null){
           VersionDocumento versionDocumento = new VersionDocumento();
           versionDocumento.setNombre(versionDocumentoDto.getNombre());
           versionDocumento.setComentario(versionDocumentoDto.getComentario());
           versionDocumento.setExtencion(versionDocumentoDto.getArchivo().getContentType());
           versionDocumento.setVersion(versionDocumentoDto.getVersion());
           versionDocumento.setSize(versionDocumentoDto.getArchivo().getSize());
           versionDocumento.setDate_creation(LocalDateTime.now());
           versionDocumento.setDate_update(LocalDateTime.now());
           versionDocumento.setArchivo( versionDocumentoDto.getArchivo().getBytes());
           versionDocumento.setUser_version_documento(user);
           versionDocumento.setDocumento_version_documento(documento);
           versionDocumento = versionDocumentoRepository.save(versionDocumento);
           System.out.println(documentoRepository.requiere_aprobacion(documento.getId()));

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
        return new ByteArrayResource(data.getArchivo());
    }


    public List<VersionDocumentoDto> getVersionDocumentos(Integer iddoc){
        List<VersionDocumento> versionDocumentos = versionDocumentoRepository.getVersionByDocumento(iddoc);
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
        dto.setComentario(versionDocumento.getComentario());
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
