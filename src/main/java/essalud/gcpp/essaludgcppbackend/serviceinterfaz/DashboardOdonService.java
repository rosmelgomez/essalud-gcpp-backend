package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardOdonRespository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardOdonService {

    private final DashboardOdonRespository DOR;

    public DashboardOdonService(DashboardOdonRespository DOR) {
        this.DOR = DOR;
    }

    ///Dashboard odontologicas
    ///  sesiones odontologicas - Atenciones
    public List<Map<String, Object>> queryDOSOA(String anio, String periodo, String codcas, String codasegu ){
        return DOR.queryDOSOA(anio, periodo, codcas, codasegu);
    }
    ///  sesiones odontologicas - horas efectivas
    public List<Map<String, Object>> queryDOSOHE(String anio, String periodo, String codcas){
        return DOR.queryDOSOHE(anio, periodo, codcas);
    }
    ///  sesiones odontologicas - horas programadas
    public List<Map<String, Object>> queryDOSOHP(String anio, String periodo, String codcas){
        return DOR.queryDOSOHP(anio, periodo, codcas);
    }
    ///  sesiones odontologicas - medicos
    public List<Map<String, Object>> queryDOSOM(String anio, String periodo, String codcas){
        return DOR.queryDOSOM(anio, periodo, codcas);
    }
    ///  sesiones odontologicas - total consultante atencion
    public List<Map<String, Object>> queryDOSOTCA(String anio, String periodo, String codcas, String codasegu ){
        return DOR.queryDOSOTCA(anio, periodo, codcas, codasegu);
    }
    ///  sesiones odontologicas - total consultante servicio
    public List<Map<String, Object>> queryDOSOTCS(String anio, String periodo, String codcas, String codasegu ){
        return DOR.queryDOSOTCS(anio, periodo, codcas, codasegu);
    }




}
