package essalud.gcpp.essaludgcppbackend.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor

public class ReporteadorDto {
    private int anio;
    private int periodo;
    private String redasiscod;
    private String cod_centro;
    private String cod_servicio;
    private String cod_actividad;
    private String cod_subactividad;
    private String sexo;
    private String capitulo;
    private String variable;
}
