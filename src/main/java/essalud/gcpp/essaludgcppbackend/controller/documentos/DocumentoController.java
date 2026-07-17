package essalud.gcpp.essaludgcppbackend.controller.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.DocumentoDto;
import essalud.gcpp.essaludgcppbackend.service.documentos.DocumentoServ;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/documento")
public class DocumentoController {

    private final DocumentoServ documentoServ;
    public DocumentoController(DocumentoServ documentoServ) {
        this.documentoServ = documentoServ;
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public void saveDocumento(@RequestBody DocumentoDto documentoDto){
        documentoServ.saveDocumento(documentoDto);
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<Map<String, Object>> getDocumentos(){
        return documentoServ.getDocumentos();
    }

    @GetMapping("/getdocbycodred/{codred}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<Map<String, Object>> getDocumentosByCodred(@PathVariable String codred){
        return documentoServ.getDocumentosByCodred(codred);
    }

    @GetMapping("/getdocbycodcas/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<Map<String, Object>> getDocumentosByCodcas(@PathVariable String codcas){
        return documentoServ.getDocumentosByCodcas(codcas);
    }

    @GetMapping("/getdocbyuser/{iduser}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<Map<String, Object>> getDocumentosUser(@PathVariable Integer iduser){
        return documentoServ.getDocumentosByUser(iduser);
    }

    @GetMapping("/getdocbyiddoc/{iddoc}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public DocumentoDto  getDocumentosByIdDoc(@PathVariable Integer iddoc){
        return documentoServ.getDocumentoById(iddoc);
    }

    @GetMapping("/getdocbyidversion/{idversion}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public DocumentoDto getDocumentosByIdVersion(@PathVariable Integer idversion){
        return documentoServ.getDocumentoByIdVersion(idversion);
    }

    @DeleteMapping("/delete/{id}/{userid}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public void deleteDocumento(@PathVariable Integer id, @PathVariable Integer userid){
        documentoServ.deleteDocumento(id, userid);
    }
}
