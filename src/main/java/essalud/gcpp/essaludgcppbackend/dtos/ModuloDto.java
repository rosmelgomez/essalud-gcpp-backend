package essalud.gcpp.essaludgcppbackend.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ModuloDto {
    private Integer id;
    private String name_modulo;
    private Boolean active;
}
