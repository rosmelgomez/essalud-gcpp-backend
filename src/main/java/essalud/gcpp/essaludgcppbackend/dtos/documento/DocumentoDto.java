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

    private String cod_red;

    private LocalDateTime date_creation;

    private LocalDateTime date_update;

    private String status;

    private Integer id_user_creation;

    private Integer id_tipo_documento;
}
