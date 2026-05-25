package essalud.gcpp.essaludgcppbackend.controller.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.CategoriaDocumento;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos.CategoriaDocumentoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria-documento")
public class CategoriaDocumentoController {

    private final CategoriaDocumentoService categoriaDocumentoService;

    public CategoriaDocumentoController(CategoriaDocumentoService categoriaDocumentoService) {
        this.categoriaDocumentoService = categoriaDocumentoService;
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<CategoriaDocumento> getCategorias() {
        return categoriaDocumentoService.getCategorias();
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED')")
    public void saveCategoria(@RequestBody CategoriaDocumento categoria) {
        categoriaDocumentoService.saveCategoria(categoria);
    }
}
