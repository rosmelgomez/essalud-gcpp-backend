package essalud.gcpp.essaludgcppbackend.service.usuario;

import essalud.gcpp.essaludgcppbackend.dtos.usuario.LogFilterDto;
import essalud.gcpp.essaludgcppbackend.dtos.usuario.LogUsuarioDto;
import essalud.gcpp.essaludgcppbackend.model.usuario.Log;
import essalud.gcpp.essaludgcppbackend.model.usuario.Usuario;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.LogsRep;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogServ {

    private final LogsRep logsRep;

    public LogServ(LogsRep logsRep) {
        this.logsRep = logsRep;
    }

    @Transactional(readOnly = true)
    public List<LogFilterDto>  getLogFilters() {
        List<Log> logs = logsRep.findAllWithUserFetched();
        List<LogFilterDto> logFilterDtos = new ArrayList<>(logs.size());
        for (Log log : logs) {
            LogFilterDto logFilterDto = new LogFilterDto();
            logFilterDto.setId(log.getId());
            logFilterDto.setIp_user(log.getIp_user());
            logFilterDto.setUrl(log.getUrl());
            logFilterDto.setBrowser(log.getBrowser());
            logFilterDto.setUser_role(log.getRol_user());
            logFilterDto.setAction(log.getAction());
            logFilterDto.setHttp_method(log.getHttp_method());
            logFilterDto.setStatus(log.getStatus());
            logFilterDto.setDate(log.getDate_creation());

            // Un log puede quedar sin usuario (usuario eliminado): no debe romper el listado
            Usuario user = log.getUser_log();
            if (user != null) {
                String fullName = ((user.getName() == null ? "" : user.getName()) + " "
                        + (user.getLastname() == null ? "" : user.getLastname())).trim();
                logFilterDto.setDni(user.getDni());
                logFilterDto.setFull_name(fullName);
                logFilterDto.setCodcas(user.getCodcas());
                logFilterDto.setCode_red(user.getCodred());
                logFilterDto.setId_user(user.getId());
            }
            logFilterDtos.add(logFilterDto);
        }
        return logFilterDtos;
    }

    public void registerLogs(LogUsuarioDto dto, Usuario user, HttpServletRequest request, String rol) {

        String ip = IpUtils.extractClientIp(request);
        String browser = BrowserDetector.detectBrowser(request);
        String os = BrowserDetector.detectOS(request);

        Log log = Log.builder()
                .user_log(user)
                .rol_user(rol)
                .action(dto.getAction())
                .status(dto.getStatus())
                .message(dto.getMessage())
                .ip_user(ip)
                .http_method(request.getMethod())
                .url(dto.getUrl())
                .browser(browser + " (" + os + ")")
                .date_creation(LocalDateTime.now(ZoneId.of("America/Lima")))
                .build();

        logsRep.save(log);
    }

    public static final class IpUtils {

        private IpUtils() {}

        public static String extractClientIp(HttpServletRequest request) {

            String xff = request.getHeader("X-Forwarded-For");
            if (isValid(xff)) {
                return normalizeIp(xff.split(",")[0].trim());
            }

            String xri = request.getHeader("X-Real-IP");
            if (isValid(xri)) {
                return normalizeIp(xri);
            }

            return normalizeIp(request.getRemoteAddr());
        }

        private static boolean isValid(String value) {
            return value != null && !value.isBlank();
        }

        private static String normalizeIp(String ip) {
            if (ip != null && ip.startsWith("::ffff:")) {
                return ip.substring(7);
            }
            return ip;
        }
    }

    public static final class BrowserDetector {

        private BrowserDetector() {}

        public static String detectBrowser(HttpServletRequest request) {

            String ua = getUserAgent(request);

            if (ua.contains("edg")) return "EDGE";
            if (ua.contains("opr") || ua.contains("opera")) return "OPERA";
            if (ua.contains("chrome")) return "CHROME";
            if (ua.contains("firefox")) return "FIREFOX";
            if (ua.contains("safari")) return "SAFARI";

            return "UNKNOWN";
        }

        public static String detectOS(HttpServletRequest request) {

            String ua = getUserAgent(request);

            if (ua.contains("windows")) return "WINDOWS";
            if (ua.contains("mac")) return "MAC";
            if (ua.contains("linux")) return "LINUX";
            if (ua.contains("android")) return "ANDROID";
            if (ua.contains("iphone") || ua.contains("ipad")) return "IOS";

            return "UNKNOWN";
        }

        private static String getUserAgent(HttpServletRequest request) {

            String ua = request.getHeader("User-Agent");

            if (ua == null) return "";

            return ua.toLowerCase();
        }
    }
}
