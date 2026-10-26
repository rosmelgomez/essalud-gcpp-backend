package essalud.gcpp.essaludgcppbackend.serviceQA;
import essalud.gcpp.essaludgcppbackend.model.usuario.Rol;
import essalud.gcpp.essaludgcppbackend.model.usuario.Usuario;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.RolRep;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsuarioRep;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullableProblems")
@Service
public class JwtUserDetailsServ implements UserDetailsService {
    /// Autoridad extra para usuarios que aun no cambian la contrasena inicial; JwtRequestFilter limita sus rutas.
    public static final String PASSWORD_CHANGE_REQUIRED = "PASSWORD_CHANGE_REQUIRED";

    private final UsuarioRep userRepository;
    private final RolRep rolRep;

    public JwtUserDetailsServ(UsuarioRep userRepository, RolRep rolRep) {
        this.userRepository = userRepository;
        this.rolRep = rolRep;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public @Nullable UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        Usuario user = userRepository.findByUsername(username);
        if (user == null) {
            user = userRepository.findByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException("este usuario "+username+" no existe  o no esta habilitado");
            }
        }
        List<Rol> roles = rolRep.listaRolesByUsername(username);
        if(user.getFlag() && roles != null) {
            List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
            for (Rol rol : roles) {
                grantedAuthorities.add(new SimpleGrantedAuthority(rol.getName()));
            }
            if (Boolean.TRUE.equals(user.getFirst_Login())) {
                grantedAuthorities.add(new SimpleGrantedAuthority(PASSWORD_CHANGE_REQUIRED));
            }
            return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),true,true,true,true,grantedAuthorities);
        }

        throw new UsernameNotFoundException("este usuario "+username+" no existe  o no esta habilitado");
    }

}
