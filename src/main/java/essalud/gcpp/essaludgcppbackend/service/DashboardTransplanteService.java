package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardTransplanteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardTransplanteService {

    private final DashboardTransplanteRepository DTR;

    public DashboardTransplanteService(DashboardTransplanteRepository DTR) {
        this.DTR = DTR;
    }

    /// Query Dashboard Transplate
    public List<Map<String, Object>> queryDTrans(int anio, int periodo, String codcas, int codasegu) {
        return DTR.queryDTrans(anio, periodo, codcas, codasegu);
    }
}
