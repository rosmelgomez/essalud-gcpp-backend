package essalud.gcpp.essaludgcppbackend.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DashboardTransplanteRep {


    private final JdbcTemplate secondJdbcTemplate;

    public DashboardTransplanteRep(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
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

    /// Query Dashboard Transplate
    public List<Map<String, Object>> queryDTrans(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dssge.dwe_centro_quirurgico_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
            SELECT cod_oricentro,
                cod_centro,
                ca.cenasides AS cenasides,
                periodo,
                anio,
                cod_area,
                cod_servicio,
                c.servhosdes AS servicio,
                cod_cpms,
                cp.cpsdes AS cpms,
                cod_tip_cirujano,
                cod_tipdoc_medico,
                dni_medico,
                fec_oper,
                autogenerado,
                cmame_pacsecnum,
                cod_tipdoc_paciente,
                doc_paciente,
                anio_edad,
                meses,
                sexo,
                cod_tip_seguro,
                cod_tipo_parentesco,
                cod_tipo_paciente,
                cod_sala,
                hor_ini_sala,
                hor_fin_sala,
                duracion_sala,
                hor_ini_anest,
                hor_fin_anest,
                duracion_anest,
                hor_ini_operac,
                hor_fin_operac,
                duracion_operac,
                acto_med,
                cod_complejidad,
                cod_anest,
                cod_tipo_programacion,
                num_solicitud,
                cod_quirof,
                fecsolicsalaoperac,
                fecsolicitadaoperac,
                fecprogram,
                cas_adscripcion,
                h_c,
                salopedes,
                cod_destegreso,
                fechcreasolicitud,
                horcreasolic,
                fecaptitud
            FROM %s cq
                LEFT JOIN dwsge.sgss_cmsho10 AS c ON cq.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca ON cq.cod_oricentro = ca.oricenasicod AND cq.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.sgss_qmcqs10 AS q ON q.cenasicod = cq.cod_centro AND q.salopecod= cq.cod_sala_operacion AND cq.COD_QUIROF = q.cenquicod
                LEFT JOIN dwsge.sgss_cmcpp10 as cp ON cp.cpscod = cq.cod_cpms
            WHERE cod_centro=?
              AND cod_cpms IN (
                '38241',
                '38240',
                '48551',
                '48552',
                '33945',
                '33935',
                '65730',
                '65710',
                '65750',
                '65755',
                '65712',
                '65714',
                '65732',
                '65752',
                '65756',
                '65757',
                '50250',
                '47135',
                '47163',
                '32851',
                '32852',
                '32854',
                '50360',
                '50365',
                '50380'
                )
              AND (
                    CASE
                        WHEN cod_tipo_paciente = '4' THEN '2'
                        ELSE '1'
                    END
                  ) IN %s
            """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }
}
