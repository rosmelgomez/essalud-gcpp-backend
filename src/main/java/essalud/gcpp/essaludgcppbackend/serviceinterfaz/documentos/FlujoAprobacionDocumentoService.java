package essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.FlujoAprobacionDocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.documentos.FlujoAprobacionDocumento;
import essalud.gcpp.essaludgcppbackend.model.documentos.VersionDocumento;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.FlujoAprobacionDocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.VersionDocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsersRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FlujoAprobacionDocumentoService {

    private final FlujoAprobacionDocumentoRepository fadr;
    private final UsersRepository usersRepository;
    private final VersionDocumentoRepository vdr;

    public FlujoAprobacionDocumentoService(FlujoAprobacionDocumentoRepository fadr, UsersRepository usersRepository, VersionDocumentoRepository vdr) {
        this.fadr = fadr;
        this.usersRepository = usersRepository;
        this.vdr = vdr;
    }

    public void saveFlujoAprobacionDocumento(Integer id_user_aprobador, VersionDocumento versionDocumento) throws IOException {
        Users user = usersRepository.findById(id_user_aprobador).orElse(null);

        if (versionDocumento != null && user != null) {
            FlujoAprobacionDocumento fad = new FlujoAprobacionDocumento();
            fad.setComentario("");
            fad.setStatus("Pendiente");
            fad.setDate_creation(LocalDateTime.now());
            fad.setDate_update(LocalDateTime.now());
            fad.setUser_flujo_aprobacion(user);
            fad.setVersion_documento_aprobacion(versionDocumento);
            fadr.save(fad);
        }
    }

    public List<FlujoAprobacionDocumentoDto> getFlujoAprobacionDocumentos(){
        List<FlujoAprobacionDocumento> lista =fadr.findAll();
        List<FlujoAprobacionDocumentoDto> listaDto = new ArrayList<>();
        for (FlujoAprobacionDocumento f : lista) {
            listaDto.add(flujoAprobacionDocumentoDto(f));
        }
        return listaDto;
    }

    public List<Map<String, Object>> getFlujoAprobacionDocumentosByUser(Integer id_user_aprobador){
      return fadr.findByUser(id_user_aprobador);
    }

    public void updateFlujoAprobacionDocumento(FlujoAprobacionDocumentoDto fadDto) {
       FlujoAprobacionDocumento fad = fadr.findById(fadDto.getId()).orElse(null);
       if (fad != null) {
           fad.setComentario(fadDto.getComentario());
           fad.setStatus(fadDto.getStatus());
           fad.setDate_update(LocalDateTime.now());
           fadr.save(fad);
       }
    }

    private FlujoAprobacionDocumentoDto flujoAprobacionDocumentoDto(FlujoAprobacionDocumento f) {
        FlujoAprobacionDocumentoDto dto = new FlujoAprobacionDocumentoDto();
        dto.setId(f.getId());
        dto.setComentario(f.getComentario());
        dto.setStatus(f.getStatus());
        dto.setDate_creation(f.getDate_creation());
        dto.setDate_update(f.getDate_update());
        dto.setId_version_documento(f.getVersion_documento_aprobacion().getId());
        dto.setId_user_aprobador(f.getUser_flujo_aprobacion().getId());
        return dto;
    }
}
