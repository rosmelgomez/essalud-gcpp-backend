package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardFarmRep;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardFarmServ {

    private final DashboardFarmRep repository;

    public DashboardFarmServ(DashboardFarmRep repository) {
        this.repository = repository;
    }


    /// Recetas atendidas
    public List<Map<String, Object>> queryDFRA(int anio, int periodo, String codcas, int codasegu){
        return repository.queryDFRA(anio, periodo, codcas, codasegu);
    }

    /// csv
    public List<Map<String, Object>> queryDFCSV(int anio, int periodo, String codcas, int codasegu){
        return repository.queryDFCSV(anio, periodo, codcas, codasegu);
    }

}
