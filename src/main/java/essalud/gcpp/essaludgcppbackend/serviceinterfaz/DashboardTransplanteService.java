package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

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
    public List<Map<String, Object>> queryDTrans(String anio, String periodo, String codcas, String codasegu) {
        return DTR.queryDTrans(anio, periodo, codcas, codasegu);
    }
}
