package essalud.gcpp.essaludgcppbackend.repositories.acceso;

import essalud.gcpp.essaludgcppbackend.model.usuario.ResolucionSolicitudAcceso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResolucionSolicitudAccesoRep extends JpaRepository<ResolucionSolicitudAcceso, Integer> {
    Optional<ResolucionSolicitudAcceso> findBySolicitudAccesoId(Integer solicitudId);
}
