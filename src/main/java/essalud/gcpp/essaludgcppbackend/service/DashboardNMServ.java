package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardNMRep;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardNMServ {

    private final DashboardNMRep DNMR;
    public DashboardNMServ(DashboardNMRep DNMR) {
        this.DNMR = DNMR;
    }

    /// Query Dashboard no medicas
    /// Obstetricia - Atenciones Medicas
    public List<Map<String, Object>> queryDNMOA(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMOA(anio, periodo, codcas, codasegu);
    }
    /// Obstetricia - Prenatal
    public List<Map<String, Object>> queryDNMOP(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMOP(anio, periodo, codcas, codasegu);
    }
    /// Obstetricia - Familiar
    public List<Map<String, Object>> queryDNMOF(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMOF(anio, periodo, codcas, codasegu);
    }
    /// Obstetricia - Complementarias
    public List<Map<String, Object>> queryDNMOC(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMOC(anio, periodo, codcas, codasegu);
    }
    /// Obstetricia - Preconcepcional
    public List<Map<String, Object>> queryDNMOPC(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMOPC(anio, periodo, codcas, codasegu);
    }

    /// Preventivo Promocional
    ///  Preventivo Promocional - Atenciones
    public List<Map<String, Object>> queryDNMPPA(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMPPA(anio, periodo, codcas, codasegu);
    }
    ///  Preventivo Promocional - domiciliaria
    public List<Map<String, Object>> queryDNMPPD(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMPPD(anio, periodo, codcas, codasegu);
    }
    ///  Preventivo Promocional - grupal
    public List<Map<String, Object>> queryDNMPPG(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMPPG(anio, periodo, codcas, codasegu);
    }
    ///  Preventivo Promocional - psicoprofilaxis
    public List<Map<String, Object>> queryDNMPPP(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMPPP(anio, periodo, codcas, codasegu);
    }
    ///  Preventivo Promocional - consejeria
    public List<Map<String, Object>> queryDNMPPC(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMPPC(anio, periodo, codcas, codasegu);
    }


    /// Nutricion
    ///  Nutricion - nutricion_total
    public List<Map<String, Object>> queryDNMNNT(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMNNT(anio, periodo, codcas, codasegu);
    }

    /// Enfermeria
    ///  Enfermeria - enfermeria_total
    public List<Map<String, Object>> queryDNMEET(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMEET(anio, periodo, codcas, codasegu);
    }
    ///  Enfermeria - enfermeria_tuberculosis
    public List<Map<String, Object>> queryDNMEETU(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMEETU(anio, periodo, codcas, codasegu);
    }
    ///  Enfermeria - enfermeria_vih
    public List<Map<String, Object>> queryDNMEEV(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMEEV(anio, periodo, codcas, codasegu);
    }
    ///  Enfermeria - enfermeria_cronicas_am
    public List<Map<String, Object>> queryDNMEECA(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMEECA(anio, periodo, codcas, codasegu);
    }
    ///  Enfermeria - enfermeria_otros
    public List<Map<String, Object>> queryDNMEEO(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMEEO(anio, periodo, codcas, codasegu);
    }
    ///  Enfermeria - enfermeria_prev_anemia
    public List<Map<String, Object>> queryDNMEEPA(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMEEPA(anio, periodo, codcas, codasegu);
    }

    /// Psicologia
    /// psicologia total consultantes
    public List<Map<String, Object>> queryDNMPSTCS(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMPSTCS(anio, periodo, codcas, codasegu);
    }
    /// psicologia - total - consultas
    public List<Map<String, Object>> queryDNMPSTC(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMPSTC(anio, periodo, codcas, codasegu);
    }
    /// Psicologia - horas - efectivas
    public List<Map<String, Object>> queryDNMPSHE(int anio, int periodo, String codcas ){
        return this.DNMR.queryDNMPSHE(anio, periodo, codcas);
    }
    /// Psicologia - horas - programadas
    public List<Map<String, Object>> queryDNMPHP(int anio, int periodo, String codcas){
        return this.DNMR.queryDNMPHP(anio, periodo, codcas);
    }
    /// Psicologia - medicos
    public List<Map<String, Object>> queryDNMPSM(int anio, int periodo, String codcas){
        return this.DNMR.queryDNMPSM(anio, periodo, codcas);
    }
    /// Psicologia - procedimiento terapéutico
    public List<Map<String, Object>> queryDNMPSPT(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMPSPT(anio, periodo, codcas, codasegu);
    }
    /// Psicologia - procedimiento diagnóstico
    public List<Map<String, Object>> queryDNMPSPD(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMPSPD(anio, periodo, codcas, codasegu);
    }

    ///Trabajo Social (TS)
    ///Trabajo - social - total
    public List<Map<String, Object>> queryDNMTST(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMTST(anio, periodo, codcas, codasegu);
    }

    ///Trabajo - social - total - procedimientos
    public List<Map<String, Object>> queryDNMTSTP(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMTSTP(anio, periodo, codcas, codasegu);
    }

    /// CRED
    /// CRED -total
    public List<Map<String, Object>> queryDNMCREDT(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMCREDT(anio, periodo, codcas, codasegu);
    }

    /// Indicadores de obstetricia
    /// total atenciones - servicio - subactividad
    public List<Map<String, Object>> queryDNMOBTASS(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMOBTASS(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMOBTAD(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMOBTAD(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMOBTACSV(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMOBTACSV(anio, periodo, codcas, codasegu);
    }

    /// Indicadores de preventiva promocional
    public List<Map<String, Object>> queryDNMPPTASS(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMPPTASS(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMPPTAD(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMPPTAD(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMPPTACSV(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMPPTACSV(anio, periodo, codcas, codasegu);
    }



    /// Indicadores de nutricion
    /// total atenciones - servicio - subactividad
    public List<Map<String, Object>> queryDNMNUTASS(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMNUTASS(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMNUTAD(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMNUTAD(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMNUTACSV(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMNUTACSV(anio, periodo, codcas, codasegu);
    }


    /// Indicadores de enfermeria
    /// total atenciones - servicio - subactividad
    public List<Map<String, Object>> queryDNMENTASS(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMENTASS(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMENTAD(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMENTAD(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMENTACSV(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMENTACSV(anio, periodo, codcas, codasegu);
    }

    /// Indicadores de psicologia
    /// total atenciones - servicio - subactividad
    public List<Map<String, Object>> queryDNMPSTASS(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMPSTASS(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMPSTAD(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMPSTAD(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMPSTACSV(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMPSTACSV(anio, periodo, codcas, codasegu);
    }

    /// Indicadores de trabajo social
    /// total atenciones - servicio - subactividad
    public List<Map<String, Object>> queryDNMTSTASS(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMTSTASS(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMTSTAD(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMTSTAD(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMTSTACSV(int anio, int periodo, String codcas, int codasegu ){
        return this.DNMR.queryDNMTSTACSV(anio, periodo, codcas, codasegu);
    }


}
