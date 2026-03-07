package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.dtos.LogUsersDto;
import essalud.gcpp.essaludgcppbackend.model.Logs;
import essalud.gcpp.essaludgcppbackend.model.Users;
import essalud.gcpp.essaludgcppbackend.repositories.LogsRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogService {

    private final LogsRepository logsRepository;

    public LogService(LogsRepository logsRepository) {
        this.logsRepository = logsRepository;
    }


    public void registerLogs(LogUsersDto dto, Users user, HttpServletRequest request) {

        String ip = IpUtils.extractClientIp(request);
        String browser = BrowserDetector.detectBrowser(request);
        String os = BrowserDetector.detectOS(request);

        Logs log = Logs.builder()
                .user_logs(user)
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
