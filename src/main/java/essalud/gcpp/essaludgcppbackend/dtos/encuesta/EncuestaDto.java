package essalud.gcpp.essaludgcppbackend.dtos.encuesta;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class EncuestaDto {
    private Integer id;
    private String codcas;
    private String status;
    private String comment;
    private LocalDateTime date_creation;
    private Integer id_user;
    private Integer id_modulo;
    private Integer id_categoria;
    private Integer id_subcategoria;
    private Integer id_variable;
}
