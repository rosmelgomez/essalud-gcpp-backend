package essalud.gcpp.essaludgcppbackend.repositories.usuario;

import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRep extends JpaRepository<Users,Integer> {
    Users findByUsername(String username);

    Users findByEmail(String email);

    Users findByDni(String dni);

    @Query(value = "SELECT us.*\n" +
            "FROM users us\n" +
            "JOIN user_rol ur ON ur.id_user =us.id\n" +
            "JOIN rol r ON r.id =ur.id_rol\n" +
            "WHERE us.codred=:codred  and r.id = 2", nativeQuery = true)
    List<Users> findByCodred (@Param("codred") String codred);

    @Query(value = "SELECT us.*\n" +
            "FROM users us\n" +
            "JOIN user_rol ur ON ur.id_user =us.id\n" +
            "JOIN rol r ON r.id =ur.id_rol\n" +
            "WHERE us.codcas=:codcas  and r.id = 3", nativeQuery = true)
    List<Users> findByCodcas (@Param("codcas") String codcas);
}
