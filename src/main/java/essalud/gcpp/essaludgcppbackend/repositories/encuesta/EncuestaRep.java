package essalud.gcpp.essaludgcppbackend.repositories.encuesta;

import essalud.gcpp.essaludgcppbackend.model.encuesta.Encuesta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncuestaRep extends JpaRepository<Encuesta, Integer> {
}
