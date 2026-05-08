package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.dtos.documento.DocumentoDto;
import essalud.gcpp.essaludgcppbackend.model.documentos.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Integer> {
    @Query(value = "SELECT td.requiere_aprobacion\n" +
            "FROM documento d\n" +
            "JOIN tipo_documento td on d.id_tipo_documento = td.id\n" +
            "WHERE d.id=:id", nativeQuery = true )
    Boolean requiere_aprobacion(@Param("id") Integer id);


    List<Documento> getDocumentosByCodcas(String codcas);

    List<Documento> getDocumentosByCodred(String codred);

    @Query(value = "SELECT d.*\n" +
            "FROM documento d\n" +
            "WHERE d.id_user_creation=:iduser", nativeQuery = true)
    List<Documento> getDocumentoByUser (@Param("iduser") Integer iduser);


}
