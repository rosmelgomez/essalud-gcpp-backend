package essalud.gcpp.essaludgcppbackend.controller;


import essalud.gcpp.essaludgcppbackend.service.DashboardFarmServ;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/farmacia")
public class DashboardFarmCon {

    private final DashboardFarmServ service;

    public DashboardFarmCon(DashboardFarmServ service) {
        this.service = service;
    }

    /// Recetas atendidas
    @GetMapping("/queryDFRA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDFRA(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(service.queryDFRA(anio, periodo, codcas, codasegu));
    }


    /// csv
    @GetMapping("/queryDFCSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDFCSV(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(service.queryDFCSV(anio, periodo, codcas, codasegu));
    }

}
