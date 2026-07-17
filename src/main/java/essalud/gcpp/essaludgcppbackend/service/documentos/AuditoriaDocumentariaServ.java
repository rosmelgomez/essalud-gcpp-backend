package essalud.gcpp.essaludgcppbackend.service.documentos;

import essalud.gcpp.essaludgcppbackend.repositories.documentos.AuditoriaDocumentariaRep;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AuditoriaDocumentariaServ {

    private final AuditoriaDocumentariaRep auditoriaDocumentariaRep;

    public AuditoriaDocumentariaServ(AuditoriaDocumentariaRep auditoriaDocumentariaRep) {
        this.auditoriaDocumentariaRep = auditoriaDocumentariaRep;
    }

    public List<Map<String, Object>> getAuditoriaConDetalles() {
        return auditoriaDocumentariaRep.getAuditoriaConDetalles();
    }
}
