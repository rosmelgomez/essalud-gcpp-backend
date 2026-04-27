package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.dtos.DocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.Documento;
import essalud.gcpp.essaludgcppbackend.repositories.DocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.FileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public void insertDocumento(DocumentoDto documentoDto){
        Documento documento = new Documento();
        documento.setNombre(documentoDto.getNombre());
        documento.setDescripcion(documentoDto.getDescripcion());
        documento.setDate_creation(LocalDateTime.now());
        documento.setDate_update(LocalDateTime.now());
        documento.setUsuario_creation(documentoDto.getUsuario_creation());
        documento.setUsuario_update(documentoDto.getUsuario_update());
        documento.setArchivo(documentoDto.getArchivo());
        documentoRepository.save(documento);
    }

    public void updateDocumento(){

    }

    public void deleteDocumento(){

    }






}
