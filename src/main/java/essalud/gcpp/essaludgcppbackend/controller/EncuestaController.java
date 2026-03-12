package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.dtos.*;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/encuesta")
public class EncuestaController {
    private final ModuloService MS;
    private final CategoriaService CS;
    private final SubCategoriaService SCS;
    private final VariableService VARS;
    private final EncuestaService ES;

    public EncuestaController(ModuloService MS, CategoriaService CS, SubCategoriaService SCS, VariableService VARS, EncuestaService ES) {
        this.MS = MS;
        this.CS = CS;
        this.SCS = SCS;
        this.VARS = VARS;
        this.ES = ES;
    }

    @GetMapping("/getModulos")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<ModuloDto> getModulos() {
        return MS.getModulos();
    }

    @GetMapping("/getCategorias")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<CategoriaDto> getCategorias() {
        return CS.findAll();
    }
    @GetMapping("/getSubcategorias")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<SubCategoriaDto> getSubcategorias() {
        return SCS.getSubCategorias();
    }

    @GetMapping("/getVariables")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public List<VariableDto> getVariables() {
        return VARS.getVariables();
    }

    @GetMapping("/getEncuestas")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<EncuestaDto> getEncuestas() {
        return ES.getEncuestas();
    }

    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public void registerEncuesta(@RequestBody EncuestaDto encuesta){
        ES.registrarEncuesta(encuesta);
    }
}
