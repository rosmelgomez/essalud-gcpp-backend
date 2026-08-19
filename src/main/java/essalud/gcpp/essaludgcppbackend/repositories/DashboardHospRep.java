package essalud.gcpp.essaludgcppbackend.repositories;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

    private String periodo_str(int periodo) {
        if (periodo < 10) {
            return   "0" + periodo;
        } else {
            return String.valueOf(periodo);
        }
    }

    /// Egresos
    public List<Map<String, Object>> queryDHE(int anio, int periodo, String codcas, int codasegu) {

        String tabla = "dwsge.dwe_hosp_egresos_" +  anio +"_"+ periodo;
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


    ///pacientes mes
    public List<Map<String, Object>> queryDHPM(int anio, int periodo, String codcas) {

        ///String dia_ultimo= ;
        LocalDateTime fecha= LocalDateTime.now(ZoneId.of("America/Lima"));
        System.out.print("\nfecha: "+ fecha);

        String fecha_inicio = anio + "-" + periodo_str(periodo) + "-01";
        String fecha_fin = anio + "-" + periodo_str(periodo) + "-30" ;

        System.out.print("\nfecha_inicio: "+ fecha_inicio);
        System.out.print("\nfecha_fin: "+ fecha_fin);

        String sql = """
            WITH parametros AS (
                SELECT
                    DATE '2026-01-01' AS fecha_inicio,
                    DATE '2026-01-31' AS fecha_fin
            ),

            hospitalizacion AS (
                SELECT
                    h.cod_centro,
                    h.doc_paciente,
                    h.acto_med,
                    h.cama,

                    (h.fec_ingr::date + h.hor_ingr::time) AS fecha_hora_ingreso,

                    CASE
                        WHEN h.fec_egreso IS NOT NULL
                        AND h.hor_egreso IS NOT NULL
                        THEN (h.fec_egreso::date + h.hor_egreso::time)
                        ELSE NULL
                    END AS fecha_hora_egreso

                FROM dssge.dw_hosp_v2 h
                CROSS JOIN parametros p

                WHERE h.cod_centro = ?
                AND h.ubicacion = 'HOSPITALIZACION'
                -- SOLO MES ACTUAL Y MES ANTERIOR
                AND h.fec_ingr::date >= (
                    date_trunc('month', p.fecha_inicio::date)
                    - interval '1 month'
                )::date

                AND h.fec_ingr::date <= p.fecha_fin::date
            ),

            calendario AS (
                SELECT
                    (
                        gs::date + time '06:00:00'
                    ) AS fecha_censo
                FROM parametros p,
                generate_series(
                    p.fecha_inicio,
                    p.fecha_fin,
                    interval '1 day'
                ) gs
            )

            SELECT
                c.fecha_censo::date AS fecha,

                COUNT(DISTINCT h.doc_paciente)
                    AS pacientes_hospitalizados

            FROM calendario c
            JOIN hospitalizacion h
                ON h.fecha_hora_ingreso <= c.fecha_censo
            AND (
                    h.fecha_hora_egreso > c.fecha_censo
                    OR h.fecha_hora_egreso IS NULL
            )

            GROUP BY c.fecha_censo
            ORDER BY c.fecha_censo
            """;

        return secondJdbcTemplate.queryForList(sql, codcas);
    }



}
