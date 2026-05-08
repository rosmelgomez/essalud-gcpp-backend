package essalud.gcpp.essaludgcppbackend.dtos.documento;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TipoDocumentoDto {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String extensiones_permitidas;
    private Boolean requiere_aprobacion;
    private Integer max_size_mb;
    private Boolean flag;
    private LocalDateTime date_creation;
    private LocalDateTime date_update;
}
