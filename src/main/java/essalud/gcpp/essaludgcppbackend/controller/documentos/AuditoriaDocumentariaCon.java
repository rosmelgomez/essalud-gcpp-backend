package essalud.gcpp.essaludgcppbackend.controller.documentos;

import essalud.gcpp.essaludgcppbackend.service.documentos.AuditoriaDocumentariaServ;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auditoria-documental")
public class AuditoriaDocumentariaCon {

    private final AuditoriaDocumentariaServ auditoriaDocumentariaServ;

    public AuditoriaDocumentariaCon(AuditoriaDocumentariaServ auditoriaDocumentariaServ) {
        this.auditoriaDocumentariaServ = auditoriaDocumentariaServ;
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Map<String, Object>> getAuditoriaConDetalles() {
        return auditoriaDocumentariaServ.getAuditoriaConDetalles();
    }
}
