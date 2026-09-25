package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface DocumentoRep extends JpaRepository<Documento, Integer> {
        @Query(value = """
                        SELECT td.requiere_aprobacion
                        FROM documento d
                        JOIN tipo_documento td on d.id_tipo_documento = td.id
                        WHERE d.id=:id""", nativeQuery = true)
        Boolean requiere_aprobacion(@Param("id") Integer id);

        @Query(value = """
                        SELECT doc.*
                        FROM documento doc
                        JOIN version_documento vd ON doc.id = vd.id_documento
                        WHERE vd.id =:idversion""", nativeQuery = true)
        Documento getDocumentoByIdVersion(@Param("idversion") Integer idversion);

        @Query(value = """
                        SELECT
                            d.id AS id,
                            d.codigo AS codigo,
                            d.titulo AS titulo,
                            d.descripcion AS descripcion,
                            d.codcas AS codcas,
                            d.codred AS codred,
                            d.publico AS publico,
                            d.solo_mi_red AS solo_mi_red,
                            d.date_creation AS date_creation,
                            d.date_update AS date_update,
                            d.flag AS flag,
                            d.id_user_creation AS id_user_creation,
                            CONCAT(u.name, ' ', u.lastname) AS nombre_usuario_creador,
                            d.id_tipo_documento AS id_tipo_documento
                        FROM documento d
                        LEFT JOIN usuario u ON d.id_user_creation = u.id
                        WHERE d.flag = true AND d.date_delete IS NULL""", nativeQuery = true)
        List<Map<String, Object>> getDocumentosConDetalles();

        @Query(value = """
                        SELECT
                            d.id AS id,
                            d.codigo AS codigo,
                            d.titulo AS titulo,
                            d.descripcion AS descripcion,
                            d.codcas AS codcas,
                            d.codred AS codred,
                            d.publico AS publico,
                            d.solo_mi_red AS solo_mi_red,
                            d.date_creation AS date_creation,
                            d.date_update AS date_update,
                            d.flag AS flag,
                            d.id_user_creation AS id_user_creation,
                            CONCAT(u.name, ' ', u.lastname) AS nombre_usuario_creador,
                            d.id_tipo_documento AS id_tipo_documento
                        FROM documento d
                        LEFT JOIN usuario u ON d.id_user_creation = u.id
                        WHERE d.codred = :codred AND d.flag = true AND d.date_delete IS NULL""", nativeQuery = true)
        List<Map<String, Object>> getDocumentosConDetallesByCodred(@Param("codred") String codred);

        @Query(value = """
                        SELECT
                            d.id AS id,
                            d.codigo AS codigo,
                            d.titulo AS titulo,
                            d.descripcion AS descripcion,
                            d.codcas AS codcas,
                            d.codred AS codred,
                            d.publico AS publico,
                            d.solo_mi_red AS solo_mi_red,
                            d.date_creation AS date_creation,
                            d.date_update AS date_update,
                            d.flag AS flag,
                            d.id_user_creation AS id_user_creation,
                            CONCAT(u.name, ' ', u.lastname) AS nombre_usuario_creador,
                            d.id_tipo_documento AS id_tipo_documento
                        FROM documento d
                        LEFT JOIN usuario u ON d.id_user_creation = u.id
                        WHERE d.codcas = :codcas AND d.flag = true AND d.date_delete IS NULL""", nativeQuery = true)
        List<Map<String, Object>> getDocumentosConDetallesByCodcas(@Param("codcas") String codcas);

        @Query(value = """
                        SELECT
                            d.id AS id,
                            d.codigo AS codigo,
                            d.titulo AS titulo,
                            d.descripcion AS descripcion,
                            d.codcas AS codcas,
                            d.codred AS codred,
                            d.publico AS publico,
                            d.solo_mi_red AS solo_mi_red,
                            d.date_creation AS date_creation,
                            d.date_update AS date_update,
                            d.flag AS flag,
                            d.id_user_creation AS id_user_creation,
                            CONCAT(u.name, ' ', u.lastname) AS nombre_usuario_creador,
                            d.id_tipo_documento AS id_tipo_documento
                        FROM documento d
                        LEFT JOIN usuario u ON d.id_user_creation = u.id
                        WHERE d.id_user_creation = :iduser AND d.flag = true AND d.date_delete IS NULL""", nativeQuery = true)
        List<Map<String, Object>> getDocumentosConDetallesByUser(@Param("iduser") Integer iduser);
}
