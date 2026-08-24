package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.service.FechaActServ;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/fecha")
public class FechaActCon {
    private final FechaActServ service;
    public FechaActCon(FechaActServ service) {
        this.service = service;
    }

    @GetMapping("/actualizacion")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public Map<String, Object> queryFechaActualizacion() {
        return service.queryFA();
    }

}
