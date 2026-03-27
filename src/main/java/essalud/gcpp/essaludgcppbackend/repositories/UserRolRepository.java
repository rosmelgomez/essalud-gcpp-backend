package essalud.gcpp.essaludgcppbackend.repositories;

import essalud.gcpp.essaludgcppbackend.model.UserRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRolRepository extends JpaRepository<UserRol, Integer> {

    @Query (value = """
    SELECT ur.*
    FROM user_rol ur
    JOIN users u ON ur.user_id = u.id
    WHERE u.id=:userId
    """, nativeQuery = true)
    List<UserRol> findByUserId(@Param("userId")Integer userId);
}
