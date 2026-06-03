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
            vd.id_documento,
            vd.deleted_at,
            vd.deleted_by_user_id,
            vd.file_key,
            vd.storage_provider,
            vd.visible
            FROM version_documento vd
            WHERE vd.id = :id""", nativeQuery = true)
    VersionDocumento getVersion(@Param("id") Integer id);

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
            vd.id_documento,
            vd.deleted_at,
            vd.deleted_by_user_id,
            vd.file_key,
            vd.storage_provider,
            vd.visible
            FROM version_documento vd
            WHERE vd.id_documento =:idDocumento AND vd.deleted_at IS NULL""", nativeQuery = true)
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
            vd.id_documento,
            vd.deleted_at,
            vd.deleted_by_user_id,
            vd.file_key,
            vd.storage_provider,
            vd.visible
            FROM version_documento vd
            WHERE vd.id_user_creation = :idUser AND vd.deleted_at IS NULL""", nativeQuery = true)
    List<VersionDocumento> getVersionByUser(@Param("idUser") Integer idUser);

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
            vd.id_documento,
            vd.deleted_at,
            vd.deleted_by_user_id,
            vd.file_key,
            vd.storage_provider,
            vd.visible
            FROM version_documento vd
            JOIN documento doc ON doc.id=vd.id_documento
            WHERE doc.codcas=:codcas AND vd.deleted_at IS NULL""", nativeQuery = true)
    List<VersionDocumento> getVersionDocumentoByCodcas(@Param("codcas")  String codcas);

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
            vd.id_documento,
            vd.deleted_at,
            vd.deleted_by_user_id,
            vd.file_key,
            vd.storage_provider,
            vd.visible
            FROM version_documento vd
            JOIN documento doc ON doc.id=vd.id_documento
            WHERE doc.codred=:codred AND vd.deleted_at IS NULL""", nativeQuery = true)
    List<VersionDocumento> getVersionDocumentoByCodred(@Param("codred")  String codred);



}
