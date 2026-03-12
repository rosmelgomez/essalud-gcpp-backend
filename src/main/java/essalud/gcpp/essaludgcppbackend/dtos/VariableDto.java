package essalud.gcpp.essaludgcppbackend.dtos;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VariableDto {
    private Integer id;
    private String name_variable;
    private Boolean active;
    private Integer id_subcategoria;
}
