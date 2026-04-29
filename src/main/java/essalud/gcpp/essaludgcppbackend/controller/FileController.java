package essalud.gcpp.essaludgcppbackend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    public List<DocumentoDto> listarDocumentos() {
        return fileService.getDocumentos();
    }






}
