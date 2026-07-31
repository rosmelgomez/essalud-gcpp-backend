package essalud.gcpp.essaludgcppbackend.controller;


import essalud.gcpp.essaludgcppbackend.service.DashboardPatoServ;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patologia")
public class DashboardPatoCon {

    private final DashboardPatoServ service;
    public  DashboardPatoCon(DashboardPatoServ service) {
        this.service = service;
    }

    /// opcion patologias
    @GetMapping("/queryOP")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryOP() {
        return ResponseEntity.ok(service.queryOP());
    }

    /// opcion anio
    @GetMapping("/queryOA")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryOA() {
        return ResponseEntity.ok(service.queryOA());
    }

    ///opcion red centro
    @GetMapping("/queryORC")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryORC() {
        return ResponseEntity.ok(service.queryORC());
    }

    ///Analitica por patologia de alto costo
    /// pacientes totales
    @GetMapping("/queryAPACPT")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryAPACPT() {
        return ResponseEntity.ok(service.queryAPACPT());
    }

    ///comorbilidad global
    @GetMapping("/queryAPACCGB")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryAPACCGB() {
        return ResponseEntity.ok(service.queryAPACCGB());
    }

    ///comorbilidad grupo
    @GetMapping("/queryAPACCGP/{patologia}/{patron}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryAPACCGP(@PathVariable String patologia,@PathVariable String patron  ) {
        return ResponseEntity.ok(service.queryAPACCGP( patologia, patron));
    }

    ///comorbilidad grupo total
    @GetMapping("/queryAPACCGPT/{patologia}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryAPACCGPT(@PathVariable String patologia) {
        return ResponseEntity.ok(service.queryAPACCGPT( patologia));
    }

    ///comorbilidad interserccion
    @GetMapping("/queryAPACCI")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryAPACCI() {
        return ResponseEntity.ok(service.queryAPACCI());
    }


    ///comorbilidad interserccion total
    @GetMapping("/queryAPACCIT")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryAPACCIT() {
        return ResponseEntity.ok(service.queryAPACCIT());
    }

    ///Diagnostico y servicios mas frecuentes por patologia
    @GetMapping("/queryAPACPAS")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryAPACPAS(@RequestParam("anios") List<String> anios) {
        return ResponseEntity.ok(service.queryAPACPAS(anios));
    }


    /// diagnostico y servicios mas frecuente por patologia
    @GetMapping("/queryAPACDSFP")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryAPACDSFP(@RequestParam("anios") List<String> anios) {
        return ResponseEntity.ok(service.queryAPACDSFP(anios));
    }

    ///total pacientes
    @GetMapping("/queryAPACTP")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryAPACTP(@RequestParam("anios") List<String> anios) {
        return ResponseEntity.ok(service.queryAPACTP(anios));
    }

    ///mayor concentracion red
    @GetMapping("/queryAPACMCR")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryAPACMCR(@RequestParam("anios") List<String> anios) {
        return ResponseEntity.ok(service.queryAPACMCR(anios));
    }

    ///mayor concentrado centro
    @GetMapping("/queryAPACMCC")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryAPACMCC(@RequestParam("anios") List<String> anios) {
        return ResponseEntity.ok(service.queryAPACMCC(anios));
    }

}
