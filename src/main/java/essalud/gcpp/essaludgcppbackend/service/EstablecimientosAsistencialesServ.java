package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.repositories.EstablecimientosAsistencialesRep;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EstablecimientosAsistencialesServ {

    private final EstablecimientosAsistencialesRep EAR;

    public EstablecimientosAsistencialesServ(EstablecimientosAsistencialesRep EAR) {
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

    public Map<String, Object>getredwithcentrosbycodcas(String codcas) {
        return this.EAR.getredwithcentrosbycodcas(codcas);
    }
}
