package essalud.gcpp.essaludgcppbackend.controller.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.DocumentoDto;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos.DocumentoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documento")
public class DocumentoController {

    private final DocumentoService documentoService;
    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public void saveDocumento(@RequestBody DocumentoDto documentoDto){
        documentoService.saveDocumento(documentoDto);
    }

    @GetMapping("/getDocumentos")
    public List<DocumentoDto> getDocumentos(){
        return documentoService.getDocumentos();
    }



}
