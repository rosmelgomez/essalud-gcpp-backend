package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.dtos.SolicitudAccesoDto;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.SolicitudAccesoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/solicitud")
public class SolicitudAccesoController {

    private final SolicitudAccesoService solicitudAccesoService;

    public SolicitudAccesoController(SolicitudAccesoService solicitudAccesoService) {
        this.solicitudAccesoService = solicitudAccesoService;
    }

    @PostMapping("/register")
    public void saveSolicitudAcceso(@RequestBody SolicitudAccesoDto  solicitudAccesoDto) {
        solicitudAccesoService.saveSolicitudAcceso(solicitudAccesoDto);
    }

    @GetMapping("/getSolicitudes")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getSolicitudes(){
        return ResponseEntity.ok(solicitudAccesoService.getSolicitudAcceso());
    }

    @PutMapping("/updateSolicitud")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateSolicitud(@RequestBody SolicitudAccesoDto solicitudAccesoDto){
       solicitudAccesoService.updateSolicitudAcceso(solicitudAccesoDto);
    }

}
