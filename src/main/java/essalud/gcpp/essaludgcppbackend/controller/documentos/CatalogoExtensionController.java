package essalud.gcpp.essaludgcppbackend.controller.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.CatalogoExtensionDto;
import essalud.gcpp.essaludgcppbackend.service.documentos.CatalogoExtensionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("catalogo-extension")
public class CatalogoExtensionController {

    private final CatalogoExtensionService ces;

    public CatalogoExtensionController(CatalogoExtensionService ces) {
        this.ces = ces;
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<CatalogoExtensionDto> getCatalogo() {
        return ces.getCatalogo();
    }
}
