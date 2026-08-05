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

    ///patologia -> area ->servicio
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

    ///ranking red
    @GetMapping("/queryAPACRR")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryAPACRR(@RequestParam("anios") List<String> anios) {
        return ResponseEntity.ok(service.queryAPACRR(anios));
    }

    /// Detalle por patología
    /// total de pacientes
    @GetMapping("/queryDPTP")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDPTP(@RequestParam("patologias") List<String> patologias, @RequestParam("anios") List<String> anios , @RequestParam("codreds") List<String> codreds, @RequestParam("codcens") List<String> codcens, @RequestParam("grupo_edades") List<String> grupo_edades) {
        return ResponseEntity.ok(service.queryDPTP(patologias, anios, codreds, codcens, grupo_edades));
    }

    ///Total diagnosticos distintos
    @GetMapping("/queryDPTDD")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDPTDD(@RequestParam("patologias") List<String> patologias, @RequestParam("anios") List<String> anios , @RequestParam("codreds") List<String> codreds, @RequestParam("codcens") List<String> codcens, @RequestParam("grupo_edades") List<String> grupo_edades) {
        return ResponseEntity.ok(service.queryDPTDD(patologias, anios, codreds, codcens, grupo_edades));
    }

    /// distribucion por sexo
    @GetMapping("/queryDPDS")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDPDS(@RequestParam("patologias") List<String> patologias, @RequestParam("anios") List<String> anios , @RequestParam("codreds") List<String> codreds, @RequestParam("codcens") List<String> codcens, @RequestParam("grupo_edades") List<String> grupo_edades) {
        return ResponseEntity.ok(service.queryDPDS(patologias, anios, codreds, codcens, grupo_edades));
    }

    ///Distribucion por grupo etario
    @GetMapping("/queryDPDGE")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDPDGE(@RequestParam("patologias") List<String> patologias, @RequestParam("anios") List<String> anios , @RequestParam("codreds") List<String> codreds, @RequestParam("codcens") List<String> codcens) {
        return ResponseEntity.ok(service.queryDPDGE(patologias, anios, codreds, codcens));
    }

    ///Tabla diagnosticos
    @GetMapping("/queryDPTD")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDPTD(@RequestParam("patologias") List<String> patologias, @RequestParam("anios") List<String> anios , @RequestParam("codreds") List<String> codreds, @RequestParam("codcens") List<String> codcens, @RequestParam("grupo_edades") List<String> grupo_edades) {
        return ResponseEntity.ok(service.queryDPTD(patologias, anios, codreds, codcens, grupo_edades));
    }

    ///evolucion anual
    @GetMapping("/queryDPEA")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDPEA(@RequestParam("patologias") List<String> patologias, @RequestParam("codreds") List<String> codreds, @RequestParam("codcens") List<String> codcens, @RequestParam("grupo_edades") List<String> grupo_edades) {
        return ResponseEntity.ok(service.queryDPEA(patologias, codreds, codcens, grupo_edades));
    }

}
