package essalud.gcpp.essaludgcppbackend.controller.documentos;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import essalud.gcpp.essaludgcppbackend.service.documentos.FileServ;

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileServ fileServ;

    public FileController(FileServ fileServ) {
        this.fileServ = fileServ;
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<byte[]> queryDMAMA(@PathVariable String name) {
        return ResponseEntity.ok(fileServ.findArchivo(name));
    }

}
