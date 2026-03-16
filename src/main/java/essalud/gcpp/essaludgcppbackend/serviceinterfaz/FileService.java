package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.repositories.FileRepository;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private final FileRepository fileRepository;
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public byte[] findArchivo(String name){
        return fileRepository.findArchivo(name);
    }
}
