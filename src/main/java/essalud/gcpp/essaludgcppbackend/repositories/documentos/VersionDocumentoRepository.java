package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.VersionDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VersionDocumentoRepository extends JpaRepository<VersionDocumento, Integer> {

    @Query(value = """
            SELECT
            vd.id,
            vd.nombre,
            vd.descripcion,
            vd.extencion,
            vd.version,
            vd.size,
            vd.date_creation,
            vd.date_update,
            NULL as archivo,
            vd.id_user_creation,
            vd.id_documento
            FROM version_documento vd
            WHERE vd.id_documento = :idDocumento""", nativeQuery = true)
    List<VersionDocumento> getVersionByDocumento(@Param("idDocumento") Integer idDocumento);

    @Query(value = """
            SELECT
            vd.id,
            vd.nombre,
            vd.descripcion,
            vd.extencion,
            vd.version,
            vd.size,
            vd.date_creation,
            vd.date_update,
            NULL as archivo,
            vd.id_user_creation,
            vd.id_documento
            FROM version_documento vd
            WHERE vd.id_user_creation = :idUser""", nativeQuery = true)
    List<VersionDocumento> getVersionByUser(@Param("idUser") Integer idUser);
}
