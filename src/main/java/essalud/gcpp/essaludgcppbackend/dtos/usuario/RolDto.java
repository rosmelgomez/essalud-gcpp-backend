package essalud.gcpp.essaludgcppbackend.dtos.usuario;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class RolDto {

    private Integer id;

    private String name;

    private LocalDateTime date_creation;

    private LocalDateTime date_update;

    private String description;

    private Boolean flag;
}
