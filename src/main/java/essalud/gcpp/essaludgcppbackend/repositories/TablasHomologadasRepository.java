package essalud.gcpp.essaludgcppbackend.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class TablasHomologadasRepository {

    private final JdbcTemplate secondJdbcTemplate;

    public TablasHomologadasRepository(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
        this.secondJdbcTemplate = secondJdbcTemplate;
    }

    ///Tabla Homologada Emergencia
    public List<Map<String, Object>> queryTHE(String codcas) {
		String tabla ="dssge.dw_homologacion_enlaces_emergencia";

        String sql = """
		SELECT
			h.cod_centro,
			c.cenasides,
			h.cod_topico,
			t.topemedes,
			h.cod_emergencia,
			e.desc_emergencia,
			h.cod_estandar,
			s.des_estandar
		FROM %s h
		LEFT OUTER JOIN dwsge.sgss_cmcas10 c
			ON c.cenasicod = h.cod_centro
		LEFT OUTER JOIN dssge.sgss_mbtoe10 t
			ON h.cod_topico = t.topemecod
		LEFT OUTER JOIN dwsge.dim_emergencia e
			ON e.cod_emergencia = h.cod_emergencia
		LEFT OUTER JOIN dwsge.dim_estandar s
			ON s.id_estandar = h.cod_estandar
		WHERE h.cod_estado = '1'
			AND h.cod_centro = ?
		ORDER BY h.cod_topico, h.cod_emergencia, h.cod_estandar
		""".formatted(tabla);
        return secondJdbcTemplate.queryForList(sql, codcas);
    }

    ///Tabla homologada consulta externa
    public List<Map<String, Object>> queryTHCE() {

		String tabla ="dssge.dw_homologacion_enlaces";

        String sql = """
		SELECT
			desc_gpo_ocup,
			cod_area,
			desc_area,
			cod_actividad,
			desc_actividad,
			cod_subactividad,
			desc_subactividad,
			cod_servicio,
			desc_servicio,
			cod_especialidad,
			especialidad,
			cod_subespecialidad,
			subespecialidad,
			cod_agrupador,
			agrupador,
			cod_variable,
			variable,
			anio,
			anio_uso
		FROM %s
		ORDER BY desc_gpo_ocup, cod_area, cod_actividad, cod_subactividad, cod_servicio
		""".formatted(tabla);
        return secondJdbcTemplate.queryForList(sql);
    }


	///Tabla homologada camas
	public List<Map<String, Object>> queryTHCAMA(String codcas) {
		String tabla ="dssge.dw_homologacion_camas";
		String sql = """
			SELECT
			cam.*,
			cas.cenasides,
			serv.servhosdes
			FROM dwsge.sgss_hmcam10 cam
			JOIN dwsge.sgss_cmcas10 cas ON cam.cenasicod = cas.cenasicod
			JOIN dwsge.sgss_cmsho10 serv ON cam.servhoscod = serv.servhoscod
			WHERE  cam.cenasicod = ?""";
		return secondJdbcTemplate.queryForList(sql, codcas);
	}
}
