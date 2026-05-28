package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.serviceinterfaz.DashboardTransplanteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transplante")
public class DashboardTransplanteController {

    private final DashboardTransplanteService service;
    public DashboardTransplanteController(DashboardTransplanteService service) {
        this.service = service;
    }

    /// transplante
    @GetMapping("/queryDTrans/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDTrans(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(service.queryDTrans(anio, periodo, codcas, codasegu));
    }




}
