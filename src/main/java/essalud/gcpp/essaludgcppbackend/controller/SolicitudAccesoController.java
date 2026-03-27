package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.dtos.SolicitudAccesoDto;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.SolicitudAccesoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
