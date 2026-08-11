package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.AuditoriaDocumentaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AuditoriaDocumentariaRep extends JpaRepository<AuditoriaDocumentaria, Long> {


    @Query(value = """
            SELECT
                ad.id AS id,
                ad.accion AS accion,
                ad.ip_address AS ip_address,
                ad.user_agent AS user_agent,
                ad.fecha_accion AS fecha_accion,
                d.codigo AS documento_codigo,
                d.titulo AS documento_titulo,
                vd.nombre AS version_nombre,
                vd.version AS version_numero,
                CONCAT(u.name, ' ', u.lastname) AS usuario_nombre,
                u.username AS usuario_username
            FROM auditoria_documentaria ad
            LEFT JOIN documento d ON ad.id_documento = d.id
            LEFT JOIN version_documento vd ON ad.id_version_documento = vd.id
            LEFT JOIN users u ON ad.id_usuario = u.id
            ORDER BY ad.fecha_accion DESC""", nativeQuery = true)
    List<Map<String, Object>> getAuditoriaConDetalles();
}
