package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.dtos.ClasificacionCamasDto;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.TablasHomolagadasService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/tablas-homologadas")
public class TablasHomologadasController {

    private final TablasHomolagadasService THS;
    public TablasHomologadasController(TablasHomolagadasService THS) {
        this.THS = THS;
    }

    ///Tabla Homologada Emergencia
    @GetMapping("/queryTHE/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<Map<String, Object>> queryTHE(@PathVariable String codcas) {
        return THS.queryTHE(codcas);
    }

    ///Tabla homologada consulta externa
    @GetMapping("/queryTHCE")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<Map<String, Object>> queryTHCE() {
        return THS.queryTHCE();
    }

    ///Tabla homologada camas
    @GetMapping("/queryTHCAMA/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<Map<String, Object>> queryTHCAMA(@PathVariable String codcas) {
        return THS.queryTHCAMA(codcas);
    }

    ///clasificar camas
    @PostMapping("/clasificar-cama")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_RED', 'USER')")
    public void clasificarCama(@RequestBody ClasificacionCamasDto clasificacion) {
        THS.insertClasificacionCamas(clasificacion);
    }

    @PutMapping("/update-clasificacion-cama")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'ADMIN_RED', 'USER')")
    public  void updateClasificacionCamas(@RequestBody ClasificacionCamasDto clasificacion) {
        THS.updateClasificacionCamas(clasificacion);
    }

    /// servicio
    @GetMapping("/queryTHCAMASERV")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<Map<String, Object>> queryTHCAMASERV() {
        return THS.queryTHCAMASERV();
    }



}
