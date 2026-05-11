package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.repositories.EstablecimientosAsistencialesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EstablecimientosAsistencialesService {

    private final EstablecimientosAsistencialesRepository EAR;

    public EstablecimientosAsistencialesService(EstablecimientosAsistencialesRepository EAR) {
        this.EAR = EAR;
    }

    public List<Map<String, Object>> getCentros() {
        return this.EAR.getCentros();
    }

    public List<Map<String, Object>> getCentrosByCodeRed(String code_red) {
        return this.EAR.getCentrosByCodeRed(code_red);
    }

    public List<Map<String, Object>> getRedes(){
        return this.EAR.getRedes();
    }

    public List<Map<String, Object>>getRedesWithCentros(){
        return this.EAR.getRedesWithCentros();
    }

    public Map<String, Object>getredeswithcentrosbycodcas(String codcas) {
        return this.EAR.getredeswithcentrosbycodcas(codcas);
    }
}
