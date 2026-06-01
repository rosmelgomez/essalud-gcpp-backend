package essalud.gcpp.essaludgcppbackend.controller.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.VersionDocumentoDto;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos.VersionDocumentoService;
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

    private final VersionDocumentoService versionDocumentoService;
    public VersionDocumentoController(VersionDocumentoService versionDocumentoService) {
        this.versionDocumentoService = versionDocumentoService;
    }

    @PostMapping(value = "/save" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public void saveVersionDocumento(@ModelAttribute VersionDocumentoDto versionDocumentoDto) throws IOException {
        versionDocumentoService.saveVersionDocumento(versionDocumentoDto);
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<VersionDocumentoDto> getAllVersionDocumentos() {
        return versionDocumentoService.getAllVersionDocumentos();
    }
    @GetMapping("/getversion/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public VersionDocumentoDto getVersionDocumento(@PathVariable Integer id) {
        return versionDocumentoService.getVersionDocumento(id);
    }

    @SneakyThrows
    @GetMapping("/archivo/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<Resource> getFile(
            @PathVariable Integer id,
            @RequestParam(value = "download", defaultValue = "false") boolean download) {
        Resource resource = versionDocumentoService.loadResource(id, download);

        Tika tika = new Tika();
        String contentType = tika.detect(resource.getInputStream());
        
        String disposition = download ? "attachment" : "inline";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @GetMapping("/getbyiddoc/{iddoc}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<VersionDocumentoDto> getVersionByDoc(@PathVariable Integer iddoc) {
        return versionDocumentoService.getVersionByDoc(iddoc);
    }

    @GetMapping("/getbyuser/{idUser}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<VersionDocumentoDto> getByUser(@PathVariable Integer idUser) {
        return versionDocumentoService.getVersionDocumentosByUser(idUser);
    }

    @PutMapping(value = "/update")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED', 'USER')")
    public void updateVersionDocumento(@RequestBody VersionDocumentoDto versionDocumentoDto) {
        versionDocumentoService.updateVersionDocumento(versionDocumentoDto);
    }

    @DeleteMapping("/delete/{id}/{userid}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED')")
    public void deleteVersionDocumento(@PathVariable Integer id, @PathVariable Integer userid) {
        versionDocumentoService.deleteVersionDocumento(id, userid);
    }

    @GetMapping("/getbycodcas/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED', 'USER')")
    public List<VersionDocumentoDto> getByCodcas(@PathVariable String codcas) {
        return versionDocumentoService.getVersionDocumentosByCodcas(codcas);
    }

    @GetMapping("/getbycodred/{codred}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED', 'USER')")
    public List<VersionDocumentoDto> getByCodred(@PathVariable String codred) {
        return versionDocumentoService.getVersionDocumentosByCodred(codred);
    }




}
