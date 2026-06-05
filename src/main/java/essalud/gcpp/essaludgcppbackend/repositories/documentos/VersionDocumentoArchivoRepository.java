package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.VersionDocumentoArchivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionDocumentoArchivoRepository extends JpaRepository<VersionDocumentoArchivo, Integer> {
}
