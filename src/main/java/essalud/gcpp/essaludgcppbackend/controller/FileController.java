package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.serviceinterfaz.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    @GetMapping("/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<byte[]> queryDMAMA(@PathVariable String name) {
        return ResponseEntity.ok(fileService.findArchivo(name));
    }

}
