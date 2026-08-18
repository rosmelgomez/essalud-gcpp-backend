package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.service.DashboardSegPaciServ;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seguimiento-paciente")
public class DashboardSegPaciCon {
    private final DashboardSegPaciServ servicio;

    public  DashboardSegPaciCon(DashboardSegPaciServ servicio) {
        this.servicio = servicio;
    }

    /// Query 1 atenciones
    @GetMapping("/query1SPA/{doc}/{tipo_doc}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> query1SPA(@PathVariable String doc, @PathVariable String tipo_doc) {
        return ResponseEntity.ok(servicio.query1SPA(doc, tipo_doc));
    }

    /// Query 2 atenciones
    @GetMapping("/query2SPA/{doc}/{tipo_doc}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> query2SPA(@PathVariable String doc, @PathVariable String tipo_doc) {
        return ResponseEntity.ok(servicio.query2SPA(doc, tipo_doc));
    }


}
