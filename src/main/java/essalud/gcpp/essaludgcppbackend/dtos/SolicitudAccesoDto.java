package essalud.gcpp.essaludgcppbackend.dtos;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SolicitudAccesoDto {

    private Integer id;

    private String name;
    private String lastname;
    private String dni;
    private String codcas;
    private String code_red;
    private String email;

    ///Solicitud Acceso
    private String rol_solicitud;
    private String descripcion;
    private LocalDateTime date_creation;
    private LocalDateTime date_update;
    private String status;


}
