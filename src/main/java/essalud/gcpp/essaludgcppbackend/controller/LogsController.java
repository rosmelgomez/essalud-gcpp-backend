package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.dtos.LogUsersDto;
import essalud.gcpp.essaludgcppbackend.model.Logs;
import essalud.gcpp.essaludgcppbackend.model.Users;
import essalud.gcpp.essaludgcppbackend.repositories.UsersRepository;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/ggi/log")
@RequiredArgsConstructor
public class LogsController {

    private final UsersRepository userRepository;
    private final LogService logService;

    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','ADMIN_RED')")
    public ResponseEntity<Void> registerLog(
            @RequestBody LogUsersDto dto,
            HttpServletRequest request,
            Authentication authentication
    ) {

        Users user = userRepository.findByUsername(authentication.getName());

        logService.registerLogs(dto, user, request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/debug-ip")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public Map<String, String> debug(HttpServletRequest request) {

        return Map.of(
                "remoteAddr", request.getRemoteAddr(),
                "xRealIp", request.getHeader("X-Real-IP"),
                "xForwardedFor", request.getHeader("X-Forwarded-For")
        );
    }
}




