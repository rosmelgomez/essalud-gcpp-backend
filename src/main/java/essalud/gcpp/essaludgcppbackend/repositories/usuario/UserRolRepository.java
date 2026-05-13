package essalud.gcpp.essaludgcppbackend.repositories.usuario;

import essalud.gcpp.essaludgcppbackend.model.usuario.UserRol;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRolRepository extends JpaRepository<UserRol, Integer> {

    @Query (value = """
    SELECT ur.*
    FROM user_rol ur
    JOIN users u ON ur.id_user = u.id
    WHERE u.id=:userId
    """, nativeQuery = true)
    List<UserRol> findByUserId(@Param("userId")Integer userId);

    @Modifying
    @Query("DELETE FROM UserRol ur WHERE ur.usuario_rol = :user")
    void deleteRolesByUser(@Param("user") Users user);

}
