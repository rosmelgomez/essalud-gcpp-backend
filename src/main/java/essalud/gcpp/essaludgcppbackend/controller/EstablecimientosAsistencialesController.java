package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.serviceinterfaz.EstablecimientosAsistencialesService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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
}
