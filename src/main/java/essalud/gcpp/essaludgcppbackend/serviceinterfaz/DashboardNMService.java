package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardNMRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardNMService {

    private final DashboardNMRepository DNMR;
    public DashboardNMService(DashboardNMRepository DNMR) {
        this.DNMR = DNMR;
    }

    /// Query Dashboard no medicas
    /// Obstetricia - Atenciones Medicas
    public List<Map<String, Object>> queryDNMOA(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMOA(anio, periodo, codcas, codasegu);
    }
    /// Obstetricia - Prenatal
    public List<Map<String, Object>> queryDNMOP(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMOP(anio, periodo, codcas, codasegu);
    }
    /// Obstetricia - Familiar
    public List<Map<String, Object>> queryDNMOF(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMOF(anio, periodo, codcas, codasegu);
    }
    /// Obstetricia - Complementarias
    public List<Map<String, Object>> queryDNMOC(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMOC(anio, periodo, codcas, codasegu);
    }
    /// Obstetricia - Preconcepcional
    public List<Map<String, Object>> queryDNMOPC(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMOPC(anio, periodo, codcas, codasegu);
    }

    /// Preventivo Promocional
    ///  Preventivo Promocional - Atenciones
    public List<Map<String, Object>> queryDNMPPA(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMPPA(anio, periodo, codcas, codasegu);
    }
    ///  Preventivo Promocional - domiciliaria
    public List<Map<String, Object>> queryDNMPPD(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMPPD(anio, periodo, codcas, codasegu);
    }
    ///  Preventivo Promocional - grupal
    public List<Map<String, Object>> queryDNMPPG(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMPPG(anio, periodo, codcas, codasegu);
    }
    ///  Preventivo Promocional - psicoprofilaxis
    public List<Map<String, Object>> queryDNMPPP(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMPPP(anio, periodo, codcas, codasegu);
    }
    ///  Preventivo Promocional - consejeria
    public List<Map<String, Object>> queryDNMPPC(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMPPC(anio, periodo, codcas, codasegu);
    }


    /// Nutricion
    ///  Nutricion - nutricion_total
    public List<Map<String, Object>> queryDNMNNT(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMNNT(anio, periodo, codcas, codasegu);
    }

    /// Enfermeria
    ///  Enfermeria - enfermeria_total
    public List<Map<String, Object>> queryDNMEET(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMEET(anio, periodo, codcas, codasegu);
    }
    ///  Enfermeria - enfermeria_tuberculosis
    public List<Map<String, Object>> queryDNMEETU(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMEETU(anio, periodo, codcas, codasegu);
    }
    ///  Enfermeria - enfermeria_vih
    public List<Map<String, Object>> queryDNMEEV(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMEEV(anio, periodo, codcas, codasegu);
    }
    ///  Enfermeria - enfermeria_cronicas_am
    public List<Map<String, Object>> queryDNMEECA(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMEECA(anio, periodo, codcas, codasegu);
    }
    ///  Enfermeria - enfermeria_otros
    public List<Map<String, Object>> queryDNMEEO(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMEEO(anio, periodo, codcas, codasegu);
    }
    ///  Enfermeria - enfermeria_prev_anemia
    public List<Map<String, Object>> queryDNMEEPA(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMEEPA(anio, periodo, codcas, codasegu);
    }

    /// Psicologia
    /// psicologia total consultantes
    public List<Map<String, Object>> queryDNMPSTCS(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMPSTCS(anio, periodo, codcas, codasegu);
    }
    /// psicologia - total - consultas
    public List<Map<String, Object>> queryDNMPSTC(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMPSTC(anio, periodo, codcas, codasegu);
    }
    /// Psicologia - horas - efectivas
    public List<Map<String, Object>> queryDNMPSHE(String anio, String periodo, String codcas ){
        return this.DNMR.queryDNMPSHE(anio, periodo, codcas);
    }
    /// Psicologia - horas - programadas
    public List<Map<String, Object>> queryDNMPHP(String anio, String periodo, String codcas){
        return this.DNMR.queryDNMPHP(anio, periodo, codcas);
    }
    /// Psicologia - medicos
    public List<Map<String, Object>> queryDNMPSM(String anio, String periodo, String codcas){
        return this.DNMR.queryDNMPSM(anio, periodo, codcas);
    }
    /// Psicologia - procedimiento terapéutico
    public List<Map<String, Object>> queryDNMPSPT(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMPSPT(anio, periodo, codcas, codasegu);
    }
    /// Psicologia - procedimiento diagnóstico
    public List<Map<String, Object>> queryDNMPSPD(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMPSPD(anio, periodo, codcas, codasegu);
    }

    ///Trabajo Social (TS)
    ///Trabajo - social - total
    public List<Map<String, Object>> queryDNMTST(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMTST(anio, periodo, codcas, codasegu);
    }

    /// CRED
    /// CRED -total
    public List<Map<String, Object>> queryDNMCREDT(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMCREDT(anio, periodo, codcas, codasegu);
    }

    /// Indicadores de obstetricia
    /// total atenciones - servicio - subactividad
    public List<Map<String, Object>> queryDNMOBTASS(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMOBTASS(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMOBTAD(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMOBTAD(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMOBTACSV(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMOBTACSV(anio, periodo, codcas, codasegu);
    }

    /// Indicadores de preventiva promocional
    public List<Map<String, Object>> queryDNMPPTASS(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMPPTASS(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMPPTAD(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMPPTAD(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMPPTACSV(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMPPTACSV(anio, periodo, codcas, codasegu);
    }



    /// Indicadores de nutricion
    /// total atenciones - servicio - subactividad
    public List<Map<String, Object>> queryDNMNUTASS(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMNUTASS(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMNUTAD(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMNUTAD(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMNUTACSV(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMNUTACSV(anio, periodo, codcas, codasegu);
    }


    /// Indicadores de enfermeria
    /// total atenciones - servicio - subactividad
    public List<Map<String, Object>> queryDNMENTASS(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMENTASS(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMENTAD(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMENTAD(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMENTACSV(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMENTACSV(anio, periodo, codcas, codasegu);
    }

    /// Indicadores de psicologia
    /// total atenciones - servicio - subactividad
    public List<Map<String, Object>> queryDNMPSTASS(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMPSTASS(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMPSTAD(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMPSTAD(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMPSTACSV(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMPSTACSV(anio, periodo, codcas, codasegu);
    }

    /// Indicadores de trabajo social
    /// total atenciones - servicio - subactividad
    public List<Map<String, Object>> queryDNMTSTASS(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMTSTASS(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMTSTAD(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMTSTAD(anio, periodo, codcas, codasegu);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMTSTACSV(String anio, String periodo, String codcas, String codasegu ){
        return this.DNMR.queryDNMTSTACSV(anio, periodo, codcas, codasegu);
    }


}
