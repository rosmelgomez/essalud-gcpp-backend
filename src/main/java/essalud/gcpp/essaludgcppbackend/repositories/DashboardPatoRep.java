package essalud.gcpp.essaludgcppbackend.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Collections;

@Repository
public class DashboardPatoRep {

    private final JdbcTemplate secondJdbcTemplate;

    public DashboardPatoRep(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
        this.secondJdbcTemplate = secondJdbcTemplate;
    }

    /// opcion patologias
    public List<Map<String, Object>> queryOP() {
        String tabla = "dssge.mv_ejec_pat_resumen";
        String sql = """
            SELECT DISTINCT patologia
            FROM %s
            WHERE patologia NOT IN ('TOTAL GENERAL', 'TOTAL CATALOGADO', 'SIN PATOLOGIA')
            ORDER BY patologia
            """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql);
    }

    /// opcion anio
    public List<Map<String, Object>> queryOA() {
        String tabla = "dssge.mv_ejec_pat_resumen";
        String sql = """
            SELECT DISTINCT anio
            FROM %s
            ORDER BY anio DESC
            """.formatted(tabla);
        return secondJdbcTemplate.queryForList(sql);
    }

    ///opcion red centro
    public List<Map<String, Object>> queryORC() {
        String sql = """
            SELECT c.redasiscod, r.redasisdes, c.cenasicod, c.cenasides
            FROM dwsge.sgss_cmcas10 c
            LEFT JOIN dwsge.sgss_cmras10 r ON c.redasiscod = r.redasiscod
            WHERE c.estregcod = '1'
            ORDER BY r.redasisdes, c.cenasides
            """;
        return secondJdbcTemplate.queryForList(sql);
    }

    ///opcion grupo etario


    ///Analitica por patologia de alto costo
    /// pacientes totales
    public List<Map<String, Object>> queryAPACPT() {
        String sql = """
            SELECT patologia, pacientes
            FROM dssge.mv_ejec_pat_resumen
            WHERE anio = 'TODOS' AND patologia = ANY(VALUES ('Raras'),('Oncologico'), ('Renal'))
            """;
        return secondJdbcTemplate.queryForList(sql);
    }

    ///comorbilidad global
    public List<Map<String, Object>> queryAPACCGB() {
        String sql = """
            SELECT patologia, anio, pacientes
            FROM dssge.mv_ejec_pat_resumen
            WHERE anio <> 'TODOS' AND patologia = ANY(VALUES ('Raras'),('Oncologico'), ('Renal')) ORDER BY anio
            """;
        return secondJdbcTemplate.queryForList(sql);
    }

    ///comorbilidad grupo
    public List<Map<String, Object>> queryAPACCGP(String patologia, String patron) {
        String sql = """
            SELECT c.patologia_b, c.pacientes
            FROM dssge.mv_ejec_comorbilidad c
            JOIN dwsge.mt_patologia p ON p.patologia = c.patologia_b
            WHERE c.patologia_a = ? AND p.patron = ?
            ORDER BY c.pacientes DESC
            """;
        return secondJdbcTemplate.queryForList(sql, patologia, patron);
    }

    ///comorbilidad grupo total
    public List<Map<String, Object>> queryAPACCGPT(String patologia) {
        String sql = """
            SELECT pacientes
            FROM dssge.mv_ejec_comorbilidad
            WHERE patologia_a = ? AND patologia_b = ?
            """;
        return secondJdbcTemplate.queryForList(sql, patologia, patologia);
    }

    ///comorbilidad interserccion
    public List<Map<String, Object>> queryAPACCI() {
        String sql = """
            SELECT patologia_a, patologia_b, pacientes
            FROM dssge.mv_ejec_comorbilidad
            WHERE patologia_a <> patologia_b AND pacientes > 0
            """;
        return secondJdbcTemplate.queryForList(sql);
    }

    ///comorbilidad interserccion total
    public List<Map<String, Object>> queryAPACCIT() {
        String sql = """
            SELECT patologia_a, pacientes
            FROM dssge.mv_ejec_comorbilidad
            WHERE patologia_a = patologia_b
            """;
        return secondJdbcTemplate.queryForList(sql);
    }

    ///patologia -> area ->servicio
    public List<Map<String, Object>> queryAPACPAS(List<String> anios) {
        if (anios == null || anios.isEmpty()) {
            return Collections.emptyList();
        }

        //'2019','2020'
        String placeholders = String.join(",", Collections.nCopies(anios.size(), "?"));

        String sql = """
            SELECT patologia, area, servhosdes, SUM(pacientes) AS pacientes
            FROM dssge.mv_ejec_pat_area_servicio
            WHERE anio IN (%s) AND patologia = ANY(VALUES ('Raras'),('Oncologico'), ('Renal')) AND area IS NOT NULL
            GROUP BY patologia, area, servhosdes
            """.formatted(placeholders);
        return secondJdbcTemplate.queryForList(sql, anios.toArray());
    }

    /// diagnostico y servicios mas frecuente por patologia
    public List<Map<String, Object>> queryAPACDSFP(List<String> anios) {
        if (anios == null || anios.isEmpty()) {
            return Collections.emptyList();
        }

        /// ('2019'), ('2020)
        String placeholders = String.join(",", Collections.nCopies(anios.size(), "(?)"));

        String sql = """
            SELECT patologia, diagdes, servhosdes, SUM(pacientes) AS pacientes
            FROM dssge.mv_ejec_pat_diag_servicio
            WHERE anio = ANY(VALUES %s ) AND patologia = ANY(VALUES ('Raras'),('Oncologico'), ('Renal'))
            GROUP BY patologia, diagdes, servhosdes
            """.formatted(placeholders);
        return secondJdbcTemplate.queryForList(sql, anios.toArray());
    }

    ///total pacientes
    public List<Map<String, Object>> queryAPACTP(List<String> anios) {
        if (anios == null || anios.isEmpty()) {
            return Collections.emptyList();
        }

        /// ('2019'), ('2020)
        String placeholders = String.join(",", Collections.nCopies(anios.size(), "(?)"));


        String sql = """
            SELECT COALESCE(SUM(pacientes),0) AS pacientes FROM dssge.mv_ejec_pat_resumen
            WHERE anio = ANY(VALUES %s) AND patologia = ANY(VALUES ('Raras'),('Oncologico'), ('Renal'))
            """.formatted(placeholders);
        return secondJdbcTemplate.queryForList(sql, anios.toArray());
    }

    ///mayor concentracion red
    public List<Map<String, Object>> queryAPACMCR(List<String> anios) {
        if (anios == null || anios.isEmpty()) {
            return Collections.emptyList();
        }

        /// ('2019'), ('2020)
        String placeholders = String.join(",", Collections.nCopies(anios.size(), "(?)"));

        String sql = """
            SELECT patologia, redasisdes, SUM(pacientes) AS pacientes
            FROM dssge.mv_ejec_pat_red
            WHERE anio = ANY(VALUES %s) AND patologia <> 'SIN PATOLOGIA' AND redasisdes IS NOT NULL
            GROUP BY patologia, redasisdes
            ORDER BY pacientes DESC LIMIT 1
            """.formatted(placeholders);
        return secondJdbcTemplate.queryForList(sql, anios.toArray());
    }

    ///mayor concentrado centro
    public List<Map<String, Object>> queryAPACMCC(List<String> anios) {
        if (anios == null || anios.isEmpty()) {
            return Collections.emptyList();
        }

        /// ('2019'), ('2020)
        String placeholders = String.join(",", Collections.nCopies(anios.size(), "(?)"));

        String sql = """
            SELECT patologia, cenasides, SUM(pacientes) AS pacientes
            FROM dssge.mv_ejec_pat_centro
            WHERE anio = ANY(VALUES %s ) AND patologia <> 'SIN PATOLOGIA' AND cenasides IS NOT NULL
            GROUP BY patologia, cenasides ORDER BY pacientes DESC LIMIT 1
            """.formatted(placeholders);
        return secondJdbcTemplate.queryForList(sql, anios.toArray());
    }

    ///ranking red
    public List<Map<String, Object>> queryAPACRR(List<String> anios) {
        if (anios == null || anios.isEmpty()) {
            return Collections.emptyList();
        }

        /// ('2019'), ('2020)
        String placeholders = String.join(",", Collections.nCopies(anios.size(), "(?)"));

        String sql = """
            SELECT redasisdes, patologia, SUM(pacientes) AS pacientes
            FROM dssge.mv_ejec_pat_red
            WHERE anio = ANY(VALUES %s ) AND patologia = ANY(VALUES ('Raras'),('Oncologico'), ('Renal')) AND redasisdes IS NOT NULL
            GROUP BY redasisdes, patologia
            """.formatted(placeholders);
        return secondJdbcTemplate.queryForList(sql, anios.toArray());
    }

    /// detalle por patología
    /// total de pacientes
    public List<Map<String, Object>> queryDPTP(List<String> patologias, List<String> anios, List<String> codreds, List<String> codcens, List<String> grupo_edades ) {
        if ((anios == null || anios.isEmpty()) ) {
            return Collections.emptyList();
        }

        String placeholdersPatologia=String.join(",", Collections.nCopies(patologias.size(), "(?)"));

        String placeholdersAnios = String.join(",", Collections.nCopies(anios.size(), "(?)"));

        String placeholdersRedes= String.join(",", Collections.nCopies(codreds.size(), "(?)"));

        String placeholdersCentros = String.join(",", Collections.nCopies(codcens.size(), "(?)"));

        String placeholdersEdades= String.join(",", Collections.nCopies(grupo_edades.size(), "(?)"));

        List<Object> union = new java.util.ArrayList<>();
        union.addAll(patologias);
        union.addAll(anios);
        union.addAll(codreds);
        union.addAll(codcens);
        union.addAll(grupo_edades);

        String sql = """
            SELECT COALESCE(SUM(pacientes),0) AS pacientes
            FROM dssge.mv_ejec_pat_detalle
            WHERE patologia = ANY(VALUES %s) AND anio = ANY(VALUES %s) AND redasiscod = ANY(VALUES %s) AND cod_centro = ANY(VALUES %s) AND grupo_edad = ANY(VALUES %s);
            """.formatted(placeholdersPatologia, placeholdersAnios, placeholdersRedes,placeholdersCentros,  placeholdersEdades);
        return secondJdbcTemplate.queryForList(sql, union.toArray());
    }

    ///total diagnosticos distintos
    public List<Map<String, Object>> queryDPTDD(List<String> patologias, List<String> anios, List<String> codreds, List<String> codcens, List<String> grupo_edades ) {
        if (anios == null || anios.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholdersPatologia=String.join(",", Collections.nCopies(patologias.size(), "(?)"));

        String placeholdersAnios = String.join(",", Collections.nCopies(anios.size(), "(?)"));

        String placeholdersRedes= String.join(",", Collections.nCopies(codreds.size(), "(?)"));

        String placeholdersCentros = String.join(",", Collections.nCopies(codcens.size(), "(?)"));

        String placeholdersEdades= String.join(",", Collections.nCopies(grupo_edades.size(), "(?)"));

        List<Object> union = new java.util.ArrayList<>();
        union.addAll(patologias);
        union.addAll(anios);
        union.addAll(codreds);
        union.addAll(codcens);
        union.addAll(grupo_edades);

        String sql = """
            SELECT COUNT(DISTINCT cod_diagnostico) AS n
            FROM dssge.mv_ejec_pat_diag
            WHERE patologia = ANY(VALUES %s) AND anio = ANY(VALUES %s) AND redasiscod = ANY(VALUES %s) AND cod_centro = ANY(VALUES %s) AND grupo_edad = ANY(VALUES %s)

            """.formatted(placeholdersPatologia, placeholdersAnios, placeholdersRedes,placeholdersCentros,  placeholdersEdades);
        return secondJdbcTemplate.queryForList(sql, union.toArray());
    }

    /// distribucion por sexo
    public List<Map<String, Object>> queryDPDS(List<String> patologias, List<String> anios, List<String> codreds, List<String> codcens, List<String> grupo_edades ) {
        if (anios == null || anios.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholdersPatologia=String.join(",", Collections.nCopies(patologias.size(), "(?)"));

        String placeholdersAnios = String.join(",", Collections.nCopies(anios.size(), "(?)"));

        String placeholdersRedes= String.join(",", Collections.nCopies(codreds.size(), "(?)"));

        String placeholdersCentros = String.join(",", Collections.nCopies(codcens.size(), "(?)"));

        String placeholdersEdades= String.join(",", Collections.nCopies(grupo_edades.size(), "(?)"));

        List<Object> union = new java.util.ArrayList<>();
        union.addAll(patologias);
        union.addAll(anios);
        union.addAll(codreds);
        union.addAll(codcens);
        union.addAll(grupo_edades);

        String sql = """
            SELECT sexo, SUM(pacientes) AS pacientes
            FROM dssge.mv_ejec_pat_sexo_edad
            WHERE patologia = ANY(VALUES %s) AND anio = ANY(VALUES %s) AND redasiscod = ANY(VALUES %s) AND cod_centro = ANY(VALUES %s) AND grupo_edad = ANY(VALUES %s)
             AND sexo <> 'TODOS'
            GROUP BY sexo
            """.formatted(placeholdersPatologia, placeholdersAnios, placeholdersRedes,placeholdersCentros,  placeholdersEdades);
        return secondJdbcTemplate.queryForList(sql, union.toArray());
    }

    ///Distribucion por grupo etario
    public List<Map<String, Object>> queryDPDGE(List<String> patologias, List<String> anios, List<String> codreds, List<String> codcens) {
        if (anios == null || anios.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholdersPatologia=String.join(",", Collections.nCopies(patologias.size(), "(?)"));

        String placeholdersAnios = String.join(",", Collections.nCopies(anios.size(), "(?)"));

        String placeholdersRedes= String.join(",", Collections.nCopies(codreds.size(), "(?)"));

        String placeholdersCentros = String.join(",", Collections.nCopies(codcens.size(), "(?)"));


        List<Object> union = new java.util.ArrayList<>();
        union.addAll(patologias);
        union.addAll(anios);
        union.addAll(codreds);
        union.addAll(codcens);


        String sql = """
           SELECT grupo_edad, SUM(pacientes) AS pacientes
           FROM dssge.mv_ejec_pat_sexo_edad
           WHERE patologia = ANY(VALUES %s) AND anio = ANY(VALUES %s) AND redasiscod = ANY(VALUES %s) AND cod_centro = ANY(VALUES %s) AND sexo = 'TODOS' AND grupo_edad <> 'TODOS'
           GROUP BY grupo_edad
           """.formatted(placeholdersPatologia, placeholdersAnios, placeholdersRedes,placeholdersCentros);
        return secondJdbcTemplate.queryForList(sql, union.toArray());
    }

    ///Tabla diagnosticos
    public List<Map<String, Object>> queryDPTD(List<String> patologias, List<String> anios, List<String> codreds, List<String> codcens, List<String> grupo_edades ) {
        if (anios == null || anios.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholdersPatologia=String.join(",", Collections.nCopies(patologias.size(), "(?)"));

        String placeholdersAnios = String.join(",", Collections.nCopies(anios.size(), "(?)"));

        String placeholdersRedes= String.join(",", Collections.nCopies(codreds.size(), "(?)"));

        String placeholdersCentros = String.join(",", Collections.nCopies(codcens.size(), "(?)"));

        String placeholdersEdades= String.join(",", Collections.nCopies(grupo_edades.size(), "(?)"));

        List<Object> union = new java.util.ArrayList<>();
        union.addAll(patologias);
        union.addAll(anios);
        union.addAll(codreds);
        union.addAll(codcens);
        union.addAll(grupo_edades);

        String sql = """
           SELECT cod_diagnostico, diagdes, tipodiagnom, SUM(pacientes) AS pacientes, SUM(registros) AS registros
           FROM dssge.mv_ejec_pat_diag
           WHERE patologia = ANY(VALUES %s) AND anio = ANY(VALUES %s) AND redasiscod = ANY(VALUES %s) AND cod_centro = ANY(VALUES %s) AND grupo_edad = ANY(VALUES %s)
           GROUP BY cod_diagnostico, diagdes, tipodiagnom
           ORDER BY pacientes DESC
           """.formatted(placeholdersPatologia, placeholdersAnios, placeholdersRedes,placeholdersCentros,  placeholdersEdades);
        return secondJdbcTemplate.queryForList(sql, union.toArray());
    }

    ///evolucion anual
    public List<Map<String, Object>> queryDPEA(List<String> patologias, List<String> codreds, List<String> codcens, List<String> grupo_edades ) {
        if (patologias == null || patologias.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholdersPatologia=String.join(",", Collections.nCopies(patologias.size(), "(?)"));

        String placeholdersRedes= String.join(",", Collections.nCopies(codreds.size(), "(?)"));

        String placeholdersCentros = String.join(",", Collections.nCopies(codcens.size(), "(?)"));

        String placeholdersEdades= String.join(",", Collections.nCopies(grupo_edades.size(), "(?)"));

        List<Object> union = new java.util.ArrayList<>();
        union.addAll(patologias);
        union.addAll(codreds);
        union.addAll(codcens);
        union.addAll(grupo_edades);

        String sql = """
           SELECT patologia, anio, SUM(pacientes) AS pacientes
           FROM dssge.mv_ejec_pat_diag
           WHERE patologia = ANY(VALUES %s) AND anio <> 'TODOS' AND redasiscod = ANY(VALUES %s) AND cod_centro = ANY(VALUES %s) AND grupo_edad = ANY(VALUES %s)
           GROUP BY patologia, anio
           ORDER BY anio
           """.formatted(placeholdersPatologia,  placeholdersRedes,placeholdersCentros, placeholdersEdades);
        return secondJdbcTemplate.queryForList(sql, union.toArray());
    }

    ///Pacientes por servicio
    public List<Map<String, Object>> queryDPPS(List<String> patologias, List<String> anios, List<String> codreds, List<String> codcens, List<String> grupo_edades ) {
        if (anios == null || anios.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholdersPatologia=String.join(",", Collections.nCopies(patologias.size(), "(?)"));

        String placeholdersAnios = String.join(",", Collections.nCopies(anios.size(), "(?)"));

        String placeholdersRedes= String.join(",", Collections.nCopies(codreds.size(), "(?)"));

        String placeholdersCentros = String.join(",", Collections.nCopies(codcens.size(), "(?)"));

        String placeholdersEdades= String.join(",", Collections.nCopies(grupo_edades.size(), "(?)"));

        List<Object> union = new java.util.ArrayList<>();
        union.addAll(patologias);
        union.addAll(anios);
        union.addAll(codreds);
        union.addAll(codcens);
        union.addAll(grupo_edades);

        String sql = """
            SELECT servhosdes, SUM(pacientes) AS pacientes
            FROM dssge.mv_ejec_pat_servicio
            WHERE patologia = ANY(VALUES %s) AND anio = ANY(VALUES %s) AND redasiscod = ANY(VALUES %s) AND cod_centro = ANY(VALUES %s) AND grupo_edad = ANY(VALUES %s) AND servhosdes IS NOT NULL
            GROUP BY servhosdes ORDER BY pacientes DESC LIMIT 15
            """.formatted(placeholdersPatologia, placeholdersAnios, placeholdersRedes,placeholdersCentros,  placeholdersEdades);
        return secondJdbcTemplate.queryForList(sql, union.toArray());
    }

    ///Ranking por centro
    public List<Map<String, Object>> queryDPRC(List<String> patologias, List<String> anios, List<String> codreds) {
        if (anios == null || anios.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholdersPatologia=String.join(",", Collections.nCopies(patologias.size(), "(?)"));

        String placeholdersAnios = String.join(",", Collections.nCopies(anios.size(), "(?)"));

        String placeholdersRedes= String.join(",", Collections.nCopies(codreds.size(), "(?)"));

        List<Object> union = new java.util.ArrayList<>();
        union.addAll(patologias);
        union.addAll(anios);
        union.addAll(codreds);

        String sql = """
            SELECT cenasides, patologia, SUM(pacientes) AS pacientes
            FROM dssge.mv_ejec_pat_centro
            WHERE patologia = ANY( VALUES %s) AND anio = ANY( VALUES %s) AND redasiscod = ANY( VALUES %s)
            AND cenasides IS NOT NULL
            GROUP BY cenasides, patologia
            """.formatted(placeholdersPatologia, placeholdersAnios, placeholdersRedes);
        return secondJdbcTemplate.queryForList(sql, union.toArray());
    }

    ////Comorbilidad del paciente
    ///buscar pacientes por documento
    public List<Map<String, Object>> queryCPCPD(String documento) {
        String sql = """
            SELECT anio, patologia, area, cenasides, servhosdes,
                    cod_diagnostico, diagdes, tipodiagnom, sexo, anio_edad
            FROM dssge.mv_ejec_base
            WHERE doc_paciente = ?
            ORDER BY anio, patologia, cod_diagnostico
            """;
        return secondJdbcTemplate.queryForList(sql, documento);
    }


}
