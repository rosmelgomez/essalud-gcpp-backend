package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardOdonResp;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardOdonServ {

    private final DashboardOdonResp DOR;

    public DashboardOdonServ(DashboardOdonResp DOR) {
        this.DOR = DOR;
    }

    ///Dashboard odontologicas
    ///  sesiones odontologicas - Atenciones
    public List<Map<String, Object>> queryDOSOA(int anio, int periodo, String codcas, int codasegu ){
        return DOR.queryDOSOA(anio, periodo, codcas, codasegu);
    }
    ///  sesiones odontologicas - horas efectivas
    public List<Map<String, Object>> queryDOSOHE(int anio, int periodo, String codcas){
        return DOR.queryDOSOHE(anio, periodo, codcas);
    }
    ///  sesiones odontologicas - horas programadas
    public List<Map<String, Object>> queryDOSOHP(int anio, int periodo, String codcas){
        return DOR.queryDOSOHP(anio, periodo, codcas);
    }
    ///  sesiones odontologicas - medicos
    public List<Map<String, Object>> queryDOSOM(int anio, int periodo, String codcas){
        return DOR.queryDOSOM(anio, periodo, codcas);
    }
    ///  sesiones odontologicas - total consultante atencion
    public List<Map<String, Object>> queryDOSOTCA(int anio, int periodo, String codcas, int codasegu ){
        return DOR.queryDOSOTCA(anio, periodo, codcas, codasegu);
    }
    ///  sesiones odontologicas - total consultante servicio
    public List<Map<String, Object>> queryDOSOTCS(int anio, int periodo, String codcas, int codasegu ){
        return DOR.queryDOSOTCS(anio, periodo, codcas, codasegu);
    }




}
