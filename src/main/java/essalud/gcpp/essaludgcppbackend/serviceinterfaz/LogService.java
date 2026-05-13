package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.dtos.LogFilterDto;
import essalud.gcpp.essaludgcppbackend.dtos.LogUsersDto;
import essalud.gcpp.essaludgcppbackend.model.Log;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.LogsRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogService {

    private final LogsRepository logsRepository;

    public LogService(LogsRepository logsRepository) {
        this.logsRepository = logsRepository;
    }

    public List<LogFilterDto>  getLogFilters() {
        List<Log> logs = logsRepository.findAllWithUser();
        List<LogFilterDto> logFilterDtos = new ArrayList<>();
        for (Log log : logs) {
            LogFilterDto logFilterDto = new LogFilterDto();
            logFilterDto.setDni(log.getUser_log().getDni());
            logFilterDto.setFull_name(log.getUser_log().getName()+ " "+log.getUser_log().getLastname());
            logFilterDto.setIp_user(log.getIp_user());
            logFilterDto.setUrl(log.getUrl());
            logFilterDto.setBrowser(log.getBrowser());
            logFilterDto.setUser_role(log.getRol_user());
            logFilterDto.setCodcas(log.getUser_log().getCodcas());
            logFilterDto.setCode_red(log.getUser_log().getCodred());
            logFilterDto.setDate(log.getDate_creation());
            logFilterDto.setId_user(log.getUser_log().getId());
            logFilterDtos.add(logFilterDto);
        }
        return logFilterDtos;
    }

    public void registerLogs(LogUsersDto dto, Users user, HttpServletRequest request, String rol) {

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
                .date_creation(LocalDateTime.now())
                .build();

        logsRepository.save(log);
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
