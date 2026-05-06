package essalud.gcpp.essaludgcppbackend.repositories;

import essalud.gcpp.essaludgcppbackend.model.documentos.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Integer> {
}
