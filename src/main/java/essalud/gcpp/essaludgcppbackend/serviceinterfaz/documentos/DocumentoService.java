package essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.DocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.documentos.Documento;
import essalud.gcpp.essaludgcppbackend.model.documentos.TipoDocumento;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.DocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.documentos.TipoDocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentoService {
    private final DocumentoRepository documentoRepository;
    private final UsersRepository usersRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;

    public DocumentoService(DocumentoRepository documentoRepository, UsersRepository usersRepository, TipoDocumentoRepository tipoDocumentoRepository) {
        this.documentoRepository = documentoRepository;
        this.usersRepository = usersRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    public void saveDocumento(DocumentoDto documento)  {
        Users user= usersRepository.findById(documento.getId_user_creation()).orElse(null);
        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(documento.getId_tipo_documento()).orElse(null);

        if (user != null && tipoDocumento != null) {
            Documento documentoNew = new Documento();
            documentoNew.setCodigo(documento.getCodigo());
            documentoNew.setTitulo(documento.getTitulo());
            documentoNew.setDescripcion(documento.getDescripcion());
            documentoNew.setCod_red(documento.getCod_red());
            documentoNew.setCodcas(documento.getCodcas());
            documentoNew.setDate_creation(LocalDateTime.now());
            documentoNew.setDate_update(LocalDateTime.now());
            documentoNew.setStatus(documento.getStatus());
            documentoNew.setTipo_documento(tipoDocumento);
            documentoNew.setUser_documento(user);
            documentoRepository.save(documentoNew);
        }

    }

    public List<DocumentoDto> getDocumentos(){
        List<Documento> documentos = documentoRepository.findAll();
        List<DocumentoDto> documentosDto = new ArrayList<>();
        for (Documento documento : documentos) {
            ModelMapper modelMapper = new ModelMapper();
            DocumentoDto documentoDto = modelMapper.map(documento, DocumentoDto.class);
            documentoDto.setId_user_creation(documento.getUser_documento().getId());
            documentoDto.setId_tipo_documento(documento.getTipo_documento().getId());
            documentosDto.add(documentoDto);
        }
        return documentosDto;
    }
}
