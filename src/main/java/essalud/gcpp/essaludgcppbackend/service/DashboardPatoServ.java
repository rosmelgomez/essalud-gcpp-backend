package essalud.gcpp.essaludgcppbackend.service;

import essalud.gcpp.essaludgcppbackend.repositories.DashboardPatoRep;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardPatoServ {

    private final DashboardPatoRep repository;

    public DashboardPatoServ(DashboardPatoRep repository) {
        this.repository = repository;
    }

    /// opcion patologias
    public List<Map<String, Object>> queryOP() {
        return repository.queryOP();
    }

    /// opcion anio
    public List<Map<String, Object>> queryOA() {
        return repository.queryOA();
    }

    ///opcion red centro
    public List<Map<String, Object>> queryORC() {
        return repository.queryORC();
    }


    ///Analitica por patologia de alto costo
    /// pacientes totales
    public List<Map<String, Object>> queryAPACPT() {
        return repository.queryAPACPT();
    }

    ///comorbilidad global
    public List<Map<String, Object>> queryAPACCGB() {
        return  repository.queryAPACCGB();
    }

    ///comorbilidad grupo
    public List<Map<String, Object>> queryAPACCGP(String patologia, String patron) {
        return repository.queryAPACCGP(patologia, patron);
    }

    ///comorbilidad grupo total
    public List<Map<String, Object>> queryAPACCGPT(String patologia) {
        return repository.queryAPACCGPT(patologia);
    }

    ///comorbilidad interserccion
    public List<Map<String, Object>> queryAPACCI() {
        return repository.queryAPACCI();
    }

    ///comorbilidad interserccion total
    public List<Map<String, Object>> queryAPACCIT() {
        return repository.queryAPACCIT();
    }

    ///Diagnostico y servicios mas frecuentes por patologia
    public List<Map<String, Object>> queryAPACPAS(List<String> anios) {
        return repository.queryAPACPAS(anios);
    }

    /// diagnostico y servicios mas frecuente por patologia
    public List<Map<String, Object>> queryAPACDSFP(List<String> anios) {
        return repository.queryAPACDSFP(anios);
    }

    ///total pacientes
    public List<Map<String, Object>> queryAPACTP(List<String> anios){
        return repository.queryAPACTP(anios);
    }

    ///mayor concentracion red
    public List<Map<String, Object>> queryAPACMCR(List<String> anios){
        return repository.queryAPACMCR(anios);
    }

    ///mayor concentrado centro
    public List<Map<String, Object>> queryAPACMCC(List<String> anios){
        return repository.queryAPACMCC(anios);
    }

    ///ranking red
    public List<Map<String, Object>> queryAPACRR(List<String> anios){
        return repository.queryAPACRR(anios);
    }

    /// Detalle por patología
    /// total de pacientes
    public List<Map<String, Object>> queryDPTP(List<String> patologias, List<String> anios, List<String> codreds, List<String> codcens, List<String> grupo_edades ) {
        return repository.queryDPTP(patologias, anios, codreds, codcens, grupo_edades);
    }


    }
