package essalud.gcpp.essaludgcppbackend.repositories.usuario;

import essalud.gcpp.essaludgcppbackend.model.usuario.UsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRolRep extends JpaRepository<UsuarioRol, Integer> {

    @Query (value = """
    SELECT ur.*
    FROM usuario_rol ur
    JOIN usuario u ON ur.id_user = u.id
    WHERE u.id=:userId
    """, nativeQuery = true)
    List<UsuarioRol> findByUserId(@Param("userId")Integer userId);



}
