package essalud.gcpp.essaludgcppbackend.dtos.encuesta;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor

public class VariableDto {
    private Integer id;
    private String name_variable;
    private Boolean active;
    private Integer id_subcategoria;
}
