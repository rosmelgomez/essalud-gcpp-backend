package essalud.gcpp.essaludgcppbackend.controller.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.VersionDocumentoDto;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos.VersionDocumentoService;
import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
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

    @SneakyThrows
    @GetMapping("/archivo/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<Resource> getFile(@PathVariable Integer id) {
        Resource resource = versionDocumentoService.loadResource(id);

        Tika tika = new Tika();
        String contentType = tika.detect(resource.getInputStream());

        System.out.println(contentType);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }


    @GetMapping("/getvdbyiddoc/{iddoc}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<VersionDocumentoDto> getD(@PathVariable Integer iddoc) {
        return versionDocumentoService.getVersionDocumentos(iddoc);
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED', 'USER')")
    public void updateVersionDocumento(@RequestBody VersionDocumentoDto versionDocumentoDto)  throws IOException  {
        versionDocumentoService.updateVersionDocumento(versionDocumentoDto);
    }

}
