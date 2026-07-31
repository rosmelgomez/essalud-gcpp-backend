package essalud.gcpp.essaludgcppbackend.repositories.usuario;

import essalud.gcpp.essaludgcppbackend.model.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRep extends JpaRepository<Usuario,Integer> {
    Usuario findByUsername(String username);

    Usuario findByEmail(String email);

    Usuario findByDni(String dni);

    @Query(value = """
            SELECT us.*
            FROM usuario us
            JOIN usuario_rol ur ON ur.id_user =us.id
            JOIN rol r ON r.id =ur.id_rol
           WHERE us.codred=:codred  and r.id = 2
    """, nativeQuery = true)
    List<Usuario> findByCodred (@Param("codred") String codred);

    @Query(value = """
        SELECT us.*
        FROM usuario us
            JOIN usuario_rol ur ON ur.id_user =us.id
            JOIN rol r ON r.id =ur.id_rol
            WHERE us.codcas=:codcas  and r.id = 3
    """, nativeQuery = true)
    List<Usuario> findByCodcas (@Param("codcas") String codcas);
}
