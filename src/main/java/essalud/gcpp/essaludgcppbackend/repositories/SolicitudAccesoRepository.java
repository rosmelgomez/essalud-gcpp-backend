package essalud.gcpp.essaludgcppbackend.repositories;

import essalud.gcpp.essaludgcppbackend.model.SolicitudAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SolicitudAccesoRepository extends JpaRepository<SolicitudAcceso, Integer> {

    @Query(value = "SELECT sa.id, us.name, us.lastname, us.dni, sa.codcas_solicitud, sa.code_red_solicitud, us.email, sa.rol_solicitud, sa.descripcion, sa.date_creation , sa.date_update, sa.status\n" +
            "FROM solicitud_acceso sa\n" +
            "JOIN users us on sa.id_user = us.id\n" +
            "WHERE sa.status = 'PENDIENTE'", nativeQuery = true)
    List<Map<String, Object>> listSolicitudes();
}
