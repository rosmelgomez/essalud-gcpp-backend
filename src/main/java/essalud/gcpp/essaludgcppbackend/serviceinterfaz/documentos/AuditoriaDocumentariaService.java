package essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos;

import essalud.gcpp.essaludgcppbackend.repositories.documentos.AuditoriaDocumentariaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AuditoriaDocumentariaService {

    private final AuditoriaDocumentariaRepository auditoriaDocumentariaRepository;

    public AuditoriaDocumentariaService(AuditoriaDocumentariaRepository auditoriaDocumentariaRepository) {
        this.auditoriaDocumentariaRepository = auditoriaDocumentariaRepository;
    }

    public List<Map<String, Object>> getAuditoriaConDetalles() {
        return auditoriaDocumentariaRepository.getAuditoriaConDetalles();
    }
}
