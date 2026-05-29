package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardEmeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardEmeService {

    private  final DashboardEmeRepository DER;
    public DashboardEmeService(DashboardEmeRepository DER) {
        this.DER = DER;
    }

    ///Dashboard Emergencias
    /// Prioridad
    public List<Map<String, Object>> queryDEP(int anio, int periodo, String codcas, int codasegu,  String prioridad ) {
        return DER.queryDEP(anio, periodo, codcas, codasegu, prioridad);
    }

    /// defunciones
    public List<Map<String, Object>> queryDED(int anio, int periodo, String codcas, int codasegu) {
        return DER.queryDED(anio, periodo, codcas, codasegu);
    }

    /// mayor a 24h
    public List<Map<String, Object>> queryDEMY24HR(int anio, int periodo, String codcas, int codasegu) {
        return DER.queryDEMY24HR(anio, periodo, codcas, codasegu);
    }
    /// menor a 24h
    public List<Map<String, Object>> queryDEMN24HR(int anio, int periodo, String codcas, int codasegu) {
        return DER.queryDEMN24HR(anio, periodo, codcas, codasegu);
    }

}
