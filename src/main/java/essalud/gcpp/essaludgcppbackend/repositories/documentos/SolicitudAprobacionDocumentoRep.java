package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.SolicitudAprobacionDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface SolicitudAprobacionDocumentoRep extends JpaRepository<SolicitudAprobacionDocumento, Integer> {

        @Query(value = """
                        SELECT
                        sad.id as id_flujo,
                        vd.nombre as nombre_version,
                        vd.descripcion as descripcion_version,
                        vd.version,
                        vd.date_creation as data_creation_version,
                        vd.date_update as data_update_version,
                        vd.id_documento,
                        us.name as name_creation_version,
                        us.lastname as lastname_creation_version,
                        sad.comentario_solicitante as comentario,
                        sad.estado as status,
                        usa.name as name_aprobador,
                        usa.lastname as lastanme_aprobador
                        FROM version_documento vd
                        LEFT JOIN documento doc ON vd.id_documento=doc.id
                        LEFT JOIN usuario us ON vd.id_user_creation = us.id
                        LEFT JOIN solicitud_aprobacion_documento sad ON vd.id = sad.id_version_documento
                        LEFT JOIN usuario usa ON sad.id_usuario_aprobador = usa.id
                        WHERE sad.id_usuario_aprobador=:id""", nativeQuery = true)
        List<Map<String, Object>> findByUser(@Param("id") Integer id);

        @Query(value = """
                        SELECT s.*
                        FROM solicitud_aprobacion_documento s
                        WHERE s.id_version_documento = :versionId""", nativeQuery = true)
        List<SolicitudAprobacionDocumento> findByVersionId(@Param("versionId") Integer versionId);
}
