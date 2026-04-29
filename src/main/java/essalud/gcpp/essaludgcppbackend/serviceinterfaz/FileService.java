package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import essalud.gcpp.essaludgcppbackend.dtos.DocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.Documento;
import essalud.gcpp.essaludgcppbackend.repositories.DocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.FileRepository;

@Service
public class FileService {

    private final FileRepository fileRepository;
    private final DocumentoRepository documentoRepository;
    public FileService(FileRepository fileRepository, DocumentoRepository documentoRepository) {
        this.fileRepository = fileRepository;
        this.documentoRepository=documentoRepository;
    }

    public byte[] findArchivo(String name){
        return fileRepository.findArchivo(name);
    }

    public void insertDocumento(DocumentoDto  documento) {
        Documento documentoNew = new Documento();
        documentoNew.setNombre(documento.getNombre());
        documentoNew.setDescripcion(documento.getDescripcion());
        documentoNew.setUsuario_creation(documento.getUsuario_creation() );
        documentoNew.setUsuario_update(documento.getUsuario_update() );
        documentoNew.setDate_creation(LocalDateTime.now());
        documentoNew.setDate_update(LocalDateTime.now());

        documentoNew.setArchivo(documento.getArchivo() != null ? documento.getArchivo() : new byte[0]);
        documentoRepository.save(documentoNew);
    }

    public List<DocumentoDto> getDocumentos(){
        List<Documento> documentos = documentoRepository.findAll();
        List<DocumentoDto> documentosDto = new ArrayList<>();
        ModelMapper mapper = new ModelMapper();

        for (Documento documento : documentos) {
            documentosDto.add(mapper.map(documento, DocumentoDto.class));
        }
       return documentosDto;
    }

    public void updateDocumento(){

    }

    public void deleteDocumento(){

    }






}
