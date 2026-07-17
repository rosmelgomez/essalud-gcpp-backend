package essalud.gcpp.essaludgcppbackend.repositories.acceso;

import essalud.gcpp.essaludgcppbackend.model.SolicitudAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SolicitudAccesoRep extends JpaRepository<SolicitudAcceso, Integer> {

    @Query(value = """
            SELECT 
                sa.id, 
                us.name, 
                us.lastname, 
                us.dni, 
                sa.codcas_solicitud, 
                sa.code_red_solicitud, 
                us.email, 
                sa.rol_solicitud, 
                sa.descripcion, 
                sa.date_creation, 
                sa.date_update, 
                sa.status,
                rsa.comentario_revisor,
                rsa.date_resolution,
                CONCAT(rev.name, ' ', rev.lastname) AS nombre_revisor
            FROM solicitud_acceso sa
            JOIN users us ON sa.id_user = us.id
            LEFT JOIN resolucion_solicitud_acceso rsa ON sa.id = rsa.id_solicitud
            LEFT JOIN users rev ON rsa.id_user_revisor = rev.id
            ORDER BY sa.date_creation DESC""", nativeQuery = true)
    List<Map<String, Object>> listSolicitudes();
}
