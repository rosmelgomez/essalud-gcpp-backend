package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.serviceinterfaz.EstablecimientosAsistencialesService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/establecimiento")
public class EstablecimientosAsistencialesController {

    private final EstablecimientosAsistencialesService EAS;

    public EstablecimientosAsistencialesController(EstablecimientosAsistencialesService EAS) {
        this.EAS = EAS;
    }

    @GetMapping("/getCentros")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> getCentros() {
        return ResponseEntity.ok(EAS.getCentros());
    }

    @GetMapping("/getCentroAsistencialByRed/{code_red}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> getCentrosByCodeRed(@PathVariable String code_red) {
        return ResponseEntity.ok(EAS.getCentrosByCodeRed(code_red));
    }

    @GetMapping("/getRedes")
    public ResponseEntity<List<Map<String, Object>>> getRedes() {
        return ResponseEntity.ok(EAS.getRedes());
    }

    @GetMapping("/getRedesWithCentros")
    public ResponseEntity<List<Map<String, Object>>> getRedesWithCentros() {
        return ResponseEntity.ok(EAS.getRedesWithCentros());
    }
}
