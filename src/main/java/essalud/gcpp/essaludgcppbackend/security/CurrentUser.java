package essalud.gcpp.essaludgcppbackend.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/// Acceso al usuario autenticado de la peticion actual.
/// Las operaciones sobre recursos propios deben usar esta identidad y no ids enviados por el cliente.
public final class CurrentUser {

    private CurrentUser() {
    }

    public static @Nullable String username() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return auth.getName();
    }

    public static boolean hasAnyAuthority(String... authorities) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        for (String authority : authorities) {
            if (auth.getAuthorities().stream().anyMatch(a -> authority.equalsIgnoreCase(a.getAuthority()))) {
                return true;
            }
        }
        return false;
    }
}
