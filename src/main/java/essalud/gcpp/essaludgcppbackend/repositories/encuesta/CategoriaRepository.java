package essalud.gcpp.essaludgcppbackend.repositories.encuesta;

import essalud.gcpp.essaludgcppbackend.model.encuesta.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria,Integer> {
}
