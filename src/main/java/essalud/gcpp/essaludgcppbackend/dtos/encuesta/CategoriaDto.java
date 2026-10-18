package essalud.gcpp.essaludgcppbackend.dtos.encuesta;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor

public class CategoriaDto {
    private Integer id;
    private String name_categoria;
    private Boolean active;
    private Integer id_modulo;
}
