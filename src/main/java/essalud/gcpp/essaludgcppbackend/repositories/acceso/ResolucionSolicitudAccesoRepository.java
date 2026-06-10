package essalud.gcpp.essaludgcppbackend.repositories.acceso;

import essalud.gcpp.essaludgcppbackend.model.ResolucionSolicitudAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResolucionSolicitudAccesoRepository extends JpaRepository<ResolucionSolicitudAcceso, Integer> {
    Optional<ResolucionSolicitudAcceso> findBySolicitudAccesoId(Integer solicitudId);
}
