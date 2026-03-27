package essalud.gcpp.essaludgcppbackend.dtos;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SolicitudAccesoDto {

    private String name;
    private String lastname;
    private String dni;
    private String codcas;
    private String code_red;
    private String email;

    ///Solicitud Acceso

    private String descripcion;
    private LocalDateTime date_creation;
    private LocalDateTime date_update;
    private String status;


}
