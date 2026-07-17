package essalud.gcpp.essaludgcppbackend.service.documentos;

import org.springframework.stereotype.Service;

import essalud.gcpp.essaludgcppbackend.repositories.documentos.FileRep;

@Service
public class FileServ {

    private final FileRep fileRep;

    public FileServ(FileRep fileRep) {
        this.fileRep = fileRep;
    }

    public byte[] findArchivo(String name) {
        return fileRep.findArchivo(name);
    }

}
