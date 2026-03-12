package essalud.gcpp.essaludgcppbackend.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubCategoriaDto {
    private Integer id;
    private String name_categoria;
    private Boolean active;
    private Integer id_categoria;
}
