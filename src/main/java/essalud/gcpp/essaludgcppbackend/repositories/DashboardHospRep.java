package essalud.gcpp.essaludgcppbackend.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardHospRep {
    private final JdbcTemplate secondJdbcTemplate;

    public DashboardHospRep(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
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

    /// Query Egresos
    public List<Map<String, Object>> queryDHE(int anio, int periodo, String codcas, int codasegu) {

        String tabla = String.format("dwsge.dwe_hosp_egresos_%d_%02d", anio, periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
            select
                ca.redasiscod,
                r.redasisdes,
                ce.cod_centro,
                ce.periodo,
                ce.anio,
                ce.cod_servicio,
                c.servhosdes AS servicio,
                ce.cod_actividad,
                am.actdes AS actividad,
                ce.cod_estacion,
                enf.estenfdes,
                ce.anio_edad,
                ce.sexo,
                ce.cod_tip_seguro,
                ts.tipsegdes as tipo_seguro,
                ce.cod_tipo_paciente,
                tp.tipopacinom as tipo_paciente,
                hc.esp as especialidad,
                ce.fec_ingr,
                ce.fec_altmed,
                ce.fec_egreso,
                ce.dias_hospit,
                ce.cuarto,
                ce.cama,
                ce.diag_cod,
                d.diagdes,
                d.edxcapdes AS capitulo
            from %s ce
            LEFT JOIN dwsge.sgss_cmdia10_chapter d ON ce.diag_cod = d.diagcod
            LEFT JOIN dwsge.sgss_cmsho10 AS c ON ce.cod_servicio = c.servhoscod
            LEFT JOIN dwsge.sgss_cmcas10 AS ca ON ce.cod_oricentro = ca.oricenasicod AND ce.cod_centro = ca.cenasicod
            LEFT JOIN dwsge.sgss_cmact10 AS am ON ce.cod_actividad = am.actcod
            LEFT JOIN dwsge.sgss_cmras10 r ON ca.redasiscod = r.redasiscod
            LEFT JOIN dwsge.sgss_cmtse10 ts ON ts.tipsegcod = ce.cod_tip_seguro
            LEFT JOIN dwsge.sgss_cbtpc10 tp ON tp.tipopacicod = ce.cod_tipo_paciente
            LEFT JOIN dwsge.sgss_hmese10 enf
                ON enf.oricenasicod = ce.cod_oricentro
                AND enf.cenasicod = ce.cod_centro
                AND enf.servhoscod = ce.cod_serv_inter
                AND enf.estenfcod = ce.cod_estacion
                AND enf.estenfestregcod = '1'
                AND enf.arehoscod = '03'
            LEFT JOIN dssge.dw_homologacion_camas hc ON hc.cod_centro = ce.cod_centro AND hc.cama = ce.cama
            WHERE ce.cod_centro = ?
            AND (CASE WHEN ce.cod_tipo_paciente = '4' THEN '2' ELSE '1' END) IN %s
            AND ce.cod_tipo_cama IN ('1')
            """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    
}
