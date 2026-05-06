package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FileRepository {

    private final JdbcTemplate secondJdbcTemplate;

    public FileRepository(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
        this.secondJdbcTemplate = secondJdbcTemplate;
    }

    public byte[] findArchivo(String name) {
        String tabla = "dwsge.f_tecnicas";
        String sql = """
            SELECT archivo_pdf
            FROM %s
            WHERE nombre = ?
            """.formatted(tabla);

        return secondJdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> rs.getBytes("archivo_pdf"),
                name
        );
    }
}
