package essalud.gcpp.essaludgcppbackend.security;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/// Limita los intentos fallidos de login por usuario para frenar ataques de fuerza bruta.
/// En memoria: suficiente para una sola instancia del backend.
@Component
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final Duration LOCK_TIME = Duration.ofMinutes(15);

    private record Attempts(int count, Instant firstFailure) {
    }

    private final Map<String, Attempts> attempts = new ConcurrentHashMap<>();

    public boolean isBlocked(String username) {
        Attempts a = attempts.get(key(username));
        if (a == null) {
            return false;
        }
        if (a.firstFailure().plus(LOCK_TIME).isBefore(Instant.now())) {
            attempts.remove(key(username));
            return false;
        }
        return a.count() >= MAX_ATTEMPTS;
    }

    public void loginFailed(String username) {
        Instant now = Instant.now();
        if (attempts.size() > 10_000) {
            attempts.values().removeIf(a -> a.firstFailure().plus(LOCK_TIME).isBefore(now));
        }
        attempts.compute(key(username), (k, a) -> {
            if (a == null || a.firstFailure().plus(LOCK_TIME).isBefore(now)) {
                return new Attempts(1, now);
            }
            return new Attempts(a.count() + 1, a.firstFailure());
        });
    }

    public void loginSucceeded(String username) {
        attempts.remove(key(username));
    }

    private String key(String username) {
        return username.trim().toLowerCase(Locale.ROOT);
    }
}
