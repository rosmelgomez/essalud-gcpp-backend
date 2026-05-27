package essalud.gcpp.essaludgcppbackend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import essalud.gcpp.essaludgcppbackend.serviceinterfaz.DashboardHospService;

@RestController
@RequestMapping("/hospitalizacion")
public class DashboardHospController {

    private final DashboardHospService dhs;

    public DashboardHospController(DashboardHospService dhs) {
        this.dhs = dhs;
    }

    @GetMapping("/queryDHTE/{anio}/{periodo}/{codcas}/{codasegu}")
    public ResponseEntity<List<Map<String, Object>>> queryDHTE(@PathVariable String anio, @PathVariable String periodo,
            @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(dhs.queryDHTE(anio, periodo, codcas, codasegu));
    }

}
