package essalud.gcpp.essaludgcppbackend.repositories.usuario;

import essalud.gcpp.essaludgcppbackend.model.usuario.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogsRep extends JpaRepository<Log, Integer> {

    @Query(value = """
             SELECT *
             FROM log_usuario log
             JOIN usuario us ON log.id_user=us.id
             WHERE us.id=:id_user
            """, nativeQuery = true)
    List<Log> findAllWithUser(@Param("id_user") Integer id_user);

    /// Todos los logs con su usuario cargado en la misma consulta (evita LazyInitializationException y N+1).
    @Query("SELECT l FROM Log l LEFT JOIN FETCH l.user_log ORDER BY l.date_creation DESC")
    List<Log> findAllWithUserFetched();
}
