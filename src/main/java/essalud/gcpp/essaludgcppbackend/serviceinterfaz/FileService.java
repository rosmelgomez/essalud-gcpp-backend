package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.repositories.DocumentoRepository;
import essalud.gcpp.essaludgcppbackend.repositories.FileRepository;
import org.springframework.stereotype.Service;

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

    public void insertDocumento(){
    }

    public void updateDocumento(){

    }

    public void deleteDocumento(){

    }






}
