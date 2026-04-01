package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.serviceinterfaz.DashboardEmeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/eme")
public class DashboardEmeController {

    private final DashboardEmeService DES;
    public DashboardEmeController(DashboardEmeService DES) {
        this.DES = DES;
    }
    @GetMapping("/queryDEP/{anio}/{periodo}/{codcas}/{codasegu}/{prioridad}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDEP(@PathVariable String anio, @PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu , @PathVariable String prioridad) {
        return ResponseEntity.ok(DES.queryDEP(anio, periodo, codcas, codasegu, prioridad));
    }
    /// defunciones
    @GetMapping("/queryDED/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<Map<String, Object>> queryDED(@PathVariable String anio, @PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return DES.queryDED(anio, periodo, codcas, codasegu);
    }
    /// mayor a 24h
    @GetMapping("/queryDEMY24HR/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<Map<String, Object>> queryDEMY24HR(@PathVariable String anio, @PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return DES.queryDEMY24HR(anio, periodo, codcas, codasegu);
    }
    /// menor a 24h
    @GetMapping("/queryDEMN24HR/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<Map<String, Object>> queryDEMN24HR(@PathVariable String anio, @PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return DES.queryDEMN24HR(anio, periodo, codcas, codasegu);
    }
}
