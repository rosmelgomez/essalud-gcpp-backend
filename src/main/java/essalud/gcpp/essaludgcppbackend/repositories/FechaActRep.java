package essalud.gcpp.essaludgcppbackend.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class FechaActRep {
    private final JdbcTemplate secondJdbcTemplate;

    public FechaActRep(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
        this.secondJdbcTemplate = secondJdbcTemplate;
    }

    public Map<String, Object> queryFA() {

        String tabla = "dwsge.fecha_act";


        String sql = """
            SELECT TO_CHAR(MIN(fecha_act), 'DD/MM/YYYY HH24:MI:SS') AS fecha_act
            FROM %s
            """.formatted(tabla);

        return secondJdbcTemplate.queryForMap(sql);
    }


}
