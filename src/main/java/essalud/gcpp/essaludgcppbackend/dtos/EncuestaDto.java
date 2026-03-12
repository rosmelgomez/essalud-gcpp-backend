package essalud.gcpp.essaludgcppbackend.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
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
