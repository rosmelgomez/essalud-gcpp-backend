package essalud.gcpp.essaludgcppbackend.dtos.tmi;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class centroTmiDto {
    private Integer id;
    private String red;
    private String orden;
    private String orden_tipo;
    private String codcentro;
    private String tipo;
    private String nivel;
    private String nivel_estandar;
    private String centro;
    private String estado;

}
