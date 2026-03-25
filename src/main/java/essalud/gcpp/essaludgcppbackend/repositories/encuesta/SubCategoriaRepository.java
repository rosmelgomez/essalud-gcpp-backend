package essalud.gcpp.essaludgcppbackend.repositories.encuesta;

import essalud.gcpp.essaludgcppbackend.model.encuesta.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoriaRepository extends JpaRepository<SubCategoria,Integer> {
}
