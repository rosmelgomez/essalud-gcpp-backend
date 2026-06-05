package essalud.gcpp.essaludgcppbackend.dtos.documento;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SolicitudAprobacionDocumentoDto {
    private Integer id;
    private String comentario;
    private String comentarioSolicitud;
    private String comentarioAprobador;
    private String status;
    private String prioridad;
    private LocalDateTime date_creation;
    private LocalDateTime date_update;
    private LocalDateTime fechaLimite;
    private LocalDateTime fechaResolucion;
    private Integer id_user_aprobador;
    private Integer id_user_solicitante;
    private Integer id_version_documento;
}
