package essalud.gcpp.essaludgcppbackend.repositories;

import essalud.gcpp.essaludgcppbackend.dtos.ReporteadorDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class DashboardReporRespository {
    private final JdbcTemplate secondJdbcTemplate;

    public DashboardReporRespository(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
        this.secondJdbcTemplate = secondJdbcTemplate;
    }

    /// Dashboard Reporteador Red
    public List<Map<String, Object>> queryDRR() {
        String tabla = "dwsge.sgss_cmras10" ;
        String sql = """
            SELECT redasiscod, redasisdes, redasismeddes
            FROM %s ORDER BY redasisdes
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

    /// Dashboard Reporteador query
    public List<Map<String, Object>> queryDRQ(ReporteadorDto repDto) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + repDto.getAnio() + "_" + repDto.getPeriodo() ;
        String filtro = filter(repDto);

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

        return secondJdbcTemplate.queryForList(sql);
    }


    private String filter (ReporteadorDto repDto) {
        String filtro = "";
        if (!Objects.equals(repDto.getAnio(), "")) {
            filtro = "anio = " + "'" + repDto.getAnio() + "'";
        }
        if (!Objects.equals(repDto.getPeriodo(), "")) {
            filtro = filtro + " AND periodo = " + "'"+repDto.getAnio() + repDto.getPeriodo() + "'";
        }
        if (!Objects.equals(repDto.getRedasiscod(), "")) {
            filtro = filtro + "AND redasiscod = " + "'" + repDto.getRedasiscod() + "'";
        }
        if (!Objects.equals(repDto.getCod_centro(), "")) {
            filtro = filtro + "AND cod_centro = " + "'" + repDto.getCod_centro() + "'";
        }
        if (!Objects.equals(repDto.getCod_servicio(), "")) {
            filtro = filtro + "AND cod_servicio = " + "'" + repDto.getCod_servicio() + "'";
        }
        if (!Objects.equals(repDto.getCod_actividad(), "")) {
            filtro = filtro + "AND cod_actividad = " + "'" + repDto.getCod_actividad() + "'";
        }
        if (!Objects.equals(repDto.getCod_subactividad(), "")) {
            filtro = filtro + "AND cod_subactividad = " + "'" + repDto.getCod_subactividad() + "'";
        }
        if (!Objects.equals(repDto.getSexo(), "")) {
            filtro = filtro + "AND sexo = " + "'" + repDto.getSexo() + "'";
        }
        if ( !Objects.equals(repDto.getCapitulo(), "")){
            filtro = filtro + "AND cod_diag = " + "'" + repDto.getCapitulo() + "'";
        }
        if (!Objects.equals(repDto.getVariable(),"")){
            filtro = filtro + "AND cod_variable = " + "'" + repDto.getVariable() + "'";
        }
        return filtro;
    }


}
