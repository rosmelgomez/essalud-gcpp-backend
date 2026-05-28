package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardHospRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardHospService {

    private final DashboardHospRepository dhqr;

    public DashboardHospService(DashboardHospRepository dhqr) {
        this.dhqr = dhqr;
    }

    /// Query Total Egresos
    public List<Map<String, Object>> queryDHTE(int anio, int periodo, String codcas, int codasegu) {
        return dhqr.queryDHTE(anio, periodo, codcas, codasegu);
    }

}
