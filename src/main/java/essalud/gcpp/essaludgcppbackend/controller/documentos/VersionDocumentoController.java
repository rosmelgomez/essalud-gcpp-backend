package essalud.gcpp.essaludgcppbackend.controller.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.VersionDocumentoDto;
import essalud.gcpp.essaludgcppbackend.service.documentos.VersionDocumentoServ;
import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("version-documento")
public class VersionDocumentoController {

    private final VersionDocumentoServ versionDocumentoServ;
    public VersionDocumentoController(VersionDocumentoServ versionDocumentoServ) {
        this.versionDocumentoServ = versionDocumentoServ;
    }

    @PostMapping(value = "/save" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public void saveVersionDocumento(@ModelAttribute VersionDocumentoDto versionDocumentoDto) throws IOException {
        versionDocumentoServ.saveVersionDocumento(versionDocumentoDto);
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<VersionDocumentoDto> getAllVersionDocumentos() {
        return versionDocumentoServ.getAllVersionDocumentos();
    }
    @GetMapping("/getversion/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public VersionDocumentoDto getVersionDocumento(@PathVariable Integer id) {
        return versionDocumentoServ.getVersionDocumento(id);
    }

    @SneakyThrows
    @GetMapping("/archivo/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<Resource> getFile(
            @PathVariable Integer id,
            @RequestParam(value = "download", defaultValue = "false") boolean download) {
        Resource resource = versionDocumentoServ.loadResource(id);

        Tika tika = new Tika();
        String contentType = tika.detect(resource.getInputStream());
        
        String disposition = download ? "attachment" : "inline";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @GetMapping("/getbyiddoc/{iddoc}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<VersionDocumentoDto> getVersionByDoc(@PathVariable Integer iddoc) {
        return versionDocumentoServ.getVersionByDoc(iddoc);
    }

    @GetMapping("/getbyuser/{idUser}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<VersionDocumentoDto> getByUser(@PathVariable Integer idUser) {
        return versionDocumentoServ.getVersionDocumentosByUser(idUser);
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED', 'USER')")
    public void updateVersionDocumento(@ModelAttribute VersionDocumentoDto versionDocumentoDto) throws IOException {
        versionDocumentoServ.updateVersionDocumento(versionDocumentoDto);
    }

    @DeleteMapping("/delete/{id}/{userid}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public void deleteVersionDocumento(@PathVariable Integer id, @PathVariable Integer userid) {
        versionDocumentoServ.deleteVersionDocumento(id, userid);
    }

    @GetMapping("/getbycodcas/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED', 'USER')")
    public List<VersionDocumentoDto> getByCodcas(@PathVariable String codcas) {
        return versionDocumentoServ.getVersionDocumentosByCodcas(codcas);
    }

    @GetMapping("/getbycodred/{codred}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED', 'USER')")
    public List<VersionDocumentoDto> getByCodred(@PathVariable String codred) {
        return versionDocumentoServ.getVersionDocumentosByCodred(codred);
    }




}
