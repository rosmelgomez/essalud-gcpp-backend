package essalud.gcpp.essaludgcppbackend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import essalud.gcpp.essaludgcppbackend.service.DashboardHospServ;

@RestController
@RequestMapping("/hospitalizacion")
public class DashboardHospCon {

    private final DashboardHospServ dhs;

    public DashboardHospCon(DashboardHospServ dhs) {
        this.dhs = dhs;
    }

    @GetMapping("/queryDHE/{anio}/{periodo}/{codcas}/{codasegu}")
    public ResponseEntity<List<Map<String, Object>>> queryDHE(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(dhs.queryDHE(anio, periodo, codcas, codasegu));
    }

}
