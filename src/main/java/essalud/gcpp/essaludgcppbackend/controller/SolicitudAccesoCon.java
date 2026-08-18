package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.dtos.SolicitudAccesoDto;
import essalud.gcpp.essaludgcppbackend.service.usuario.SolicitudAccesoServ;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/solicitud")
public class SolicitudAccesoCon {

    private final SolicitudAccesoServ solicitudAccesoServ;

    public SolicitudAccesoCon(SolicitudAccesoServ solicitudAccesoServ) {
        this.solicitudAccesoServ = solicitudAccesoServ;
    }

    @PostMapping("/register")
    public void saveSolicitudAcceso(@RequestBody SolicitudAccesoDto  solicitudAccesoDto) {
        solicitudAccesoServ.saveSolicitudAcceso(solicitudAccesoDto);
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getSolicitudes(){
        return ResponseEntity.ok(solicitudAccesoServ.getSolicitudAcceso());
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateSolicitud(@RequestBody SolicitudAccesoDto solicitudAccesoDto){
       solicitudAccesoServ.updateSolicitudAcceso(solicitudAccesoDto);
    }

}
