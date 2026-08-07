package essalud.gcpp.essaludgcppbackend.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardSeguimientoPacienteRep {

    private final JdbcTemplate secondJdbcTemplate;

    public DashboardSeguimientoPacienteRep(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
        this.secondJdbcTemplate = secondJdbcTemplate;
    }

}
