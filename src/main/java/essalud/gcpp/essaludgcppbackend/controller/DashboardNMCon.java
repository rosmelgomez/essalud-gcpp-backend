package essalud.gcpp.essaludgcppbackend.controller;


import essalud.gcpp.essaludgcppbackend.service.DashboardNMServ;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/no-medica")
public class DashboardNMCon {
    private final DashboardNMServ DNMR;
    public DashboardNMCon(DashboardNMServ DNMR) {
        this.DNMR = DNMR;
    }

    /// Query Dashboard no medicas
    /// Obstetricia - Atenciones Medicas
    @GetMapping("/queryDNMOA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMOA(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMOA(anio, periodo, codcas, codasegu));
    }
    /// Obstetricia - Prenatal
    @GetMapping("/queryDNMOP/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMOP(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMOP(anio, periodo, codcas, codasegu));
    }
    /// Obstetricia - Familiar
    @GetMapping("/queryDNMOF/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMOF(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMOF(anio, periodo, codcas, codasegu));
    }
    /// Obstetricia - Complementarias
    @GetMapping("/queryDNMOC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMOC(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMOC(anio, periodo, codcas, codasegu));
    }
    /// Obstetricia - Preconcepcional
    @GetMapping("/queryDNMOPC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMOPC(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMOPC(anio, periodo, codcas, codasegu));
    }

    /// Preventivo Promocional
    ///  Preventivo Promocional - Atenciones
    @GetMapping("/queryDNMPPA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPPA(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMPPA(anio, periodo, codcas, codasegu));
    }
    ///  Preventivo Promocional - domiciliaria
    @GetMapping("/queryDNMPPD/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPPD(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMPPD(anio, periodo, codcas, codasegu));
    }
    ///  Preventivo Promocional - grupal
    @GetMapping("/queryDNMPPG/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPPG(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMPPG(anio, periodo, codcas, codasegu));
    }
    ///  Preventivo Promocional - psicoprofilaxis
    @GetMapping("/queryDNMPPP/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPPP(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMPPP(anio, periodo, codcas, codasegu));
    }
    ///  Preventivo Promocional - consejeria
    @GetMapping("/queryDNMPPC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPPC(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMPPC(anio, periodo, codcas, codasegu));
    }

    /// Nutricion
    ///  Nutricion - nutricion_total
    @GetMapping("/queryDNMNNT/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMNNT(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMNNT(anio, periodo, codcas, codasegu));
    }

    /// Enfermeria
    ///  Enfermeria - enfermeria_total
    @GetMapping("/queryDNMEET/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMEET(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMEET(anio, periodo, codcas, codasegu));
    }
    ///  Enfermeria - enfermeria_tuberculosis
    @GetMapping("/queryDNMEETU/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMEETU(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMEETU(anio, periodo, codcas, codasegu));
    }
    ///  Enfermeria - enfermeria_vih
    @GetMapping("/queryDNMEEV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMEEV(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMEEV(anio, periodo, codcas, codasegu));
    }
    ///  Enfermeria - enfermeria_cronicas_am
    @GetMapping("/queryDNMEECA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMEECA(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMEECA(anio, periodo, codcas, codasegu));
    }
    ///  Enfermeria - enfermeria_otros
    @GetMapping("/queryDNMEEO/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMEEO(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMEEO(anio, periodo, codcas, codasegu));
    }
    ///  Enfermeria - enfermeria_prev_anemia
    @GetMapping("/queryDNMEEPA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMEEPA(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMEEPA(anio, periodo, codcas, codasegu));
    }


    /// Psicologia
    /// psicologia total consultantes
    @GetMapping("/queryDNMPSTCS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPSTCS(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMPSTCS(anio, periodo, codcas, codasegu));
    }
    /// psicologia - total - consultas
    @GetMapping("/queryDNMPSTC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPSTC(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMPSTC(anio, periodo, codcas, codasegu));
    }
    /// Psicologia - horas - efectivas
    @GetMapping("/queryDNMPSHE/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPSHE(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DNMR.queryDNMPSHE(anio, periodo, codcas));
    }
    /// Psicologia - horas - programadas
    @GetMapping("/queryDNMPHP/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPHP(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DNMR.queryDNMPHP(anio, periodo, codcas));
    }
    /// Psicologia - medicos
    @GetMapping("/queryDNMPSM/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPSM(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DNMR.queryDNMPSM(anio, periodo, codcas));
    }
    /// Psicologia - procedimiento terapéutico
    @GetMapping("/queryDNMPSPT/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPSPT(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMPSPT(anio, periodo, codcas, codasegu));
    }
    /// Psicologia - procedimiento diagnóstico
    @GetMapping("/queryDNMPSPD/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPSPD(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMPSPD(anio, periodo, codcas, codasegu));
    }

    ///Trabajo Social (TS)
    ///Trabajo - social - total
    @GetMapping("/queryDNMTST/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMTST(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMTST(anio, periodo, codcas, codasegu));
    }

    /// CRED
    /// CRED -total
    @GetMapping("/queryDNMCREDT/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMCREDT(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMCREDT(anio, periodo, codcas, codasegu));
    }

    /// Indicadores de obstetricia
    @GetMapping("/queryDNMOBTASS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMOBTASS(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMOBTASS(anio, periodo, codcas, codasegu));
    }
    /// total atenciones - diagnostico
    @GetMapping("/queryDNMOBTAD/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMOBTAD(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMOBTAD(anio, periodo, codcas, codasegu));
    }
    /// total atenciones - csv
    @GetMapping("/queryDNMOBTACSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMOBTACSV(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMOBTACSV(anio, periodo, codcas, codasegu));
    }

    /// Indicadores de preventiva promocional
    @GetMapping("/queryDNMPPTASS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPPTASS(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMPPTASS(anio, periodo, codcas, codasegu));
    }
    /// total atenciones - diagnostico
    @GetMapping("/queryDNMPPTAD/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPPTAD(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMPPTAD(anio, periodo, codcas, codasegu));
    }
    /// total atenciones - csv
    @GetMapping("/queryDNMPPTACSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPPTACSV(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMPPTACSV(anio, periodo, codcas, codasegu));
    }


    /// Indicadores de nutricion
    /// total atenciones - servicio - subactividad
    @GetMapping("/queryDNMNUTASS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMNUTASS(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMNUTASS(anio, periodo, codcas, codasegu));
    }
    /// total atenciones - diagnostico
    @GetMapping("/queryDNMNUTAD/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMNUTAD(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMNUTAD(anio, periodo, codcas, codasegu));
    }
    /// total atenciones - csv
    @GetMapping("/queryDNMNUTACSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMNUTACSV(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMNUTACSV(anio, periodo, codcas, codasegu));
    }

    /// Indicadores de enfermeria
    /// total atenciones - servicio - subactividad
    @GetMapping("/queryDNMENTASS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMENTASS(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMENTASS(anio, periodo, codcas, codasegu));
    }
    /// total atenciones - diagnostico
    @GetMapping("/queryDNMENTAD/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMENTAD(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMENTAD(anio, periodo, codcas, codasegu));
    }
    /// total atenciones - csv
    @GetMapping("/queryDNMENTACSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMENTACSV(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMENTACSV(anio, periodo, codcas, codasegu));
    }

    /// Indicadores de psicologia
    /// total atenciones - servicio - subactividad
    @GetMapping("/queryDNMPSTASS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPSTASS(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMPSTASS(anio, periodo, codcas, codasegu));
    }
    /// total atenciones - diagnostico
    @GetMapping("/queryDNMPSTAD/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPSTAD(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMPSTAD(anio, periodo, codcas, codasegu));
    }
    /// total atenciones - csv
    @GetMapping("/queryDNMPSTACSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMPSTACSV(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMPSTACSV(anio, periodo, codcas, codasegu));
    }

    /// Indicadores de trabajo social
    /// total atenciones - servicio - subactividad
    @GetMapping("/queryDNMTSTASS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMTSTASS(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMTSTASS(anio, periodo, codcas, codasegu));
    }
    /// total atenciones - diagnostico
    @GetMapping("/queryDNMTSTAD/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMTSTAD(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMTSTAD(anio, periodo, codcas, codasegu));
    }
    /// total atenciones - csv
    @GetMapping("/queryDNMTSTACSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<List<Map<String, Object>>> queryDNMTSTACSV(@PathVariable int anio, @PathVariable int periodo, @PathVariable String codcas, @PathVariable int codasegu) {
        return ResponseEntity.ok(DNMR.queryDNMTSTACSV(anio, periodo, codcas, codasegu));
    }


}
