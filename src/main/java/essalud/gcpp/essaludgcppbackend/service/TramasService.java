package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.repositories.TramasRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TramasService {

    private final TramasRepository tramasRepository;
    public TramasService(TramasRepository tramasRepository) {
        this.tramasRepository = tramasRepository;
    }

    public List<Map<String, Object>> queryTablaB1(String fi, String ff, String codcas){
       return tramasRepository.queryTablaB1(fi,ff,codcas);
    }

    public List<Map<String, Object>> queryTablaB2(String fi, String ff, String codcas){
        return tramasRepository.queryTablaB2(fi,ff,codcas);
    }

}
