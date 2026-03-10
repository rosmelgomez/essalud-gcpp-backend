package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.serviceinterfaz.DashboardMService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/medica")
public class DashboardMController {

    private final DashboardMService DMS;
    public DashboardMController(DashboardMService DMS) {
        this.DMS = DMS;
    }

    /// Query Dashboard medicas
    /// Atenciones Medicas
    @GetMapping("/queryDMAMA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMAMA(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMAMA(anio, periodo, codcas, codasegu));
    }
    /// Atenciones médicas - horas efectivas
    @GetMapping("/queryDMAMHE/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMAMHE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMAMHE(anio, periodo, codcas));
    }
    /// Atenciones médicas - horas programadas
    @GetMapping("/queryDMAMHP/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMAMHP(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMAMHP(anio, periodo, codcas));
    }
    /// Atenciones médicas - citadas
    @GetMapping("/queryDMAMC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMAMC(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMAMC(anio, periodo, codcas, codasegu));
    }
    /// Atenciones médicas - desercion
    @GetMapping("/queryDMAMD/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMAMD(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMAMD(anio, periodo, codcas, codasegu));
    }
    /// Atenciones médicas - medicos
    @GetMapping("/queryDMAMM/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMAMM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMAMM(anio, periodo, codcas));
    }
    /// Atenciones médicas - total consultante
    @GetMapping("/queryDMAMTC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMAMTC(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMAMTC(anio, periodo, codcas, codasegu));
    }
    /// Medicinas Complementaria - atenciones
    @GetMapping("/queryDMAMTCS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMAMTCS(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMAMTCS(anio, periodo, codcas, codasegu));
    }

    /// Medicinas Complementaria - atenciones
    @GetMapping("/queryDMMCA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMCA(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMMCA(anio, periodo, codcas, codasegu));
    }
    /// Medicinas Complementaria - horas efectivas
    @GetMapping("/queryDMMCHE/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMCHE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMMCHE(anio, periodo, codcas));
    }
    /// Medicinas Complementaria - horas programadas
    @GetMapping("/queryDMMCHP/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMCHP(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMMCHP(anio, periodo, codcas));
    }
    /// Medicinas Complementaria - medicos
    @GetMapping("/queryDMMCM/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMCM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMMCM(anio, periodo, codcas));
    }
    /// Medicinas Complementaria - total consultante
    @GetMapping("/queryDMMCTC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMCTC(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMMCTC(anio, periodo, codcas, codasegu));
    }
    /// Medicinas Complementaria - total consultante servicio
    @GetMapping("/queryDMMCTCS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMCTCS(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMMCTCS(anio, periodo, codcas, codasegu));
    }

    /// Medicina Ocupacional - atenciones
    @GetMapping("/queryDMMOA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMOA(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMMOA(anio, periodo, codcas, codasegu));
    }
    /// Medicina Ocupacional - horas efectivas
    @GetMapping("/queryDMMOHE/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMOHE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMMOHE(anio, periodo, codcas));
    }
    /// Medicina Ocupacional - horas programadas
    @GetMapping("/queryDMMOHP/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMOHP(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMMOHP(anio, periodo, codcas));
    }
    /// Medicina Ocupacional - medicos
    @GetMapping("/queryDMMOM/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMOM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMMOM(anio, periodo, codcas));
    }
    /// Medicina Ocupacional - total consultante
    @GetMapping("/queryDMMOTC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMOTC(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMMOTC(anio, periodo, codcas, codasegu));
    }
    /// Medicina Ocupacional - total consultante servicio
    @GetMapping("/queryDMMOTCS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMOTCS(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMMOTCS(anio, periodo, codcas, codasegu));
    }

    /// Médico Personal - Atenciones
    @GetMapping("/queryDMMPA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMPA(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMMPA(anio, periodo, codcas, codasegu));
    }
    /// Médico Personal - Horas Efectivas
    @GetMapping("/queryDMMPHE/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMPHE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMMPHE(anio, periodo, codcas));
    }
    /// Médico Personal - Horas Programadas
    @GetMapping("/queryDMMPHP/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMPHP(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMMPHP(anio, periodo, codcas));
    }
    /// Médico Personal - Medicos
    @GetMapping("/queryDMMPM/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMPM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMMPM(anio, periodo, codcas));
    }
    /// Médico Personal - Total Consultante
    @GetMapping("/queryDMMPTC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMPTC(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMMPTC(anio, periodo, codcas, codasegu));
    }
    /// Médico Personal - Total Consultante Servicio
    @GetMapping("/queryDMMPTCS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMPTCS(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMMPTCS(anio, periodo, codcas, codasegu));
    }

    /// Consulta Atención Inmediata - atenciones
    @GetMapping("/queryDMCAIA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMCAIA(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMCAIA(anio, periodo, codcas, codasegu));
    }
    /// Consulta Atención Inmediata - horas efectivas
    @GetMapping("/queryDMCAIHE/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMCAIHE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMCAIHE(anio, periodo, codcas));
    }
    /// Consulta Atención Inmediata - horas programadas
    @GetMapping("/queryDMCAIHP/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMCAIHP(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMCAIHP(anio, periodo, codcas));
    }
    /// Consulta Atención Inmediata - medicos
    @GetMapping("/queryDMCAIM/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMCAIM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMCAIM(anio, periodo, codcas));
    }
    /// Consulta Atención Inmediata - total de consultante
    @GetMapping("/queryDMCAITC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMCAITC(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMCAITC(anio, periodo, codcas, codasegu));
    }
    /// Consulta Atención Inmediata - total de consultante servicio
    @GetMapping("/queryDMCAITCS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMCAITCS(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMCAITCS(anio, periodo, codcas, codasegu));
    }
    /// Consulta Apoyo Desc - atenciones
    @GetMapping("/queryDMCADA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMCADA(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMCADA(anio, periodo, codcas, codasegu));
    }
    /// Consulta Apoyo Desc - horas efectivas
    @GetMapping("/queryDMCADHE/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMCADHE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMCADHE(anio, periodo, codcas));
    }
    /// Consulta Apoyo Desc - horas programadas
    @GetMapping("/queryDMCADHP/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMCADHP(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMCADHP(anio, periodo, codcas));
    }
    /// Consulta Apoyo Desc - medicos
    @GetMapping("/queryDMCADM/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMCADM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMCADM(anio, periodo, codcas));
    }
    /// Consulta Apoyo Desc - total consultante
    @GetMapping("/queryDMCADTC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMCADTC(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMCADTC(anio, periodo, codcas, codasegu));
    }
    /// Consulta Apoyo Desc - total consultante servicio
    @GetMapping("/queryDMCADTCS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMCADTCS(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.queryDMCADTCS(anio, periodo, codcas, codasegu));
    }

    /// Indicadores de atenciones medicas
    /// total_atenciones - servicio - subactividad
    @GetMapping("/query0DMAMTASS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query0DMAMTASS(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query0DMAMTASS(anio, periodo, codcas, codasegu));
    }
    /// total_atenciones - agrupador - especialidad
    @GetMapping("/query1DMAMTAAE/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query1DMAMTAAE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query1DMAMTAAE(anio, periodo, codcas, codasegu));
    }
    /// total_atenciones  - no considerados
    @GetMapping("/query2DMAMTANC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query2DMAMTANC(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query2DMAMTANC(anio, periodo, codcas, codasegu));
    }
    /// total_atenciones  - sexo grupo etario
    @GetMapping("/query3DMAMTASGE/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query3DMAMTASGE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query3DMAMTASGE(anio, periodo, codcas, codasegu));
    }
    /// total_atenciones  - CSV
    @GetMapping("/query4DMAMTACSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query4DMAMTACSV(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query4DMAMTACSV(anio, periodo, codcas, codasegu));
    }
    /// total_medicos - query 0
    @GetMapping("/query0DMAMTM/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query0DMAMTM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query0DMAMTM(anio, periodo, codcas, codasegu));
    }
    /// total_medicos - query 1
    @GetMapping("/query1DMAMTM/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query1DMAMTM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query1DMAMTM(anio, periodo, codcas, codasegu));
    }
    /// total_medicos - query 2 csv
    @GetMapping("/query2DMAMTMCSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query2DMAMTMCSV(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query2DMAMTMCSV(anio, periodo, codcas, codasegu));
    }
    /// total_horas_efectivas
    @GetMapping("/query0DMAMHE/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query0DMAMHE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.query0DMAMHE(anio, periodo, codcas));
    }
    /// total_horas_programadas
    @GetMapping("/query0DMAMHP/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query0DMAMHP(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.query0DMAMHP(anio, periodo, codcas));
    }
    /// total_citados
    @GetMapping("/query0DMAMTC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query0DMAMTC(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query0DMAMTC(anio, periodo, codcas, codasegu));
    }

    /// total_deserción - query 0
    @GetMapping("/query0DMAMTD/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query0DMAMTD(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query0DMAMTD(anio, periodo, codcas, codasegu));
    }
    /// total_deserción - query 1
    @GetMapping("/query1DMAMTD/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query1DMAMTD(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query1DMAMTD(anio, periodo, codcas, codasegu));
    }
    /// total_deserción - query 2 csv
    @GetMapping("/query2DMAMTDCSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query2DMAMTDCSV(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query2DMAMTDCSV(anio, periodo, codcas, codasegu));
    }


    ///Indicadores de medicina complementaria
    /// total - atenciones- servicio - subactividad
    @GetMapping("/query0DMMCTASS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query0DMMCTASS(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query0DMMCTASS(anio, periodo, codcas, codasegu));
    }
    /// total - atenciones- agrupador - especialidad
    @GetMapping("/query1DMMCTAAE/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query1DMMCTAAE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query1DMMCTAAE(anio, periodo, codcas, codasegu));
    }
    /// Total - atenciones - no considerados
    @GetMapping("/query2DMMCTANC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query2DMMCTANC(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query2DMMCTANC(anio, periodo, codcas, codasegu));
    }
    /// Total - atenciones- sexo y grupo etario vs atenciones
    @GetMapping("/query3DMMCTASGEA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query3DMMCTASGEA(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query3DMMCTASGEA(anio, periodo, codcas, codasegu));
    }
    /// Total - atenciones- CSV
    @GetMapping("/query4DMMCTACSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query4DMMCTACSV(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query4DMMCTACSV(anio, periodo, codcas, codasegu));
    }
    /// total - medicos query0
    @GetMapping("/query0DMMCTM/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query0DMMCTM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query0DMMCTM(anio, periodo, codcas, codasegu));
    }
    /// total - medicos query1
    @GetMapping("/query1DMMCTM/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query1DMMCTM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query1DMMCTM(anio, periodo, codcas, codasegu));
    }
    /// Total - medicos query2 CSV
    @GetMapping("/query2DMMCTMCSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query2DMMCTMCSV(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query2DMMCTMCSV(anio, periodo, codcas, codasegu));
    }
    /// Total - horas- programadas
    @GetMapping("/queryDMMCTHP/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMCTHP(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMMCTHP(anio, periodo, codcas));
    }
    /// Total - horas- efectivas
    @GetMapping("/queryDMMCTHE/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMCTHE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMMCTHE(anio, periodo, codcas));
    }

    ///Indicadores de medicina ocupacional
    /// total - atenciones- servicio - subactividad
    @GetMapping("/query0DMMOTASS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query0DMMOTASS(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query0DMMOTASS(anio, periodo, codcas, codasegu));
    }
    /// total - atenciones- agrupador - especialidad
    @GetMapping("/query1DMMOTAAE/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query1DMMOTAAE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query1DMMOTAAE(anio, periodo, codcas, codasegu));
    }
    /// Total - atenciones - no considerados
    @GetMapping("/query2DMMOTANC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query2DMMOTANC(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query2DMMOTANC(anio, periodo, codcas, codasegu));
    }
    /// Total - atenciones- sexo y grupo etario vs atenciones
    @GetMapping("/query3DMMOTASGEA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query3DMMOTASGEA(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query3DMMOTASGEA(anio, periodo, codcas, codasegu));
    }
    /// Total - atenciones- CSV
    @GetMapping("/query4DMMOTACSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query4DMMOTACSV(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query4DMMOTACSV(anio, periodo, codcas, codasegu));
    }
    /// total - medicos query0
    @GetMapping("/query0DMOCTM/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query0DMOCTM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query0DMOCTM(anio, periodo, codcas, codasegu));
    }
    /// total - medicos query1
    @GetMapping("/query1DMOCTM/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query1DMOCTM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query1DMOCTM(anio, periodo, codcas, codasegu));
    }
    /// Total - medicos query2 CSV
    @GetMapping("/query2DMMOTMCSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query2DMMOTMCSV(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query2DMMOTMCSV(anio, periodo, codcas, codasegu));
    }
    /// Total - horas- programadas
    @GetMapping("/queryDMMOTHP/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMOTHP(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMMOTHP(anio, periodo, codcas));
    }
    /// Total - horas- efectivas
    @GetMapping("/queryDMMOTHE/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMOTHE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMMOTHE(anio, periodo, codcas));
    }

    ///Indicadores de medico de personal
    /// total - atenciones- servicio - subactividad
    @GetMapping("/query0DMMPTASS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query0DMMPTASS(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query0DMMPTASS(anio, periodo, codcas, codasegu));
    }
    /// total - atenciones- agrupador - especialidad
    @GetMapping("/query1DMMPTAAE/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query1DMMPTAAE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query1DMMPTAAE(anio, periodo, codcas, codasegu));
    }
    /// Total - atenciones - no considerados
    @GetMapping("/query2DMMPTANC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query2DMMPTANC(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query2DMMPTANC(anio, periodo, codcas, codasegu));
    }
    /// Total - atenciones- sexo y grupo etario vs atenciones
    @GetMapping("/query3DMMPTASGEA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query3DMMPTASGEA(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query3DMMPTASGEA(anio, periodo, codcas, codasegu));
    }
    /// Total - atenciones- CSV
    @GetMapping("/query4DMMPTACSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query4DMMPTACSV(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query4DMMPTACSV(anio, periodo, codcas, codasegu));
    }
    /// total - medicos query0
    @GetMapping("/query0DMMPTM/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query0DMMPTM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query0DMMPTM(anio, periodo, codcas, codasegu));
    }
    /// total - medicos query1
    @GetMapping("/query1DMMPTM/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query1DMMPTM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query1DMMPTM(anio, periodo, codcas, codasegu));
    }
    /// Total - medicos query2 CSV
    @GetMapping("/query2DMMPTMCSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query2DMMPTMCSV(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query2DMMPTMCSV(anio, periodo, codcas, codasegu));
    }
    /// Total - horas- programadas
    @GetMapping("/queryDMMPTHP/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMPTHP(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMMPTHP(anio, periodo, codcas));
    }
    /// Total - horas- efectivas
    @GetMapping("/queryDMMPTHE/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMMPTHE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMMPTHE(anio, periodo, codcas));
    }


    ///Indicadores de consulta de atencion inmediata
    /// total - atenciones- servicio - subactividad
    @GetMapping("/query0DMCAITASS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query0DMCAITASS(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query0DMCAITASS(anio, periodo, codcas, codasegu));
    }
    /// total - atenciones- agrupador - especialidad
    @GetMapping("/query1DMCAITAAE/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query1DMCAITAAE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query1DMCAITAAE(anio, periodo, codcas, codasegu));
    }
    /// Total - atenciones - no considerados
    @GetMapping("/query2DMCAITANC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query2DMCAITANC(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query2DMCAITANC(anio, periodo, codcas, codasegu));
    }
    /// Total - atenciones- sexo y grupo etario vs atenciones
    @GetMapping("/query3DMCAITASGEA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query3DMCAITASGEA(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query3DMCAITASGEA(anio, periodo, codcas, codasegu));
    }
    /// Total - atenciones- CSV
    @GetMapping("/query4DMCAITACSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query4DMCAITACSV(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query4DMCAITACSV(anio, periodo, codcas, codasegu));
    }
    /// total - medicos query0
    @GetMapping("/query0DMCAITM/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query0DMCAITM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query0DMCAITM(anio, periodo, codcas, codasegu));
    }
    /// total - medicos query1
    @GetMapping("/query1DMCAITM/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query1DMCAITM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query1DMCAITM(anio, periodo, codcas, codasegu));
    }
    /// Total - medicos query2 CSV
    @GetMapping("/query2DMCAITMCSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query2DMCAITMCSV(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query2DMCAITMCSV(anio, periodo, codcas, codasegu));
    }
    /// Total - horas- programadas
    @GetMapping("/queryDMCAITHP/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMCAITHP(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMCAITHP(anio, periodo, codcas));
    }
    /// Total - horas- efectivas
    @GetMapping("/queryDMCAITHE/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMCAITHE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMCAITHE(anio, periodo, codcas));
    }


    ///Indicadores de cons apoyo desc
    /// total - atenciones- servicio - subactividad
    @GetMapping("/query0DMCADTASS/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query0DMCADTASS(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query0DMCADTASS(anio, periodo, codcas, codasegu));
    }
    /// total - atenciones- agrupador - especialidad
    @GetMapping("/query1DMCADTAAE/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query1DMCADTAAE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query1DMCADTAAE(anio, periodo, codcas, codasegu));
    }
    /// Total - atenciones - no considerados
    @GetMapping("/query2DMCADTANC/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query2DMCADTANC(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query2DMCADTANC(anio, periodo, codcas, codasegu));
    }
    /// Total - atenciones- sexo y grupo etario vs atenciones
    @GetMapping("/query3DMCADTASGEA/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query3DMCADTASGEA(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query3DMCADTASGEA(anio, periodo, codcas, codasegu));
    }
    /// Total - atenciones- CSV
    @GetMapping("/query4DMCADTACSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query4DMCADTACSV(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query4DMCADTACSV(anio, periodo, codcas, codasegu));
    }
    /// total - medicos query0
    @GetMapping("/query0DMCADTM/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query0DMCADTM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query0DMCADTM(anio, periodo, codcas, codasegu));
    }
    /// total - medicos query1
    @GetMapping("/query1DMCADTM/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query1DMCADTM(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query1DMCADTM(anio, periodo, codcas, codasegu));
    }
    /// Total - medicos query2 CSV
    @GetMapping("/query2DMCADTMCSV/{anio}/{periodo}/{codcas}/{codasegu}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> query2DMCADTMCSV(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas, @PathVariable String codasegu) {
        return ResponseEntity.ok(DMS.query2DMCADTMCSV(anio, periodo, codcas, codasegu));
    }
    /// Total - horas- programadas
    @GetMapping("/queryDMCADTHP/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMCADTHP(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMCADTHP(anio, periodo, codcas));
    }
    /// Total - horas- efectivas
    @GetMapping("/queryDMCADTHE/{anio}/{periodo}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<List<Map<String, Object>>> queryDMCADTHE(@PathVariable String anio,@PathVariable String periodo, @PathVariable String codcas) {
        return ResponseEntity.ok(DMS.queryDMCADTHE(anio, periodo, codcas));
    }


    }
