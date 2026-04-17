package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardIQuirurgicasRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

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

        List<Map<String, Object>> data = diqr.queryDIQGC(anio, periodo, codcas, codasegu);

        return getMaps(complejidad, data);
    }

    /// emergencia
    public List<Map<String, Object>> queryDIQE(String anio, String periodo, String codcas, String codasegu) {
        return diqr.queryDIQE(anio, periodo, codcas, codasegu);
    }

    ///horas quirurgicas electivas
    public List<Map<String, Object>> queryDIQHQE(String anio, String periodo, String codcas, String codasegu) {
        return diqr.queryDIQHQE(anio, periodo, codcas, codasegu);
    }

    ///horas quirurgicas emergencia
    public List<Map<String, Object>> queryDIQHQEME(String anio, String periodo, String codcas, String codasegu){
        return diqr.queryDIQHQEME(anio, periodo, codcas, codasegu);
    }

    ///Indicadores de grado de complejidad
    public List<Map<String, Object>> queryDIQIGC(String anio, String periodo, String codcas, String codasegu, String complejidad) {
        List<Map<String, Object>> data = diqr.queryDIQIGC(anio, periodo, codcas, codasegu);
        return getMaps(complejidad, data);
    }

    @NonNull
    private List<Map<String, Object>> getMaps(String complejidad, List<Map<String, Object>> data) {
        return data.stream()
                .filter(map -> {
                    Object value = map.get("cod_complejidad");

                    // Caso 1: quieres los NULL
                    if (complejidad == null || "null".equalsIgnoreCase(complejidad)) {
                        return value == null;
                    }

                    // Caso 2: quieres valores específicos
                    return value != null && value.toString().equals(complejidad);
                })
                .toList();
    }
}
