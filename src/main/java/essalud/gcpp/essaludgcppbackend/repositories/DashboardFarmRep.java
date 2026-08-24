package essalud.gcpp.essaludgcppbackend.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DashboardFarmRep {

    private final JdbcTemplate secondJdbcTemplate;

    public DashboardFarmRep(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
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

    /// Recetas atendidas
    public List<Map<String, Object>> queryDFRA(int anio, int periodo, String codcas, int codasegu) {

        String tabla = "dssge.dw_farm_" +  anio +"_"+ periodo_str(periodo);
        String code_asegurado = code_asegurado(codasegu);

        String sql = """
            SELECT
                f.cod_area,
                COALESCE(a.arehosdes, 'SIN ÁREA (' || f.cod_area || ')') AS area,
                ca.cenasides,
                COUNT(*) AS recetas
            FROM %s f
            LEFT JOIN dwsge.sgss_cmaho10 a ON a.arehoscod = f.cod_area
            LEFT JOIN dwsge.sgss_cmcas10 ca
                ON ca.oricenasicod = f.cod_oricentro AND ca.cenasicod = f.cod_centro
            WHERE f.cod_centro = ?
                AND (
                        CASE
                            WHEN f.cod_tipo_paciente = '4' THEN '2'
                            ELSE '1'
                        END
                    ) IN %s
                AND cant_atendida > 0
            GROUP BY f.cod_area, a.arehosdes, ca.cenasides
            ORDER BY recetas DESC
            """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    /// csv
    public List<Map<String, Object>> queryDFCSV(int anio, int periodo, String codcas, int codasegu) {

        String tabla = "dssge.dw_farm_" +  anio +"_"+ periodo_str(periodo);
        String code_asegurado = code_asegurado(codasegu);

        String sql = """
            SELECT
                f.anio,
                f.periodo,
                f.cod_centro,
                ca.cenasides,
                f.cod_area,
                a.arehosdes AS area,
                f.cod_topico,
                f.cod_servicio,
                s.servhosdes AS servicio,
                f.cod_actividad,
                f.cod_subactividad,
                f.num_actomed,
                f.dni_profesional,
                f.cmp,
                f.fecha_solicitud,
                f.num_receta,
                f.cod_medicamento,
                f.cant_solicitud,
                f.cant_atendida,
                f.unidad,
                f.duracion_med,
                f.cod_dx,
                f.dni,
                f.sexo,
                f.edad_anio,
                f.meses,
                f.dias,
                f.cod_farmacia,
                f.fecha_despacho,
                f.hora_despacho,
                f.numrecetmanual,
                f.precio,
                f.cod_tipo_seguro,
                f.cod_tipo_parentesco,
                f.cod_tipo_paciente,
                f.cod_prioridad,
                f.tipo_movimiento
            FROM %s f
            LEFT JOIN dwsge.sgss_cmaho10 a ON a.arehoscod = f.cod_area
            LEFT JOIN dwsge.sgss_cmsho10 s ON s.servhoscod = f.cod_servicio
            LEFT JOIN dwsge.sgss_cmcas10 ca
                ON ca.oricenasicod = f.cod_oricentro AND ca.cenasicod = f.cod_centro
            WHERE f.cod_centro = ?
                AND (
                        CASE
                            WHEN f.cod_tipo_paciente = '4' THEN '2'
                            ELSE '1'
                        END
                    ) IN %s
                AND cant_atendida > 0
            ORDER BY f.fecha_despacho, f.num_receta
            """.formatted(tabla, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);
    }



}
