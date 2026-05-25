package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Integer> {
    @Query(value = """
            SELECT td.requiere_aprobacion
            FROM documento d
            JOIN tipo_documento td on d.id_tipo_documento = td.id
            WHERE d.id=:id""", nativeQuery = true )
    Boolean requiere_aprobacion(@Param("id") Integer id);

    @Query(value = "SELECT d FROM Documento d WHERE d.flag = true AND d.deletedAt IS NULL")
    List<Documento> findAllActive();

    @Query(value = "SELECT d FROM Documento d WHERE d.codcas = :codcas AND d.flag = true AND d.deletedAt IS NULL")
    List<Documento> getDocumentosByCodcas(@Param("codcas") String codcas);

    @Query(value = "SELECT d FROM Documento d WHERE d.codred = :codred AND d.flag = true AND d.deletedAt IS NULL")
    List<Documento> getDocumentosByCodred(@Param("codred") String codred);

    @Query(value = """
            SELECT d.*
            FROM documento d
            WHERE d.id_user_creation=:iduser AND d.flag = true AND d.deleted_at IS NULL""", nativeQuery = true)
    List<Documento> getDocumentoByUser (@Param("iduser") Integer iduser);

    @Query(value = """
            SELECT doc.*
            FROM documento doc
            JOIN version_documento vd ON doc.id = vd.id_documento
            WHERE vd.id =:idversion""", nativeQuery = true )
    Documento getDocumentoByIdVersion( @Param("idversion") Integer idversion);
}
