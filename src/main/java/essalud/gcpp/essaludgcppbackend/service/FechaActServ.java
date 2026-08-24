package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.repositories.FechaActRep;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FechaActServ {

    private final FechaActRep repository;

    public FechaActServ(FechaActRep repository) {
        this.repository = repository;
    }

    public Map<String, Object> queryFA() {
        return  repository.queryFA();
    }

}
