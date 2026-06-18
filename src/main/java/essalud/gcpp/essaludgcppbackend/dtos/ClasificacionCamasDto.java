package essalud.gcpp.essaludgcppbackend.dtos;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClasificacionCamasDto {
    private Integer id;
    private String codcas;
    private String camcod;
    private String arehoscod;
    private String habcod;
    private String servhoscod;
    private String descripcion;
}
