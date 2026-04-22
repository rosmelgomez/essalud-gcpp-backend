package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.serviceinterfaz.TablasHomolagadasService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("queryTHE/{codcas}")
    public List<Map<String, Object>> queryTHE(@PathVariable String codcas) {
        return THS.queryTHE(codcas);
    }

    ///Tabla homologada consulta externa
    @GetMapping("/queryTHCE")
    public List<Map<String, Object>> queryTHCE() {
        return THS.queryTHCE();
    }



}
