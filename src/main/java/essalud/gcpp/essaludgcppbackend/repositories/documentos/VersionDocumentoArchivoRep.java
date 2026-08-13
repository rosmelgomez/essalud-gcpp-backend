package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.VersionDocumentoArchivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionDocumentoArchivoRep extends JpaRepository<VersionDocumentoArchivo, Integer> {
}
