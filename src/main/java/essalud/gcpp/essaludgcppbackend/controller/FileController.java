package essalud.gcpp.essaludgcppbackend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import essalud.gcpp.essaludgcppbackend.dtos.DocumentoDto;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.FileService;

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

    @PostMapping("/insert-archivo")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<Void> insertArchivo(@ModelAttribute DocumentoDto documentoDto, @RequestParam(value = "documento", required = false) MultipartFile documento) throws IOException {
        if (documento != null && !documento.isEmpty()) {
            documentoDto.setArchivo(documento.getBytes());
        }
        fileService.insertDocumento(documentoDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-documentos")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity< List<DocumentoDto>> listarDocumentos() {
        return ResponseEntity.ok(fileService.getDocumentos());
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
   public ResponseEntity<Void> updateDocumento(@RequestBody DocumentoDto documentoDto) {
        fileService.updateDocumento(documentoDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<Void> deleteDocumento(@PathVariable Integer id) {
        fileService.deleteDocumento(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/documento/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<Resource> getFile(@PathVariable Integer id) {
        Resource resource = fileService.loadResource(id);
       // String contentType=Files.probeContentType(resource.getFile().toPath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "documento")
                .contentType(MediaType.APPLICATION_PDF)
                //.contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    /*@GetMapping("/documento/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<Resource> getFile(@PathVariable Integer id) {
        return ResponseEntity.ok().body(fileService.loadResource(id));
    }*/


}
