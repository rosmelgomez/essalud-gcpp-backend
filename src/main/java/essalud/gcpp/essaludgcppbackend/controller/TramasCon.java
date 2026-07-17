package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.service.TramasServ;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tramas")
public class TramasCon {

    private final TramasServ tramasServ;
    public TramasCon(TramasServ tramasServ) {
        this.tramasServ = tramasServ;
    }

    @GetMapping("/queryTablaB1/{fi}/{ff}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<Map<String, Object>>  queryTablaB1(@PathVariable String fi, @PathVariable String ff, @PathVariable String codcas ){
        return tramasServ.queryTablaB1(fi,ff,codcas);
    }

    @GetMapping("/queryTablaB2/{fi}/{ff}/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<Map<String, Object>>  queryTablaB2(@PathVariable String fi, @PathVariable String ff, @PathVariable String codcas ){
        return tramasServ.queryTablaB2(fi,ff,codcas);
    }

}
