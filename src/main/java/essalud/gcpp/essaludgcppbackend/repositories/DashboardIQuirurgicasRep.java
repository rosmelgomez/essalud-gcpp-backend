package essalud.gcpp.essaludgcppbackend.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DashboardIQuirurgicasRep {

    private final JdbcTemplate secondJdbcTemplate;

    public DashboardIQuirurgicasRep(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
        this.secondJdbcTemplate = secondJdbcTemplate;
    }

    private String code_asegurado(int codasegu) {
        return switch (codasegu) {
            case 1 -> "('1')"; /// Asegurado
            case 2 -> "('2')"; /// No asegurado
            case 3 -> "('1','2')"; /// Todos
            default -> "('1','2')";
        };
    }

    private String periodo_str(int periodo) {
        if (periodo < 10) {
            return   "0" + periodo;
        } else {
            return String.valueOf(periodo);
        }
    }

    /// Query Dashboard intervenciones quirurgicas
    /// Grado de complejidad
    public List<Map<String, Object>> queryDIQGC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dssge.dwe_centro_quirurgico_" +anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
            SELECT DISTINCT ON (cq.acto_med, cq.cod_cpms, cq.fec_oper)
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
                b.conopedes as des_tipo_programacion,
                cq.num_solicitud,
                cq.cod_quirof,
                cq.fec_oper
            FROM %s cq
            LEFT JOIN dwsge.sgss_cmsho10 AS c
                ON cq.cod_servicio = c.servhoscod
            LEFT JOIN dwsge.sgss_cmcas10 AS ca
                ON cq.cod_oricentro = ca.oricenasicod
            AND cq.cod_centro = ca.cenasicod
            LEFT JOIN dwsge.sgss_cmcpp10 as cp
                ON cp.cpscod = cq.cod_cpms
            LEFT JOIN dwsge.sgss_cmaho10 as h
                ON h.arehoscod = cq.cod_area
            LEFT JOIN dwsge.sgss_qbcep10 as b ON b.conopecod = cq.cod_tipo_programacion
            WHERE cq.cod_centro = ?
                        AND (
                                CASE
                                    WHEN cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                            ) IN %s
            ORDER BY cq.acto_med, cq.cod_cpms, cq.fec_oper DESC;
            """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// emergencia
    public List<Map<String, Object>> queryDIQE(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dssge.dwe_centro_quirurgico_" +anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT DISTINCT ON (cq.acto_med, cq.cod_cpms)
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
                    b.conopedes as des_tipo_programacion,
                    cq.num_solicitud,
                    cq.cod_quirof,
                    ---q.salopedes,
                    cq.fec_oper
                FROM %s cq
                LEFT JOIN dwsge.sgss_cmsho10 AS c
                    ON cq.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca
                    ON cq.cod_oricentro = ca.oricenasicod
                AND cq.cod_centro = ca.cenasicod
                ---LEFT JOIN dwsge.sgss_qmcqs10 AS q
                ---    ON q.cenasicod = cq.cod_centro
                ---AND q.salopecod = cq.cod_sala_operacion
                ---AND cq.cod_quirof = q.cenquicod
                LEFT JOIN dwsge.sgss_cmcpp10 as cp
                    ON cp.cpscod = cq.cod_cpms
                LEFT JOIN dwsge.sgss_cmaho10 as h
                    ON h.arehoscod = cq.cod_area
                LEFT JOIN dwsge.sgss_qbcep10 as b ON b.conopecod = cq.cod_tipo_programacion
                WHERE cq.cod_centro = ?
                            AND (
                                    CASE
                                        WHEN cod_tipo_paciente = '4' THEN '2'
                                        ELSE '1'
                                    END
                                ) IN %s
                AND cq.cod_tipo_programacion = '2'
                ORDER BY cq.acto_med, cq.cod_cpms, cq.fec_oper DESC;
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Horas
    public List<Map<String, Object>> queryDIQH(int anio, int periodo, String codcas, int codasegu) {

        String tabla = "dssge.dwe_centro_quirurgico_" +anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
            SELECT DISTINCT ON (cq.acto_med, cq.cod_cpms, cq.fec_oper)
                cq.acto_med,
                cq.fec_oper,
                CASE
                    WHEN cq.duracion_sala = '00:00' THEN
                        TO_CHAR(
                            (
                                GREATEST(
                                    cq.hor_ini_anest::TIME,
                                    cq.hor_fin_anest::TIME,
                                    cq.hor_ini_operac::TIME,
                                    cq.hor_fin_operac::TIME
                                )
                                -
                                LEAST(
                                    cq.hor_ini_anest::TIME,
                                    cq.hor_fin_anest::TIME,
                                    cq.hor_ini_operac::TIME,
                                    cq.hor_fin_operac::TIME
                                )
                            ),
                            'HH24:MI'
                        )
                    ELSE cq.duracion_sala
                END AS duracion_sala,
                cq.cod_tipo_programacion
            FROM %s cq
            WHERE cq.cod_centro = ?
                AND (
                        CASE
                            WHEN cod_tipo_paciente = '4' THEN '2'
                            ELSE '1'
                        END
                    ) IN %s
            ORDER BY cq.acto_med, cq.cod_cpms, cq.fec_oper DESC
            """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    ///suspendidas
    public List<Map<String, Object>> queryDIQS(int anio, int periodo, String codcas) {

        String tabla = "dssge.dw_cq_sus_" +anio + "_" + periodo_str(periodo);

        String sql = """
            SELECT DISTINCT ON (cq.cod_cps, cq.num_solicitud)
                cq.*
            FROM %s cq
            WHERE cq.cod_centro = ?
                AND (
                        CASE
                            WHEN cq.cod_tipo_paciente = '4' THEN '2'
                            ELSE '1'
                        END
                    ) IN ('1', '2')
            ORDER BY cq.cod_cps, cq.num_solicitud
            """.formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }





    /// Indicadores de grado de complejidad
    public List<Map<String, Object>> queryDIQIGC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dssge.dwe_centro_quirurgico_" +anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT DISTINCT ON (cq.acto_med, cq.cod_cpms)
                    cq.cod_oricentro, cq.cod_centro,
                    ca.cenasides AS cenasides,
                    cq.periodo, cq.anio,
                    h.arehosdes   AS area,
                    c.servhosdes  AS servicio,
                    cp.cpsdes     AS cpms,
                    cq.cod_tipdoc_paciente, cq.doc_paciente,
                    cq.anio_edad, cq.meses, cq.sexo,
                    cq.acto_med, cq.cod_complejidad,
                    cq.cod_anest, cq.cod_tipo_programacion,
                    b.conopedes   AS des_tipo_programacion,
                    cq.num_solicitud, cq.cod_quirof,
                    q.salopedes,
                    cq.fec_oper
                FROM %s cq
                LEFT JOIN dwsge.sgss_cmsho10  c  ON cq.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.sgss_cmcas10  ca ON cq.cod_oricentro = ca.oricenasicod
                                                 AND cq.cod_centro   = ca.cenasicod
                LEFT JOIN dwsge.sgss_qmcqs10  q  ON q.cenasicod  = cq.cod_centro
                                                 AND q.salopecod  = cq.cod_sala_operacion
                                                 AND cq.cod_quirof = q.cenquicod
                LEFT JOIN dwsge.sgss_cmcpp10  cp ON cp.cpscod      = cq.cod_cpms
                LEFT JOIN dwsge.sgss_cmaho10  h  ON h.arehoscod    = cq.cod_area
                LEFT JOIN dwsge.sgss_qbcep10  b  ON b.conopecod    = cq.cod_tipo_programacion
                WHERE cq.cod_centro = ?
                  AND (CASE WHEN cq.cod_tipo_paciente = '4' THEN '2' ELSE '1' END) IN %s
                ORDER BY cq.acto_med, cq.cod_cpms, cq.fec_oper DESC
                """.formatted(tabla, code_asegurado);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

}
