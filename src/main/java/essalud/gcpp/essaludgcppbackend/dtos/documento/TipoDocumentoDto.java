package essalud.gcpp.essaludgcppbackend.dtos.documento;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class TipoDocumentoDto {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String extensiones_permitidas;
    private List<Integer> id_extensiones;
    private Boolean requiere_aprobacion;
    private Integer max_size_mb;
    private Boolean flag;
    private LocalDateTime date_creation;
    private LocalDateTime date_update;
}
