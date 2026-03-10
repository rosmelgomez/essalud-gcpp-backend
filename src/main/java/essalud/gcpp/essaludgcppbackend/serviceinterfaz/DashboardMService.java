package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardMRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardMService {

    private final DashboardMRepository DMR;
    public DashboardMService(DashboardMRepository DMR) {
        this.DMR = DMR;
    }

    /// Query Dashboard medicas
    /// Atenciones Medicas
    public List<Map<String, Object>> queryDMAMA(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMAMA(anio, periodo, codcas, codasegu);
    }
    /// Atenciones médicas - horas efectivas
    public List<Map<String, Object>> queryDMAMHE(String anio, String periodo, String codcas){
        return DMR.queryDMAMHE(anio, periodo, codcas);
    }
    /// Atenciones médicas - horas programadas
    public List<Map<String, Object>> queryDMAMHP(String anio, String periodo, String codcas){
        return DMR.queryDMAMHP(anio, periodo, codcas);
    }
    /// Atenciones médicas - citadas
    public List<Map<String, Object>> queryDMAMC(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMAMC(anio, periodo, codcas, codasegu);
    }
    /// Atenciones médicas - desercion
    public List<Map<String, Object>> queryDMAMD(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMAMD(anio, periodo, codcas, codasegu);
    }
    /// Atenciones médicas - medicos
    public List<Map<String, Object>> queryDMAMM(String anio, String periodo, String codcas){
        return DMR.queryDMAMM(anio, periodo, codcas);
    }
    /// Atenciones médicas - total consultante
    public List<Map<String, Object>> queryDMAMTC(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMAMTC(anio, periodo, codcas, codasegu);
    }
    /// Atenciones médicas - total consultante servicio
    public List<Map<String, Object>> queryDMAMTCS(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMAMTCS(anio, periodo, codcas, codasegu);
    }

    /// Medicinas Complementaria - atenciones
    public List<Map<String, Object>> queryDMMCA(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMMCA(anio, periodo, codcas, codasegu);
    }
    /// Medicinas Complementaria - horas efectivas
    public List<Map<String, Object>> queryDMMCHE(String anio, String periodo, String codcas){
        return DMR.queryDMMCHE(anio, periodo, codcas);
    }
    /// Medicinas Complementaria - horas programadas
    public List<Map<String, Object>> queryDMMCHP(String anio, String periodo, String codcas){
        return DMR.queryDMMCHP(anio, periodo, codcas);
    }
    /// Medicinas Complementaria - medicos
    public List<Map<String, Object>> queryDMMCM(String anio, String periodo, String codcas){
        return DMR.queryDMMCM(anio, periodo, codcas);
    }
    /// Medicinas Complementaria - total consultante
    public List<Map<String, Object>> queryDMMCTC(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMMCTC(anio, periodo, codcas, codasegu);
    }
    /// Medicinas Complementaria - total consultante servicio
    public List<Map<String, Object>> queryDMMCTCS(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMMCTCS(anio, periodo, codcas, codasegu);
    }

    /// Medicina Ocupacional - atenciones
    public List<Map<String, Object>> queryDMMOA(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMMOA(anio, periodo, codcas, codasegu);
    }
    /// Medicina Ocupacional - horas efectivas
    public List<Map<String, Object>> queryDMMOHE(String anio, String periodo, String codcas){
        return DMR.queryDMMOHE(anio, periodo, codcas);
    }
    /// Medicina Ocupacional - horas programadas
    public List<Map<String, Object>> queryDMMOHP(String anio, String periodo, String codcas){
        return DMR.queryDMMOHP(anio, periodo, codcas);
    }
    /// Medicina Ocupacional - medicos
    public List<Map<String, Object>> queryDMMOM(String anio, String periodo, String codcas){
        return DMR.queryDMMOM(anio, periodo, codcas);
    }
    /// Medicina Ocupacional - total consultante
    public List<Map<String, Object>> queryDMMOTC(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMMOTC(anio, periodo, codcas, codasegu);
    }
    /// Medicina Ocupacional - total consultante servicio
    public List<Map<String, Object>> queryDMMOTCS(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMMOTCS(anio, periodo, codcas, codasegu);
    }

    /// Médico Personal - Atenciones
    public List<Map<String, Object>> queryDMMPA(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMMPA(anio, periodo, codcas, codasegu);
    }
    /// Médico Personal - Horas Efectivas
    public List<Map<String, Object>> queryDMMPHE(String anio, String periodo, String codcas){
        return DMR.queryDMMPHE(anio, periodo, codcas);
    }
    /// Médico Personal - Horas Programadas
    public List<Map<String, Object>> queryDMMPHP(String anio, String periodo, String codcas){
        return DMR.queryDMMPHP(anio, periodo, codcas);
    }
    /// Médico Personal - Medicos
    public List<Map<String, Object>> queryDMMPM(String anio, String periodo, String codcas){
        return DMR.queryDMMPM(anio, periodo, codcas);
    }
    /// Médico Personal - Total Consultante
    public List<Map<String, Object>> queryDMMPTC(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMMPTC(anio, periodo, codcas, codasegu);
    }
    /// Médico Personal - Total Consultante Servicio
    public List<Map<String, Object>> queryDMMPTCS(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMMPTCS(anio, periodo, codcas, codasegu);
    }

    /// Consulta Atención Inmediata - atenciones
    public List<Map<String, Object>> queryDMCAIA(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMCAIA(anio, periodo, codcas, codasegu);
    }
    /// Consulta Atención Inmediata - horas efectivas
    public List<Map<String, Object>> queryDMCAIHE(String anio, String periodo, String codcas){
        return DMR.queryDMCAIHE(anio, periodo, codcas);
    }
    /// Consulta Atención Inmediata - horas programadas
    public List<Map<String, Object>> queryDMCAIHP(String anio, String periodo, String codcas){
        return DMR.queryDMCAIHP(anio, periodo, codcas);
    }
    /// Consulta Atención Inmediata - medicos
    public List<Map<String, Object>> queryDMCAIM(String anio, String periodo, String codcas){
        return DMR.queryDMCAIM(anio, periodo, codcas);
    }
    /// Consulta Atención Inmediata - total de consultante
    public List<Map<String, Object>> queryDMCAITC(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMCAITC(anio, periodo, codcas, codasegu);
    }
    /// Consulta Atención Inmediata - total de consultante servicio
    public List<Map<String, Object>> queryDMCAITCS(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMCAITCS(anio, periodo, codcas, codasegu);
    }

    /// Consulta Apoyo Desc - atenciones
    public List<Map<String, Object>> queryDMCADA(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMCADA(anio, periodo, codcas, codasegu);
    }
    /// Consulta Apoyo Desc - horas efectivas
    public List<Map<String, Object>> queryDMCADHE(String anio, String periodo, String codcas){
        return DMR.queryDMCADHE(anio, periodo, codcas);
    }
    /// Consulta Apoyo Desc - horas programadas
    public List<Map<String, Object>> queryDMCADHP(String anio, String periodo, String codcas){
        return DMR.queryDMCADHP(anio, periodo, codcas);
    }
    /// Consulta Apoyo Desc - medicos
    public List<Map<String, Object>> queryDMCADM(String anio, String periodo, String codcas){
        return DMR.queryDMCADM(anio, periodo, codcas);
    }
    /// Consulta Apoyo Desc - total consultante
    public List<Map<String, Object>> queryDMCADTC(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMCADTC(anio, periodo, codcas, codasegu);
    }
    /// Consulta Apoyo Desc - total consultante servicio
    public List<Map<String, Object>> queryDMCADTCS(String anio, String periodo, String codcas, String codasegu ){
        return DMR.queryDMCADTCS(anio, periodo, codcas, codasegu);
    }


    /// Indicadores de atenciones medicas
    /// total_atenciones - servicio - subactividad
    public List<Map<String, Object>> query0DMAMTASS(String anio, String periodo, String codcas, String codasegu ){
        return DMR.query0DMAMTASS(anio, periodo, codcas, codasegu);
    }
    /// total_atenciones - agrupador - especialidad
    public List<Map<String, Object>> query1DMAMTAAE(String anio, String periodo, String codcas, String codasegu ){
        return DMR.query1DMAMTAAE(anio, periodo, codcas, codasegu);
    }
    /// total_atenciones  - no considerados
    public List<Map<String, Object>> query2DMAMTANC(String anio, String periodo, String codcas, String codasegu ){
        return DMR.query2DMAMTANC(anio, periodo, codcas, codasegu);
    }
    /// total_atenciones  - sexo grupo etario
    public List<Map<String, Object>> query3DMAMTASGE(String anio, String periodo, String codcas, String codasegu ){
        return DMR.query3DMAMTASGE(anio, periodo, codcas, codasegu);
    }
    /// total_atenciones  - CSV
    public List<Map<String, Object>> query4DMAMTACSV(String anio, String periodo, String codcas, String codasegu ){
        return DMR.query4DMAMTACSV(anio, periodo, codcas, codasegu);
    }

    /// total_medicos - query 0
    public List<Map<String, Object>> query0DMAMTM(String anio, String periodo, String codcas, String codasegu ){
        return DMR.query0DMAMTM(anio, periodo, codcas, codasegu);
    }
    /// total_medicos - query 1
    public List<Map<String, Object>> query1DMAMTM(String anio, String periodo, String codcas, String codasegu ){
        return DMR.query1DMAMTM(anio, periodo, codcas, codasegu);
    }
    /// total_medicos - query 2 csv
    public List<Map<String, Object>> query2DMAMTMCSV(String anio, String periodo, String codcas, String codasegu ){
        return DMR.query2DMAMTMCSV(anio, periodo, codcas, codasegu);
    }
    /// total_horas_efectivas
    public List<Map<String, Object>> query0DMAMHE(String anio, String periodo, String codcas){
        return DMR.query0DMAMHE(anio, periodo, codcas);
    }
    /// total_horas_programadas
    public List<Map<String, Object>> query0DMAMHP(String anio, String periodo, String codcas){
        return DMR.query0DMAMHP(anio, periodo, codcas);
    }

    /// total_citados
    public List<Map<String, Object>> query0DMAMTC(String anio, String periodo, String codcas, String codasegu ){
        return DMR.query0DMAMTC(anio, periodo, codcas, codasegu);
    }

    /// total_deserción - query 0
    public List<Map<String, Object>> query0DMAMTD(String anio, String periodo, String codcas, String codasegu ){
        return DMR.query0DMAMTD(anio, periodo, codcas, codasegu);
    }
    /// total_deserción - query 1
    public List<Map<String, Object>> query1DMAMTD(String anio, String periodo, String codcas, String codasegu ){
        return DMR.query1DMAMTD(anio, periodo, codcas, codasegu);
    }
    /// total_deserción - query 2 csv
    public List<Map<String, Object>> query2DMAMTDCSV(String anio, String periodo, String codcas, String codasegu ){
        return DMR.query2DMAMTDCSV(anio, periodo, codcas, codasegu);
    }















}
