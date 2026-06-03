package essalud.gcpp.essaludgcppbackend.dtos.documento;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DocumentoDto {
    private Integer id;
    private String codigo;
    private String titulo;
    private String descripcion;
    private String codcas;
    private String codred;
    private Boolean publico;
    private Boolean solo_mi_red;
    private LocalDateTime date_creation;
    private LocalDateTime date_update;
    private Boolean flag;
    private Integer id_user_creation;
    private Integer id_tipo_documento;
}
