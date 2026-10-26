package essalud.gcpp.essaludgcppbackend.repositories;

import essalud.gcpp.essaludgcppbackend.dtos.ReporteadorDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class DashboardReporResp {
    private final JdbcTemplate secondJdbcTemplate;

    public DashboardReporResp(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
        this.secondJdbcTemplate = secondJdbcTemplate;
    }

    /// Dashboard Reporteador Red
    public List<Map<String, Object>> queryDRR() {
        String tabla = "dwsge.sgss_cmras10" ;
        String sql = """
            SELECT redasiscod, redasisdes, redasismeddes
            FROM %s ORDER BY redasisdes ASC
            """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql );
    }

    /// Dashboard Reporteador centro
    public List<Map<String, Object>> queryDRC() {
        String tabla = "dwsge.sgss_cmcas10" ;
        String sql = """
            SELECT cenasicod, cenasides,redasiscod
            FROM %s ORDER BY cenasides
            """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql );
    }

    /// Dashboard Reporteador servicio
    public List<Map<String, Object>> queryDRS() {
        String tabla = "dwsge.sgss_cmsho10" ;
        String sql = """
            SELECT servhoscod, servhosdes
            FROM %s ORDER BY servhosdes
            """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql );
    }

    /// Dashboard Reporteador actividad
    public List<Map<String, Object>> queryDRAC() {
        String tabla = "dwsge.sgss_cmact10" ;
        String sql = """
            SELECT actcod, actdes
            FROM %s ORDER BY actdes
            """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql );
    }

    /// Dashboard Reporteador  subactividad
    public List<Map<String, Object>> queryDRSUBAC() {
        String tabla = "dwsge.sgss_cmace10" ;
        String sql = """
                SELECT actcod, actespcod, actespnom
                FROM %s ORDER BY actcod, actespcod
            """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql );
    }

    /// Dashboard Reporteador capitulo
    public List<Map<String, Object>> queryDRCAP() {
        String tabla = "dwsge.sgss_cmdia10_chapter" ;
        String sql = """
            SELECT diagcod, edxcapdes
            FROM %s ORDER BY edxcapdes
            """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql );
    }
    /// Dashboard Reporteador variable
    public List<Map<String, Object>> queryDRVAR() {
        String tabla = "dwsge.dim_variable" ;
        String sql = """
            SELECT cod_variable, variable
            FROM %s ORDER BY variable
            """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql );
    }

    private String periodo_str(int periodo) {
        if (periodo < 10) {
            return   "0" + periodo;
        } else {
            return String.valueOf(periodo);
        }
    }

    /// Dashboard Reporteador query
    public List<Map<String, Object>> queryDRQ(ReporteadorDto repDto) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + repDto.getAnio() + "_" +periodo_str(repDto.getPeriodo()) ;
        List<Object> params = new ArrayList<>();
        String filtro = filter(repDto, params);

        String sql = """
        ( WITH report_data AS
        (SELECT cod_oricentro,
               ca.redasiscod,
               r.redasisdes,
               cod_centro,
               cenasides,
               periodo,
               anio,
               v.cod_variable,
               v.variable,
               cod_servicio,
               c.servhosdes AS servicio,
               cod_actividad,
               am.actdes AS actividad,
               cod_subactividad,
               a.actespnom AS subactividad,
               dni_medico,
               acto_med,
               doc_paciente,
               anio_edad,
               sexo,
               cod_diag,
               d.diagdes,
               d.edxcapdes AS capitulo
        FROM %s ce
        LEFT JOIN dwsge.sgss_cmdia10_chapter d ON ce.cod_diag = d.diagcod
        LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
        LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
        LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
        LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
        LEFT JOIN dwsge.sgss_cmras10 r ON ca.redasiscod = r.redasiscod
        LEFT JOIN dwsge.dim_variable v ON v.cod_variable = ce.cod_variable
        WHERE ce.clasificacion IN (2,4,6))
        SELECT *
        FROM report_data
        WHERE %s )
        """.formatted(tabla, filtro);

        return secondJdbcTemplate.queryForList(sql, params.toArray());
    }


    /// Construye el WHERE con placeholders; los valores van en params para evitar inyeccion SQL.
    private String filter(ReporteadorDto repDto, List<Object> params) {
        List<String> condiciones = new ArrayList<>();
        if (repDto.getAnio() != 0) {
            condiciones.add("anio = ?");
            params.add(String.valueOf(repDto.getAnio()));
        }
        if (repDto.getPeriodo() != 0) {
            condiciones.add("periodo = ?");
            params.add(String.valueOf(repDto.getAnio()) + repDto.getPeriodo());
        }
        addFiltro(condiciones, params, "redasiscod", repDto.getRedasiscod());
        addFiltro(condiciones, params, "cod_centro", repDto.getCod_centro());
        addFiltro(condiciones, params, "cod_servicio", repDto.getCod_servicio());
        addFiltro(condiciones, params, "cod_actividad", repDto.getCod_actividad());
        addFiltro(condiciones, params, "cod_subactividad", repDto.getCod_subactividad());
        addFiltro(condiciones, params, "sexo", repDto.getSexo());
        addFiltro(condiciones, params, "cod_diag", repDto.getCapitulo());
        addFiltro(condiciones, params, "cod_variable", repDto.getVariable());
        return condiciones.isEmpty() ? "TRUE" : String.join(" AND ", condiciones);
    }

    private void addFiltro(List<String> condiciones, List<Object> params, String columna, String valor) {
        if (valor != null && !valor.isEmpty()) {
            condiciones.add(columna + " = ?");
            params.add(valor);
        }
    }


}
