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
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<DocumentoDto> getDocumentos(){
        return documentoService.getDocumentos();
    }

    @GetMapping("/getDocumentosByCodred/{codred}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<DocumentoDto> getDocumentosByCodred(@PathVariable String codred){
        return documentoService.getDocumentosByCodred(codred);
    }

    @GetMapping("/getDocumentosByCodcas/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<DocumentoDto> getDocumentosByCodcas(@PathVariable String codcas){
        return documentoService.getDocumentosByCodcas(codcas);
    }

    @GetMapping("/getDocumentosByUser/{iduser}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<DocumentoDto> getDocumentosUser(@PathVariable Integer iduser){
        return documentoService.getDocumentosByUser(iduser);
    }



}
