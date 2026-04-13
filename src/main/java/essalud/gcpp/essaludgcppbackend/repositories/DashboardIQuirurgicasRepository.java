package essalud.gcpp.essaludgcppbackend.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DashboardIQuirurgicasRepository {   private final JdbcTemplate secondJdbcTemplate;

    public DashboardIQuirurgicasRepository(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
        this.secondJdbcTemplate = secondJdbcTemplate;
    }

    private String code_asegurado(String codasegu) {
        return switch (codasegu) {
            case "1" -> "('1')"; /// Asegurado
            case "2" -> "('2')"; /// No asegurado
            case "3" -> "('1','2')"; /// Todos
            default -> "('1','2')";
        };
    }

    /// Query Dashboard intervenciones quirurgicas
    /// Grado de complejidad
    public List<Map<String, Object>> queryDIQGC(String anio, String periodo, String codcas, String codasegu, String complejidad) {
        String tabla = "dssge.dwe_centro_quirurgico_" + anio + "_" + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
            SELECT DISTINCT ON (cq.acto_med, cq.num_solicitud)
                cq.cod_oricentro,
                cq.cod_centro,
                ca.cenasides AS cenasides,
                cq.periodo,
                cq.anio,
                cq.cod_area,
                h.arehosdes AS area,
                cq.cod_servicio,
                c.servhosdes AS servicio,
                cq.cod_cpms,
                cp.cpsdes AS cpms,
                cq.cod_tipdoc_paciente,
                cq.doc_paciente,
                cq.anio_edad,
                cq.meses,
                cq.sexo,
                cq.cod_sala,
                cq.acto_med,
                cq.cod_complejidad,
                cq.cod_anest,
                cq.cod_tipo_programacion,
                cq.num_solicitud,
                cq.cod_quirof,
                q.salopedes,
                cq.fec_oper
            FROM %s cq
            LEFT JOIN dwsge.sgss_cmsho10 AS c
                ON cq.cod_servicio = c.servhoscod
            LEFT JOIN dwsge.sgss_cmcas10 AS ca 
                ON cq.cod_oricentro = ca.oricenasicod 
            AND cq.cod_centro = ca.cenasicod
            LEFT JOIN dwsge.sgss_qmcqs10 AS q 
                ON q.cenasicod = cq.cod_centro 
            AND q.salopecod = cq.cod_sala_operacion 
            AND cq.cod_quirof = q.cenquicod
            LEFT JOIN dwsge.sgss_cmcpp10 as cp 
                ON cp.cpscod = cq.cod_cpms
            LEFT JOIN dwsge.sgss_cmaho10 as h
                ON h.arehoscod = cq.cod_area
            WHERE cq.cod_centro = ?
                        AND (
                                CASE
                                    WHEN cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                            ) IN %s
                        AND cq.cod_complejidad = ? 
            ORDER BY cq.acto_med, cq.num_solicitud, cq.fec_oper DESC;
            """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, complejidad);
    }



}
