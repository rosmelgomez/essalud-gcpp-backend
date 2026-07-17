package essalud.gcpp.essaludgcppbackend.serviceQA;
import essalud.gcpp.essaludgcppbackend.model.usuario.Rol;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.RolRep;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsersRep;
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
public class JwtUserDetailsServ implements UserDetailsService {
    private final UsersRep userRepository;
    private final RolRep rolRep;

    public JwtUserDetailsServ(UsersRep userRepository, RolRep rolRep) {
        this.userRepository = userRepository;
        this.rolRep = rolRep;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            user = userRepository.findByEmail(username);
        }
        List<Rol> roles = rolRep.listaRolesByUsername(username);
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
