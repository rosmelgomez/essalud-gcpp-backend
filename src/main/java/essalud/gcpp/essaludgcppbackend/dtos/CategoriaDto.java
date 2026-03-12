package essalud.gcpp.essaludgcppbackend.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoriaDto {
    private Integer id;
    private String name_categoria;
    private Boolean active;
    private Integer id_modulo;
}
