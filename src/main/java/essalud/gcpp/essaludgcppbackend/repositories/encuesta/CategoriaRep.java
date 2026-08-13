package essalud.gcpp.essaludgcppbackend.repositories.encuesta;

import essalud.gcpp.essaludgcppbackend.model.encuesta.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRep extends JpaRepository<Categoria, Integer> {
}
