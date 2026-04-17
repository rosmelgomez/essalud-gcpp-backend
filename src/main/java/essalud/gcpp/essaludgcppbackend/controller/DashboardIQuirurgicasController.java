package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.serviceinterfaz.DashboardIQuirurgicasService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/intervenciones-q")
public class DashboardIQuirurgicasController {
    private final DashboardIQuirurgicasService diqs;
    public DashboardIQuirurgicasController(DashboardIQuirurgicasService diqs) {
        this.diqs = diqs;
    }

    /// Query Dashboard intervenciones quirurgicas
    /// Grado de complejidad
    @GetMapping("/queryDIQGC/{anio}/{periodo}/{codcas}/{codasegu}/{complejidad}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDIQGC(@PathVariable String anio, @PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu,  @PathVariable String complejidad ) {
        return ResponseEntity.ok(diqs.queryDIQGC(anio, periodo, codcas, codasegu, complejidad));
    }

    /// emergencia
    @GetMapping("/queryDIQE/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDIQE(@PathVariable String anio, @PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(diqs.queryDIQE(anio, periodo, codcas, codasegu));
    }

    ///horas quirurgicas electivas
    @GetMapping("/queryDIQHQE/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDIQHQE(@PathVariable String anio, @PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(diqs.queryDIQHQE(anio, periodo, codcas, codasegu));
    }

    ///horas quirurgicas emergencia
    @GetMapping("/queryDIQHQEME/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDIQHQEME(@PathVariable String anio, @PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(diqs.queryDIQHQEME(anio, periodo, codcas, codasegu));
    }

    ///Indicadores de grado de complejidad
    @GetMapping("/queryDIQIGC/{anio}/{periodo}/{codcas}/{codasegu}/{complejidad}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDIQIGC(@PathVariable String anio, @PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu,  @PathVariable String complejidad ) {
        return ResponseEntity.ok(diqs.queryDIQIGC(anio, periodo, codcas, codasegu, complejidad));
    }


}
