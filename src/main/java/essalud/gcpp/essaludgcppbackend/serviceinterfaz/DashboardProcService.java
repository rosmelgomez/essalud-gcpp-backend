package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardProcRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardProcService {

    private final DashboardProcRepository dpr;
    public DashboardProcService(DashboardProcRepository dpr) {
        this.dpr = dpr;
    }

    public List<Map<String, Object>> queryDP(int anio, int periodo, String codcas, int codasegu, int codes) {
        return dpr.queryDP(anio, periodo, codcas, codasegu, codes);
    }


}
