package essalud.gcpp.essaludgcppbackend.repositories.usuario;

import essalud.gcpp.essaludgcppbackend.model.usuario.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogsRep extends JpaRepository<Log,Integer> {

    @Query(value = """
     SELECT *
     FROM log_usuario log
     JOIN usuario us ON log.id_user=us.id
     WHERE us.id=:id_user
    """, nativeQuery = true)
    List<Log> findAllWithUser(@Param("id_user") Integer id_user);
}
