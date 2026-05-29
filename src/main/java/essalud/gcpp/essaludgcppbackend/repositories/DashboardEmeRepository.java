package essalud.gcpp.essaludgcppbackend.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DashboardEmeRepository {
    private final JdbcTemplate secondJdbcTemplate;

    public DashboardEmeRepository(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
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


    ///Dashboard Emergencias
    /// Prioridad
    public List<Map<String, Object>> queryDEP(int anio, int periodo, String codcas, int codasegu, String prioridad) {
        String tabla = "dwsge.dwe_emergencia_atenciones_homologacion_" + anio;
        //String periodo_sql=anio+periodo;
        String periodo_sql = String.format("%d%02d", anio, periodo);
        System.out.println(periodo_sql);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
            SELECT
                d.cod_centro,
                d.periodo,
                d.cod_topico,
                d.topemedes AS topico_essi,
                d.acto_med,
                d.fecha_aten,
                d.hora_aten,
                d.cod_tipo_paciente,
                d.tipopacinom,
                d.cod_prioridad,
                d.cod_emergencia,
                d.secuen_aten,
                d.cod_estandar,
                d.des_estandar AS topico_ses,
                d.cod_diagnostico,
                d.diagdes,
                d.cod_prioridad_n
            FROM (
                SELECT
                    ROW_NUMBER() OVER (
                        PARTITION BY cod_centro, cod_estandar, acto_med, cod_emergencia
                        ORDER BY CAST(secuen_aten AS INTEGER) ASC
                    ) AS SECUENCIA,
                    c.*
                FROM (
                    SELECT
                        a.cod_centro,
                        a.periodo,
                        a.cod_topico,
                        top.topemedes,
                        a.acto_med,
                        a.fecha_aten,
                        a.hora_aten,
                        a.cod_tipo_paciente,
                        tp.tipopacinom,
                        a.cod_prioridad,
                        a.cod_emergencia,
                        a.secuen_aten,
                        a.cod_estandar,
                        es.des_estandar,
                        a.cod_diagnostico,
                        dg.diagdes,

                        CASE
                            WHEN a.cod_estandar = '04' THEN '1'
                            WHEN a.cod_prioridad = '1' THEN '2'
                            ELSE a.cod_prioridad
                        END AS cod_prioridad_n

                    FROM %s a

                    LEFT JOIN dwsge.sgss_cmdia10 dg
                        ON dg.diagcod = a.cod_diagnostico

                    LEFT JOIN dwsge.sgss_cbtpc10 tp
                        ON tp.tipopacicod = a.cod_tipo_paciente

                    LEFT JOIN dwsge.sgss_mbtoe10 top
                        ON top.topemecod = a.cod_topico

                    LEFT JOIN dwsge.dim_estandar es
                        ON es.id_estandar = a.cod_estandar

                    WHERE
                        a.cod_diagnostico IS NOT NULL
                        AND a.cod_estandar IN ('04','05','06','07','08','09','10','11','12','13','14')
                        AND (
                            CASE
                                WHEN a.cod_tipo_paciente = '4' THEN '2'
                                ELSE '1'
                            END
                        ) IN %s

                ) c
            ) d
            WHERE
                d.SECUENCIA = 1
                AND d.cod_centro = ?
                AND d.periodo = ?
                AND d.cod_prioridad_n= ?
            """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas,periodo_sql, prioridad);
    }

    /// defunciones
    public List<Map<String, Object>> queryDED(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_emergencia_defunciones_homologacion_" + anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
        SELECT * FROM %s
        WHERE cod_centro=?
        and (
                    CASE
                        WHEN cod_tipo_paciente = '4' THEN '2'
                        ELSE '1'
                    END
                    ) IN %s
        """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// mayor a 24h
    public List<Map<String, Object>> queryDEMY24HR(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_emergencia_estancia_homologacion_"+anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
            SELECT des_estancia, COUNT(*) AS total
            FROM %s estancias
            LEFT JOIN dwsge.dim_estancia est ON est.id_estancia = estancias.rango_estancia
            WHERE estancias.cod_centro = ?
              AND des_estancia = 'Mayor 24h'
              AND estancia_horas IS NOT NULL
              AND (
                        CASE
                            WHEN estancias.cod_tipo_paciente = '4' THEN '2'
                            ELSE '1'
                        END
                        ) IN %s
            GROUP BY des_estancia
            """.formatted(tabla, code_asegurado);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// menor a 24h
    public List<Map<String, Object>> queryDEMN24HR(int anio, int periodo, String codcas, int codasegu) {
        String tabla = "dwsge.dwe_emergencia_estancia_homologacion_"+ anio + "_" + periodo_str(periodo);

        String code_asegurado = code_asegurado(codasegu);

        String sql = """
            SELECT des_estancia, COUNT(*) AS total
            FROM %s estancias
            LEFT JOIN dwsge.dim_estancia est ON est.id_estancia = estancias.rango_estancia
            WHERE estancias.cod_centro = ?
              AND des_estancia = 'Menor 24h'
              AND estancia_horas IS NOT NULL
              AND (
                        CASE
                            WHEN estancias.cod_tipo_paciente = '4' THEN '2'
                            ELSE '1'
                        END
                        ) IN %s
            GROUP BY des_estancia
        """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }


}
