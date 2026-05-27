package essalud.gcpp.essaludgcppbackend.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

public class DashboardProcRepository {
    private final JdbcTemplate secondJdbcTemplate;

    public DashboardProcRepository(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
        this.secondJdbcTemplate = secondJdbcTemplate;
    }

    private String code_asegurado(String codasegu) {
        return switch (codasegu) {
            case "1" -> "('1')"; /// Asegurado
            case "2" -> "('2')"; /// No asegurado
            case "3" -> "('1','2')"; /// Todos
            default -> "('1','2')";
        };
    }

    private String codes(String tarjeta) {
        switch (tarjeta) {
            case "1": // Ablación Transcatéter
                return "('93651','93652','93653','93654')";
            case "2": // Administración de Oxígeno por Casco Cefálico (OXIHOOD)"
                return "('94799.02')";
            case "3": // Angiografía Cerebral
                return "('36221','36222','36223','36224','36225','36228','36251','61254','61257','61623','61630','61635','61640','64627')";
            case "4": // Angioplastia coronaria
                return "('37252','37253')";
            case "5": // Angiografía Retinal
                return "('92235','92227','92250','92300')";
            case "6": // Angioplastia con Balón
                return "('35456','35474','92981','92982')";
            case "7": // Angioplastia con Stent Metálico
                return "('37238')";
            case "8": // Audiometría
                return "('92552','92553','92556','92583','92551','92555','92557','92560','92561')";
            case "9": // Biopsia Endomiocárdica
                return "('93505')";
            case "10": // Cardio Holter
                return "('93224','93225','93233')";
            case "11": // Cardioversión Eléctrica Electiva
                return "('92960','92961')";
            case "12": // Cateterismo + Medición de CIA
                return "('93451')";
            case "13": // Cateterismo Cardíaco
                return "('36013','36014','93455','93456','93458','93503','93556','93562','93452','93454','93544')";
            case "14": // Colposcopía
                return "('56820','57420','57454','57455','58110','56821','57421','57452','57456','57461')";
            case "15": // Cono Frío
                return "('57520')";
            case "16": // Cono LEEP
                return "('57522')";
            case "17": // Crioterapia
                return "('17340')";
            case "18": // Dilatación con Protesis de Coartación de Aorta
                return "('35472')";
            case "19": // Ecocardiografía Stress
                return "('93350','93351')";
            case "20": // Ecocardiografía Transesofágica
                return "('93312','93313','93314','93315','93317')";
            case "21": // Ecocardiografía Transtorácica
                return "('93306','93307','93308','93320','93882.06','93321')";
            case "22": // Electrocardiografía
                return "('93000','93005','93010')";
            case "23": // Electroencefalografía
                return "('95812','95812.02','95813','95816','95819','95822')";
            case "24": // Electromiografía y Velocidad de Conducción
                return "('95860','95861','95863','95864','95872','95885','95886','95887','96867','95877','95900','95903','95904','95905','95907','95908','95909','95910','95937')";
            case "25": // Endoscopía Diagnóstica no Digestiva
                return "('31231','31505','31575','31622','31623','45338','31624','31625','31627','31628','31632','91010','31633','31641','31645','31646','45334','45337','45379','46614','49082','90901','90911','91038','91111','91122','91212','92511','96366')";
            case "26": // Endoscopía Digestiva Diagnóstica
                return "('43234','43239','44388','44391','45358','45359','45378','45380','91200','91202')";
            case "27": // Espirometría
                return "('94060','94010','94012.01','94315')";
            case "28": // Estimulacion Electrica Cerebral
                return "('95979','95975','95978','95974','95971')";
            case "29": // Estudios Fisiológicos (Electrofisiológicos)
                return "('93613','93618','93619','93623','93609')";
            case "30": // Implantación de Cardiovector Desfibrilador Automático
                return "('33215','33225','33263','33240','33244','33249')";
            case "31": // Instalación y Mantenimiento de CPAC de burbuja
                return "('94660')";
            case "32": // Instalación y Mantenimiento del Cateter Venoso Central de Inserción
                       // Periferica (PICC)
                return "('36568','36569')";
            case "33": // Laparoscopía Diagnóstica
                return "('49320')";
            case "34": // Laserterapia Ocular
                return "('92136','92250','92286','92287','96905')";
            case "35": // Marcapaso Definitivo Bicameral
                return "('33208','33213','33214','33228','33230')";
            case "36": // Marcapaso Definitivo para Resincronización
                return "('33221','33229')";
            case "37": // Marcapaso Definitivo Unicameral
                return "('33206','33207','33212','33227','93612','33233')";
            case "38": // Marcapaso Transitorio
                return "('92953','33211')";
            case "39": // Oclusión de Defecto Septal Interauricular
                return "('93580')";
            case "40": // Oclusión de Defecto Septal Interventricular
                return "('93581')";
            case "41": // Perimetría (Campimetría)
                return "('92081','92082','92083')";
            case "42": // Procedimiento Corneal Instrumentado
                return "('92025','92100','92137')";
            case "43": // Procedimientos Médicos de Rehabilitación
                return "('1983','1984','1985','23929','26011','26989','27427','28001','29220','29240','29260','29280','29530','29540','36513','36514','62271','92020','92548','93015','96008','96009','97784','97785','97786','97125','97003','99187','99193','99194','99199','99207','99210','99214','98889','99489','36516','50590','28890','62311','64418','64445','64450','64470','64475','64612','64613','64722','90287','90885','92015','92506','29799','31299','93668','93750','95851','95852','95860','96001','96004','96110','96111','97010','97014','97034')";
            case "44": // Prueba de Esfuerzo
                return "('93015','93016','93017','93018','93464')";
            case "45": // Reserva de Flujo Fraccionado
                return "('92953','33211')";
            case "46": // Test de Inclinación
                return "('93660')";
            case "47": // Test del Aliento
                return "('83013')";
            case "48": // Tomografía de Coherencia Óptica (OCT)":
                return "('92132','92133','92134')";
            case "49": // Tratamiento del Dolor
                return "('1984.01','1984','20553','27299','62310','62311','64483','64510','90780','96365','96367','97785','97786','97810','99187','99193','99194','99489.02')";
            case "50": // Trombólisis Sistémica
                return "('37195')";
            case "51": // Ultrasonido Endovascular
                return "('92978')";
            case "52": // Urodinamia
                return "('51726','51729','51741')";
            case "53": // Valvuloplastia Mitral con Balón
                return "('92987')";
            case "54": // Valvuloplastía Pulmonar y/o Aórtica
                return "('92998')";
            case "55": // Video/Epilepsia
                return "('95951')";
            default:
                return "()";
        }
    }

    /// Query procedimientos
    public List<Map<String, Object>> queryDP(String anio, String periodo, String codcas, String codasegu,
            String codes) {

        String tabla1 = "";
        String tabla2 = "";
        String tabla3 = "";
        String tabla4 = "";

        String code_asegurado = code_asegurado(codasegu);
        String codes_proc = codes(codes);

        String sql = """
                SELECT
                    ce.cod_servicio,
                    ce.cod_especialidad,
                    ca.cenasides,
                    ag.agrupador AS agrupador,
                    am.actdes AS actividad,
                    a.actespnom AS subactividad,
                    ce.cod_tipo_consulta,
                    ce.cod_diag,
                    c.servhosdes AS descripcion_servicio,
                    e.especialidad AS descripcion_especialidad,
                    t.tipcondes AS descripcion_tipo_consulta,
                    d.diagdes AS descripcion_diagnostico,
                    dni_medico,
                    doc_paciente,
                    CASE WHEN ce.cod_tipo_paciente = '4' THEN '2' ELSE '1' END AS cod_tipo_paciente,
                    sexo,
                    fecha_atencion,
                    acto_med
                FROM %s AS ce
                LEFT JOIN dwsge.sgss_cmsho10 AS c
                    ON ce.cod_servicio = c.servhoscod
                LEFT JOIN dwsge.dim_especialidad AS e
                    ON ce.cod_especialidad = e.cod_especialidad
                LEFT JOIN dwsge.sgss_cmtco10 AS t
                    ON ce.cod_tipo_consulta = t.tipconcod
                LEFT JOIN dwsge.sgss_cmdia10 AS d
                    ON ce.cod_diag = d.diagcod
                LEFT JOIN dwsge.sgss_cmace10 AS a
                    ON ce.cod_actividad = a.actcod
                    AND ce.cod_subactividad = a.actespcod
                LEFT JOIN dwsge.sgss_cmact10 AS am
                    ON ce.cod_actividad = am.actcod
                LEFT JOIN dwsge.sgss_cmcas10 AS ca
                    ON ce.cod_oricentro = ca.oricenasicod
                    AND ce.cod_centro = ca.cenasicod
                LEFT JOIN dwsge.dim_agrupador as ag ON ce.cod_agrupador = ag.cod_agrupador
                WHERE ce.cod_centro = ?
                AND ce.cod_servicio = 'E11'
                AND ce.cod_actividad = '91'
                AND ce.cod_subactividad in ('003','004','074','262')
                AND (
                        CASE
                            WHEN ce.cod_tipo_paciente = '4' THEN '2'
                            ELSE '1'
                        END
                        ) IN %s
                """.formatted(tabla1, code_asegurado);

        return secondJdbcTemplate.queryForList(sql, codcas);

    }
}
