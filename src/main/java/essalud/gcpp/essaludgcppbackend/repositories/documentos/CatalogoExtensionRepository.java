package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.CatalogoExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogoExtensionRepository extends JpaRepository<CatalogoExtension, Integer> {
}
