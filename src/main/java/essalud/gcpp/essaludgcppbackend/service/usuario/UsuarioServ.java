package essalud.gcpp.essaludgcppbackend.service.usuario;

import essalud.gcpp.essaludgcppbackend.dtos.usuario.UsuarioLoginDto;
import essalud.gcpp.essaludgcppbackend.dtos.usuario.UsuarioDto;
import essalud.gcpp.essaludgcppbackend.model.usuario.Rol;
import essalud.gcpp.essaludgcppbackend.model.usuario.UsuarioRol;
import essalud.gcpp.essaludgcppbackend.model.usuario.Usuario;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.RolRep;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsuarioRolRep;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsuarioRep;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioServ {

    private final UsuarioRep usuarioRep;
    private final RolRep rolRep;
    private final UsuarioRolRep usuarioRolRep;
    public UsuarioServ(UsuarioRep usuarioRep, RolRep rolRep, UsuarioRolRep usuarioRolRep) {
        this.usuarioRep = usuarioRep;
        this.rolRep = rolRep;
        this.usuarioRolRep = usuarioRolRep;
    }

    public void saveUser(UsuarioDto usuarioDto) {
        Usuario user = usuarioRep.findByUsername(usuarioDto.getUsername());
        if (user == null) {
            Usuario newUser = new Usuario();
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(usuarioDto.getPassword());

            newUser.setName(usuarioDto.getUsername());
            newUser.setLastname(usuarioDto.getLastname());
            newUser.setDni(usuarioDto.getDni());
            newUser.setCodcas(usuarioDto.getCodcas());
            newUser.setCodred(usuarioDto.getCodred());
            newUser.setUsername(usuarioDto.getUsername());
            newUser.setPassword(encodedPassword);
            newUser.setDate_creation(LocalDateTime.now());
            newUser.setDate_update(null);
            newUser.setFlag(usuarioDto.getFlag());
            newUser.setFirst_Login(true);

            newUser = usuarioRep.save(newUser);

            /// obtener roles y registrar user_rol
             for (String role : usuarioDto.getRoles()) {
                 Rol rol = rolRep.findRol(role);
                 UsuarioRol usuarioRol = new UsuarioRol();
                 usuarioRol.setFlag(true);
                 usuarioRol.setDate_creation(LocalDateTime.now());
                 usuarioRol.setUsuario_rol(newUser);
                 usuarioRol.setRol_user(rol);
                 usuarioRolRep.save(usuarioRol);
             }

        }
    }

    @Transactional
    public void updateUser(UsuarioDto usuarioDto) {

        Usuario user = usuarioRep.findById(usuarioDto.getId()).orElse(null);

        if (user != null) {
            user.setName(usuarioDto.getName());
            user.setLastname(usuarioDto.getLastname());
            user.setDni(usuarioDto.getDni());
            user.setCodcas(usuarioDto.getCodcas());
            user.setCodred(usuarioDto.getCodred());
            user.setUsername(usuarioDto.getUsername());
            user.setFlag(usuarioDto.getFlag());
            user.setDate_update(LocalDateTime.now());

            if (usuarioDto.getPassword() != null && !usuarioDto.getPassword().isEmpty()) {
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                user.setPassword(passwordEncoder.encode(usuarioDto.getPassword()));
            }

            usuarioRep.save(user);

            updateRolesUserByName(user, usuarioDto.getRoles());
        }


    }

    @Transactional
    public void updateRolesUserByName(Usuario user, List<String> rolesNames) {
        // Obtener roles actuales del usuario
        List<UsuarioRol> asignacionesActuales = usuarioRolRep.findByUserId(user.getId());

        // Mapear nombres de roles actuales para facilitar la comparación
        Set<String> nombresActuales = asignacionesActuales.stream()
                .map(ur -> ur.getRol_user().getName())
                .collect(Collectors.toSet());

        // Asegurar que la lista de entrada no sea nula para evitar NullPointerException
        Set<String> nuevosNombres = (rolesNames != null) ? new HashSet<>(rolesNames) : new HashSet<>();

        // --- LÓGICA DE DESACTIVACIÓN ---
        // Si el rol está en la DB pero NO está en la lista nueva, lo desactivamos (o eliminamos)
        for (UsuarioRol asignacion : asignacionesActuales) {
            if (!nuevosNombres.contains(asignacion.getRol_user().getName())) {
                // Opción A: Desactivar (si usas borrado lógico)
                asignacion.setFlag(false);
                usuarioRolRep.save(asignacion);

                // Opción B: Si prefieres borrarlo físicamente:
                // userRolRepository.delete(asignacion);
            } else {
                // Si ya existe y estaba desactivado, lo volvemos a activar
                if (!asignacion.getFlag()) {
                    asignacion.setFlag(true);
                    usuarioRolRep.save(asignacion);
                }
            }
        }

        // Si el rol está en la lista nueva pero NO está en la DB, lo creamos
        for (String nombreNuevo : nuevosNombres) {
            if (!nombresActuales.contains(nombreNuevo)) {
                Rol rol = rolRep.findRol(nombreNuevo);
                if (rol != null) {
                    UsuarioRol nuevoUsuarioRol = new UsuarioRol();
                    nuevoUsuarioRol.setUsuario_rol(user);
                    nuevoUsuarioRol.setRol_user(rol);
                    nuevoUsuarioRol.setDate_creation(LocalDateTime.now());
                    nuevoUsuarioRol.setFlag(true);
                    usuarioRolRep.save(nuevoUsuarioRol);
                }
            }
        }
    }

    public UsuarioDto getUserByUsernameOrEmail(String username) {
        Usuario user = usuarioRep.findByUsername(username);
        if (user == null) {
            user = usuarioRep.findByEmail(username);
        }
        List<Rol> roles =  rolRep.listaRolesByUsername(user.getUsername());
        return user(user, roles);
    }

    public UsuarioDto getUserByDni(String dni) {
        Usuario user = usuarioRep.findByDni(dni);
        List<Rol> roles =  rolRep.listaRolesByUsername(user.getUsername());
        return user(user, roles);
    }

    public List<UsuarioDto> getAllUsers() {
        List<Usuario> users = usuarioRep.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<UsuarioDto> usuarioDtos = new ArrayList<>();
        for (Usuario user : users) {
            List<Rol> roles =  rolRep.listaRolesByUsername(user.getUsername());
            usuarioDtos.add(user(user, roles));
        }
        return usuarioDtos;
    }

    public void changePassword(UsuarioLoginDto usuarioLoginDto) {
        Usuario user = usuarioRep.findByUsername(usuarioLoginDto.getUsername());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(usuarioLoginDto.getPassword());
        user.setPassword(encodedPassword);
        user.setFirst_Login(false);
        user.setDate_update(LocalDateTime.now());
        usuarioRep.save(user);
    }

    public void deleteUser(Integer id) {
        Usuario user = usuarioRep.findById(id).orElse(null);
        if (user != null) {
            user.setFlag(false);
            usuarioRep.save(user);
        }
    }

    public  List<UsuarioDto> getUsersByCod_red(String code_red) {
        List<UsuarioDto> usuarioDtos = new ArrayList<>();
        List<Usuario> users= usuarioRep.findByCodred(code_red);
        for (Usuario user : users) {
            List<Rol> roles =  rolRep.listaRolesByUsername(user.getUsername());
            usuarioDtos.add(user(user,roles));
        }
        return usuarioDtos;
    }

    public  List<UsuarioDto> getUsersByCodcas(String codcas) {
        List<UsuarioDto> usuarioDtos = new ArrayList<>();
        List<Usuario> users= usuarioRep.findByCodcas(codcas);
        for (Usuario user : users) {
            List<Rol> roles =  rolRep.listaRolesByUsername(user.getUsername());
            usuarioDtos.add(user(user,roles));
        }
        return usuarioDtos;
    }

    public UsuarioDto getUserById(Integer id) {
        Usuario user = usuarioRep.findById(id).orElse(null);
        assert user != null;
        List<Rol> roles =  rolRep.listaRolesByUsername(user.getUsername());
        return user(user,roles);
    }

    private UsuarioDto user (Usuario user, List<Rol> roles) {
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setId(user.getId());
        usuarioDto.setName(user.getName());
        usuarioDto.setLastname(user.getLastname());
        usuarioDto.setDni(user.getDni());
        usuarioDto.setCodcas(user.getCodcas());
        usuarioDto.setCodred(user.getCodred());
        usuarioDto.setUsername(user.getUsername());
        usuarioDto.setPassword("");
        List<String> roleNames = new ArrayList<>();
        for (Rol role : roles) {
          roleNames.add(role.getName());
        }
        usuarioDto.setRoles(roleNames);
        usuarioDto.setFlag(user.getFlag());
        usuarioDto.setFirstLogin(user.getFirst_Login());
        usuarioDto.setDate_creation(user.getDate_creation());
        usuarioDto.setDate_update(user.getDate_update());
        return usuarioDto;
    }





}
