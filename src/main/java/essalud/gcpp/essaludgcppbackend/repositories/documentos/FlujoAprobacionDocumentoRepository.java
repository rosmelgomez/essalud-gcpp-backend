package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.FlujoAprobacionDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface FlujoAprobacionDocumentoRepository extends JpaRepository<FlujoAprobacionDocumento, Integer> {

    @Query(value = """
            SELECT
            fad.id as id_flujo,
            vd.nombre as nombre_version,
            vd.descripcion as descripcion_version,
            vd.version,
            vd.date_creation as data_creation_version,
            vd.date_update as data_update_version,
            vd.id_documento,
            us.name as name_creation_version,\s
            us.lastname as lastname_creation_version,
            fad.comentario,
            fad.status,
            usa.name as name_aprobador,
            usa.lastname as lastanme_aprobador
            FROM version_documento vd
            JOIN documento doc ON vd.id_documento=doc.id
            JOIN users us ON vd.id_user_creation = us.id
            JOIN flujo_aprobacion_documento fad ON vd.id = fad.id_version_documento
            JOIN users usa ON fad.id_user_aprobador = usa.id
            WHERE fad.id_user_aprobador=:id""", nativeQuery = true)
    List<Map<String, Object>>findByUser(@Param("id") Integer id);



}
