package essalud.gcpp.essaludgcppbackend.repositories.usuario;

import essalud.gcpp.essaludgcppbackend.model.usuario.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolRep extends JpaRepository<Rol, Integer> {

    @Query(value = """
    SELECT r.*
    FROM user_rol ur
    JOIN rol r ON ur.id_rol = r.id
    JOIN users u ON ur.id_user = u.id
    WHERE r.flag = true and ur.flag = true and u.username = :username
    """, nativeQuery = true)
    List<Rol> listaRolesByUsername(@Param("username") String username);

    @Query (value = """
    SELECT *
    FROM  rol
    WHERE name =:name
    """, nativeQuery = true)
    Rol findRol(@Param("name") String name);

}
