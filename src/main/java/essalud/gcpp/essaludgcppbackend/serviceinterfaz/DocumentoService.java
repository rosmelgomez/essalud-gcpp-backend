package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.repositories.DocumentoRepository;
import org.springframework.stereotype.Service;

@Service
public class DocumentoService {

    private final DocumentoRepository DR;

    public DocumentoService(DocumentoRepository DR){
        this.DR=DR;
    }
}
