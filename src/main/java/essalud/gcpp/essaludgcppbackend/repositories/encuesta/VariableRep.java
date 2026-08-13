package essalud.gcpp.essaludgcppbackend.repositories.encuesta;

import essalud.gcpp.essaludgcppbackend.model.encuesta.Variable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariableRep extends JpaRepository<Variable, Integer> {
}
