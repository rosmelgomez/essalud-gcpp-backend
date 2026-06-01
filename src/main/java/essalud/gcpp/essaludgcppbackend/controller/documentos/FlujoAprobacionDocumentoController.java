package essalud.gcpp.essaludgcppbackend.controller.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.FlujoAprobacionDocumentoDto;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.documentos.FlujoAprobacionDocumentoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flujo-aprobacion-documento")
public class FlujoAprobacionDocumentoController {

    private final FlujoAprobacionDocumentoService fads;
    public FlujoAprobacionDocumentoController(FlujoAprobacionDocumentoService fads) {
        this.fads = fads;
    }

    @GetMapping("getall")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<FlujoAprobacionDocumentoDto> getDocumentos(){
        return fads.getFlujoAprobacionDocumentos();
    }

    @GetMapping("/getflujobyuser/{idUser}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED')")
    public List<Map<String, Object>> getFlujoByUser(@PathVariable Integer idUser){
        return fads.getFlujoAprobacionDocumentosByUser(idUser);
    }

    @PutMapping("update")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_RED')")
    public void updateFlujoAprobacionDocumento(@RequestBody FlujoAprobacionDocumentoDto dto){
        fads.updateFlujoAprobacionDocumento(dto);
    }

    @GetMapping("/historial/{flujoId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<Map<String, Object>> getHistorial(@PathVariable Integer flujoId) {
        return fads.getHistorialByFlujoId(flujoId);
    }

    @GetMapping("/version/{versionId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<FlujoAprobacionDocumentoDto> getFlujoByVersionId(@PathVariable Integer versionId) {
        return fads.getFlujoByVersionId(versionId);
    }
}
