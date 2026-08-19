package essalud.gcpp.essaludgcppbackend.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DashboardOdonResp {
    private final JdbcTemplate secondJdbcTemplate;

    public DashboardOdonResp(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
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

    ///Dashboard odontologicas
    ///  sesiones odontologicas - Atenciones
    public List<Map<String, Object>> queryDOSOA(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dw_consulta_externa_odonto_homologacion_" + anio + "_" + periodo_str(periodo);

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
                AND ce.cod_servicio = 'E11'
                AND ce.cod_actividad = '91'
                AND ce.cod_subactividad in ('003','004','074','262')
                AND (
                        CASE
                            WHEN ce.cod_tipo_paciente = '4' THEN '2'
                            ELSE '1'
                        END
                        ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    ///  sesiones odontologicas - horas efectivas
    public List<Map<String, Object>> queryDOSOHE(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_nm_" + anio + "_" + periodo_str(periodo);

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
                AND ce.cod_servicio = 'E11'
                AND ce.cod_actividad = '91'
                AND ce.cod_subactividad in ('003','004','074','262')
                AND ce.ate>0
                """.formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    ///  sesiones odontologicas - horas programadas
    public List<Map<String, Object>> queryDOSOHP(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_nm_" + anio + "_" + periodo_str(periodo);

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
                WHERE  (
                        p.cod_mot_suspension IS NULL
                        OR p.cod_mot_suspension NOT IN ('04','09','10','99','13','16','11')
                    )
                AND p.cod_centro = ?
                AND p.cod_servicio = 'E11'
                AND p.cod_actividad = '91'
                AND p.cod_subactividad in ('003','004','074','262')
                """.formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    ///  sesiones odontologicas - medicos
    public List<Map<String, Object>> queryDOSOM(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dw_consulta_externa_odonto_homologacion_" + anio + "_" + periodo_str(periodo);

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
                                WHERE a.cod_centro=?
                                AND a.cod_servicio = 'E11'
                                AND a.cod_actividad = '91'
                                AND a.cod_subactividad in ('003','004','074','262')
                                GROUP BY a.cod_centro, a.dni_medico, ag.agrupador, a.periodo,CASE WHEN a.cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                                ORDER BY a.dni_medico, a.periodo, (count(*))) b) c
                WHERE c.medico = '1'::bigint
                """.formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    ///  sesiones odontologicas - total consultante atencion
    public List<Map<String, Object>> queryDOSOTCA(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dw_consulta_externa_odonto_homologacion_" + anio;
        String periodo_sql= anio+periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
            WITH fecha_min_paciente AS (
                SELECT cod_oricentro,cod_centro,doc_paciente,
                    CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                       to_char(MIN(to_date(fecha_atencion,'DD/MM/YYYY')),'YYYYMM') periodo
                FROM %s
                WHERE  cod_servicio = 'E11'
                AND cod_actividad = '91'
                AND cod_subactividad in ('003','004','074','262')
                AND cod_centro= ?
                AND (
                        CASE
                            WHEN cod_tipo_paciente = '4' THEN '2'
                            ELSE '1'
                        END
                        ) IN %s
                GROUP BY cod_oricentro,cod_centro,doc_paciente,CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
            )
            SELECT COUNT(DISTINCT doc_paciente) AS cantidad
            FROM fecha_min_paciente WHERE periodo= ?
            """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas,periodo_sql);
    }
    ///  sesiones odontologicas - total consultante servicio
    public List<Map<String, Object>> queryDOSOTCS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dw_consulta_externa_odonto_homologacion_" + anio;
        String periodo_sql= anio+periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
            WITH fecha_min_paciente AS (
                SELECT p.doc_paciente,ag.agrupador,
                    CASE WHEN p.cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                       to_char(MIN(to_date(p.fecha_atencion,'DD/MM/YYYY')),'YYYYMM') periodo
                FROM %s p
                LEFT JOIN dwsge.dim_agrupador ag ON p.cod_agrupador = ag.cod_agrupador
                WHERE p.cod_servicio = 'E11'
                AND p.cod_actividad = '91'
                AND p.cod_subactividad in ('003','004','074','262')
                AND p.cod_centro= ?
                AND (
                        CASE
                            WHEN p.cod_tipo_paciente = '4' THEN '2'
                            ELSE '1'
                        END
                        ) IN %s
                GROUP BY p.doc_paciente,ag.agrupador,CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END
            )
            SELECT agrupador,COUNT(DISTINCT doc_paciente) AS cantidad
            FROM fecha_min_paciente WHERE periodo= ? GROUP BY agrupador
            """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }

}
