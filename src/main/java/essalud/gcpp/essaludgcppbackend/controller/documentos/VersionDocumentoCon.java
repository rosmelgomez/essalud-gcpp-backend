package essalud.gcpp.essaludgcppbackend.controller.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.VersionDocumentoDto;
import essalud.gcpp.essaludgcppbackend.service.documentos.VersionDocumentoServ;
import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("version-documento")
public class VersionDocumentoCon {

    private final VersionDocumentoServ versionDocumentoServ;
    public VersionDocumentoCon(VersionDocumentoServ versionDocumentoServ) {
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

        // Solo se muestran inline tipos que el navegador no ejecuta; HTML, SVG, XML, etc. se fuerzan a descarga
        // para evitar XSS almacenado desde archivos subidos.
        boolean inlineSafe = INLINE_SAFE_TYPES.contains(contentType);
        ContentDisposition disposition = (download || !inlineSafe ? ContentDisposition.attachment() : ContentDisposition.inline())
                .filename(resource.getFilename() != null ? resource.getFilename() : "documento", StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(inlineSafe ? MediaType.parseMediaType(contentType) : MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .header("X-Content-Type-Options", "nosniff")
                .body(resource);
    }

    private static final Set<String> INLINE_SAFE_TYPES = Set.of(
            "application/pdf", "image/png", "image/jpeg", "image/gif", "image/webp", "text/plain");


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
        // userid se mantiene en la ruta por compatibilidad; el servicio usa el usuario autenticado.
        versionDocumentoServ.deleteVersionDocumento(id);
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
