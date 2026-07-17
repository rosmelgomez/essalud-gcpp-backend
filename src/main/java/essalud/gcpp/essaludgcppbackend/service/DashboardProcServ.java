package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardProcRep;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardProcServ {

    private final DashboardProcRep dpr;
    public DashboardProcServ(DashboardProcRep dpr) {
        this.dpr = dpr;
    }

    public List<Map<String, Object>> queryDP(int anio, int periodo, String codcas, int codasegu, int codes) {
        return dpr.queryDP(anio, periodo, codcas, codasegu, codes);
    }


}
