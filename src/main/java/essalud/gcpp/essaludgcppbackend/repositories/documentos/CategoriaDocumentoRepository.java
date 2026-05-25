package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.CategoriaDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaDocumentoRepository extends JpaRepository<CategoriaDocumento, Integer> {
    List<CategoriaDocumento> findByFlagTrue();
}
