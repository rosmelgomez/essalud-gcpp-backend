package essalud.gcpp.essaludgcppbackend.controller.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.SolicitudAprobacionDocumentoDto;
import essalud.gcpp.essaludgcppbackend.service.documentos.SolicitudAprobacionDocumentoServ;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/solicitud-aprobacion-documento")
public class SolicitudAprobacionDocumentoCon {

    private final SolicitudAprobacionDocumentoServ sads;
    public SolicitudAprobacionDocumentoCon(SolicitudAprobacionDocumentoServ sads) {
        this.sads = sads;
    }

    @GetMapping("getall")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<SolicitudAprobacionDocumentoDto> getDocumentos(){
        return sads.getFlujoAprobacionDocumentos();
    }

    @GetMapping("/getflujobyuser/{idUser}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED')")
    public List<Map<String, Object>> getFlujoByUser(@PathVariable Integer idUser){
        return sads.getFlujoAprobacionDocumentosByUser(idUser);
    }

    @PutMapping("update")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_RED')")
    public void updateFlujoAprobacionDocumento(@RequestBody SolicitudAprobacionDocumentoDto dto){
        sads.updateFlujoAprobacionDocumento(dto);
    }

    @GetMapping("/historial/{flujoId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<Map<String, Object>> getHistorial(@PathVariable Integer flujoId) {
        return sads.getHistorialByFlujoId(flujoId);
    }

    @GetMapping("/version/{versionId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<SolicitudAprobacionDocumentoDto> getFlujoByVersionId(@PathVariable Integer versionId) {
        return sads.getFlujoByVersionId(versionId);
    }
}
