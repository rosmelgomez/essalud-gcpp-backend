package essalud.gcpp.essaludgcppbackend.repositories;

import essalud.gcpp.essaludgcppbackend.model.Logs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogsRepository extends JpaRepository<Logs,Integer> {
}
