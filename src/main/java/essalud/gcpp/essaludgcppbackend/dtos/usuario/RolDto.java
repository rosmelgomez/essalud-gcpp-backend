package essalud.gcpp.essaludgcppbackend.dtos.usuario;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RolDto {

    private Integer id;

    private String name;

    private LocalDateTime date_creation;

    private LocalDateTime date_update;

    private String description;

    private Boolean flag;
}
