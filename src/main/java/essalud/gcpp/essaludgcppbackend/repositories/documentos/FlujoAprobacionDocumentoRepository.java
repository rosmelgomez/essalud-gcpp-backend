package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.FlujoAprobacionDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlujoAprobacionDocumentoRepository extends JpaRepository<FlujoAprobacionDocumento, Integer> {

    @Query(value = "SELECT fad.* \n" +
            "FROM flujo_aprobacion_documento fad\n" +
            "WHERE fad.id_user_aprobador =:id", nativeQuery = true)
    List<FlujoAprobacionDocumento> findByUser(@Param("id") Integer id);
}
