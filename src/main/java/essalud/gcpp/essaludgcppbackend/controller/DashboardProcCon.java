package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.service.DashboardProcServ;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/procedimiento")
public class DashboardProcCon {

    private final DashboardProcServ dps;

    public DashboardProcCon(DashboardProcServ dps) {
        this.dps = dps;
    }

    @GetMapping("/queryDP/{anio}/{periodo}/{codcas}/{codasegu}/{codes}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<Map<String, Object>> queryDP(@PathVariable int anio, @PathVariable int periodo,@PathVariable String codcas, @PathVariable int codasegu, @PathVariable int codes) {
        return dps.queryDP(anio, periodo, codcas, codasegu, codes);
    }

}