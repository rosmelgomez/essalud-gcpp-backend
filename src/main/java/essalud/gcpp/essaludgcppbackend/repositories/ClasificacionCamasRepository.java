package essalud.gcpp.essaludgcppbackend.repositories;

import essalud.gcpp.essaludgcppbackend.model.homologacion.ClasificacionCamas;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClasificacionCamasRepository extends CrudRepository<ClasificacionCamas, Integer> {

    @Query(value = """
        SELECT *
        FROM public.clasificacion_camas
        WHERE cenasicod =:cenasicod
        AND camcod =:camcod
        AND arehoscod =:arehoscod
        AND habcod =:habcod
        AND servhoscod =:servhoscod""", nativeQuery = true)
    ClasificacionCamas getClasificacion(@Param("cenasicod") String cenasicod, @Param("camcod") String camcod, @Param("arehoscod") String arehoscod, @Param("habcod") String habcod, @Param("servhoscod") String servhoscod );
}
