package essalud.gcpp.essaludgcppbackend.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DashboardMRepository {
    private final JdbcTemplate secondJdbcTemplate;

    public DashboardMRepository(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
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

    /// Query Dashboard medicas
    /// Atenciones Medicas
    public List<Map<String, Object>> queryDMAMA(String anio, String periodo, String codcas, String codasegu) {
        String tabla = "dwsge.dw_consulta_externa_homologacion_" + anio + "_" + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                        SELECT 
                            ce.cod_servicio,
                            ce.cod_especialidad,
                            ca.cenasides,
                            ag.agrupador AS agrupador,
                            am.actdes AS actividad,
                            a.actespnom AS subactividad,
                            ce.cod_tipo_consulta,
                            ce.cod_diag,
                            c.servhosdes AS descripcion_servicio,
                            e.especialidad AS descripcion_especialidad,
                            t.tipcondes AS descripcion_tipo_consulta,
                            d.diagdes AS descripcion_diagnostico,
                            dni_medico,
                            doc_paciente,
                            CASE WHEN ce.cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                            sexo,
                            fecha_atencion,
                            acto_med
                        FROM %s AS ce
                        LEFT JOIN dwsge.sgss_cmsho10 AS c 
                            ON ce.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON ce.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmtco10 AS t
                            ON ce.cod_tipo_consulta = t.tipconcod
                        LEFT JOIN dwsge.sgss_cmdia10 AS d
                            ON ce.cod_diag = d.diagcod
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON ce.cod_actividad = a.actcod
                            AND ce.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON ce.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON ce.cod_oricentro = ca.oricenasicod
                            AND ce.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                        WHERE ce.cod_centro = ?
                        AND ce.cod_actividad = '91'
                        AND ce.clasificacion in (2,4,6)
                        AND ce.cod_variable = '001'
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Atenciones médicas - horas efectivas
    public List<Map<String, Object>> queryDMAMHE(String anio, String periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo;


        String sql = """
                        SELECT 
                            ce.*,
                            c.servhosdes,
                            e.especialidad,
                            a.actespnom,
                            am.actdes,
                            ag.agrupador,
                            ca.cenasides
                        FROM %s AS ce
                        LEFT JOIN dwsge.sgss_cmsho10 AS c 
                            ON ce.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON ce.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON ce.cod_actividad = a.actcod
                            AND ce.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON ce.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON ce.cod_oricentro = ca.oricenasicod
                            AND ce.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                        WHERE ce.cod_centro = ?
                        AND ce.cod_actividad = '91'
                        AND ce.cod_variable = '001'
                        AND ce.ate>0
                """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Atenciones médicas - horas programadas
    public List<Map<String, Object>> queryDMAMHP(String anio, String periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo;

        String sql = """
                        SELECT 
                            p.*,
                            c.servhosdes,
                            e.especialidad,
                            ag.agrupador,
                            a.actespnom,
                            am.actdes,
                            ca.cenasides 
                        FROM %s p
                        LEFT JOIN dwsge.sgss_cmsho10 AS c 
                            ON p.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON p.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON p.cod_actividad = a.actcod
                            AND p.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON p.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON p.cod_oricentro = ca.oricenasicod
                            AND p.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON p.cod_agrupador = ag.cod_agrupador
                        WHERE p.cod_variable = '001'
                        AND (
                                p.cod_mot_suspension IS NULL 
                                OR p.cod_mot_suspension NOT IN ('04','09','10','99','13','16','11')
                            )
                        AND p.cod_centro = ?
                        AND p.cod_actividad = '91'
                """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Atenciones médicas - citadas
    public List<Map<String, Object>> queryDMAMC(String anio, String periodo, String codcas, String codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_citados_homologacion_" + anio + "_" + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                        SELECT 
                            p.*,
                            c.servhosdes,
                            e.especialidad,
                            a.actespnom,
                            am.actdes,
                            ag.agrupador,
                            ca.cenasides,
                            CASE WHEN cod_paciente = '4' THEN '2' ELSE '1' END AS cod_paciente
                        FROM %s p
                        LEFT JOIN dwsge.sgss_cmsho10 AS c 
                            ON p.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON p.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON p.cod_actividad = a.actcod
                            AND p.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON p.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON p.cod_oricentro = ca.oricenasicod
                            AND p.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON p.cod_agrupador = ag.cod_agrupador
                        WHERE p.cod_centro = ?
                        AND p.cod_actividad = '91'
                        AND p.cod_variable = '001'
                        AND p.cod_estado <> '0'
                        AND (
                                CASE 
                                    WHEN cod_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Atenciones médicas - desercion
    public List<Map<String, Object>> queryDMAMD(String anio, String periodo, String codcas, String codasegu) {
        String tabla1 = "dwsge.dw_consulta_externa_homologacion_" + anio + "_" + periodo;
        String tabla2 = "dwsge.dwe_consulta_externa_citados_homologacion_" + anio + "_" + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                        SELECT
                            c.servhosdes,
                            e.especialidad,
                            a.actespnom,
                            am.actdes,
                            ag.agrupador,
                            ca.cenasides,
                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente   
                        FROM %s ce
                        LEFT JOIN dwsge.sgss_cmsho10 AS c 
                            ON ce.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON ce.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON ce.cod_actividad = a.actcod
                            AND ce.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON ce.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON ce.cod_oricentro = ca.oricenasicod
                            AND ce.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                        WHERE ce.cod_centro = ?
                        AND ce.cod_actividad = '91'
                        AND ce.clasificacion IN (1,3,0)
                        AND ce.cod_variable = '001'
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                
                        UNION ALL
                
                        SELECT 
                            c.servhosdes,
                            e.especialidad,
                            a.actespnom,
                            am.actdes,
                            ag.agrupador,
                            ca.cenasides,
                            CASE WHEN cod_paciente = '4' THEN '2' ELSE '1' END AS cod_paciente
                        FROM %s p
                        LEFT JOIN dwsge.sgss_cmsho10 AS c 
                            ON p.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON p.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON p.cod_actividad = a.actcod
                            AND p.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON p.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON p.cod_oricentro = ca.oricenasicod
                            AND p.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON p.cod_agrupador = ag.cod_agrupador
                        WHERE p.cod_centro = ?
                        AND p.cod_actividad = '91'
                        AND p.cod_variable = '001'
                        AND p.cod_estado IN ('1','2','5')
                        AND (
                                CASE 
                                    WHEN cod_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla1, code_asegurado, tabla2, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, codcas);
    }
    /// Atenciones médicas - medicos
    public List<Map<String, Object>> queryDMAMM(String anio, String periodo, String codcas) {
        String tabla = "dwsge.dw_consulta_externa_homologacion_" + anio + "_" + periodo;

        String sql = """
                        SELECT c.cod_centro,
                            c.dni_medico,
                            c.agrupador,
                            c.periodo,
                            c.cantidad_medicos,
                            c.medico
                        FROM ( SELECT b.cod_centro,
                                    b.dni_medico,
                                    b.agrupador,
                                    b.periodo,
                                    b.cantidad_medicos,
                                    row_number() OVER (PARTITION BY b.cod_centro, b.dni_medico, b.periodo ORDER BY b.cantidad_medicos DESC) AS medico
                                FROM ( SELECT a.cod_centro,
                                            a.dni_medico,
                                            ag.agrupador,
                                            a.periodo,
                                            CASE WHEN a.cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                                            count(*) AS cantidad_medicos
                                        FROM (SELECT * FROM %s) a
                                        LEFT JOIN dwsge.dim_agrupador ag
                                        ON a.cod_agrupador = ag.cod_agrupador
                                        WHERE a.cod_centro= ?
                                        AND a.cod_actividad = '91'
                                        AND a.cod_variable = '001'
                                        AND a.clasificacion in (2,4,6)
                                        GROUP BY a.cod_centro, a.dni_medico, ag.agrupador, a.periodo,CASE WHEN a.cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                                        ORDER BY a.dni_medico, a.periodo, (count(*))) b) c
                        WHERE c.medico = '1'::bigint
                """.formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Atenciones médicas - total consultante
    public List<Map<String, Object>> queryDMAMTC(String anio, String periodo, String codcas, String codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        String period_sql = anio + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    WITH fecha_min_paciente AS (
                        SELECT cod_oricentro,cod_centro,doc_paciente,
                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                               to_char(MIN(to_date(fecha_atencion,'DD/MM/YYYY')),'YYYYMM') periodo
                        FROM %s
                        WHERE cod_variable='001' AND cod_actividad='91'
                        AND clasificacion IN (2,4,6) AND cod_centro=?
                        AND (
                                CASE
                                    WHEN cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                        GROUP BY cod_oricentro,cod_centro,doc_paciente,CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                    )
                    SELECT COUNT(DISTINCT doc_paciente) AS cantidad
                    FROM fecha_min_paciente WHERE periodo=?
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, period_sql);
    }
    /// Atenciones médicas - total consultante servicio
    public List<Map<String, Object>> queryDMAMTCS(String anio, String periodo, String codcas, String codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        String period_sql = anio + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    WITH fecha_min_paciente AS (
                        SELECT p.doc_paciente,ag.agrupador,
                            CASE WHEN p.cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                               to_char(MIN(to_date(p.fecha_atencion,'DD/MM/YYYY')),'YYYYMM') periodo
                        FROM %s p
                        LEFT JOIN dwsge.dim_agrupador ag ON p.cod_agrupador = ag.cod_agrupador
                        WHERE p.cod_variable='001' AND p.cod_actividad='91'
                        AND p.clasificacion IN (2,4,6) AND p.cod_centro= ?
                        AND (
                                CASE 
                                    WHEN p.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                        GROUP BY p.doc_paciente,ag.agrupador,CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                    )
                    SELECT agrupador,COUNT(DISTINCT doc_paciente) AS cantidad
                    FROM fecha_min_paciente WHERE periodo=? GROUP BY agrupador
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, period_sql);
    }

    /// Medicinas Complementaria - atenciones
    public List<Map<String, Object>> queryDMMCA(String anio, String periodo, String codcas, String codasegu) {
        String tabla = "dwsge.dw_consulta_externa_homologacion_" + anio + "_" + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                        SELECT 
                            ce.cod_servicio,
                            ce.cod_especialidad,
                            ca.cenasides,
                            ag.agrupador AS agrupador,
                            am.actdes AS actividad,
                            a.actespnom AS subactividad,
                            ce.cod_tipo_consulta,
                            ce.cod_diag,
                            c.servhosdes AS descripcion_servicio,
                            e.especialidad AS descripcion_especialidad,
                            t.tipcondes AS descripcion_tipo_consulta,
                            d.diagdes AS descripcion_diagnostico,
                            dni_medico,
                            doc_paciente,
                            cod_tipdoc_paciente,
                            sexo,
                            fecha_atencion,
                            acto_med,
                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente
                        FROM %s AS ce
                        LEFT JOIN dwsge.sgss_cmsho10 AS c 
                            ON ce.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON ce.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmtco10 AS t
                            ON ce.cod_tipo_consulta = t.tipconcod
                        LEFT JOIN dwsge.sgss_cmdia10 AS d
                            ON ce.cod_diag = d.diagcod
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON ce.cod_actividad = a.actcod
                            AND ce.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON ce.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON ce.cod_oricentro = ca.oricenasicod
                            AND ce.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                        WHERE ce.cod_centro = ?
                        AND cod_servicio= 'A91'
                        AND cod_actividad = '91'
                        AND ce.clasificacion in (2,4,6)
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Medicinas Complementaria - horas efectivas
    public List<Map<String, Object>> queryDMMCHE(String anio, String periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo;

        String sql = """
                        SELECT
                            ce.*,
                            c.servhosdes,
                            e.especialidad,
                            a.actespnom,
                            am.actdes,
                            ag.agrupador,
                            ca.cenasides
                        FROM %s AS ce
                        LEFT JOIN dwsge.sgss_cmsho10 AS c 
                            ON ce.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON ce.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON ce.cod_actividad = a.actcod
                            AND ce.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON ce.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON ce.cod_oricentro = ca.oricenasicod
                            AND ce.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                        WHERE ce.cod_centro = ?
                        AND cod_servicio= 'A91'
                        AND cod_actividad = '91'
                        AND ce.ate>0
                """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Medicinas Complementaria - horas programadas
    public List<Map<String, Object>> queryDMMCHP(String anio, String periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo;

        String sql = """
                        SELECT
                            p.*,
                            c.servhosdes,
                            e.especialidad,
                            ag.agrupador,
                            a.actespnom,
                            am.actdes,
                            ca.cenasides
                        FROM %s p
                        LEFT JOIN dwsge.sgss_cmsho10 AS c
                            ON p.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON p.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON p.cod_actividad = a.actcod
                            AND p.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON p.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON p.cod_oricentro = ca.oricenasicod
                            AND p.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON p.cod_agrupador = ag.cod_agrupador
                        WHERE (
                                p.cod_mot_suspension IS NULL 
                                OR p.cod_mot_suspension NOT IN ('04','09','10','99','13','16','11')
                            )
                        AND p.cod_centro = ?
                        AND cod_servicio= 'A91'
                        AND cod_actividad = '91'
                """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Medicinas Complementaria - medicos
    public List<Map<String, Object>> queryDMMCM(String anio, String periodo, String codcas) {
        String tabla = "dwsge.dw_consulta_externa_homologacion_" + anio + "_" + periodo;

        String sql = """
                        SELECT c.cod_centro,
                            c.dni_medico,
                            c.agrupador,
                            c.periodo,
                            c.cantidad_medicos,
                            c.medico
                        FROM ( SELECT b.cod_centro,
                                    b.dni_medico,
                                    b.agrupador,
                                    b.periodo,
                                    b.cantidad_medicos,
                                    row_number() OVER (PARTITION BY b.cod_centro, b.dni_medico, b.periodo ORDER BY b.cantidad_medicos DESC) AS medico
                                FROM ( SELECT a.cod_centro,
                                            a.dni_medico,
                                            ag.agrupador,
                                            a.periodo,
                                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                                            count(*) AS cantidad_medicos
                                        FROM (SELECT * FROM %s) a
                                        LEFT JOIN dwsge.dim_agrupador ag
                                        ON a.cod_agrupador = ag.cod_agrupador
                                        WHERE a.cod_centro= ?
                                        AND cod_servicio= 'A91'
                                        AND cod_actividad = '91'
                                        AND a.clasificacion in (2,4,6)
                                        GROUP BY a.cod_centro, a.dni_medico, ag.agrupador, a.periodo, CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                                        ORDER BY a.dni_medico, a.periodo, (count(*))) b) c
                        WHERE c.medico = '1'::bigint
                """.formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Medicinas Complementaria - total consultante
    public List<Map<String, Object>> queryDMMCTC(String anio, String periodo, String codcas, String codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        String periodo_sql = anio + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    WITH fecha_min_paciente AS (
                        SELECT cod_oricentro,cod_centro,doc_paciente,
                        CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                               to_char(MIN(to_date(fecha_atencion,'DD/MM/YYYY')),'YYYYMM') periodo
                        FROM %s
                        WHERE cod_servicio='A91'
                        AND cod_actividad = '91'
                        AND (
                                CASE 
                                    WHEN cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                        AND clasificacion IN (2,4,6) AND cod_centro = ?
                        GROUP BY cod_oricentro,cod_centro,doc_paciente, CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                    )
                    SELECT COUNT(DISTINCT doc_paciente) AS cantidad
                    FROM fecha_min_paciente WHERE periodo=?
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }
    /// Medicinas Complementaria - total consultante servicio
    public List<Map<String, Object>> queryDMMCTCS(String anio, String periodo, String codcas, String codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        String periodo_sql = anio + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    WITH fecha_min_paciente AS (
                        SELECT p.doc_paciente,ag.agrupador,
                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                               to_char(MIN(to_date(p.fecha_atencion,'DD/MM/YYYY')),'YYYYMM') periodo
                        FROM %s p
                        LEFT JOIN dwsge.dim_agrupador ag ON p.cod_agrupador = ag.cod_agrupador
                        WHERE p.cod_servicio='A91'
                        AND cod_actividad = '91'
                        AND p.clasificacion IN (2,4,6) AND p.cod_centro=?
                        AND (
                                CASE 
                                    WHEN p.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                        GROUP BY p.doc_paciente,ag.agrupador, CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                    )
                    SELECT agrupador,COUNT(DISTINCT doc_paciente) AS cantidad
                    FROM fecha_min_paciente WHERE periodo=? GROUP BY agrupador
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }

    /// Medicina Ocupacional - atenciones
    public List<Map<String, Object>> queryDMMOA(String anio, String periodo, String codcas, String codasegu) {
        String tabla = "dwsge.dw_consulta_externa_homologacion_" + anio + "_" + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                        SELECT
                            ce.cod_servicio,
                            ce.cod_especialidad,
                            ca.cenasides,
                            ag.agrupador AS agrupador,
                            am.actdes AS actividad,
                            a.actespnom AS subactividad,
                            ce.cod_tipo_consulta,
                            ce.cod_diag,
                            c.servhosdes AS descripcion_servicio,
                            e.especialidad AS descripcion_especialidad,
                            t.tipcondes AS descripcion_tipo_consulta,
                            d.diagdes AS descripcion_diagnostico,
                            dni_medico,
                            doc_paciente,
                            cod_tipdoc_paciente,
                            sexo,
                            fecha_atencion,
                            acto_med,
                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente
                        FROM %s AS ce
                        LEFT JOIN dwsge.sgss_cmsho10 AS c 
                            ON ce.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON ce.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmtco10 AS t
                            ON ce.cod_tipo_consulta = t.tipconcod
                        LEFT JOIN dwsge.sgss_cmdia10 AS d
                            ON ce.cod_diag = d.diagcod
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON ce.cod_actividad = a.actcod
                            AND ce.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON ce.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON ce.cod_oricentro = ca.oricenasicod
                            AND ce.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                        WHERE ce.cod_centro = ?
                        AND ce.cod_actividad in ('B8', '91')
                        AND ce.cod_subactividad = '070'
                        AND ce.cod_servicio in ('AB1', 'AM6')
                        AND ce.clasificacion in (2,4,6)
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Medicina Ocupacional - horas efectivas
    public List<Map<String, Object>> queryDMMOHE(String anio, String periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo;

        String sql = """
                        SELECT 
                            ce.*, 
                            c.servhosdes,
                            e.especialidad,
                            a.actespnom,
                            am.actdes,
                            ag.agrupador,
                            ca.cenasides
                        FROM %s AS ce
                        LEFT JOIN dwsge.sgss_cmsho10 AS c 
                            ON ce.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON ce.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON ce.cod_actividad = a.actcod
                            AND ce.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON ce.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON ce.cod_oricentro = ca.oricenasicod
                            AND ce.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                        WHERE ce.cod_centro = ?
                        AND ce.cod_actividad IN ('B8', '91')
                        AND ce.cod_subactividad = '070'
                        AND ce.cod_servicio IN ('AB1', 'AM6')
                        AND ce.ate>0
                """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Medicina Ocupacional - horas programadas
    public List<Map<String, Object>> queryDMMOHP(String anio, String periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo;

        String sql = """
                SELECT 
                    p.*, 
                    c.servhosdes,
                    e.especialidad,
                    ag.agrupador,
                    a.actespnom,
                    am.actdes,
                    ca.cenasides 
                FROM %s p
                LEFT JOIN dwsge.sgss_cmsho10 AS c 
                    ON p.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e
                    ON p.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmace10 AS a
                    ON p.cod_actividad = a.actcod
                    AND p.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am
                    ON p.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca
                    ON p.cod_oricentro = ca.oricenasicod
                    AND p.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON p.cod_agrupador = ag.cod_agrupador
                WHERE (
                        p.cod_mot_suspension IS NULL 
                        OR p.cod_mot_suspension NOT IN ('04','09','10','99','13','16','11')
                    )
                AND p.cod_centro = ?
                AND p.cod_actividad = '91'
                AND p.cod_subactividad = '003'
                """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Medicina Ocupacional - medicos
    public List<Map<String, Object>> queryDMMOM(String anio, String periodo, String codcas) {
        String tabla = "dwsge.dw_consulta_externa_homologacion_" + anio + "_" + periodo;

        String sql = """
                        SELECT c.cod_centro,
                            c.dni_medico,
                            c.agrupador,
                            c.periodo,
                            c.cantidad_medicos,
                            c.medico
                        FROM ( SELECT b.cod_centro,
                                    b.dni_medico,
                                    b.agrupador,
                                    b.periodo,
                                    b.cantidad_medicos,
                                    row_number() OVER (PARTITION BY b.cod_centro, b.dni_medico, b.periodo ORDER BY b.cantidad_medicos DESC) AS medico
                                FROM ( SELECT a.cod_centro,
                                            a.dni_medico,
                                            ag.agrupador,
                                            a.periodo,
                                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                                            count(*) AS cantidad_medicos
                                        FROM (SELECT * FROM %s) a
                                        LEFT JOIN dwsge.dim_agrupador ag 
                                        ON a.cod_agrupador = ag.cod_agrupador
                                        WHERE a.cod_centro= ?
                                        AND a.cod_actividad = '91'
                                        AND a.cod_subactividad = '003'
                                        AND a.clasificacion in (2,4,6)
                                        GROUP BY a.cod_centro, a.dni_medico, ag.agrupador, a.periodo, CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                                        ORDER BY a.dni_medico, a.periodo, (count(*))) b) c
                        WHERE c.medico = '1'::bigint
                """.formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Medicina Ocupacional - total consultante
    public List<Map<String, Object>> queryDMMOTC(String anio, String periodo, String codcas, String codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        String periodo_sql = anio + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    WITH fecha_min_paciente AS (
                        SELECT cod_oricentro,cod_centro,doc_paciente,
        CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                            to_char(MIN(to_date(fecha_atencion,'DD/MM/YYYY')),'YYYYMM') periodo
                        FROM %s
                        WHERE cod_actividad = '91'
                        AND cod_subactividad = '003'
                        AND (
                                CASE 
                                    WHEN cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                        AND clasificacion IN (2,4,6) AND cod_centro= ?
                        GROUP BY cod_oricentro,cod_centro,doc_paciente, CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                    )
                    SELECT COUNT(DISTINCT doc_paciente) AS cantidad
                    FROM fecha_min_paciente WHERE periodo= ?
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }
    /// Medicina Ocupacional - total consultante servicio
    public List<Map<String, Object>> queryDMMOTCS(String anio, String periodo, String codcas, String codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        String periodo_sql = anio + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    WITH fecha_min_paciente AS (
                        SELECT p.doc_paciente,ag.agrupador,
                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                            to_char(MIN(to_date(p.fecha_atencion,'DD/MM/YYYY')),'YYYYMM') periodo
                        FROM %s p
                        LEFT JOIN dwsge.dim_agrupador ag ON p.cod_agrupador = ag.cod_agrupador
                        WHERE p.clasificacion IN (2,4,6) 
                        AND p.cod_centro= ?
                        AND p.cod_actividad = '91'
                        AND p.cod_subactividad = '003'
                        AND (
                                CASE 
                                    WHEN p.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                        GROUP BY p.doc_paciente,ag.agrupador, CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                    )
                    SELECT agrupador,COUNT(DISTINCT doc_paciente) AS cantidad
                    FROM fecha_min_paciente WHERE periodo= ? GROUP BY agrupador
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }

    /// Médico Personal - Atenciones
    public List<Map<String, Object>> queryDMMPA(String anio, String periodo, String codcas, String codasegu) {
        String tabla = "dwsge.dw_consulta_externa_homologacion_" + anio + "_" + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                        SELECT 
                            ce.cod_servicio,
                            ce.cod_especialidad,
                            ca.cenasides,
                            ag.agrupador AS agrupador,
                            am.actdes AS actividad,
                            a.actespnom AS subactividad,
                            ce.cod_tipo_consulta,
                            ce.cod_diag,
                            c.servhosdes AS descripcion_servicio,
                            e.especialidad AS descripcion_especialidad,
                            t.tipcondes AS descripcion_tipo_consulta,
                            d.diagdes AS descripcion_diagnostico,
                            dni_medico,
                            doc_paciente,
                            cod_tipdoc_paciente,
                            sexo,
                            fecha_atencion,
                            acto_med,
                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente
                        FROM %s AS ce
                        LEFT JOIN dwsge.sgss_cmsho10 AS c 
                            ON ce.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON ce.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmtco10 AS t
                            ON ce.cod_tipo_consulta = t.tipconcod
                        LEFT JOIN dwsge.sgss_cmdia10 AS d
                            ON ce.cod_diag = d.diagcod
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON ce.cod_actividad = a.actcod
                            AND ce.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON ce.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON ce.cod_oricentro = ca.oricenasicod
                            AND ce.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                        WHERE ce.cod_centro = ?
                        AND ce.cod_actividad = '91'
                        AND ce.cod_subactividad = '682'
                        AND ce.cod_servicio = 'L16'
                        AND ce.clasificacion in (2,4,6)
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Médico Personal - Horas Efectivas
    public List<Map<String, Object>> queryDMMPHE(String anio, String periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo;

        String sql = """
                        SELECT 
                            ce.*, 
                            c.servhosdes,
                            e.especialidad,
                            a.actespnom,
                            am.actdes,
                            ag.agrupador,
                            ca.cenasides
                        FROM %s AS ce
                        LEFT JOIN dwsge.sgss_cmsho10 AS c 
                            ON ce.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON ce.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON ce.cod_actividad = a.actcod
                            AND ce.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON ce.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON ce.cod_oricentro = ca.oricenasicod
                            AND ce.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                        WHERE ce.cod_centro = ?
                        AND ce.cod_actividad = '91'
                        AND ce.cod_subactividad = '682'
                        AND ce.cod_servicio = 'L16'
                        AND ce.ate>0
                """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Médico Personal - Horas Programadas
    public List<Map<String, Object>> queryDMMPHP(String anio, String periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo;

        String sql = """
                SELECT 
                    p.*, 
                    c.servhosdes,
                    e.especialidad,
                    ag.agrupador,
                    a.actespnom,
                    am.actdes,
                    ca.cenasides 
                FROM %s p
                LEFT JOIN dwsge.sgss_cmsho10 AS c 
                    ON p.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e
                    ON p.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmace10 AS a
                    ON p.cod_actividad = a.actcod
                    AND p.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am
                    ON p.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca
                    ON p.cod_oricentro = ca.oricenasicod
                    AND p.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON p.cod_agrupador = ag.cod_agrupador
                WHERE (
                        p.cod_mot_suspension IS NULL 
                        OR p.cod_mot_suspension NOT IN ('04','09','10','99','13','16','11')
                    )
                AND p.cod_centro = ?
                AND p.cod_actividad = '91'
                AND p.cod_subactividad = '682'
                AND p.cod_servicio = 'L16'
                """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Médico Personal - Medicos
    public List<Map<String, Object>> queryDMMPM(String anio, String periodo, String codcas) {
        String tabla = "dwsge.dw_consulta_externa_homologacion_" + anio + "_" + periodo;

        String sql = """
                        SELECT c.cod_centro,
                            c.dni_medico,
                            c.agrupador,
                            c.periodo,
                            c.cantidad_medicos,
                            c.medico
                        FROM ( SELECT b.cod_centro,
                                    b.dni_medico,
                                    b.agrupador,
                                    b.periodo,
                                    b.cantidad_medicos,
                                    row_number() OVER (PARTITION BY b.cod_centro, b.dni_medico, b.periodo ORDER BY b.cantidad_medicos DESC) AS medico
                                FROM ( SELECT a.cod_centro,
                                            a.dni_medico,
                                            ag.agrupador,
                                            a.periodo,
                                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,                
                                            count(*) AS cantidad_medicos
                                        FROM (SELECT * FROM %s) a
                                        LEFT JOIN dwsge.dim_agrupador ag 
                                        ON a.cod_agrupador = ag.cod_agrupador
                                        WHERE a.cod_centro= ?
                                        AND a.cod_actividad = '91'
                                        AND a.cod_subactividad = '682'
                                        AND a.cod_servicio = 'L16'
                                        AND a.clasificacion in (2,4,6)
                                        GROUP BY a.cod_centro, a.dni_medico, ag.agrupador, a.periodo, CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                                        ORDER BY a.dni_medico, a.periodo, (count(*))) b) c
                        WHERE c.medico = '1'::bigint
                """.formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Médico Personal - Total Consultante
    public List<Map<String, Object>> queryDMMPTC(String anio, String periodo, String codcas, String codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        String periodo_sql = anio + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    WITH fecha_min_paciente AS (
                        SELECT cod_oricentro,cod_centro,doc_paciente,
        CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                            to_char(MIN(to_date(fecha_atencion,'DD/MM/YYYY')),'YYYYMM') periodo
                        FROM %s
                        WHERE cod_actividad = '91'
                        AND cod_subactividad = '682'
                        AND cod_servicio = 'L16'
                        AND (
                                CASE 
                                    WHEN cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                        AND clasificacion IN (2,4,6) AND cod_centro= ?
                        GROUP BY cod_oricentro,cod_centro,doc_paciente, CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                    )
                    SELECT COUNT(DISTINCT doc_paciente) AS cantidad
                    FROM fecha_min_paciente WHERE periodo= ?
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }
    /// Médico Personal - Total Consultante Servicio
    public List<Map<String, Object>> queryDMMPTCS(String anio, String periodo, String codcas, String codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        String periodo_sql = anio + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    WITH fecha_min_paciente AS (
                        SELECT p.doc_paciente,ag.agrupador,
                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                            to_char(MIN(to_date(p.fecha_atencion,'DD/MM/YYYY')),'YYYYMM') periodo
                        FROM %s p
                        LEFT JOIN dwsge.dim_agrupador ag ON p.cod_agrupador = ag.cod_agrupador
                        WHERE p.clasificacion IN (2,4,6)
                        AND p.cod_centro= ?
                        AND p.cod_actividad = '91'
                        AND p.cod_subactividad = '682'
                        AND p.cod_servicio = 'L16'
                        AND (
                                CASE 
                                    WHEN p.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                        GROUP BY p.doc_paciente,ag.agrupador, CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                    )
                    SELECT agrupador,COUNT(DISTINCT doc_paciente) AS cantidad
                    FROM fecha_min_paciente WHERE periodo= ? GROUP BY agrupador
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }

    /// Consulta Atención Inmediata - atenciones
    public List<Map<String, Object>> queryDMCAIA(String anio, String periodo, String codcas, String codasegu ) {
        String tabla= "dwsge.dw_consulta_externa_homologacion_" + anio + "_" + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                        SELECT 
                            ce.cod_servicio,
                            ce.cod_especialidad,
                            ca.cenasides,
                            ag.agrupador AS agrupador,
                            am.actdes AS actividad,
                            a.actespnom AS subactividad,
                            ce.cod_tipo_consulta,
                            ce.cod_diag,
                            c.servhosdes AS descripcion_servicio,
                            e.especialidad AS descripcion_especialidad,
                            t.tipcondes AS descripcion_tipo_consulta,
                            d.diagdes AS descripcion_diagnostico,
                            dni_medico,
                            doc_paciente,
                            cod_tipdoc_paciente,
                            sexo,
                            fecha_atencion,
                            acto_med,
                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente
                        FROM %s AS ce
                        LEFT JOIN dwsge.sgss_cmsho10 AS c 
                            ON ce.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON ce.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmtco10 AS t
                            ON ce.cod_tipo_consulta = t.tipconcod
                        LEFT JOIN dwsge.sgss_cmdia10 AS d
                            ON ce.cod_diag = d.diagcod
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON ce.cod_actividad = a.actcod
                            AND ce.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON ce.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON ce.cod_oricentro = ca.oricenasicod
                            AND ce.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                        WHERE ce.cod_centro = ?
                        AND a.actespcod in ('002', '112')
                        AND ce.cod_actividad ='91'
                        AND ce.clasificacion in (2,4,6)
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
        """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql,codcas);
    }
    /// Consulta Atención Inmediata - horas efectivas
    public List<Map<String, Object>> queryDMCAIHE(String anio, String periodo, String codcas) {
        String tabla= "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo;

        String sql = """
                        SELECT 
                            ce.*,
                            c.servhosdes,
                            e.especialidad,
                            a.actespnom,
                            am.actdes,
                            ag.agrupador,
                            ca.cenasides
                        FROM %s AS ce
                        LEFT JOIN dwsge.sgss_cmsho10 AS c 
                            ON ce.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON ce.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON ce.cod_actividad = a.actcod
                            AND ce.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON ce.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON ce.cod_oricentro = ca.oricenasicod
                            AND ce.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                        WHERE ce.cod_centro = ?
                        AND a.actespcod = '002'
                        AND ce.cod_actividad ='91'
                        AND ce.ate>0
        """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql,codcas);
    }
    /// Consulta Atención Inmediata - horas programadas
    public List<Map<String, Object>> queryDMCAIHP(String anio, String periodo, String codcas) {
        String tabla= "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo;

        String sql = """
                SELECT 
                    p.*,
                    c.servhosdes,
                    e.especialidad,
                    ag.agrupador,
                    a.actespnom,
                    am.actdes,
                    ca.cenasides 
                FROM %s p
                LEFT JOIN dwsge.sgss_cmsho10 AS c 
                    ON p.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e
                    ON p.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmace10 AS a
                    ON p.cod_actividad = a.actcod
                    AND p.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am
                    ON p.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca
                    ON p.cod_oricentro = ca.oricenasicod
                    AND p.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON p.cod_agrupador = ag.cod_agrupador
                WHERE (
                        p.cod_mot_suspension IS NULL 
                        OR p.cod_mot_suspension NOT IN ('04','09','10','99','13','16','11')
                    )
                AND p.cod_centro = ?
                AND a.actespcod = '002'
                AND p.cod_actividad ='91'
        """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql,codcas);
    }
    /// Consulta Atención Inmediata - medicos
    public List<Map<String, Object>> queryDMCAIM(String anio, String periodo, String codcas) {
        String tabla= "dwsge.dw_consulta_externa_homologacion_" + anio + "_" + periodo;

        String sql = """
                        SELECT c.cod_centro,
                            c.dni_medico,
                            c.agrupador,
                            c.periodo,
                            c.cantidad_medicos,
                            c.medico
                        FROM ( SELECT b.cod_centro,
                                    b.dni_medico,
                                    b.agrupador,
                                    b.periodo,
                                    b.cantidad_medicos,
                                    row_number() OVER (PARTITION BY b.cod_centro, b.dni_medico, b.periodo ORDER BY b.cantidad_medicos DESC) AS medico
                                FROM ( SELECT a.cod_centro,
                                            a.dni_medico,
                                            ag.agrupador,
                                            a.periodo,
                                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                                            count(*) AS cantidad_medicos
                                        FROM (SELECT * FROM %s) a
                                        LEFT JOIN dwsge.dim_agrupador ag
                                        ON a.cod_agrupador = ag.cod_agrupador
                                        WHERE a.cod_centro= ?
                                        AND cod_subactividad = '002'
                                        AND a.cod_actividad ='91'
                                        AND a.clasificacion in (2,4,6)
                                        GROUP BY a.cod_centro, a.dni_medico, ag.agrupador, a.periodo, CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                                        ORDER BY a.dni_medico, a.periodo, (count(*))) b) c
                        WHERE c.medico = '1'::bigint
        """.formatted(tabla);

        return secondJdbcTemplate.queryForList(sql,codcas);
    }
    /// Consulta Atención Inmediata - total de consultante
    public List<Map<String, Object>> queryDMCAITC(String anio, String periodo, String codcas, String codasegu ) {
        String tabla= "dwsge.dwe_consulta_externa_homologacion_" + anio;
        String periodo_sql= anio+periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    WITH fecha_min_paciente AS (
                        SELECT cod_oricentro,cod_centro,doc_paciente,
        CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                            to_char(MIN(to_date(fecha_atencion,'DD/MM/YYYY')),'YYYYMM') periodo
                        FROM %s
                        WHERE cod_subactividad = '002'
                        AND cod_actividad ='91'
                        AND (
                                CASE 
                                    WHEN cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                        AND clasificacion IN (2,4,6) AND cod_centro= ?
                        GROUP BY cod_oricentro,cod_centro,doc_paciente, CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                    )
                    SELECT COUNT(DISTINCT doc_paciente) AS cantidad
                    FROM fecha_min_paciente WHERE periodo= ?
        """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql,codcas, periodo_sql);
    }
    /// Consulta Atención Inmediata - total de consultante servicio
    public List<Map<String, Object>> queryDMCAITCS(String anio, String periodo, String codcas, String codasegu ) {
        String tabla= "dwsge.dwe_consulta_externa_homologacion_" + anio;
        String periodo_sql= anio+periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    WITH fecha_min_paciente AS (
                        SELECT p.doc_paciente,ag.agrupador,
                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                            to_char(MIN(to_date(p.fecha_atencion,'DD/MM/YYYY')),'YYYYMM') periodo
                        FROM %s p
                        LEFT JOIN dwsge.dim_agrupador ag ON p.cod_agrupador = ag.cod_agrupador
                        WHERE p.clasificacion IN (2,4,6)
                        AND p.cod_centro= ?
                        AND p.cod_subactividad = '002'
                        AND p.cod_actividad ='91'
                        AND (
                                CASE
                                    WHEN p.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                        GROUP BY p.doc_paciente,ag.agrupador, CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                    )
                    SELECT agrupador,COUNT(DISTINCT doc_paciente) AS cantidad
                    FROM fecha_min_paciente WHERE periodo= ? GROUP BY agrupador
        """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql,codcas, periodo_sql);
    }

    /// Consulta Apoyo Desc - atenciones
    public List<Map<String, Object>> queryDMCADA(String anio, String periodo, String codcas, String codasegu ) {
        String tabla= "dwsge.dw_consulta_externa_homologacion_" + anio + "_" + periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                        SELECT 
                            ce.cod_servicio,
                            ce.cod_especialidad,
                            ca.cenasides,
                            ag.agrupador AS agrupador,
                            am.actdes AS actividad,
                            a.actespnom AS subactividad,
                            ce.cod_tipo_consulta,
                            ce.cod_diag,
                            c.servhosdes AS descripcion_servicio,
                            e.especialidad AS descripcion_especialidad,
                            t.tipcondes AS descripcion_tipo_consulta,
                            d.diagdes AS descripcion_diagnostico,
                            dni_medico,
                            doc_paciente,
                            cod_tipdoc_paciente,
                            sexo,
                            fecha_atencion,
                            acto_med,
                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente
                        FROM %s AS ce
                        LEFT JOIN dwsge.sgss_cmsho10 AS c 
                            ON ce.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON ce.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmtco10 AS t
                            ON ce.cod_tipo_consulta = t.tipconcod
                        LEFT JOIN dwsge.sgss_cmdia10 AS d
                            ON ce.cod_diag = d.diagcod
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON ce.cod_actividad = a.actcod
                            AND ce.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON ce.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON ce.cod_oricentro = ca.oricenasicod
                            AND ce.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                        WHERE ce.cod_centro = ?
                        AND ce.cod_actividad = '91'
                        AND ce.cod_subactividad = '003'
                        AND ce.clasificacion in (2,4,6)
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
        """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql,codcas);
    }
    /// Consulta Apoyo Desc - horas efectivas
    public List<Map<String, Object>> queryDMCADHE(String anio, String periodo, String codcas) {
        String tabla= "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo;

        String sql = """
                        SELECT 
                            ce.*, 
                            c.servhosdes,
                            e.especialidad,
                            a.actespnom,
                            am.actdes,
                            ag.agrupador,
                            ca.cenasides
                        FROM %s AS ce
                        LEFT JOIN dwsge.sgss_cmsho10 AS c 
                            ON ce.cod_servicio = c.servhoscod
                        LEFT JOIN dwsge.dim_especialidad AS e
                            ON ce.cod_especialidad = e.cod_especialidad
                        LEFT JOIN dwsge.sgss_cmace10 AS a
                            ON ce.cod_actividad = a.actcod
                            AND ce.cod_subactividad = a.actespcod
                        LEFT JOIN dwsge.sgss_cmact10 AS am
                            ON ce.cod_actividad = am.actcod
                        LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON ce.cod_oricentro = ca.oricenasicod
                            AND ce.cod_centro = ca.cenasicod
                        LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                        WHERE ce.cod_centro = ?
                        AND ce.cod_actividad = '91'
                        AND ce.cod_subactividad = '003'
                        AND ce.ate>0
        """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql,codcas);
    }
    /// Consulta Apoyo Desc - horas programadas
    public List<Map<String, Object>> queryDMCADHP(String anio, String periodo, String codcas) {
        String tabla= "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo;

        String sql = """
                SELECT 
                    p.*, 
                    c.servhosdes,
                    e.especialidad,
                    ag.agrupador,
                    a.actespnom,
                    am.actdes,
                    ca.cenasides 
                FROM %s p
                LEFT JOIN dwsge.sgss_cmsho10 AS c 
                    ON p.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e
                    ON p.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmace10 AS a
                    ON p.cod_actividad = a.actcod
                    AND p.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am
                    ON p.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca
                    ON p.cod_oricentro = ca.oricenasicod
                    AND p.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON p.cod_agrupador = ag.cod_agrupador
                WHERE (
                        p.cod_mot_suspension IS NULL 
                        OR p.cod_mot_suspension NOT IN ('04','09','10','99','13','16','11')
                    )
                AND p.cod_centro = ?
                AND p.cod_actividad = '91'
                AND p.cod_subactividad = '003'
        """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql,codcas);
    }
    /// Consulta Apoyo Desc - medicos
    public List<Map<String, Object>> queryDMCADM(String anio, String periodo, String codcas) {
        String tabla= "dwsge.dw_consulta_externa_homologacion_" + anio + "_" + periodo;

        String sql = """
                        SELECT c.cod_centro,
                            c.dni_medico,
                            c.agrupador,
                            c.periodo,
                            c.cantidad_medicos,
                            c.medico
                        FROM ( SELECT b.cod_centro,
                                    b.dni_medico,
                                    b.agrupador,
                                    b.periodo,
                                    b.cantidad_medicos,
                                    row_number() OVER (PARTITION BY b.cod_centro, b.dni_medico, b.periodo ORDER BY b.cantidad_medicos DESC) AS medico
                                FROM ( SELECT a.cod_centro,
                                            a.dni_medico,
                                            ag.agrupador,
                                            a.periodo,
                                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,                
                                            count(*) AS cantidad_medicos
                                        FROM (SELECT * FROM %s) a
                                        LEFT JOIN dwsge.dim_agrupador ag 
                                        ON a.cod_agrupador = ag.cod_agrupador
                                        WHERE a.cod_centro= ?
                                        AND a.cod_actividad = '91'
                                        AND a.cod_subactividad = '003'
                                        AND a.clasificacion in (2,4,6)
                                        GROUP BY a.cod_centro, a.dni_medico, ag.agrupador, a.periodo, CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                                        ORDER BY a.dni_medico, a.periodo, (count(*))) b) c
                        WHERE c.medico = '1'::bigint
        """.formatted(tabla);

        return secondJdbcTemplate.queryForList(sql,codcas);
    }
    /// Consulta Apoyo Desc - total consultante
    public List<Map<String, Object>> queryDMCADTC(String anio, String periodo, String codcas, String codasegu ) {
        String tabla= "dwsge.dwe_consulta_externa_homologacion_" + anio;
        String periodo_sql= anio+periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    WITH fecha_min_paciente AS (
                        SELECT cod_oricentro,cod_centro,doc_paciente,
        CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                            to_char(MIN(to_date(fecha_atencion,'DD/MM/YYYY')),'YYYYMM') periodo
                        FROM %s
                        WHERE cod_actividad = '91'
                        AND cod_subactividad = '003'
                        AND (
                                CASE
                                    WHEN cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                        AND clasificacion IN (2,4,6) AND cod_centro= ?
                        GROUP BY cod_oricentro,cod_centro,doc_paciente, CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                    )
                    SELECT COUNT(DISTINCT doc_paciente) AS cantidad
                    FROM fecha_min_paciente WHERE periodo= ?
        """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql,codcas, periodo_sql);
    }
    /// Consulta Apoyo Desc - total consultante servicio
    public List<Map<String, Object>> queryDMCADTCS(String anio, String periodo, String codcas, String codasegu ) {
        String tabla= "dwsge.dwe_consulta_externa_homologacion_" + anio;
        String periodo_sql= anio+periodo;

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    WITH fecha_min_paciente AS (
                        SELECT p.doc_paciente,ag.agrupador,
                            CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                            to_char(MIN(to_date(p.fecha_atencion,'DD/MM/YYYY')),'YYYYMM') periodo
                        FROM %s p
                        LEFT JOIN dwsge.dim_agrupador ag ON p.cod_agrupador = ag.cod_agrupador
                        WHERE p.clasificacion IN (2,4,6) 
                        AND p.cod_centro= ?
                        AND p.cod_actividad = '91'
                        AND p.cod_subactividad = '003'
                        AND (
                                CASE 
                                    WHEN p.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                        GROUP BY p.doc_paciente,ag.agrupador, CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                    )
                    SELECT agrupador,COUNT(DISTINCT doc_paciente) AS cantidad
                    FROM fecha_min_paciente WHERE periodo= ? GROUP BY agrupador
        """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql,codcas, periodo_sql);
    }

    ///* Indicadores de atenciones medicas */
    /// total_atenciones - servicio - subactividad

    /// total_atenciones - agrupador - especialidad


}
