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

    /// Query Dashboard medicas
    /// Atenciones Medicas
    public List<Map<String, Object>> queryDMAMA(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);
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
    public List<Map<String, Object>> queryDMAMHE(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

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
    public List<Map<String, Object>> queryDMAMHP(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

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
    public List<Map<String, Object>> queryDMAMC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_citados_homologacion_" + anio + "_" + periodo_str(periodo);

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
    public List<Map<String, Object>> queryDMAMD(int anio, int periodo, String codcas, int codasegu) {

        String tabla1 = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);
        String tabla2 = "dwsge.dwe_consulta_externa_citados_homologacion_" + anio + "_" + periodo_str(periodo);

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
    public List<Map<String, Object>> queryDMAMM(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

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
                """
                .formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Atenciones médicas - total consultante
    public List<Map<String, Object>> queryDMAMTC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        // String periodo_sql = anio + periodo; ///AAAAMM
        String periodo_sql = String.format("%d%02d", anio, periodo);

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
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }

    /// Atenciones médicas - total consultante servicio
    public List<Map<String, Object>> queryDMAMTCS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        // String periodo_sql = anio + periodo; ///AAAAMM
        String periodo_sql = String.format("%d%02d", anio, periodo);

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

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }

    /// Medicinas Complementaria - atenciones
    public List<Map<String, Object>> queryDMMCA(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

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
    public List<Map<String, Object>> queryDMMCHE(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

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
    public List<Map<String, Object>> queryDMMCHP(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

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
    public List<Map<String, Object>> queryDMMCM(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

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
                """
                .formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Medicinas Complementaria - total consultante
    public List<Map<String, Object>> queryDMMCTC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        // //String periodo_sql = anio + periodo; ///AAAAMM
        String periodo_sql = String.format("%d%02d", anio, periodo);

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
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }

    /// Medicinas Complementaria - total consultante servicio
    public List<Map<String, Object>> queryDMMCTCS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        // String periodo_sql = anio + periodo; ///AAAAMM
        String periodo_sql = String.format("%d%02d", anio, periodo);

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
    public List<Map<String, Object>> queryDMMOA(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

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
    public List<Map<String, Object>> queryDMMOHE(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

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
    public List<Map<String, Object>> queryDMMOHP(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

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
    public List<Map<String, Object>> queryDMMOM(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

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
                """
                .formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Medicina Ocupacional - total consultante
    public List<Map<String, Object>> queryDMMOTC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        // String periodo_sql = anio + periodo; ///AAAAMM
        String periodo_sql = String.format("%d%02d", anio, periodo);

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
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }

    /// Medicina Ocupacional - total consultante servicio
    public List<Map<String, Object>> queryDMMOTCS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        // String periodo_sql = anio + periodo; ///AAAAMM
        String periodo_sql = String.format("%d%02d", anio, periodo);

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
    public List<Map<String, Object>> queryDMMPA(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

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
    public List<Map<String, Object>> queryDMMPHE(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

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
    public List<Map<String, Object>> queryDMMPHP(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

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
    public List<Map<String, Object>> queryDMMPM(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

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
                """
                .formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Médico Personal - Total Consultante
    public List<Map<String, Object>> queryDMMPTC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        // String periodo_sql = anio + periodo; ///AAAAMM
        String periodo_sql = String.format("%d%02d", anio, periodo);

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
               """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }

    /// Médico Personal - Total Consultante Servicio
    public List<Map<String, Object>> queryDMMPTCS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        // String periodo_sql = anio + periodo; ///AAAAMM
        String periodo_sql = String.format("%d%02d", anio, periodo);

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
    public List<Map<String, Object>> queryDMCAIA(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

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

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Consulta Atención Inmediata - horas efectivas
    public List<Map<String, Object>> queryDMCAIHE(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

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
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Consulta Atención Inmediata - horas programadas
    public List<Map<String, Object>> queryDMCAIHP(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

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
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Consulta Atención Inmediata - medicos
    public List<Map<String, Object>> queryDMCAIM(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

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
                """
                .formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Consulta Atención Inmediata - total de consultante
    public List<Map<String, Object>> queryDMCAITC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        // String periodo_sql = anio + periodo; ///AAAAMM
        String periodo_sql = String.format("%d%02d", anio, periodo);

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
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }

    /// Consulta Atención Inmediata - total de consultante servicio
    public List<Map<String, Object>> queryDMCAITCS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        // String periodo_sql = anio + periodo; ///AAAAMM
        String periodo_sql = String.format("%d%02d", anio, periodo);

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
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }

    /// Consulta Apoyo Desc - atenciones
    public List<Map<String, Object>> queryDMCADA(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

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

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Consulta Apoyo Desc - horas efectivas
    public List<Map<String, Object>> queryDMCADHE(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

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
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Consulta Apoyo Desc - horas programadas
    public List<Map<String, Object>> queryDMCADHP(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

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

    /// Consulta Apoyo Desc - medicos
    public List<Map<String, Object>> queryDMCADM(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

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
                """
                .formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Consulta Apoyo Desc - total consultante
    public List<Map<String, Object>> queryDMCADTC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        // String periodo_sql = anio + periodo; ///AAAAMM
        String periodo_sql = String.format("%d%02d", anio, periodo);

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
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }

    /// Consulta Apoyo Desc - total consultante servicio
    public List<Map<String, Object>> queryDMCADTCS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio;
        // String periodo_sql = anio + periodo; ///AAAAMM
        String periodo_sql = String.format("%d%02d", anio, periodo);

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
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }

    /// Indicadores de atenciones medicas
    /// total_atenciones - servicio - subactividad
    public List<Map<String, Object>> query0DMAMTASS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    c.servhosdes AS descripcion_servicio,
                    a.actespnom AS descripcion_subactividad
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
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

    /// total_atenciones - agrupador - especialidad
    public List<Map<String, Object>> query1DMAMTAAE(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
                    a.actespnom AS subactividad,
                    ce.cod_tipo_consulta,
                    ce.cod_diag,
                    d.diagdes AS descripcion_diagnostico,
                    c.servhosdes AS descripcion_servicio,
                    e.especialidad AS descripcion_especialidad,
                    t.tipcondes AS descripcion_tipo_consulta,
                    dni_medico,
                    doc_paciente,
                    cod_tipdoc_paciente,
                    sexo,
                    fecha_atencion,
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
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
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total_atenciones - no considerados
    public List<Map<String, Object>> query2DMAMTANC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    c.servhosdes AS Servicio2,
                    am.actdes AS Actividad2,
                    a.actespnom AS Subactividad2,
                    t.tipcondes AS Tipo_consulta2,
                    ce.cod_diag as CodDiag2,
                    d.diagdes AS Diagnostico2,
                    dni_medico as dni_medico2,
                    acto_med as acto_med2,
                    sexo as sexo2,
                    fecha_atencion as fecha_atencion2,
                    cl.desc_cl AS desc_cl2
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                LEFT JOIN dwsge.dwe_cl10 as cl ON ce.clasificacion = cl.id_cl
                WHERE ce.cod_centro = ?
                  AND ce.cod_actividad = '91'
                  AND ce.clasificacion in (1,3,5,0)
                  AND ce.cod_variable = '001'
                  AND (
                            CASE
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total_atenciones - sexo grupo etario
    public List<Map<String, Object>> query3DMAMTASGE(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ge.grupo_etario,
                    ce.sexo,
                    COUNT(*) AS atenciones
                FROM %s AS ce
                LEFT JOIN dwsge.dim_grupo_etario as ge ON ce.anio_edad = ge.edad
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
                GROUP BY ge.grupo_etario, ce.sexo
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total_atenciones - CSV
    public List<Map<String, Object>> query4DMAMTACSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
                    a.actespnom AS subactividad,
                    ce.cod_tipo_consulta,
                    ce.cod_diag,
                    d.diagdes AS descripcion_diagnostico,
                    c.servhosdes AS descripcion_servicio,
                    e.especialidad AS descripcion_especialidad,
                    t.tipcondes AS descripcion_tipo_consulta,
                    dni_medico,
                    doc_paciente,
                    anio_edad,
                    cod_tipdoc_paciente,
                    sexo,
                    fecha_atencion,
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
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
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total_medicos - query 0
    public List<Map<String, Object>> query0DMAMTM(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                 SELECT c.servhosdes AS descripcion_servicio,
                     ce.dni_medico,
                     ce.fecha_atencion
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
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

    /// total_medicos - query 1
    public List<Map<String, Object>> query1DMAMTM(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                 SELECT ce.dni_medico,
                     ce.fecha_atencion
                FROM %s AS ce
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

    /// total_medicos - query 2 csv
    public List<Map<String, Object>> query2DMAMTMCSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
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
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
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
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total_horas_efectivas
    public List<Map<String, Object>> query0DMAMHE(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

        String sql = """
                SELECT
                    ce.dni_medico,
                    c.servhosdes as servicio,
                    ag.agrupador,
                    e.especialidad,
                    a.actespnom as subactividad,
                    ce.fecha_prog,
                    ce.horas_efec_def
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
                LEFT JOIN dwsge.dim_agrupador AS ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                AND ce.cod_actividad = '91'
                AND ce.cod_variable = '001'
                AND ce.ate > 0;
                """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total_horas_programadas
    public List<Map<String, Object>> query0DMAMHP(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_programacion_" + anio + "_" + periodo_str(periodo);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    ce.cod_tipdoc_medico,
                    ce.dni_medico,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
                    a.actespnom AS detalle_subactividad,
                    c.servhosdes AS descripcion_servicio,
                    e.especialidad AS descripcion_especialidad,
                	ce.total_horas,
                    ce.fecha_prog
                FROM %s ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador AS ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                    AND (
                        ce.cod_motivo_suspension IS NULL
                        OR ce.cod_motivo_suspension NOT IN ('04','09','10','99','13','16','11')
                    )
                  AND ce.cod_actividad = '91'
                  AND ce.cod_variable = '001'
                """
                .formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total_citados
    public List<Map<String, Object>> query0DMAMTC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_citados_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
                    a.actespnom AS detalle_subactividad,
                    c.servhosdes AS descripcion_servicio,
                    e.especialidad AS descripcion_especialidad,
                    cb.estcitnom AS estado_cita,
                    ct.tipopacinom AS tipo_paciente,
                    ce.cod_paciente,
                    ce.acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.sgss_cbeci10 AS cb ON cb.estcitcod = ce.cod_estado
                LEFT JOIN dwsge.dim_agrupador AS ag ON ce.cod_agrupador = ag.cod_agrupador
                LEFT JOIN dwsge.sgss_cbtpc10 AS ct ON ct.tipopacicod = ce.cod_paciente
                WHERE ce.cod_centro = ?
                  AND ce.cod_actividad = '91'
                  AND ce.cod_variable = '001'
                  AND ce.cod_estado <>'0'
                  AND (
                                CASE
                                    WHEN ce.cod_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total_deserción - query 0
    public List<Map<String, Object>> query0DMAMTD(int anio, int periodo, String codcas, int codasegu) {
        String tabla1 = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);
        String tabla2 = "dwsge.dwe_consulta_externa_citados_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    c.servhosdes as servicio,
                    a.actespnom as subactividad,
                    ag.agrupador AS agrupador,
                    e.especialidad,
                    am.actdes,
                    ca.cenasides,
                    ce.acto_med
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
                AND ce.clasificacion IN (1,3,5,0)
                AND ce.cod_variable = '001'
                AND (
                                CASE
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s

                UNION ALL

                SELECT
                    c.servhosdes as servicio,
                    a.actespnom as subactividad,
                    ag.agrupador AS agrupador,
                    e.especialidad,
                    am.actdes,
                    ca.cenasides,
                    p.acto_med
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
                                    WHEN p.cod_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla1, code_asegurado, tabla2, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, codcas);
    }

    /// total_deserción - query 1
    public List<Map<String, Object>> query1DMAMTD(int anio, int periodo, String codcas, int codasegu) {
        String tabla1 = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);
        String tabla2 = "dwsge.dwe_consulta_externa_citados_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    c.servhosdes as servicio,
                    a.actespnom as subactividad,
                    ag.agrupador AS agrupador,
                    e.especialidad,
                    am.actdes,
                    ca.cenasides,
                    ce.acto_med
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
                    c.servhosdes as servicio,
                    a.actespnom as subactividad,
                    ag.agrupador AS agrupador,
                    e.especialidad,
                    am.actdes,
                    ca.cenasides,
                    p.acto_med
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
                                    WHEN p.cod_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s;
                """.formatted(tabla1, code_asegurado, tabla2, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, codcas);
    }

    /// total_deserción - query 2 csv
    public List<Map<String, Object>> query2DMAMTDCSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla1 = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);
        String tabla2 = "dwsge.dwe_consulta_externa_citados_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    c.servhosdes as servicio,
                    a.actespnom as subactividad,
                    ag.agrupador AS agrupador,
                    e.especialidad,
                    am.actdes,
                    ca.cenasides,
                    ce.acto_med
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
                    c.servhosdes as servicio,
                    a.actespnom as subactividad,
                    ag.agrupador AS agrupador,
                    e.especialidad,
                    am.actdes,
                    ca.cenasides,
                    p.acto_med
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
                                    WHEN p.cod_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s;
                """.formatted(tabla1, code_asegurado, tabla2, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, codcas);
    }

    /// Indicadores de medicina complementaria
    /// total - atenciones- servicio - subactividad
    public List<Map<String, Object>> query0DMMCTASS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    c.servhosdes AS descripcion_servicio,
                    a.actespnom AS descripcion_subactividad
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                WHERE ce.cod_centro = ?
                  AND cod_servicio= 'A91'
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

    /// total - atenciones- agrupador - especialidad
    public List<Map<String, Object>> query1DMMCTAAE(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
                    a.actespnom AS subactividad,
                    ce.cod_tipo_consulta,
                    ce.cod_diag,
                    d.diagdes AS descripcion_diagnostico,
                    c.servhosdes AS descripcion_servicio,
                    e.especialidad AS descripcion_especialidad,
                    t.tipcondes AS descripcion_tipo_consulta,
                    dni_medico,
                    doc_paciente,
                    cod_tipdoc_paciente,
                    sexo,
                    fecha_atencion,
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                  AND cod_servicio= 'A91'
                  AND ce.clasificacion in (2,4,6)
                  AND (
                            CASE
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - atenciones - no considerados
    public List<Map<String, Object>> query2DMMCTANC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    c.servhosdes AS Servicio2,
                    am.actdes AS Actividad2,
                    a.actespnom AS Subactividad2,
                    t.tipcondes AS Tipo_consulta2,
                    ce.cod_diag as CodDiag2,
                    d.diagdes AS Diagnostico2,
                    dni_medico as dni_medico2,
                    acto_med as acto_med2,
                    sexo as sexo2,
                    fecha_atencion as fecha_atencion2,
                    cl.desc_cl AS desc_cl2
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                LEFT JOIN dwsge.dwe_cl10 as cl ON ce.clasificacion = cl.id_cl
                WHERE ce.cod_centro = ?
                  AND cod_servicio= 'A91'
                  AND ce.clasificacion in (1,3,5,0)
                  AND (
                            CASE
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - atenciones- sexo y grupo etario vs atenciones
    public List<Map<String, Object>> query3DMMCTASGEA(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ge.grupo_etario,
                    ce.sexo,
                    COUNT(*) AS atenciones
                FROM %s AS ce
                LEFT JOIN dwsge.dim_grupo_etario as ge ON ce.anio_edad = ge.edad
                WHERE ce.cod_centro = ?
                  AND cod_servicio= 'A91'
                  AND ce.clasificacion in (2,4,6)
                  AND (
                            CASE
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                GROUP BY ge.grupo_etario, ce.sexo
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - atenciones- CSV
    public List<Map<String, Object>> query4DMMCTACSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
                    a.actespnom AS subactividad,
                    ce.cod_tipo_consulta,
                    ce.cod_diag,
                    d.diagdes AS descripcion_diagnostico,
                    c.servhosdes AS descripcion_servicio,
                    e.especialidad AS descripcion_especialidad,
                    t.tipcondes AS descripcion_tipo_consulta,
                    dni_medico,
                    doc_paciente,
                    anio_edad,
                    cod_tipdoc_paciente,
                    sexo,
                    fecha_atencion,
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                  AND cod_servicio= 'A91'
                  AND ce.clasificacion in (2,4,6)
                      AND (
                            CASE
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total - medicos query0
    public List<Map<String, Object>> query0DMMCTM(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                 SELECT c.servhosdes AS descripcion_servicio,
                     ce.dni_medico,
                     ce.fecha_atencion
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                WHERE ce.cod_centro = ?
                  AND ce.clasificacion in (2,4,6)
                  AND ce.cod_servicio= 'A91'
                  AND ce.cod_actividad ='91'
                  AND (
                                    CASE
                                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                        ELSE '1'
                                    END
                                    ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total - medicos query1
    public List<Map<String, Object>> query1DMMCTM(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                 SELECT ce.dni_medico,
                     ce.fecha_atencion
                FROM %s AS ce
                WHERE ce.cod_centro = ?
                  AND ce.clasificacion in (2,4,6)
                  AND ce.cod_servicio= 'A91'
                  AND ce.cod_actividad ='91'
                  AND (
                                    CASE
                                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                        ELSE '1'
                                    END
                                    ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - medicos query2 CSV
    public List<Map<String, Object>> query2DMMCTMCSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
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
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                  AND cod_servicio= 'A91'
                  AND cod_actividad ='91'
                  AND ce.clasificacion in (2,4,6)
                  AND (
                                    CASE
                                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                        ELSE '1'
                                    END
                                    ) IN %s
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - horas- programadas
    public List<Map<String, Object>> queryDMMCTHP(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_programacion_" + anio + "_" + periodo_str(periodo);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    ce.cod_tipdoc_medico,
                    ce.dni_medico,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
                    a.actespnom AS detalle_subactividad,
                    c.servhosdes AS descripcion_servicio,
                    e.especialidad AS descripcion_especialidad,
                    ce.total_horas,
                    ce.fecha_prog
                FROM %s ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador AS ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                    AND (
                        ce.cod_motivo_suspension IS NULL
                        OR ce.cod_motivo_suspension NOT IN ('04','09','10','99','13','16','11')
                    )
                AND ce.cod_servicio= 'A91'
                AND ce.cod_actividad ='91'
                """
                .formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - horas- efectivas
    public List<Map<String, Object>> queryDMMCTHE(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

        String sql = """
                SELECT
                    ce.dni_medico,
                    c.servhosdes as servicio,
                    ag.agrupador,
                    e.especialidad,
                    a.actespnom as subactividad,
                    ce.fecha_prog,
                    ce.horas_efec_def
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
                LEFT JOIN dwsge.dim_agrupador AS ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                    AND cod_servicio= 'A91'
                    AND cod_actividad ='91'
                    AND ce.ate>0
                """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Indicadores de medicina ocupacional
    /// total - atenciones- servicio - subactividad
    public List<Map<String, Object>> query0DMMOTASS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    c.servhosdes AS descripcion_servicio,
                    a.actespnom AS descripcion_subactividad
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                WHERE ce.cod_centro = ?
                  AND (ce.cod_actividad = 'B8' OR ce.cod_actividad = '91')
                  AND ce.cod_subactividad = '070'
                  AND (ce.cod_servicio = 'AB1' OR ce.cod_servicio = 'AM6')
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

    /// total - atenciones- agrupador - especialidad
    public List<Map<String, Object>> query1DMMOTAAE(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
                    a.actespnom AS subactividad,
                    ce.cod_tipo_consulta,
                    ce.cod_diag,
                    d.diagdes AS descripcion_diagnostico,
                    c.servhosdes AS descripcion_servicio,
                    e.especialidad AS descripcion_especialidad,
                    t.tipcondes AS descripcion_tipo_consulta,
                    dni_medico,
                    doc_paciente,
                    cod_tipdoc_paciente,
                    sexo,
                    fecha_atencion,
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                  AND (ce.cod_actividad = 'B8' OR ce.cod_actividad = '91')
                  AND ce.cod_subactividad = '070'
                  AND (ce.cod_servicio = 'AB1' OR ce.cod_servicio = 'AM6')
                  AND ce.clasificacion in (2,4,6)
                  AND (
                            CASE
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - atenciones - no considerados
    public List<Map<String, Object>> query2DMMOTANC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    c.servhosdes AS Servicio2,
                    am.actdes AS Actividad2,
                    a.actespnom AS Subactividad2,
                    t.tipcondes AS Tipo_consulta2,
                    ce.cod_diag as CodDiag2,
                    d.diagdes AS Diagnostico2,
                    dni_medico as dni_medico2,
                    acto_med as acto_med2,
                    sexo as sexo2,
                    fecha_atencion as fecha_atencion2,
                    cl.desc_cl AS desc_cl2
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                LEFT JOIN dwsge.dwe_cl10 as cl ON ce.clasificacion = cl.id_cl
                WHERE ce.cod_centro = ?
                  AND (ce.cod_actividad = 'B8' OR ce.cod_actividad = '91')
                  AND ce.cod_subactividad = '070'
                  AND (ce.cod_servicio = 'AB1' OR ce.cod_servicio = 'AM6')
                  AND ce.clasificacion in (1,3,5,0)
                  AND (
                            CASE
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - atenciones- sexo y grupo etario vs atenciones
    public List<Map<String, Object>> query3DMMOTASGEA(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ge.grupo_etario,
                    ce.sexo,
                    COUNT(*) AS atenciones
                FROM %s AS ce
                LEFT JOIN dwsge.dim_grupo_etario as ge ON ce.anio_edad = ge.edad
                WHERE ce.cod_centro = ?
                  AND (ce.cod_actividad = 'B8' OR ce.cod_actividad = '91')
                  AND ce.cod_subactividad = '070'
                  AND (ce.cod_servicio = 'AB1' OR ce.cod_servicio = 'AM6')
                  AND ce.clasificacion in (2,4,6)
                  AND (
                            CASE
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                GROUP BY ge.grupo_etario, ce.sexo
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - atenciones- CSV
    public List<Map<String, Object>> query4DMMOTACSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
                    a.actespnom AS subactividad,
                    ce.cod_tipo_consulta,
                    ce.cod_diag,
                    d.diagdes AS descripcion_diagnostico,
                    c.servhosdes AS descripcion_servicio,
                    e.especialidad AS descripcion_especialidad,
                    t.tipcondes AS descripcion_tipo_consulta,
                    dni_medico,
                    doc_paciente,
                    anio_edad,
                    cod_tipdoc_paciente,
                    sexo,
                    fecha_atencion,
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                  AND (ce.cod_actividad = 'B8' OR ce.cod_actividad = '91')
                  AND ce.cod_subactividad = '070'
                  AND (ce.cod_servicio = 'AB1' OR ce.cod_servicio = 'AM6')
                  AND ce.clasificacion in (2,4,6)
                      AND (
                            CASE
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total - medicos query0
    public List<Map<String, Object>> query0DMOCTM(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                 SELECT c.servhosdes AS descripcion_servicio,
                     ce.dni_medico,
                     ce.fecha_atencion
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                WHERE ce.cod_centro = ?
                  AND ce.clasificacion in (2,4,6)
                  AND (ce.cod_actividad = 'B8' OR ce.cod_actividad = '91')
                  AND ce.cod_subactividad = '070'
                  AND (ce.cod_servicio = 'AB1' OR ce.cod_servicio = 'AM6')
                  AND (
                                    CASE
                                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                        ELSE '1'
                                    END
                                    ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total - medicos query1
    public List<Map<String, Object>> query1DMOCTM(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                 SELECT ce.dni_medico,
                     ce.fecha_atencion
                FROM %s AS ce
                WHERE ce.cod_centro = ?
                  AND ce.clasificacion in (2,4,6)
                  AND (ce.cod_actividad = 'B8' OR ce.cod_actividad = '91')
                  AND ce.cod_subactividad = '070'
                  AND (ce.cod_servicio = 'AB1' OR ce.cod_servicio = 'AM6')
                  AND (
                                    CASE
                                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                        ELSE '1'
                                    END
                                    ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - medicos query2 CSV
    public List<Map<String, Object>> query2DMMOTMCSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
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
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                  AND (ce.cod_actividad = 'B8' OR ce.cod_actividad = '91')
                  AND ce.cod_subactividad = '070'
                  AND (ce.cod_servicio = 'AB1' OR ce.cod_servicio = 'AM6')
                  AND ce.clasificacion in (2,4,6)
                  AND (
                                    CASE
                                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                        ELSE '1'
                                    END
                                    ) IN %s
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - horas- programadas
    public List<Map<String, Object>> queryDMMOTHP(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_programacion_" + anio + "_" + periodo_str(periodo);

        String sql = """
                  SELECT
                   ce.cod_servicio,
                      ce.cod_especialidad,
                      ca.cenasides,
                      ce.cod_tipdoc_medico,
                      ce.dni_medico,
                      am.actdes AS actividad,
                      ag.agrupador AS agrupador,
                      a.actespnom AS detalle_subactividad,
                      c.servhosdes AS descripcion_servicio,
                      e.especialidad AS descripcion_especialidad,
                ce.total_horas,
                      ce.fecha_prog
                  FROM %s ce
                  LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                  LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                  LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                  LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                  LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                  LEFT JOIN dwsge.dim_agrupador AS ag ON ce.cod_agrupador = ag.cod_agrupador
                  WHERE ce.cod_centro = ?
                      AND (
                          ce.cod_motivo_suspension IS NULL
                          OR ce.cod_motivo_suspension NOT IN ('04','09','10','99','13','16','11')
                      )
                      AND (ce.cod_actividad = 'B8' OR ce.cod_actividad = '91')
                      AND ce.cod_subactividad = '070'
                      AND (ce.cod_servicio = 'AB1' OR ce.cod_servicio = 'AM6')
                """
                .formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - horas- efectivas
    public List<Map<String, Object>> queryDMMOTHE(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

        String sql = """
                SELECT
                    ce.dni_medico,
                    c.servhosdes as servicio,
                    ag.agrupador,
                    e.especialidad,
                    a.actespnom as subactividad,
                    ce.fecha_prog,
                    ce.horas_efec_def
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
                LEFT JOIN dwsge.dim_agrupador AS ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                  AND (ce.cod_actividad = 'B8' OR ce.cod_actividad = '91')
                  AND ce.cod_subactividad = '070'
                  AND (ce.cod_servicio = 'AB1' OR ce.cod_servicio = 'AM6')
                  AND ce.ate>0
                """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Indicadores de medico de personal
    /// total - atenciones- servicio - subactividad
    public List<Map<String, Object>> query0DMMPTASS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    c.servhosdes AS descripcion_servicio,
                    a.actespnom AS descripcion_subactividad
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
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

    /// total - atenciones- agrupador - especialidad
    public List<Map<String, Object>> query1DMMPTAAE(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
                    a.actespnom AS subactividad,
                    ce.cod_tipo_consulta,
                    ce.cod_diag,
                    d.diagdes AS descripcion_diagnostico,
                    c.servhosdes AS descripcion_servicio,
                    e.especialidad AS descripcion_especialidad,
                    t.tipcondes AS descripcion_tipo_consulta,
                    dni_medico,
                    doc_paciente,
                    cod_tipdoc_paciente,
                    sexo,
                    fecha_atencion,
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
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
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - atenciones - no considerados
    public List<Map<String, Object>> query2DMMPTANC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    c.servhosdes AS Servicio2,
                    am.actdes AS Actividad2,
                    a.actespnom AS Subactividad2,
                    t.tipcondes AS Tipo_consulta2,
                    ce.cod_diag as CodDiag2,
                    d.diagdes AS Diagnostico2,
                    dni_medico as dni_medico2,
                    acto_med as acto_med2,
                    sexo as sexo2,
                    fecha_atencion as fecha_atencion2,
                    cl.desc_cl AS desc_cl2
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                LEFT JOIN dwsge.dwe_cl10 as cl ON ce.clasificacion = cl.id_cl
                WHERE ce.cod_centro = ?
                                AND ce.cod_actividad = '91'
                                AND ce.cod_subactividad = '682'
                                AND ce.cod_servicio = 'L16'
                  AND ce.clasificacion in (1,3,5,0)
                  AND (
                            CASE
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - atenciones- sexo y grupo etario vs atenciones
    public List<Map<String, Object>> query3DMMPTASGEA(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ge.grupo_etario,
                    ce.sexo,
                    COUNT(*) AS atenciones
                FROM %s AS ce
                LEFT JOIN dwsge.dim_grupo_etario as ge ON ce.anio_edad = ge.edad
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
                GROUP BY ge.grupo_etario, ce.sexo
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - atenciones- CSV
    public List<Map<String, Object>> query4DMMPTACSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
                    a.actespnom AS subactividad,
                    ce.cod_tipo_consulta,
                    ce.cod_diag,
                    d.diagdes AS descripcion_diagnostico,
                    c.servhosdes AS descripcion_servicio,
                    e.especialidad AS descripcion_especialidad,
                    t.tipcondes AS descripcion_tipo_consulta,
                    dni_medico,
                    doc_paciente,
                    anio_edad,
                    cod_tipdoc_paciente,
                    sexo,
                    fecha_atencion,
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
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
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total - medicos query0
    public List<Map<String, Object>> query0DMMPTM(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                 SELECT c.servhosdes AS descripcion_servicio,
                     ce.dni_medico,
                     ce.fecha_atencion
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                WHERE ce.cod_centro = ?
                  AND ce.clasificacion in (2,4,6)
                                AND ce.cod_actividad = '91'
                                AND ce.cod_subactividad = '682'
                                AND ce.cod_servicio = 'L16'
                  AND (
                                    CASE
                                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                        ELSE '1'
                                    END
                                    ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total - medicos query1
    public List<Map<String, Object>> query1DMMPTM(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                 SELECT ce.dni_medico,
                     ce.fecha_atencion
                FROM %s AS ce
                WHERE ce.cod_centro = ?
                  AND ce.clasificacion in (2,4,6)
                                AND ce.cod_actividad = '91'
                                AND ce.cod_subactividad = '682'
                                AND ce.cod_servicio = 'L16'
                  AND (
                                    CASE
                                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                        ELSE '1'
                                    END
                                    ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - medicos query2 CSV
    public List<Map<String, Object>> query2DMMPTMCSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
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
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
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
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - horas- programadas
    public List<Map<String, Object>> queryDMMPTHP(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_programacion_" + anio + "_" + periodo_str(periodo);

        String sql = """
                  SELECT
                   ce.cod_servicio,
                      ce.cod_especialidad,
                      ca.cenasides,
                      ce.cod_tipdoc_medico,
                      ce.dni_medico,
                      am.actdes AS actividad,
                      ag.agrupador AS agrupador,
                      a.actespnom AS detalle_subactividad,
                      c.servhosdes AS descripcion_servicio,
                      e.especialidad AS descripcion_especialidad,
                ce.total_horas,
                      ce.fecha_prog
                  FROM %s ce
                  LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                  LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                  LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                  LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                  LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                  LEFT JOIN dwsge.dim_agrupador AS ag ON ce.cod_agrupador = ag.cod_agrupador
                  WHERE ce.cod_centro = ?
                      AND (
                          ce.cod_motivo_suspension IS NULL
                          OR ce.cod_motivo_suspension NOT IN ('04','09','10','99','13','16','11')
                      )
                                      AND ce.cod_actividad = '91'
                                      AND ce.cod_subactividad = '682'
                                      AND ce.cod_servicio = 'L16'
                """
                .formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - horas- efectivas
    public List<Map<String, Object>> queryDMMPTHE(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

        String sql = """
                SELECT
                    ce.dni_medico,
                    c.servhosdes as servicio,
                    ag.agrupador,
                    e.especialidad,
                    a.actespnom as subactividad,
                    ce.fecha_prog,
                    ce.horas_efec_def
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
                LEFT JOIN dwsge.dim_agrupador AS ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                                AND ce.cod_actividad = '91'
                                AND ce.cod_subactividad = '682'
                                AND ce.cod_servicio = 'L16'
                                AND ce.ate>0
                """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Indicadores de consulta de atencion inmediata
    /// total - atenciones- servicio - subactividad
    public List<Map<String, Object>> query0DMCAITASS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    c.servhosdes AS descripcion_servicio,
                    a.actespnom AS descripcion_subactividad
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                WHERE ce.cod_centro = ?
                  AND a.actespcod = '002'
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

    /// total - atenciones- agrupador - especialidad
    public List<Map<String, Object>> query1DMCAITAAE(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
                    a.actespnom AS subactividad,
                    ce.cod_tipo_consulta,
                    ce.cod_diag,
                    d.diagdes AS descripcion_diagnostico,
                    c.servhosdes AS descripcion_servicio,
                    e.especialidad AS descripcion_especialidad,
                    t.tipcondes AS descripcion_tipo_consulta,
                    dni_medico,
                    doc_paciente,
                    cod_tipdoc_paciente,
                    sexo,
                    fecha_atencion,
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                  AND a.actespcod = '002'
                  AND ce.clasificacion in (2,4,6)
                  AND (
                            CASE
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - atenciones - no considerados
    public List<Map<String, Object>> query2DMCAITANC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    c.servhosdes AS Servicio2,
                    am.actdes AS Actividad2,
                    a.actespnom AS Subactividad2,
                    t.tipcondes AS Tipo_consulta2,
                    ce.cod_diag as CodDiag2,
                    d.diagdes AS Diagnostico2,
                    dni_medico as dni_medico2,
                    acto_med as acto_med2,
                    sexo as sexo2,
                    fecha_atencion as fecha_atencion2,
                    cl.desc_cl AS desc_cl2
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                LEFT JOIN dwsge.dwe_cl10 as cl ON ce.clasificacion = cl.id_cl
                WHERE ce.cod_centro = ?
                  AND a.actespcod = '002'
                  AND ce.clasificacion in (1,3,5,0)
                  AND (
                            CASE
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - atenciones- sexo y grupo etario vs atenciones
    public List<Map<String, Object>> query3DMCAITASGEA(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ge.grupo_etario,
                    ce.sexo,
                    COUNT(*) AS atenciones
                FROM %s AS ce
                LEFT JOIN dwsge.dim_grupo_etario as ge ON ce.anio_edad = ge.edad
                WHERE ce.cod_centro = ?
                  AND ce.cod_subactividad = '002'
                  AND ce.clasificacion in (2,4,6)
                  AND (
                            CASE
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                GROUP BY ge.grupo_etario, ce.sexo
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - atenciones- CSV
    public List<Map<String, Object>> query4DMCAITACSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
                    a.actespnom AS subactividad,
                    ce.cod_tipo_consulta,
                    ce.cod_diag,
                    d.diagdes AS descripcion_diagnostico,
                    c.servhosdes AS descripcion_servicio,
                    e.especialidad AS descripcion_especialidad,
                    t.tipcondes AS descripcion_tipo_consulta,
                    dni_medico,
                    doc_paciente,
                    anio_edad,
                    cod_tipdoc_paciente,
                    sexo,
                    fecha_atencion,
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                  AND a.actespcod = '002'
                  AND ce.clasificacion in (2,4,6)
                      AND (
                            CASE
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total - medicos query0
    public List<Map<String, Object>> query0DMCAITM(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                 SELECT c.servhosdes AS descripcion_servicio,
                     ce.dni_medico,
                     ce.fecha_atencion
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                WHERE ce.cod_centro = ?
                  AND ce.clasificacion in (2,4,6)
                  AND ce.cod_subactividad = '002'
                  AND (
                                    CASE
                                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                        ELSE '1'
                                    END
                                    ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total - medicos query1
    public List<Map<String, Object>> query1DMCAITM(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                 SELECT ce.dni_medico,
                     ce.fecha_atencion
                FROM %s AS ce
                WHERE ce.cod_centro = ?
                  AND ce.clasificacion in (2,4,6)
                  AND ce.cod_subactividad = '002'
                  AND (
                                    CASE
                                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                        ELSE '1'
                                    END
                                    ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - medicos query2 CSV
    public List<Map<String, Object>> query2DMCAITMCSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
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
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                  AND a.actespcod = '002'
                  AND ce.clasificacion in (2,4,6)
                  AND (
                                    CASE
                                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                        ELSE '1'
                                    END
                                    ) IN %s
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - horas- programadas
    public List<Map<String, Object>> queryDMCAITHP(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_programacion_" + anio + "_" + periodo_str(periodo);

        String sql = """
                  SELECT
                   ce.cod_servicio,
                      ce.cod_especialidad,
                      ca.cenasides,
                      ce.cod_tipdoc_medico,
                      ce.dni_medico,
                      am.actdes AS actividad,
                      ag.agrupador AS agrupador,
                      a.actespnom AS detalle_subactividad,
                      c.servhosdes AS descripcion_servicio,
                      e.especialidad AS descripcion_especialidad,
                ce.total_horas,
                      ce.fecha_prog
                  FROM %s ce
                  LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                  LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                  LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                  LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                  LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                  LEFT JOIN dwsge.dim_agrupador AS ag ON ce.cod_agrupador = ag.cod_agrupador
                  WHERE ce.cod_centro = ?
                      AND (
                          ce.cod_motivo_suspension IS NULL
                          OR ce.cod_motivo_suspension NOT IN ('04','09','10','99','13','16','11')
                      )
                        AND a.actespcod = '002'
                """
                .formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - horas- efectivas
    public List<Map<String, Object>> queryDMCAITHE(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

        String sql = """
                SELECT
                    ce.dni_medico,
                    c.servhosdes as servicio,
                    ag.agrupador,
                    e.especialidad,
                    a.actespnom as subactividad,
                    ce.fecha_prog,
                    ce.horas_efec_def
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
                LEFT JOIN dwsge.dim_agrupador AS ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                  AND a.actespcod = '002'
                  AND ce.ate>0
                """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Indicadores de cons apoyo desc
    /// total - atenciones- servicio - subactividad
    public List<Map<String, Object>> query0DMCADTASS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    c.servhosdes AS descripcion_servicio,
                    a.actespnom AS descripcion_subactividad
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
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

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total - atenciones- agrupador - especialidad
    public List<Map<String, Object>> query1DMCADTAAE(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
                    a.actespnom AS subactividad,
                    ce.cod_tipo_consulta,
                    ce.cod_diag,
                    d.diagdes AS descripcion_diagnostico,
                    c.servhosdes AS descripcion_servicio,
                    e.especialidad AS descripcion_especialidad,
                    t.tipcondes AS descripcion_tipo_consulta,
                    dni_medico,
                    doc_paciente,
                    cod_tipdoc_paciente,
                    sexo,
                    fecha_atencion,
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
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
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - atenciones - no considerados
    public List<Map<String, Object>> query2DMCADTANC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    c.servhosdes AS Servicio2,
                    am.actdes AS Actividad2,
                    a.actespnom AS Subactividad2,
                    t.tipcondes AS Tipo_consulta2,
                    ce.cod_diag as CodDiag2,
                    d.diagdes AS Diagnostico2,
                    dni_medico as dni_medico2,
                    acto_med as acto_med2,
                    sexo as sexo2,
                    fecha_atencion as fecha_atencion2,
                    cl.desc_cl AS desc_cl2
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                LEFT JOIN dwsge.dwe_cl10 as cl ON ce.clasificacion = cl.id_cl
                WHERE ce.cod_centro = ?
                                AND ce.cod_actividad = '91'
                                AND ce.cod_subactividad = '003'
                  AND ce.clasificacion in (1,3,5,0)
                  AND (
                            CASE
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - atenciones- sexo y grupo etario vs atenciones
    public List<Map<String, Object>> query3DMCADTASGEA(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ge.grupo_etario,
                    ce.sexo,
                    COUNT(*) AS atenciones
                FROM %s AS ce
                LEFT JOIN dwsge.dim_grupo_etario as ge ON ce.anio_edad = ge.edad
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
                GROUP BY ge.grupo_etario, ce.sexo
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - atenciones- CSV
    public List<Map<String, Object>> query4DMCADTACSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
                    a.actespnom AS subactividad,
                    ce.cod_tipo_consulta,
                    ce.cod_diag,
                    d.diagdes AS descripcion_diagnostico,
                    c.servhosdes AS descripcion_servicio,
                    e.especialidad AS descripcion_especialidad,
                    t.tipcondes AS descripcion_tipo_consulta,
                    dni_medico,
                    doc_paciente,
                    anio_edad,
                    cod_tipdoc_paciente,
                    sexo,
                    fecha_atencion,
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
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
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total - medicos query0
    public List<Map<String, Object>> query0DMCADTM(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                 SELECT c.servhosdes AS descripcion_servicio,
                     ce.dni_medico,
                     ce.fecha_atencion
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                WHERE ce.cod_centro = ?
                  AND ce.clasificacion in (2,4,6)
                                AND ce.cod_actividad = '91'
                                AND ce.cod_subactividad = '003'
                  AND (
                                    CASE
                                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                        ELSE '1'
                                    END
                                    ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// total - medicos query1
    public List<Map<String, Object>> query1DMCADTM(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                 SELECT ce.dni_medico,
                     ce.fecha_atencion
                FROM %s AS ce
                WHERE ce.cod_centro = ?
                  AND ce.clasificacion in (2,4,6)
                                AND ce.cod_actividad = '91'
                                AND ce.cod_subactividad = '003'
                  AND (
                                    CASE
                                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                        ELSE '1'
                                    END
                                    ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - medicos query2 CSV
    public List<Map<String, Object>> query2DMCADTMCSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    am.actdes AS actividad,
                    ag.agrupador AS agrupador,
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
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
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
                """
                .formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - horas- programadas
    public List<Map<String, Object>> queryDMCADTHP(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_programacion_" + anio + "_" + periodo_str(periodo);

        String sql = """
                  SELECT
                   ce.cod_servicio,
                      ce.cod_especialidad,
                      ca.cenasides,
                      ce.cod_tipdoc_medico,
                      ce.dni_medico,
                      am.actdes AS actividad,
                      ag.agrupador AS agrupador,
                      a.actespnom AS detalle_subactividad,
                      c.servhosdes AS descripcion_servicio,
                      e.especialidad AS descripcion_especialidad,
                ce.total_horas,
                      ce.fecha_prog
                  FROM %s ce
                  LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
                  LEFT JOIN dwsge.dim_especialidad AS e ON ce.cod_especialidad = e.cod_especialidad
                  LEFT JOIN dwsge.sgss_cmace10 AS a ON ce.cod_actividad = a.actcod AND ce.cod_subactividad = a.actespcod
                  LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
                  LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
                  LEFT JOIN dwsge.dim_agrupador AS ag ON ce.cod_agrupador = ag.cod_agrupador
                  WHERE ce.cod_centro = ?
                      AND (
                          ce.cod_motivo_suspension IS NULL
                          OR ce.cod_motivo_suspension NOT IN ('04','09','10','99','13','16','11')
                      )
                                      AND ce.cod_actividad = '91'
                                      AND ce.cod_subactividad = '003'
               """
                .formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Total - horas- efectivas
    public List<Map<String, Object>> queryDMCADTHE(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_" + anio + "_" + periodo_str(periodo);

        String sql = """
                SELECT
                    ce.dni_medico,
                    c.servhosdes as servicio,
                    ag.agrupador,
                    e.especialidad,
                    a.actespnom as subactividad,
                    ce.fecha_prog,
                    ce.horas_efec_def
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
                LEFT JOIN dwsge.dim_agrupador AS ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                                AND ce.cod_actividad = '91'
                                AND ce.cod_subactividad = '003'
                                AND ce.ate>0
                """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

}
