package essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos;

import org.springframework.stereotype.Service;

import essalud.gcpp.essaludgcppbackend.repositories.documentos.FileRepository;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public byte[] findArchivo(String name) {
        return fileRepository.findArchivo(name);
    }

}
