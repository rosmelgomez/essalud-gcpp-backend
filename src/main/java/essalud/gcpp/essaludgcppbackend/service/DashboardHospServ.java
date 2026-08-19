package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardHospRep;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardHospServ {

    private final DashboardHospRep dhqr;

    public DashboardHospServ(DashboardHospRep dhqr) {
        this.dhqr = dhqr;
    }

    /// Query Total Egresos
    public List<Map<String, Object>> queryDHE(int anio, int periodo, String codcas, int codasegu) {
        return dhqr.queryDHE(anio, periodo, codcas, codasegu);
    }

    //////pacientes mes
    public List<Map<String, Object>> queryDHPM(int anio, int periodo, String codcas) {
        return dhqr.queryDHPM(anio, periodo, codcas);
    }

}
