package essalud.gcpp.essaludgcppbackend.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardProcRep {
        private final JdbcTemplate secondJdbcTemplate;

        public DashboardProcRep(@Qualifier("secondJdbcTemplate") JdbcTemplate secondJdbcTemplate) {
                this.secondJdbcTemplate = secondJdbcTemplate;
        }

        private String code_asegurado(int codasegu) {
                return switch (codasegu) {
                        case 1 -> "('1')"; /// Asegurado
                        case 2 -> "('2')"; /// No asegurado
                        case 3 -> "('1','2')"; /// Todos
                        default -> "('1','2')";
                };
        }

        private String codes(int tarjeta) {
                return switch (tarjeta) {
                        case 1 -> // Ablación Transcatéter
                                "('93651','93652','93653','93654')";
                        case 2 -> // Administración de Oxígeno por Casco Cefálico (OXIHOOD)"
                                "('94799.02')";
                        case 3 -> // Angiografía Cerebral
                                "('36221','36222','36223','36224','36225','36228','36251','61254','61257','61623','61630','61635','61640','64627')";
                        case 4 -> // Angioplastia coronaria
                                "('37252','37253')";
                        case 5 -> // Angiografía Retinal
                                "('92235','92227','92250','92300')";
                        case 6 -> // Angioplastia con Balón
                                "('35456','35474','92981','92982')";
                        case 7 -> // Angioplastia con Stent Metálico
                                "('37238')";
                        case 8 -> // Audiometría
                                "('92552','92553','92556','92583','92551','92555','92557','92560','92561')";
                        case 9 -> // Biopsia Endomiocárdica
                                "('93505')";
                        case 10 -> // Cardio Holter
                                "('93224','93225','93233')";
                        case 11 -> // Cardioversión Eléctrica Electiva
                                "('92960','92961')";
                        case 12 -> // Cateterismo + Medición de CIA
                                "('93451')";
                        case 13 -> // Cateterismo Cardíaco
                                "('36013','36014','93455','93456','93458','93503','93556','93562','93452','93454','93544')";
                        case 14 -> // Colposcopía
                                "('56820','57420','57454','57455','58110','56821','57421','57452','57456','57461')";
                        case 15 -> // Cono Frío
                                "('57520')";
                        case 16 -> // Cono LEEP
                                "('57522')";
                        case 17 -> // Crioterapia
                                "('17340')";
                        case 18 -> // Dilatación con Protesis de Coartación de Aorta
                                "('35472')";
                        case 19 -> // Ecocardiografía Stress
                                "('93350','93351')";
                        case 20 -> // Ecocardiografía Transesofágica
                                "('93312','93313','93314','93315','93317')";
                        case 21 -> // Ecocardiografía Transtorácica
                                "('93306','93307','93308','93320','93882.06','93321')";
                        case 22 -> // Electrocardiografía
                                "('93000','93005','93010')";
                        case 23 -> // Electroencefalografía
                                "('95812','95812.02','95813','95816','95819','95822')";
                        case 24 -> // Electromiografía y Velocidad de Conducción
                                "('95860','95861','95863','95864','95872','95885','95886','95887','96867','95877','95900','95903','95904','95905','95907','95908','95909','95910','95937')";
                        case 25 -> // Endoscopía Diagnóstica no Digestiva
                                "('31231','31505','31575','31622','31623','45338','31624','31625','31627','31628','31632','91010','31633','31641','31645','31646','45334','45337','45379','46614','49082','90901','90911','91038','91111','91122','91212','92511','96366')";
                        case 26 -> // Endoscopía Digestiva Diagnóstica
                                "('43234','43239','44388','44391','45358','45359','45378','45380','91200','91202')";
                        case 27 -> // Espirometría
                                "('94060','94010','94012.01','94315')";
                        case 28 -> // Estimulacion Electrica Cerebral
                                "('95979','95975','95978','95974','95971')";
                        case 29 -> // Estudios Fisiológicos (Electrofisiológicos)
                                "('93613','93618','93619','93623','93609')";
                        case 30 -> // Implantación de Cardiovector Desfibrilador Automático
                                "('33215','33225','33263','33240','33244','33249')";
                        case 31 -> // Instalación y Mantenimiento de CPAC de burbuja
                                "('94660')";
                        case 32 -> // Instalación y Mantenimiento del Cateter Venoso Central de Inserción Periferica (PICC)
                                "('36568','36569')";
                        case 33 -> // Laparoscopía Diagnóstica
                                "('49320')";
                        case 34 -> // Laserterapia Ocular
                                "('92136','92250','92286','92287','96905')";
                        case 35 -> // Marcapaso Definitivo Bicameral
                                "('33208','33213','33214','33228','33230')";
                        case 36 -> // Marcapaso Definitivo para Resincronización
                                "('33221','33229')";
                        case 37 -> // Marcapaso Definitivo Unicameral
                                "('33206','33207','33212','33227','93612','33233')";
                        case 38 -> // Marcapaso Transitorio
                                "('92953','33211')";
                        case 39 -> // Oclusión de Defecto Septal Interauricular
                                "('93580')";
                        case 40 -> // Oclusión de Defecto Septal Interventricular
                                "('93581')";
                        case 41 -> // Perimetría (Campimetría)
                                "('92081','92082','92083')";
                        case 42 -> // Procedimiento Corneal Instrumentado
                                "('92025','92100','92137')";
                        case 43 -> // Procedimientos Médicos de Rehabilitación
                                "('1983','1984','1985','23929','26011','26989','27427','28001','29220','29240','29260','29280','29530','29540','36513','36514','62271','92020','92548','93015','96008','96009','97784','97785','97786','97125','97003','99187','99193','99194','99199','99207','99210','99214','98889','99489','36516','50590','28890','62311','64418','64445','64450','64470','64475','64612','64613','64722','90287','90885','92015','92506','29799','31299','93668','93750','95851','95852','95860','96001','96004','96110','96111','97010','97014','97034')";
                        case 44 -> // Prueba de Esfuerzo
                                "('93015','93016','93017','93018','93464')";
                        case 45 -> // Reserva de Flujo Fraccionado
                                "('92953','33211')";
                        case 46 -> // Test de Inclinación
                                "('93660')";
                        case 47 -> // Test del Aliento
                                "('83013')";
                        case 48 -> // Tomografía de Coherencia Óptica (OCT):
                                "('92132','92133','92134')";
                        case 49 -> // Tratamiento del Dolor
                                "('1984.01','1984','20553','27299','62310','62311','64483','64510','90780','96365','96367','97785','97786','97810','99187','99193','99194','99489.02')";
                        case 50 -> // Trombólisis Sistémica
                                "('37195')";
                        case 51 -> // Ultrasonido Endovascular
                                "('92978')";
                        case 52 -> // Urodinamia
                                "('51726','51729','51741')";
                        case 53 -> // Valvuloplastia Mitral con Balón
                                "('92987')";
                        case 54 -> // Valvuloplastía Pulmonar y/o Aórtica
                                "('92998')";
                        case 55 -> // Video/Epilepsia
                                "('95951')";
                        default -> "()";
                };
        }

        private String periodo_str(int periodo) {
                if (periodo < 10) {
                        return   "0" + periodo;
                } else {
                        return String.valueOf(periodo);
                }
        }

        /// Query procedimientos
        public List<Map<String, Object>> queryDP(int anio, int periodo, String codcas, int codasegu, int codes) {

                String tabla1 = "dssge.dwe_centro_quirurgico_" + anio + "_" + periodo_str(periodo);
                String tabla2 = "dssge.dw_proc_" + anio + "_" + periodo_str(periodo);
                String tabla3 = "dssge.dw_proc_eme_" + anio + "_" + periodo_str(periodo);
                String tabla4 = "dssge.dw_proc_hos_" + anio + "_" + periodo_str(periodo);

                String code_asegurado = code_asegurado(codasegu);
                String codes_str = codes(codes);

                String sql = """
                                WITH base AS (
                                    SELECT DISTINCT
                                        doc_paciente,
                                        cod_cpms,
                                        fec_oper
                                    FROM %s
                                    WHERE cod_sala IS NOT NULL
                                      AND cod_cpms IN %s
                                      AND cod_centro = ?
                                ),
                                union_proc AS (
                                    SELECT
                                        cod_oricentro, cod_centro, c.cenasides, anio, periodo, cod_servicio, s.servhosdescor as servicio,
                                        dni_medico, doc_paciente, tip_doc_paciente, anio_edad, sexo,
                                        cod_tipo_seguro, cod_tipo_parentesco, cod_tipo_paciente,
                                        fecha_aten, acto_med, codproced, cantproced::numeric,
                                        cod_actividad, a.actdes as actividad, cod_subactividad, cp.cpsdes AS cpms, 'CE' as area
                                    FROM %s as pc
                                    LEFT JOIN dwsge.sgss_cmsho10 as s ON s.servhoscod = pc.cod_servicio
                                    LEFT JOIN dwsge.sgss_cmcpp10 as cp ON cp.cpscod = pc.codproced
                                    LEFT JOIN dwsge.sgss_cmcas10 as c ON c.cenasicod = pc.cod_centro AND c.oricenasicod = pc.cod_oricentro
                                    LEFT JOIN dwsge.sgss_cmact10 as a ON a.actcod = pc.cod_actividad
                                    WHERE cod_centro = ?
                                      AND codproced IN %s
                                      AND cod_actividad = '96'
                                      AND (CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END) IN %s

                                    UNION ALL

                                    SELECT
                                        cod_oricentro, cod_centro, c.cenasides, anio, periodo, cod_servicio,s.servhosdescor as servicio,
                                        dni_medico, doc_paciente, tip_doc_paciente, anio_edad, sexo,
                                        cod_tipo_seguro, cod_tipo_parentesco, cod_tipo_paciente,
                                        fecha_aten, acto_med, codproced, cantproced::numeric,
                                        cod_actividad, a.actdes as actividad, cod_subactividad, cp.cpsdes AS cpms, 'EME' as area
                                    FROM %s as pc
                                    LEFT JOIN dwsge.sgss_cmsho10 as s ON s.servhoscod = pc.cod_servicio
                                    LEFT JOIN dwsge.sgss_cmcpp10 as cp ON cp.cpscod = pc.codproced
                                    LEFT JOIN dwsge.sgss_cmcas10 as c ON c.cenasicod = pc.cod_centro AND c.oricenasicod = pc.cod_oricentro
                                    LEFT JOIN dwsge.sgss_cmact10 as a ON a.actcod = pc.cod_actividad
                                    WHERE cod_centro = ?
                                      AND codproced IN %s
                                      AND cod_actividad = '96'
                                      AND (CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END) IN %s

                                    UNION ALL

                                    SELECT
                                        cod_oricentro, cod_centro, c.cenasides, anio, periodo, cod_servicio,s.servhosdescor as servicio,
                                        dni_medico, doc_paciente, tip_doc_paciente, anio_edad, sexo,
                                        cod_tipo_seguro, cod_tipo_parentesco, cod_tipo_paciente,
                                        fecha_aten, acto_med, codproced, cantproced::numeric,
                                        cod_actividad, a.actdes as actividad, cod_subactividad, cp.cpsdes AS cpms, 'HOS' as area
                                    FROM %s as pc
                                    LEFT JOIN dwsge.sgss_cmsho10 as s ON s.servhoscod = pc.cod_servicio
                                    LEFT JOIN dwsge.sgss_cmcpp10 as cp ON cp.cpscod = pc.codproced
                                    LEFT JOIN dwsge.sgss_cmcas10 as c ON c.cenasicod = pc.cod_centro AND c.oricenasicod = pc.cod_oricentro
                                    LEFT JOIN dwsge.sgss_cmact10 as a ON a.actcod = pc.cod_actividad
                                    WHERE cod_centro = ?
                                      AND codproced IN %s
                                      AND cod_actividad = '96'
                                      AND (CASE WHEN cod_tipo_paciente = '4' THEN '2' ELSE '1' END) IN %s
                                )
                                SELECT *
                                FROM union_proc p
                                WHERE NOT EXISTS (
                                    SELECT 1
                                    FROM base b
                                    WHERE b.doc_paciente = p.doc_paciente
                                      AND b.cod_cpms     = p.codproced
                                      AND b.fec_oper     = p.fecha_aten
                                )
                                """
                                .formatted(tabla1, codes_str, tabla2, codes_str, code_asegurado, tabla3, codes_str,
                                                code_asegurado,
                                                tabla4, codes_str, code_asegurado);

                return secondJdbcTemplate.queryForList(sql, codcas, codcas, codcas, codcas);

        }
}
