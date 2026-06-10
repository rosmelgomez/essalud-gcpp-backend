package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.dtos.ReporteadorDto;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.DashboardReporService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reporteador")
public class DashboardReporController {

    private final DashboardReporService DRS;
    public DashboardReporController(DashboardReporService DRS) {
        this.DRS = DRS;
    }

    /// Dashboard Reporteador Red
    @GetMapping("/queryDRR")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDRR() {
        return ResponseEntity.ok(DRS.queryDRR());
    }

    /// Dashboard Reporteador centro
    @GetMapping("/queryDRC")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDRC() {
        return ResponseEntity.ok(DRS.queryDRC());
    }

    /// Dashboard Reporteador servicio
    @GetMapping("/queryDRS")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDRS() {
        return ResponseEntity.ok(DRS.queryDRS());
    }

    /// Dashboard Reporteador actividad
    @GetMapping("/queryDRAC")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDRAC() {
        return ResponseEntity.ok(DRS.queryDRAC());
    }

    /// Dashboard Reporteador  subactividad
    @GetMapping("/queryDRSUBAC")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDRSUBAC() {
        return ResponseEntity.ok(DRS.queryDRSUBAC());
    }

    /// Dashboard Reporteador capitulo
    @GetMapping("/queryDRCAP")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDRCAP() {
        return ResponseEntity.ok(DRS.queryDRCAP());
    }

    /// Dashboard Reporteador variable
    @GetMapping("/queryDRVAR")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDRVAR() {
        return ResponseEntity.ok(DRS.queryDRVAR());
    }

    /// Dashboard Reporteador query
    @PostMapping("/queryDRQ")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDRQ(@RequestBody ReporteadorDto repDto) {
        return ResponseEntity.ok(DRS.queryDRQ(repDto));
    }



}
