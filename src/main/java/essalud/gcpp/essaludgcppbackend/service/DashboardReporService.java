package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.dtos.ReporteadorDto;
import essalud.gcpp.essaludgcppbackend.repositories.DashboardReporRespository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardReporService {

    private final DashboardReporRespository DRR;
    public DashboardReporService(DashboardReporRespository DRR) {
        this.DRR = DRR;
    }

    /// Dashboard Reporteador Red
    public List<Map<String, Object>> queryDRR(){
        return DRR.queryDRR();
    }
    /// Dashboard Reporteador centro
    public List<Map<String, Object>> queryDRC(){
        return DRR.queryDRC();
    }
    /// Dashboard Reporteador servicio
    public List<Map<String, Object>> queryDRS(){
        return DRR.queryDRS();
    }
    /// Dashboard Reporteador actividad
    public List<Map<String, Object>> queryDRAC(){
        return DRR.queryDRAC();
    }
    /// Dashboard Reporteador  subactividad
    public List<Map<String, Object>> queryDRSUBAC(){
        return DRR.queryDRSUBAC();
    }
    /// Dashboard Reporteador capitulo
    public List<Map<String, Object>> queryDRCAP(){
        return DRR.queryDRCAP();
    }
    /// Dashboard Reporteador variable
    public List<Map<String, Object>> queryDRVAR(){
        return DRR.queryDRVAR();
    }
    /// Dashboard Reporteador query
    public List<Map<String, Object>> queryDRQ(ReporteadorDto repDto){
        return DRR.queryDRQ(repDto);
    }
}
