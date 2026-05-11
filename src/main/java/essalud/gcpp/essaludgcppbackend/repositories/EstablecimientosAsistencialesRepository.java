package essalud.gcpp.essaludgcppbackend.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EstablecimientosAsistencialesRepository {

    private final JdbcTemplate secondJdbcTemplate;

    public EstablecimientosAsistencialesRepository(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
        this.secondJdbcTemplate = secondJdbcTemplate;
    }

    public List<Map<String, Object>> getCentros() {

        String sql = """
        SELECT cenasicod, cenasides
        FROM dwsge.sgss_cmcas10
        WHERE estregcod ='1'
        ORDER BY id ASC""";
        return secondJdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getCentrosByCodeRed(String code_red) {
        String tabla= "dwsge.sgss_cmcas10";

        String sql = """
        SELECT
            r.redasiscod,
            r.redasisdes,
            c.cenasicod,
            c.cenasides
        FROM %s c
        LEFT JOIN dwsge.sgss_cmras10 r
            ON c.redasiscod = r.redasiscod
        WHERE c.redasiscod = ?
        AND c.estregcod ='1'
        """.formatted(tabla);

        return secondJdbcTemplate.queryForList(sql, code_red);
    }

    public List<Map<String, Object>> getRedes() {

        String sql = """
        SELECT DISTINCT
            r.redasiscod,
            r.redasisdes
        FROM dwsge.sgss_cmcas10 c
        LEFT JOIN dwsge.sgss_cmras10 r
            ON c.redasiscod = r.redasiscod
        WHERE r.redasiscod IS NOT NULL
        AND c.estregcod ='1'
        ORDER BY r.redasisdes ASC
        """;
        return secondJdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getRedesWithCentros() {

        String tabla= "dwsge.sgss_cmcas10";
        String sql = """
        SELECT
            r.redasiscod,
            r.redasisdes,
            c.cenasicod,
            c.cenasides
        FROM %s c
        LEFT JOIN dwsge.sgss_cmras10 r
            ON c.redasiscod = r.redasiscod
        AND c.estregcod ='1'
        """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql);
    }

    public Map<String, Object> getredeswithcentrosbycodcas(String codcas) {

        String tabla= "dwsge.sgss_cmcas10";
        String sql = """
        SELECT
            r.redasiscod,
            r.redasisdes,
            c.cenasicod,
            c.cenasides
        FROM %s c
        LEFT JOIN dwsge.sgss_cmras10 r
            ON c.redasiscod = r.redasiscod
        AND c.estregcod ='1'
        WHERE c.cenasicod= ?
        """.formatted(tabla);
        return secondJdbcTemplate.queryForMap(sql, codcas);
    }
}
