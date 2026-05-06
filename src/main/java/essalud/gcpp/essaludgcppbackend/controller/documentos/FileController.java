package essalud.gcpp.essaludgcppbackend.controller.documentos;

import java.io.IOException;
import java.util.List;

import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import essalud.gcpp.essaludgcppbackend.dtos.documento.DocumentoDto;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos.FileService;

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<byte[]> queryDMAMA(@PathVariable String name) {
        return ResponseEntity.ok(fileService.findArchivo(name));
    }


    @PostMapping(value = "/insert-archivo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public void insertArchivo(@ModelAttribute DocumentoDto documentoDto) throws IOException {
        fileService.insertDocumento(documentoDto);
    }
    @GetMapping("/get-documentos")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity< List<DocumentoDto>> listarDocumentos() {
        return ResponseEntity.ok(fileService.getDocumentos());
    }

    @PutMapping(value = "/update" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
   public void updateDocumento(@ModelAttribute DocumentoDto documentoDto) throws IOException {
        fileService.updateDocumento(documentoDto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public void deleteDocumento(@PathVariable Integer id) {
        fileService.deleteDocumento(id);
    }

    @SneakyThrows
    @GetMapping("/documento/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<Resource> getFile(@PathVariable Integer id) {
        Resource resource = fileService.loadResource(id);

        Tika tika = new Tika();
        String contentType = tika.detect(resource.getInputStream());

        System.out.println(contentType);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

}
