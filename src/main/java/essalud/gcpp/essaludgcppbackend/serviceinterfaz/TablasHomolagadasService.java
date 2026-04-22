package essalud.gcpp.essaludgcppbackend.serviceinterfaz;


import essalud.gcpp.essaludgcppbackend.repositories.TablasHomologadasRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TablasHomolagadasService {

    private final TablasHomologadasRepository THR;
    public TablasHomolagadasService(TablasHomologadasRepository THR) {
        this.THR = THR;
    }

    ///Tabla Homologada Emergencia
    public List<Map<String, Object>> queryTHE(String codcas) {
        return THR.queryTHE(codcas);
    }

    ///Tabla homologada consulta externa
    public List<Map<String, Object>> queryTHCE() {
        return THR.queryTHCE();
    }

}
