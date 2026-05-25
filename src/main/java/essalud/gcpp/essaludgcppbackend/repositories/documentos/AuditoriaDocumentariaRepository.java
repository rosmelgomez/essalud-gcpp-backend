package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.AuditoriaDocumentaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditoriaDocumentariaRepository extends JpaRepository<AuditoriaDocumentaria, Long> {
    List<AuditoriaDocumentaria> findByDocumentoId(Integer documentoId);
}
