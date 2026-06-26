package essalud.gcpp.essaludgcppbackend.service;


import essalud.gcpp.essaludgcppbackend.dtos.ClasificacionCamasDto;
import essalud.gcpp.essaludgcppbackend.model.homologacion.ClasificacionCamas;
import essalud.gcpp.essaludgcppbackend.repositories.ClasificacionCamasRepository;
import essalud.gcpp.essaludgcppbackend.repositories.TablasHomologadasRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TablasHomolagadasService {

    private final TablasHomologadasRepository THR;
    private final ClasificacionCamasRepository CCR;
    public TablasHomolagadasService(TablasHomologadasRepository THR,  ClasificacionCamasRepository CCR) {
        this.THR = THR;
        this.CCR = CCR;
    }

    ///Tabla Homologada Emergencia
    public List<Map<String, Object>> queryTHE(String codcas) {
        return THR.queryTHE(codcas);
    }

    ///Tabla homologada consulta externa
    public List<Map<String, Object>> queryTHCE() {
        return THR.queryTHCE();
    }

    ///Tabla homologada camas
    public List<Map<String, Object>> queryTHCAMA(String codcas) {

        List<Map<String, Object>> cams = THR.queryTHCAMA(codcas);
        List<Map<String, Object>> new_cams = new ArrayList<>();

        for (Map<String, Object> cam : cams) {
            String camcod = cam.get("camcod").toString();
            String arehoscod = cam.get("arehoscod").toString();
            String habcod = cam.get("habcod").toString();
            String servhoscod = cam.get("servhoscod").toString();

            ClasificacionCamas clasificacion= this.CCR.getClasificacion(codcas, camcod, arehoscod, habcod, servhoscod);
            if(clasificacion!=null){
                cam.put("id_clasificacion", clasificacion.getId());
                cam.put("descripcion_clasificacion", clasificacion.getDescripcion());
            } else {
                cam.put("id_clasificacion", null);
                cam.put("descripcion_clasificacion", null);
            }
            new_cams.add(cam);
        }

        return new_cams;
    }

    ///clasificar camas
    public void insertClasificacionCamas(ClasificacionCamasDto clasificacion) {
        ClasificacionCamas clasificacionCamas = new ClasificacionCamas();
        insertUpdateClasificacionCamas(clasificacionCamas, clasificacion);
    }

    ///modificar clasificaciones por camas
    public void updateClasificacionCamas(ClasificacionCamasDto clasificacion) {
        CCR.findById(clasificacion.getId()).ifPresent(clasificacionCamas -> insertUpdateClasificacionCamas(clasificacionCamas, clasificacion));
    }

    /// agregar o modificar
    private void insertUpdateClasificacionCamas(ClasificacionCamas clasificacionCamas, ClasificacionCamasDto clasificacion){
        clasificacionCamas.setCamcod(clasificacion.getCamcod());
        clasificacionCamas.setArehoscod(clasificacion.getArehoscod());
        clasificacionCamas.setHabcod(clasificacion.getHabcod());
        clasificacionCamas.setServhoscod(clasificacion.getServhoscod());
        clasificacionCamas.setCenasicod(clasificacion.getCenasicod());
        clasificacionCamas.setDescripcion(clasificacion.getDescripcion());
        CCR.save(clasificacionCamas);
    }

    /// servicio
    public List<Map<String, Object>> queryTHCAMASERV() {
        return THR.queryTHCAMASERV();
    }

}
