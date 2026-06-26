package essalud.gcpp.essaludgcppbackend.service_implements;
import essalud.gcpp.essaludgcppbackend.model.usuario.Rol;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.RolRepository;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsersRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class   JwtUserDetailsService implements UserDetailsService {
    private final UsersRepository userRepository;
    private final RolRepository  rolRepository;

    public JwtUserDetailsService(UsersRepository userRepository,  RolRepository rolRepository) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            user = userRepository.findByEmail(username);
        }
        List<Rol> roles = rolRepository.listaRolesByUsername(username);
        if(user!=null && user.getFlag() && roles!=null) {
            List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
            for (Rol rol : roles) {
                grantedAuthorities.add(new SimpleGrantedAuthority(rol.getName()));
            }
            return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),true,true,true,true,grantedAuthorities);
        }
        throw new UsernameNotFoundException("este usuario "+username+" no existe  o no esta habilitado");
    }

}
