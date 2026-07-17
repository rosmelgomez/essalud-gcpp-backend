package essalud.gcpp.essaludgcppbackend.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DashboardNMRep {

    private final JdbcTemplate secondJdbcTemplate;

    public DashboardNMRep(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
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


    /// Query Dashboard no medicas
    /// Obstetricia - Atenciones Medicas
    public List<Map<String, Object>> queryDNMOA(int anio, int periodo, String codcas, int codasegu) {
        String tabla1 = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);
        String tabla2 ="dssge.dw_proc_"+ anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,a.actespnom,ce.acto_med, ce.doc_paciente
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND ((cod_servicio ='F21'
                        AND cod_actividad = 'B1'
                        AND ce.cod_subactividad in ('694','417', '418','040','127'))
                        OR (cod_servicio ='F21'
                        AND cod_actividad = 'B1' AND cod_subactividad in ('007', '480', '643', '008') AND ce.cod_tipo_gravidez = '1'))
                        AND (
                                CASE
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                    UNION ALL

                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,a.actespnom, ce.acto_med::NUMERIC, ce.doc_paciente
                    FROM %s ce
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    WHERE ce.cod_centro = ?
                    AND ce.cod_actividad ='96'
                    AND ce.cod_servicio ='F21'
                    AND ce.cod_subactividad ='127'
                    AND (
                            CASE
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                """.formatted(tabla1, code_asegurado, tabla2, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, codcas);
    }
    /// Obstetricia - Prenatal
    public List<Map<String, Object>> queryDNMOP(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE ce.cod_centro = ?
                        AND ce.cod_servicio ='F21'
                        AND cod_actividad = 'B1'
                        AND cod_tipo_gravidez = '1'
                        AND ce.cod_subactividad in ('007', '480', '643', '008')
                        AND (
                                CASE
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Obstetricia - Familiar
    public List<Map<String, Object>> queryDNMOF(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE ce.cod_centro = ?
                        AND ce.cod_servicio ='F21'
                        AND cod_actividad = 'B1'
                        AND ce.cod_subactividad ='694'
                        AND (
                                CASE
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Obstetricia - Complementarias
    public List<Map<String, Object>> queryDNMOC(int anio, int periodo, String codcas, int codasegu) {
        String tabla1 = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);
        String tabla2 ="dssge.dw_proc_"+ anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,a.actespnom,ce.acto_med, ce.doc_paciente
                    FROM %s ce
                    LEFT JOIN dwsge.sgss_cmsho10 AS c
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE ce.cod_centro = ?
                        AND ((ce.cod_servicio ='F21' AND ce.cod_actividad = 'B1' AND ce.cod_subactividad ='417')
                        OR 
                        (ce.cod_servicio ='F21' AND ce.cod_actividad = 'B1' AND ce.cod_subactividad ='418')
                        OR
                        (ce.cod_servicio ='F21' AND ce.cod_actividad = '96' AND ce.cod_subactividad ='127'))
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                    UNION ALL

                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,a.actespnom, ce.acto_med::NUMERIC, ce.doc_paciente
                    FROM %s ce
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    WHERE ce.cod_centro = ?
                    AND ce.cod_actividad ='96'
                    AND ce.cod_servicio ='F21'
                    AND ce.cod_subactividad ='127'
                    AND (
                            CASE 
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                            ) IN %s
                """.formatted(tabla1, code_asegurado, tabla2, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas, codcas);
    }
    /// Obstetricia - Preconcepcional
    public List<Map<String, Object>> queryDNMOPC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE ce.cod_centro = ?
                        AND ce.cod_servicio ='F21'
                        AND ce.cod_actividad = 'B1'
                        AND ce.cod_subactividad ='040'
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Preventivo Promocional
    ///  Preventivo Promocional - Atenciones
    public List<Map<String, Object>> queryDNMPPA(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                    WHERE ce.cod_centro = ?
                    AND ce.cod_servicio = 'E21'
                    AND (
                            (ce.cod_actividad = 'A1' AND ce.cod_subactividad IN ('079','078','685'))
                            OR
                            (ce.cod_actividad = 'B1' AND ce.cod_subactividad IN ('724','416'))
                        )
                    AND (
                            CASE 
                                WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                        ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    ///  Preventivo Promocional - domiciliaria
    public List<Map<String, Object>> queryDNMPPD(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE ce.cod_centro = ?
                        AND ce.cod_servicio ='E21'
                        AND ce.cod_actividad = 'A1'
                        AND ce.cod_subactividad in ('079', '078')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    ///  Preventivo Promocional - grupal
    public List<Map<String, Object>> queryDNMPPG(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE ce.cod_centro = ?
                        AND ce.cod_servicio ='E21'
                        AND ce.cod_actividad = 'A1'
                        AND ce.cod_subactividad ='685'
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    ///  Preventivo Promocional - psicoprofilaxis
    public List<Map<String, Object>> queryDNMPPP(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE ce.cod_centro = ?
                        AND ce.cod_servicio ='E21'
                        AND ce.cod_actividad = 'B1'
                        AND ce.cod_subactividad in ('724')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    ///  Preventivo Promocional - consejeria
    public List<Map<String, Object>> queryDNMPPC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE ce.cod_centro = ?
                        AND ce.cod_servicio ='E21'
                        AND ce.cod_actividad = 'B1'
                        AND ce.cod_subactividad ='416'
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Nutricion
    ///  Nutricion - nutricion_total
    public List<Map<String, Object>> queryDNMNNT(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F31'
                        AND cod_actividad ='B1'
                        AND ce.cod_subactividad in ('050', '056', '093', '322','471','609','692','693','975')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Enfermeria
    ///  Enfermeria - enfermeria_total
    public List<Map<String, Object>> queryDNMEET(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F11'
                        AND cod_actividad ='B1'
                        AND ce.cod_subactividad in ('072','073','093','576','680','010','801','056')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    ///  Enfermeria - enfermeria_tuberculosis
    public List<Map<String, Object>> queryDNMEETU(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F11'
                        AND cod_actividad ='B1'
                        AND ce.cod_subactividad in ('072','073','093')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    ///  Enfermeria - enfermeria_vih
    public List<Map<String, Object>> queryDNMEEV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F11'
                        AND cod_actividad ='B1'
                        AND ce.cod_subactividad in ('576','680')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    ///  Enfermeria - enfermeria_cronicas_am
    public List<Map<String, Object>> queryDNMEECA(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F11'
                        AND cod_actividad ='B1'
                        AND ce.cod_subactividad in ('010')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    ///  Enfermeria - enfermeria_otros
    public List<Map<String, Object>> queryDNMEEO(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F11'
                        AND cod_actividad ='B1'
                        AND ce.cod_subactividad in ('801')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    ///  Enfermeria - enfermeria_prev_anemia
    public List<Map<String, Object>> queryDNMEEPA(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F11'
                        AND cod_actividad ='B1'
                        AND ce.cod_subactividad in ('056')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Psicologia
    /// psicologia total consultantes
    public List<Map<String, Object>> queryDNMPSTCS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio ;
        String periodo_sql = String.format("%d%02d", anio, periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
            WITH fecha_min_paciente AS (
                SELECT cod_oricentro,cod_centro,doc_paciente,
                    CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                       to_char(MIN(to_date(fecha_atencion,'YYYY/MM/DD')),'YYYYMM') periodo
                FROM %s p
                WHERE p.cod_servicio ='E21'
                    AND p.cod_actividad ='B1'
                    AND p.cod_subactividad ='005'
                    AND p.cod_centro= ?
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

        return secondJdbcTemplate.queryForList(sql, codcas, periodo_sql);
    }
    /// psicologia - total - consultas
    public List<Map<String, Object>> queryDNMPSTC(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.dni_medico,ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='E21'
                        AND cod_actividad ='B1'
                        AND ce.cod_subactividad ='005'
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Psicologia - horas - efectivas
    public List<Map<String, Object>> queryDNMPSHE(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_nm_" + anio + "_" + periodo_str(periodo);

        String sql = """
                SELECT 
                    ce.*,
                    c.servhosdes,
                    a.actespnom,
                    am.actdes,
                    ca.cenasides
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c 
                    ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.sgss_cmace10 AS a
                    ON ce.cod_actividad = a.actcod
                    AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am
                    ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca
                    ON ce.cod_centro = ca.cenasicod
                WHERE ce.cod_centro = ?
                    AND ce.cod_servicio ='E21'
                    AND ce.cod_actividad ='B1'
                    AND ce.cod_subactividad ='005'
                    AND ce.ate>0
                """.formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Psicologia - horas - programadas
    public List<Map<String, Object>> queryDNMPHP(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_horas_efectivas_nm_" + anio + "_" + periodo_str(periodo);

        String sql = """
                SELECT 
                    p.*,
                    c.servhosdes,
                    a.actespnom,
                    am.actdes,
                    ca.cenasides 
                FROM %s p
                LEFT JOIN dwsge.sgss_cmsho10 AS c 
                    ON p.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.sgss_cmace10 AS a
                    ON p.cod_actividad = a.actcod
                    AND p.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am
                    ON p.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca
                    ON p.cod_centro = ca.cenasicod
                WHERE (
                        p.cod_mot_suspension IS NULL 
                        OR p.cod_mot_suspension NOT IN ('04','09','10','99','13','16','11')
                    )
                AND p.cod_centro = ?
                AND p.cod_servicio ='E21'
                AND p.cod_actividad ='B1'
                AND p.cod_subactividad ='005'
                """.formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Psicologia - medicos
    public List<Map<String, Object>> queryDNMPSM(int anio, int periodo, String codcas) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String sql = """
                SELECT c.cod_centro,
                    c.dni_medico,
                    c.periodo,
                    c.cantidad_medicos,
                    c.medico
                FROM ( SELECT b.cod_centro,
                            b.dni_medico,
                            b.periodo,
                            b.cantidad_medicos,
                            row_number() OVER (PARTITION BY b.cod_centro, b.dni_medico, b.periodo ORDER BY b.cantidad_medicos DESC) AS medico
                        FROM ( SELECT a.cod_centro,
                                    a.dni_medico,
                                    a.periodo,
                                    CASE WHEN a.cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                                    count(*) AS cantidad_medicos
                                FROM (SELECT * FROM %s) a
                                WHERE a.cod_centro= ?
                                    AND a.cod_servicio ='E21'
                                    AND a.cod_actividad ='B1'
                                    AND a.cod_subactividad ='005'
                                    GROUP BY a.cod_centro, a.dni_medico, a.periodo,CASE WHEN a.cod_tipo_paciente = '4' THEN '2' ELSE '1' END
                                    ORDER BY a.dni_medico, a.periodo, (count(*))) b) c
                WHERE c.medico = '1'::bigint
                """.formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Psicologia - procedimiento terapéutico
    public List<Map<String, Object>> queryDNMPSPT(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND ((cod_servicio ='E21'AND cod_actividad ='B1' AND ce.cod_subactividad in ('752','760','763','006'))
                        or
                        (cod_servicio ='E21'AND cod_actividad ='A7' AND ce.cod_subactividad in ('005','006','752','760','763')))
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// Psicologia - procedimiento diagnóstico
    public List<Map<String, Object>> queryDNMPSPD(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='E21'
                        AND cod_actividad ='B1'
                        AND ce.cod_subactividad ='705'
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    ///Trabajo Social (TS)
    ///Trabajo - social - total
    public List<Map<String, Object>> queryDNMTST(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,ca.cenasides,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F51'
                        AND cod_actividad ='B1'
                        AND ce.cod_subactividad ='055'
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// CRED
    /// CRED -total
    public List<Map<String, Object>> queryDNMCREDT(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                        WITH base AS (
                            SELECT *,
                                (anio_edad::int * 12 + meses::int) AS edad_meses
                            FROM %s ce
                            LEFT JOIN dwsge.sgss_cmcas10 AS ca
                            ON ce.cod_oricentro = ca.oricenasicod
                            AND ce.cod_centro = ca.cenasicod
                        ),
                                
                        clasificado AS (
                            SELECT *,
                                CASE
                                    WHEN edad_meses = 0 THEN 1
                                    WHEN edad_meses BETWEEN 1 AND 11 THEN 2
                                    WHEN edad_meses BETWEEN 12 AND 23 THEN 3
                                    WHEN edad_meses BETWEEN 24 AND 35 THEN 4
                                    WHEN edad_meses BETWEEN 36 AND 47 THEN 5
                                    WHEN edad_meses BETWEEN 48 AND 59 THEN 6
                                    WHEN edad_meses BETWEEN 60 AND 143 THEN 7
                                    WHEN edad_meses BETWEEN 144 AND 215 THEN 8
                                    ELSE 99
                                END AS orden_edad,

                                CASE
                                    WHEN edad_meses = 0 THEN '0-28 días'
                                    WHEN edad_meses BETWEEN 1 AND 11 THEN '29 días < 1 año'
                                    WHEN edad_meses BETWEEN 12 AND 23 THEN '1 a <2'
                                    WHEN edad_meses BETWEEN 24 AND 35 THEN '2 a <3'
                                    WHEN edad_meses BETWEEN 36 AND 47 THEN '3 a <4'
                                    WHEN edad_meses BETWEEN 48 AND 59 THEN '4 a <5'
                                    WHEN edad_meses BETWEEN 60 AND 143 THEN '5 a <12'
                                    WHEN edad_meses BETWEEN 144 AND 215 THEN '12 a <18'
                                    ELSE 'Fuera de rango'
                                END AS actespnom
                            FROM base
                        )

                        SELECT
                            *
                        FROM clasificado
                        WHERE cod_centro = ?
                        AND cod_servicio = 'F11'
                        AND cod_actividad ='B1'
                        AND cod_subactividad in ('382','384')
                        AND (
                                CASE 
                                    WHEN cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }


    /// Indicadores de obstetricia
    /// total atenciones - servicio - subactividad
    public List<Map<String, Object>> queryDNMOBTASS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
        SELECT
            c.servhosdes AS descripcion_servicio,
            a.actespnom AS descripcion_subactividad
        
        FROM %s ce
        LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
            ON dg.diagcod=ce.diagcod
        LEFT JOIN dwsge.sgss_cmsho10 AS c 
            ON ce.cod_servicio = c.servhoscod
        LEFT JOIN dwsge.sgss_cmace10 AS a
            ON ce.cod_actividad = a.actcod
            AND ce.cod_subactividad = a.actespcod
        LEFT JOIN dwsge.sgss_cmact10 AS am
            ON ce.cod_actividad = am.actcod
        LEFT JOIN dwsge.sgss_cmcas10 AS ca
            ON ce.cod_oricentro = ca.oricenasicod
            AND ce.cod_centro = ca.cenasicod
            WHERE cod_centro = ?
            AND cod_servicio ='F21'
            AND ce.cod_subactividad in ('007', '480', '643','694','417', '418', '127', '008','040')
            AND (
                    CASE 
                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                        ELSE '1'
                    END
                    ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMOBTAD(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F21'
                        AND ce.cod_subactividad in ('007', '480', '643','694','417', '418', '127', '008','040')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMOBTACSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                     SELECT ce.cod_oricentro, ce.cod_centro,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F21'
                        AND ce.cod_subactividad in ('007', '480', '643','694','417', '418', '127', '008','040')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }


    /// Indicadores de preventiva promocional
    /// total atenciones - servicio - subactividad
    public List<Map<String, Object>> queryDNMPPTASS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
        SELECT 
            c.servhosdes AS descripcion_servicio,
            a.actespnom AS descripcion_subactividad
        FROM %s ce
        LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
            ON dg.diagcod=ce.diagcod
        LEFT JOIN dwsge.sgss_cmsho10 AS c 
            ON ce.cod_servicio = c.servhoscod
        LEFT JOIN dwsge.sgss_cmace10 AS a
            ON ce.cod_actividad = a.actcod
            AND ce.cod_subactividad = a.actespcod
        LEFT JOIN dwsge.sgss_cmact10 AS am
            ON ce.cod_actividad = am.actcod
        LEFT JOIN dwsge.sgss_cmcas10 AS ca
            ON ce.cod_oricentro = ca.oricenasicod
            AND ce.cod_centro = ca.cenasicod
            WHERE cod_centro = ?
            AND cod_servicio ='F21'
            AND ce.cod_subactividad in ('079', '078', '685','724','416')
            AND (
                    CASE 
                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                        ELSE '1'
                    END
                    ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMPPTAD(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F21'
                        AND ce.cod_subactividad in ('079', '078', '685','724','416')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMPPTACSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F21'
                        AND ce.cod_subactividad in ('079', '078', '685','724','416')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }


    /// Indicadores de nutricion
    /// total atenciones - servicio - subactividad
    public List<Map<String, Object>> queryDNMNUTASS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
        SELECT 
            c.servhosdes AS descripcion_servicio,
            a.actespnom AS descripcion_subactividad
        FROM %s ce
        LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
            ON dg.diagcod=ce.diagcod
        LEFT JOIN dwsge.sgss_cmsho10 AS c 
            ON ce.cod_servicio = c.servhoscod
        LEFT JOIN dwsge.sgss_cmace10 AS a
            ON ce.cod_actividad = a.actcod
            AND ce.cod_subactividad = a.actespcod
        LEFT JOIN dwsge.sgss_cmact10 AS am
            ON ce.cod_actividad = am.actcod
        LEFT JOIN dwsge.sgss_cmcas10 AS ca
            ON ce.cod_oricentro = ca.oricenasicod
            AND ce.cod_centro = ca.cenasicod
            WHERE cod_centro = ?
            AND cod_servicio ='F31'
            AND ce.cod_subactividad in ('050', '056', '203', '093', '322')
            AND (
                    CASE 
                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                        ELSE '1'
                    END
                    ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMNUTAD(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F31'
                        AND ce.cod_subactividad in ('050', '056', '203', '093', '322')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMNUTACSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F31'
                        AND ce.cod_subactividad in ('050', '056', '203', '093', '322')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }


    /// Indicadores de enfermeria
    /// total atenciones - servicio - subactividad
    public List<Map<String, Object>> queryDNMENTASS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
        SELECT 
            c.servhosdes AS descripcion_servicio,
            a.actespnom AS descripcion_subactividad
        FROM %s ce
        LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
            ON dg.diagcod=ce.diagcod
        LEFT JOIN dwsge.sgss_cmsho10 AS c 
            ON ce.cod_servicio = c.servhoscod
        LEFT JOIN dwsge.sgss_cmace10 AS a
            ON ce.cod_actividad = a.actcod
            AND ce.cod_subactividad = a.actespcod
        LEFT JOIN dwsge.sgss_cmact10 AS am
            ON ce.cod_actividad = am.actcod
        LEFT JOIN dwsge.sgss_cmcas10 AS ca
            ON ce.cod_oricentro = ca.oricenasicod
            AND ce.cod_centro = ca.cenasicod
            WHERE cod_centro = ?
            AND cod_servicio ='F11'
            AND cod_actividad ='B1'
            AND ce.cod_subactividad in ('072','073','093','576','680','010','801','056')
            AND (
                    CASE 
                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                        ELSE '1'
                    END
                    ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMENTAD(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F11'
                        AND cod_actividad ='B1'
                        AND ce.cod_subactividad in ('072','073','093','576','680','010','801','056')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMENTACSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F11'
                        AND cod_actividad ='B1'
                        AND ce.cod_subactividad in ('072','073','093','576','680','010','801','056')
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// Indicadores de psicologia
    /// total atenciones - servicio - subactividad
    public List<Map<String, Object>> queryDNMPSTASS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
        SELECT 
            c.servhosdes AS descripcion_servicio,
            a.actespnom AS descripcion_subactividad
        FROM %s ce
        LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
            ON dg.diagcod=ce.diagcod
        LEFT JOIN dwsge.sgss_cmsho10 AS c 
            ON ce.cod_servicio = c.servhoscod
        LEFT JOIN dwsge.sgss_cmace10 AS a
            ON ce.cod_actividad = a.actcod
            AND ce.cod_subactividad = a.actespcod
        LEFT JOIN dwsge.sgss_cmact10 AS am
            ON ce.cod_actividad = am.actcod
        LEFT JOIN dwsge.sgss_cmcas10 AS ca
            ON ce.cod_oricentro = ca.oricenasicod
            AND ce.cod_centro = ca.cenasicod
            WHERE cod_centro = ?
            AND cod_servicio ='E21'
            AND cod_actividad ='B1'
            AND ce.cod_subactividad ='005'
            AND (
                    CASE 
                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                        ELSE '1'
                    END
                    ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMPSTAD(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='E21'
                        AND cod_actividad ='B1'
                        AND ce.cod_subactividad ='005'
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMPSTACSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg 
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c 
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='E21'
                        AND cod_actividad ='B1'
                        AND ce.cod_subactividad ='005'
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }


    /// Indicadores de trabajo social
    /// total atenciones - servicio - subactividad
    public List<Map<String, Object>> queryDNMTSTASS(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
        SELECT
            c.servhosdes AS descripcion_servicio,
            a.actespnom AS descripcion_subactividad
        FROM %s ce
        LEFT OUTER JOIN dwsge.sgss_cmdia10 dg
            ON dg.diagcod=ce.diagcod
        LEFT JOIN dwsge.sgss_cmsho10 AS c
            ON ce.cod_servicio = c.servhoscod
        LEFT JOIN dwsge.sgss_cmace10 AS a
            ON ce.cod_actividad = a.actcod
            AND ce.cod_subactividad = a.actespcod
        LEFT JOIN dwsge.sgss_cmact10 AS am
            ON ce.cod_actividad = am.actcod
        LEFT JOIN dwsge.sgss_cmcas10 AS ca
            ON ce.cod_oricentro = ca.oricenasicod
            AND ce.cod_centro = ca.cenasicod
            WHERE cod_centro = ?
            AND cod_servicio ='F51'
            AND cod_actividad ='B1'
            AND ce.cod_subactividad ='055'
            AND (
                    CASE
                        WHEN ce.cod_tipo_paciente = '4' THEN '2'
                        ELSE '1'
                    END
                    ) IN %s""".formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// total atenciones - diagnostico
    public List<Map<String, Object>> queryDNMTSTAD(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT
                    ce.cod_oricentro,
                    ce.cod_centro,
                    a.actespnom,
                    c.servhosdes,
                    ce.cod_servicio,
                    ce.cod_actividad,
                    ce.cod_subactividad,
                    ce.acto_med,
                    ce.doc_paciente,
                    ce.diagcod,
                    dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F51'
                        AND cod_actividad ='B1'
                        AND ce.cod_subactividad ='055'
                        AND (
                                CASE
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
    /// total atenciones - csv
    public List<Map<String, Object>> queryDNMTSTACSV(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_consulta_externa_no_medicas_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
                    SELECT ce.cod_oricentro, ce.cod_centro,a.actespnom,c.servhosdes,ce.cod_servicio, ce.cod_actividad, ce.cod_subactividad,ce.acto_med, ce.doc_paciente, ce.diagcod, dg.diagdes
                    FROM %s ce
                    LEFT OUTER JOIN dwsge.sgss_cmdia10 dg
                        ON dg.diagcod=ce.diagcod
                    LEFT JOIN dwsge.sgss_cmsho10 AS c
                        ON ce.cod_servicio = c.servhoscod
                    LEFT JOIN dwsge.sgss_cmace10 AS a
                        ON ce.cod_actividad = a.actcod
                        AND ce.cod_subactividad = a.actespcod
                    LEFT JOIN dwsge.sgss_cmact10 AS am
                        ON ce.cod_actividad = am.actcod
                    LEFT JOIN dwsge.sgss_cmcas10 AS ca
                        ON ce.cod_oricentro = ca.oricenasicod
                        AND ce.cod_centro = ca.cenasicod
                        WHERE cod_centro = ?
                        AND cod_servicio ='F51'
                        AND cod_actividad ='B1'
                        AND ce.cod_subactividad ='055'
                        AND (
                                CASE 
                                    WHEN ce.cod_tipo_paciente = '4' THEN '2'
                                    ELSE '1'
                                END
                                ) IN %s
                """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }



}
