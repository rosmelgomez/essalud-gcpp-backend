package essalud.gcpp.essaludgcppbackend.repositories;

import essalud.gcpp.essaludgcppbackend.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogsRepository extends JpaRepository<Log,Integer> {

    @Query("""
    SELECT l FROM Log l
    JOIN FETCH l.user_log u
    """)
    List<Log> findAllWithUser();
}
