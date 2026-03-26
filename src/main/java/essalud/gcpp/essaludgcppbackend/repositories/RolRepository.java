package essalud.gcpp.essaludgcppbackend.repositories;

import essalud.gcpp.essaludgcppbackend.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    @Query(value = "SELECT r.* " +
            "FROM user_rol ur " +
            "JOIN rol r ON ur.rol_id = r.id " +
            "JOIN users u ON ur.user_id = u.id " +
            "WHERE u.username = :username", nativeQuery = true)
    List<Rol> listaRolesByUsername(@Param("username") String username);
}
