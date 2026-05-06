package essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.VersionDocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.documentos.Documento;
import essalud.gcpp.essaludgcppbackend.model.documentos.VersionDocumento;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.DocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.VersionDocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VersionDocumentoService {
    private final UsersRepository usersRepository;
    private final DocumentoRepository documentoRepository;
    private final VersionDocumentoRepository versionDocumentoRepository;

    public VersionDocumentoService(UsersRepository usersRepository, DocumentoRepository documentoRepository, VersionDocumentoRepository versionDocumentoRepository) {
        this.usersRepository = usersRepository;
        this.documentoRepository = documentoRepository;
        this.versionDocumentoRepository = versionDocumentoRepository;
    }


    public void saveVersionDocumento(VersionDocumentoDto versionDocumentoDto){
       Documento documento = documentoRepository.findById(versionDocumentoDto.getId_documento()).orElse(null);
       Users user = usersRepository.findById(versionDocumentoDto.getId_user_creation()).orElse(null);

       if(documento != null && user != null){
           VersionDocumento versionDocumento = new VersionDocumento();
           versionDocumento.setNombre(versionDocumentoDto.getNombre());
           versionDocumento.setComentario(versionDocumentoDto.getComentario());
           versionDocumento.setExtencion(versionDocumentoDto.getExtencion());
           versionDocumento.setVersion(versionDocumentoDto.getVersion());
           versionDocumento.setSize(versionDocumentoDto.getSize());
           versionDocumento.setDate_creation(LocalDateTime.now());
           versionDocumento.setDate_update(LocalDateTime.now());
           versionDocumento.setUser_version_documento(user);
           versionDocumento.setDocumento(documento);
           versionDocumentoRepository.save(versionDocumento);
       }
    }


    public List<VersionDocumentoDto> getVersionDocumentos(){
        List<VersionDocumento> versionDocumentos = versionDocumentoRepository.findAll();
        List<VersionDocumentoDto> versionDocumentosDto = new ArrayList<>();
        for (VersionDocumento versionDocumento : versionDocumentos){
            ModelMapper  modelMapper = new ModelMapper();
            VersionDocumentoDto dto = modelMapper.map(versionDocumento, VersionDocumentoDto.class);
            dto.setId_documento(versionDocumento.getDocumento().getId());
            dto.setId_user_creation(versionDocumento.getUser_version_documento().getId());
            versionDocumentosDto.add(dto);
        }
        return versionDocumentosDto;
    }
}
