package essalud.gcpp.essaludgcppbackend.controller.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.TipoDocumentoDto;
import essalud.gcpp.essaludgcppbackend.service.documentos.TipoDocumentoServ;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("tipo-documento")
public class TipoDocumentoCon {

    private final TipoDocumentoServ tds;
    public TipoDocumentoCon(TipoDocumentoServ tds) {
        this.tds = tds;
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void saveTipoDocumento(@RequestBody TipoDocumentoDto tipoDocumentoDto) {
        tds.saveTipoDocumento(tipoDocumentoDto);
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<TipoDocumentoDto> getTipoDocumentos() {
        return tds.getTipoDocumentos();
    }


    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateTipoDocumento(@RequestBody TipoDocumentoDto tipoDocumentoDto) {
        tds.updateTipoDocumento(tipoDocumentoDto);
    }
}
