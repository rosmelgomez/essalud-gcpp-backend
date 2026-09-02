package essalud.gcpp.essaludgcppbackend.dtos.tmi;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class estandaresDto {
    private Integer id;
    private String orden_indicador;
    private String codigo_unico;
    private String tipo;
    private String indicador;
    private String nivel_centro;
    private String tipo_centro;
    private String nivel_estandar_centro;
    private float estandar;
    private float acept;
    private float aceptmenos;

}
