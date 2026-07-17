package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.repositories.TramasRep;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TramasServ {

    private final TramasRep tramasRep;
    public TramasServ(TramasRep tramasRep) {
        this.tramasRep = tramasRep;
    }

    public List<Map<String, Object>> queryTablaB1(String fi, String ff, String codcas){
       return tramasRep.queryTablaB1(fi,ff,codcas);
    }

    public List<Map<String, Object>> queryTablaB2(String fi, String ff, String codcas){
        return tramasRep.queryTablaB2(fi,ff,codcas);
    }

}
