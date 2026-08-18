package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardSegPaciRep;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardSegPaciServ {

    private final DashboardSegPaciRep repositorio;

    public DashboardSegPaciServ(DashboardSegPaciRep repositorio) {
        this.repositorio = repositorio;
    }


    /// Query 1 atenciones
    public List<Map<String, Object>> query1SPA(String doc, String tipo_doc){
        return  repositorio.query1SPA(doc, tipo_doc);
    }

    /// Query 2 atenciones
    public List<Map<String, Object>> query2SPA(String doc, String tipo_doc){
        return   repositorio.query2SPA(doc, tipo_doc);
    }


}
