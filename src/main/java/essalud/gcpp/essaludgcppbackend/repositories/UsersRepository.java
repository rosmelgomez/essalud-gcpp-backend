package essalud.gcpp.essaludgcppbackend.repositories;

import essalud.gcpp.essaludgcppbackend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,Integer> {
    Users findByUsername(String username);
}
