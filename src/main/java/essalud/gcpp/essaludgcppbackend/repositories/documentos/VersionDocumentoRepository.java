package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.VersionDocumentoDto;
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
            vd.date_delete,
            NULL as archivo,
            vd.visible,
            vd.id_user_creation,
            vd.id_documento,
            fad.id_user_aprobador,
            fad.prioridad,
            fad.comentario_solicitante as comentario_solicitud
            FROM version_documento vd
            LEFT JOIN flujo_aprobacion_documento fad ON vd.id = fad.id_version_documento
            WHERE vd.id = :id""", nativeQuery = true)
    VersionDocumentoDto getVersion(@Param("id") Integer id);

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
            vd.date_delete,
            NULL as archivo,
            vd.visible,
            vd.id_user_creation,
            vd.id_documento,
            fad.id_user_aprobador,
            fad.prioridad,
            fad.comentario_solicitante as comentario_solicitud
            FROM version_documento vd
            LEFT JOIN flujo_aprobacion_documento fad ON vd.id = fad.id_version_documento
            JOIN documento doc ON doc.id=vd.id_documento
            WHERE vd.id_documento =:idDocumento AND vd.date_delete IS NULL""", nativeQuery = true)
    List<VersionDocumentoDto> getVersionByDocumento(@Param("idDocumento") Integer idDocumento);

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
            vd.date_delete,
            NULL as archivo,
            vd.visible,
            vd.id_user_creation,
            vd.id_documento,
            fad.id_user_aprobador,
            fad.prioridad,
            fad.comentario_solicitante as comentario_solicitud
            FROM version_documento vd
            LEFT JOIN flujo_aprobacion_documento fad ON vd.id = fad.id_version_documento
            WHERE vd.id_user_creation = :idUser AND vd.date_delete IS NULL""", nativeQuery = true)
    List<VersionDocumentoDto> getVersionByUser(@Param("idUser") Integer idUser);

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
            vd.date_delete,
            NULL as archivo,
            vd.visible,
            vd.id_user_creation,
            vd.id_documento,
            fad.id_user_aprobador,
            fad.prioridad,
            fad.comentario_solicitante as comentario_solicitud
            FROM version_documento vd
            LEFT JOIN flujo_aprobacion_documento fad ON vd.id = fad.id_version_documento
            JOIN documento doc ON doc.id=vd.id_documento
            WHERE doc.codcas=:codcas AND vd.date_delete IS NULL""", nativeQuery = true)
    List<VersionDocumentoDto> getVersionDocumentoByCodcas(@Param("codcas")  String codcas);

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
            vd.date_delete,
            NULL as archivo,
            vd.visible,
            vd.id_user_creation,
            vd.id_documento,
            fad.id_user_aprobador,
            fad.prioridad,
            fad.comentario_solicitante as comentario_solicitud
            FROM version_documento vd
            LEFT JOIN flujo_aprobacion_documento fad ON vd.id = fad.id_version_documento
            JOIN documento doc ON doc.id=vd.id_documento
            WHERE doc.codred=:codred AND vd.date_delete IS NULL""", nativeQuery = true)
    List<VersionDocumentoDto> getVersionDocumentoByCodred(@Param("codred")  String codred);



}
