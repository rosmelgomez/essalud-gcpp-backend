package essalud.gcpp.essaludgcppbackend.controller.usuario;

import essalud.gcpp.essaludgcppbackend.dtos.usuario.LogFilterDto;
import essalud.gcpp.essaludgcppbackend.dtos.usuario.LogUsuarioDto;
import essalud.gcpp.essaludgcppbackend.model.usuario.Usuario;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsuarioRep;
import essalud.gcpp.essaludgcppbackend.service.usuario.LogServ;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/log")
public class LogCon {

    private final UsuarioRep userRepository;
    private final LogServ logServ;

    public LogCon(UsuarioRep userRepository, LogServ logServ) {
        this.userRepository = userRepository;
        this.logServ = logServ;
    }


    @PostMapping("/register")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','ADMIN_RED')")
    public ResponseEntity<Void> registerLog(
            @RequestBody LogUsuarioDto dto,
            HttpServletRequest request,
            Authentication authentication
    ) {

        Usuario user = userRepository.findByUsername(authentication.getName());
        String rol =authentication.getAuthorities().iterator().next().getAuthority();

        logServ.registerLogs(dto, user, request, rol);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<LogFilterDto>> getLogUsers(){
        return ResponseEntity.ok(logServ.getLogFilters());
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




