package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.ResolucionAprobacionDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResolucionAprobacionDocumentoRepository extends JpaRepository<ResolucionAprobacionDocumento, Integer> {
}
