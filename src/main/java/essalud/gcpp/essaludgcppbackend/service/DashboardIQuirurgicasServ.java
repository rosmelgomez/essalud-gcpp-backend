package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardIQuirurgicasRep;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardIQuirurgicasServ {

    private final DashboardIQuirurgicasRep diqr;

    public DashboardIQuirurgicasServ(DashboardIQuirurgicasRep diqr) {
        this.diqr = diqr;
    }

    /// Query Dashboard intervenciones quirurgicas
    /// Grado de complejidad
    public List<Map<String, Object>> queryDIQGC(int anio, int periodo, String codcas, int codasegu, String complejidad) {
        List<Map<String, Object>> data = diqr.queryDIQGC(anio, periodo, codcas, codasegu);
        return getMaps(complejidad, data);
    }

    /// emergencia
    public List<Map<String, Object>> queryDIQE(int anio, int periodo, String codcas, int codasegu) {
        return diqr.queryDIQE(anio, periodo, codcas, codasegu);
    }

    /// horas quirurgicas electivas
    public Map<String, Object> queryDIQHQE(int anio, int periodo, String codcas, int codasegu) {
        List<Map<String, Object>> data = diqr.queryDIQH(anio, periodo, codcas, codasegu);
        return convertMap(data, "1");
    }

    /// horas quirurgicas emergencia
    public Map<String, Object> queryDIQHQEME(int anio, int periodo, String codcas, int codasegu) {
        List<Map<String, Object>> data = diqr.queryDIQH(anio, periodo, codcas, codasegu);
        return convertMap(data,"2");
    }

    /// Indicadores de grado de complejidad
    public List<Map<String, Object>> queryDIQIGC(int anio, int periodo, String codcas, int codasegu, String complejidad) {
        List<Map<String, Object>> data = diqr.queryDIQIGC(anio, periodo, codcas, codasegu);
        return getMaps(complejidad, data);
    }

    private Map<String, Object> convertMap(List<Map<String, Object>> data, String programacion) {
        data = data.stream()
                .filter(map -> map.get("cod_tipo_programacion").equals(programacion))
                .toList();

        data.forEach(map -> {
            String duracion = (String) map.get("duracion_sala");
            if (duracion != null) {
                String[] parts = duracion.split(":");
                if (parts.length == 2) {
                    double hours = Integer.parseInt(parts[0]) + Integer.parseInt(parts[1]) / 60.0;
                    map.put("horas_quirurgicas", hours);
                }
            }
        });
        Double totalHorasElectivas = data.stream()
                .mapToDouble(map -> (Double) map.get("horas_quirurgicas"))
                .sum();
        Map<String, Object> totalMap = new HashMap<>();
        totalMap.put("totalHorasElectivas", totalHorasElectivas);
        return totalMap;
    }

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
