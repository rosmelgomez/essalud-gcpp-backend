package essalud.gcpp.essaludgcppbackend.controller.documentos;

import essalud.gcpp.essaludgcppbackend.service.documentos.AuditoriaDocumentariaService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auditoria-documental")
public class AuditoriaDocumentariaController {

    private final AuditoriaDocumentariaService auditoriaDocumentariaService;

    public AuditoriaDocumentariaController(AuditoriaDocumentariaService auditoriaDocumentariaService) {
        this.auditoriaDocumentariaService = auditoriaDocumentariaService;
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Map<String, Object>> getAuditoriaConDetalles() {
        return auditoriaDocumentariaService.getAuditoriaConDetalles();
    }
}
