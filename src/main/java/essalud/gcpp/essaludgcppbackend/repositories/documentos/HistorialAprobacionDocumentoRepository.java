package essalud.gcpp.essaludgcppbackend.repositories.documentos;

import essalud.gcpp.essaludgcppbackend.model.documentos.HistorialAprobacionDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialAprobacionDocumentoRepository extends JpaRepository<HistorialAprobacionDocumento, Integer> {
    
    @Query("SELECT h FROM HistorialAprobacionDocumento h WHERE h.solicitud_aprobacion.id = :flujoId ORDER BY h.fecha_accion DESC")
    List<HistorialAprobacionDocumento> findByFlujoIdOrderByFechaDesc(@Param("flujoId") Integer flujoId);
}
