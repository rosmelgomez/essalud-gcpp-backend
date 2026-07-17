package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardTransplanteRep;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardTransplanteServ {

    private final DashboardTransplanteRep DTR;

    public DashboardTransplanteServ(DashboardTransplanteRep DTR) {
        this.DTR = DTR;
    }

    /// Query Dashboard Transplate
    public List<Map<String, Object>> queryDTrans(int anio, int periodo, String codcas, int codasegu) {
        return DTR.queryDTrans(anio, periodo, codcas, codasegu);
    }
}
