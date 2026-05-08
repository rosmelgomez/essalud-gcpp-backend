package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.VersionDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VersionDocumentoRepository extends JpaRepository<VersionDocumento, Integer> {

    @Query(value = "SELECT vd.* \n" +
            "FROM version_documento vd\n" +
            "WHERE vd.id_documento  = :iddoc", nativeQuery = true)
    List<VersionDocumento> getVersionByDocumento (@Param("iddoc") Integer iddoc);

}
