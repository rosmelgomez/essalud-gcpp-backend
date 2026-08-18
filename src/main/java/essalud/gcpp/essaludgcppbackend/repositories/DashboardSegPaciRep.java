package essalud.gcpp.essaludgcppbackend.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardSegPaciRep {

    private final JdbcTemplate firstOracleJdbcTemplate;
    private final JdbcTemplate secondJdbcTemplate;

    public DashboardSegPaciRep(@Qualifier("firstOracleJdbcTemplate") JdbcTemplate firstOracleJdbcTemplate,@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
        this.firstOracleJdbcTemplate = firstOracleJdbcTemplate;
        this.secondJdbcTemplate=secondJdbcTemplate;
    }

    /// Query 1 atenciones
    public List<Map<String, Object>> query1SPA(String doc, String tipo_doc) {
        String sql = """
           SELECT
               c.cenasicod as cod_centro,
               r.redasismeddes  AS RED,
               c.cenasidescor   AS CENTRO,
               a.actmednum      AS ACTO_MED,
               are.arehosdes    AS AREA,
               ser.servhosdes   AS SERVICIO,
               tp.tipoparenom   AS TIPO_ATENCION,
               a.actmededadaten AS EDAD_EN_ATENCION,
               a.actmedfecaten  AS FECHA_ATENCION,
               a.actmedate      AS ACTMEDATE,
               a.actmedpro      AS ACTMEDPRO,
               a.actmedrec      AS ACTMEDREC,
               a.actmedexaima   AS ACTMEDEXAIMA,
               a.actmedexalab   AS ACTMEDEXALAB,
               a.actmedexapat   AS ACTMEDEXAPAT,
               a.actmedhos      AS ACTMEDHOS,
               a.actmedope      AS ACTMEDOPE,
               a.actmedcitt     AS ACTMEDCITT
           FROM sgss.cmame10 a
           LEFT OUTER JOIN sgss.cmcas10 c ON c.oricenasicod = a.oricenasicod
                                         AND c.cenasicod    = a.cenasicod
           LEFT OUTER JOIN sgss.cmras10 r   ON r.redasiscod   = c.redasiscod
           LEFT OUTER JOIN sgss.cmaho10 are ON are.arehoscod  = a.actmedarehoscod
           LEFT OUTER JOIN sgss.cmsho10 ser ON ser.servhoscod = a.actmedservhoscod
           LEFT OUTER JOIN sgss.cbtpa10 tp  ON tp.tipoparecod  = a.tipoparecod
           INNER JOIN sgss.cmper10 p ON p.persecnum = a.actmedpacsecnum
           WHERE p.pertipdocidencod = ?
             AND p.perdocidennum    = ?
           ORDER BY a.actmedfecaten DESC
           """;

        return firstOracleJdbcTemplate.queryForList(sql, tipo_doc,doc );
    }

    /// Query 2 atenciones
    public List<Map<String, Object>> query2SPA(String doc, String tipo_doc) {
        String sql = """
           SELECT ce.cod_centro, ce.acto_med, ce.cod_diagnostico, d.diagdes
           FROM dssge.mtd_paciente_atencion_diagnostico ce
           LEFT JOIN dwsge.sgss_cmdia10 d ON d.diagcod = ce.cod_diagnostico
           WHERE ce.doc_paciente = ? AND ce.cod_tipdoc_paciente = ?
           """;

        return secondJdbcTemplate.queryForList(sql, doc, tipo_doc );
    }

}
