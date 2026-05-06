package essalud.gcpp.essaludgcppbackend.repositories.usuario;

import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {
    Users findByUsername(String username);

    Users findByEmail(String email);

    Users findByDni(String dni);

}
