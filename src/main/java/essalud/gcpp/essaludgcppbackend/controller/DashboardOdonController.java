package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.serviceinterfaz.DashboardOdonService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/odon")
public class DashboardOdonController {

    private final DashboardOdonService DOS;

    public DashboardOdonController(DashboardOdonService DOS) {
        this.DOS = DOS;
    }

    ///Dashboard odontologicas
    ///  sesiones odontologicas - Atenciones
    @GetMapping("/queryDOSOA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDOSOA(@PathVariable String anio, @PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DOS.queryDOSOA(anio, periodo, codcas, codasegu));
    }
    ///  sesiones odontologicas - horas efectivas
    @GetMapping("/queryDOSOHE/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDOSOHE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DOS.queryDOSOHE(anio, periodo, codcas));
    }
    ///  sesiones odontologicas - horas programadas
    @GetMapping("/queryDOSOHP/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDOSOHP(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DOS.queryDOSOHP(anio, periodo, codcas));
    }
    ///  sesiones odontologicas - medicos
    @GetMapping("/queryDOSOM/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDOSOM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DOS.queryDOSOM(anio, periodo, codcas));
    }
    ///  sesiones odontologicas - total consultante atencion
    @GetMapping("/queryDOSOTCA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDOSOTCA(@PathVariable String anio, @PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DOS.queryDOSOTCA(anio, periodo, codcas, codasegu));
    }
    ///  sesiones odontologicas - total consultante servicio
    @GetMapping("/queryDOSOTCS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDOSOTCS(@PathVariable String anio, @PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DOS.queryDOSOTCS(anio, periodo, codcas, codasegu));
    }
}
