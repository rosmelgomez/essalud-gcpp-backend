package essalud.gcpp.essaludgcppbackend.dtos.encuesta;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor

public class ModuloDto {
    private Integer id;
    private String name_modulo;
    private Boolean active;
}
