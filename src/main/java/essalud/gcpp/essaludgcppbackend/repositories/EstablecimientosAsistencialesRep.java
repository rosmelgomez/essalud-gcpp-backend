package essalud.gcpp.essaludgcppbackend.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EstablecimientosAsistencialesRep {

    private final JdbcTemplate secondJdbcTemplate;

    public EstablecimientosAsistencialesRep(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
        this.secondJdbcTemplate = secondJdbcTemplate;
    }

    public List<Map<String, Object>> getCentros() {

        String tabla = "dwsge.sgss_cmcas10";
        ///String tabla = "public.sgss_cmcas10";


        String sql = """
        SELECT cenasicod, cenasides
        FROM %s
        WHERE estregcod ='1'
        ORDER BY id ASC""".formatted(tabla);
        return secondJdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getCentrosByCodeRed(String code_red) {
        String tabla1= "dwsge.sgss_cmcas10";
        String tabla2= "dwsge.sgss_cmras10";

        ///String tabla1= "public.sgss_cmcas10";
        ///String tabla2= "public.sgss_cmras10";

        String sql = """
        SELECT
            r.redasiscod,
            r.redasisdes,
            c.cenasicod,
            c.cenasides
        FROM %s c
        LEFT JOIN %s r
            ON c.redasiscod = r.redasiscod
        WHERE c.redasiscod = ?
        AND c.estregcod ='1'
        """.formatted(tabla1,tabla2 );

        return secondJdbcTemplate.queryForList(sql, code_red);
    }

    public List<Map<String, Object>> getRedes() {

        String tabla1 = "dwsge.sgss_cmcas10";
        String tabla2 = "dwsge.sgss_cmras10";

        ///String tabla1 = "public.sgss_cmcas10";
        ///String tabla2 = "public.sgss_cmras10";

        String sql = """
        SELECT DISTINCT
            r.redasiscod,
            r.redasisdes
        FROM %s c
        LEFT JOIN %s r
            ON c.redasiscod = r.redasiscod
        WHERE r.redasiscod IS NOT NULL
        AND c.estregcod ='1'
        ORDER BY r.redasisdes ASC
        """.formatted(tabla1,tabla2);
        return secondJdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getRedesWithCentros() {

        String tabla1= "dwsge.sgss_cmcas10";
        String tabla2= "dwsge.sgss_cmras10";

        ///String tabla1= "public.sgss_cmcas10";
        ///String tabla2 = "public.sgss_cmras10";

        String sql = """
        SELECT
            r.redasiscod,
            r.redasisdes,
            c.cenasicod,
            c.cenasides
        FROM %s c
        LEFT JOIN %s r
            ON c.redasiscod = r.redasiscod
        AND c.estregcod ='1'
        """.formatted(tabla1, tabla2);
        return secondJdbcTemplate.queryForList(sql);
    }

    public Map<String, Object> getredwithcentrosbycodcas(String codcas) {

        String tabla1= "dwsge.sgss_cmcas10";
        String tabla2 = "dwsge.sgss_cmras10";

        ///String tabla1= "public.sgss_cmcas10";
        ///String tabla2 = "public.sgss_cmras10";

        String sql = """
        SELECT
            r.redasiscod,
            r.redasisdes,
            c.cenasicod,
            c.cenasides
        FROM %s c
        LEFT JOIN %s r
            ON c.redasiscod = r.redasiscod
        AND c.estregcod ='1'
        WHERE c.cenasicod= ?
        """.formatted(tabla1, tabla2);
        return secondJdbcTemplate.queryForMap(sql, codcas);
    }
}
