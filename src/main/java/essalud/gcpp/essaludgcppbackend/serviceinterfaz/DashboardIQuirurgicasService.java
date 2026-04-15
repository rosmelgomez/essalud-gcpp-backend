package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardIQuirurgicasRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DashboardIQuirurgicasService {

    private final DashboardIQuirurgicasRepository   diqr;
    public DashboardIQuirurgicasService( DashboardIQuirurgicasRepository diqr) {
        this.diqr = diqr;
    }

    /// Query Dashboard intervenciones quirurgicas
    /// Grado de complejidad
    public List<Map<String, Object>> queryDIQGC(String anio, String periodo, String codcas, String codasegu, String complejidad) {

        return diqr.queryDIQGC(anio, periodo, codcas, codasegu)
                .stream()
                .filter(map -> {
                    Object value = map.get("cod_complejidad");
                    return value != null && value.toString().equals(complejidad);
                })
                .toList();
    }

}
