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

    @GetMapping("/getall")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<DocumentoDto> getDocumentos(){
        return documentoService.getDocumentos();
    }

    @GetMapping("/getdocbycodred/{codred}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<DocumentoDto> getDocumentosByCodred(@PathVariable String codred){
        return documentoService.getDocumentosByCodred(codred);
    }

    @GetMapping("/getdocbycodcas/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<DocumentoDto> getDocumentosByCodcas(@PathVariable String codcas){
        return documentoService.getDocumentosByCodcas(codcas);
    }

    @GetMapping("/getdocbyuser/{iduser}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<DocumentoDto> getDocumentosUser(@PathVariable Integer iduser){
        return documentoService.getDocumentosByUser(iduser);
    }

    @GetMapping("/getdocbyiddoc/{iddoc}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public DocumentoDto  getDocumentosByIdDoc(@PathVariable Integer iddoc){
        return documentoService.getDocumentoById(iddoc);
    }

    @GetMapping("/getdocbyidversion/{idversion}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public DocumentoDto getDocumentosByIdVersion(@PathVariable Integer idversion){
        return documentoService.getDocumentoByIdVersion(idversion);
    }

    @DeleteMapping("/delete/{id}/{userid}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED')")
    public void deleteDocumento(@PathVariable Integer id, @PathVariable Integer userid){
        documentoService.deleteDocumento(id, userid);
    }
}
