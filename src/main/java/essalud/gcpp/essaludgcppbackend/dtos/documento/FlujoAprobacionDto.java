package essalud.gcpp.essaludgcppbackend.dtos.documento;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FlujoAprobacionDto {

    private Integer id;

    private String comentario;

    private String status;

    private LocalDateTime date_creation;

    private LocalDateTime date_update;

    private Integer id_user_aprobador;

    private Integer id_version_documento;
}
