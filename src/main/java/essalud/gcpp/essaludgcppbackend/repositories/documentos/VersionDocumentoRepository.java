package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.VersionDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VersionDocumentoRepository extends JpaRepository<VersionDocumento, Integer> {

    @Query(value = "SELECT\n" +
            "vd.id,\n" +
            "vd.nombre,\n" +
            "vd.comentario,\n" +
            "vd.extencion,\n" +
            "vd.version,\n" +
            "vd.size,\n" +
            "vd.date_creation,\n" +
            "vd.date_update,\n" +
            "NULL as archivo,\n" +
            "vd.id_user_creation,\n" +
            "vd.id_documento\n" +
            "FROM version_documento vd\n" +
            "WHERE vd.id_documento = :idDocumento", nativeQuery = true)
    List<VersionDocumento> getVersionByDocumento(@Param("idDocumento") Integer idDocumento);
}
