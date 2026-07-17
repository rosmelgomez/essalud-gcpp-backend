package essalud.gcpp.essaludgcppbackend.service.usuario;

import essalud.gcpp.essaludgcppbackend.dtos.usuario.UserLoginDto;
import essalud.gcpp.essaludgcppbackend.dtos.usuario.UsersDto;
import essalud.gcpp.essaludgcppbackend.model.usuario.Rol;
import essalud.gcpp.essaludgcppbackend.model.usuario.UserRol;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.RolRep;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UserRolRep;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsersRep;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsersServ {

    private final UsersRep usersRep;
    private final RolRep rolRep;
    private final UserRolRep userRolRep;
    public UsersServ(UsersRep usersRep, RolRep rolRep, UserRolRep userRolRep) {
        this.usersRep = usersRep;
        this.rolRep = rolRep;
        this.userRolRep = userRolRep;
    }

    public void saveUser(UsersDto usersDto) {
        Users user = usersRep.findByUsername(usersDto.getUsername());
        if (user == null) {
            Users newUser = new Users();
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(usersDto.getPassword());

            newUser.setName(usersDto.getUsername());
            newUser.setLastname(usersDto.getLastname());
            newUser.setDni(usersDto.getDni());
            newUser.setCodcas(usersDto.getCodcas());
            newUser.setCodred(usersDto.getCodred());
            newUser.setUsername(usersDto.getUsername());
            newUser.setPassword(encodedPassword);
            newUser.setDate_creation(LocalDateTime.now());
            newUser.setDate_update(null);
            newUser.setFlag(usersDto.getFlag());
            newUser.setFirstLogin(true);

            newUser = usersRep.save(newUser);

            /// obtener roles y registrar user_rol
             for (String role : usersDto.getRoles()) {
                 Rol rol = rolRep.findRol(role);
                 UserRol  userRol = new UserRol();
                 userRol.setFlag(true);
                 userRol.setDate_creation(LocalDateTime.now());
                 userRol.setUsuario_rol(newUser);
                 userRol.setRol_user(rol);
                 userRolRep.save(userRol);
             }

        }
    }

    @Transactional
    public void updateUser(UsersDto usersDto) {

        Users user = usersRep.findById(usersDto.getId()).orElse(null);

        if (user != null) {
            user.setName(usersDto.getName());
            user.setLastname(usersDto.getLastname());
            user.setDni(usersDto.getDni());
            user.setCodcas(usersDto.getCodcas());
            user.setCodred(usersDto.getCodred());
            user.setUsername(usersDto.getUsername());
            user.setFlag(usersDto.getFlag());
            user.setDate_update(LocalDateTime.now());

            if (usersDto.getPassword() != null && !usersDto.getPassword().isEmpty()) {
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                user.setPassword(passwordEncoder.encode(usersDto.getPassword()));
            }

            usersRep.save(user);

            updateRolesUserByName(user, usersDto.getRoles());
        }


    }

    @Transactional
    public void updateRolesUserByName(Users user, List<String> rolesNames) {
        // 1. Obtener roles actuales del usuario (registros de la tabla intermedia UserRol)
        List<UserRol> asignacionesActuales = userRolRep.findByUserId(user.getId());

        // 2. Mapear nombres de roles actuales para facilitar la comparación
        Set<String> nombresActuales = asignacionesActuales.stream()
                .map(ur -> ur.getRol_user().getName())
                .collect(Collectors.toSet());

        // Asegurar que la lista de entrada no sea nula para evitar NullPointerException
        Set<String> nuevosNombres = (rolesNames != null) ? new HashSet<>(rolesNames) : new HashSet<>();

        // --- LÓGICA DE DESACTIVACIÓN ---
        // Si el rol está en la DB pero NO está en la lista nueva, lo desactivamos (o eliminamos)
        for (UserRol asignacion : asignacionesActuales) {
            if (!nuevosNombres.contains(asignacion.getRol_user().getName())) {
                // Opción A: Desactivar (si usas borrado lógico)
                asignacion.setFlag(false);
                userRolRep.save(asignacion);

                // Opción B: Si prefieres borrarlo físicamente:
                // userRolRepository.delete(asignacion);
            } else {
                // Si ya existe y estaba desactivado, lo volvemos a activar
                if (!asignacion.getFlag()) {
                    asignacion.setFlag(true);
                    userRolRep.save(asignacion);
                }
            }
        }

        // Si el rol está en la lista nueva pero NO está en la DB, lo creamos
        for (String nombreNuevo : nuevosNombres) {
            if (!nombresActuales.contains(nombreNuevo)) {
                Rol rol = rolRep.findRol(nombreNuevo);
                if (rol != null) {
                    UserRol nuevoUserRol = new UserRol();
                    nuevoUserRol.setUsuario_rol(user);
                    nuevoUserRol.setRol_user(rol);
                    nuevoUserRol.setDate_creation(LocalDateTime.now());
                    nuevoUserRol.setFlag(true);
                    userRolRep.save(nuevoUserRol);
                }
            }
        }
    }

    public UsersDto getUserByUsernameOrEmail(String username) {
        Users user = usersRep.findByUsername(username);
        if (user == null) {
            user = usersRep.findByEmail(username);
        }
        List<Rol> roles =  rolRep.listaRolesByUsername(user.getUsername());
        return user(user, roles);
    }

    public UsersDto getUserByDni(String dni) {
        Users user = usersRep.findByDni(dni);
        List<Rol> roles =  rolRep.listaRolesByUsername(user.getUsername());
        return user(user, roles);
    }

    public List<UsersDto> getAllUsers() {
        List<Users> users = usersRep.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<UsersDto> usersDtos = new ArrayList<>();
        for (Users user : users) {
            List<Rol> roles =  rolRep.listaRolesByUsername(user.getUsername());
            usersDtos.add(user(user, roles));
        }
        return usersDtos;
    }

    public void changePassword(UserLoginDto userLoginDto) {
        Users user = usersRep.findByUsername(userLoginDto.getUsername());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userLoginDto.getPassword());
        user.setPassword(encodedPassword);
        user.setFirstLogin(false);
        user.setDate_update(LocalDateTime.now());
        usersRep.save(user);
    }

    public void deleteUser(Integer id) {
        Users user = usersRep.findById(id).orElse(null);
        if (user != null) {
            user.setFlag(false);
            usersRep.save(user);
        }
    }

    public  List<UsersDto> getUsersByCod_red(String code_red) {
        List<UsersDto> usersDtos = new ArrayList<>();
        List<Users> users= usersRep.findByCodred(code_red);
        for (Users user : users) {
            List<Rol> roles =  rolRep.listaRolesByUsername(user.getUsername());
            usersDtos.add(user(user,roles));
        }
        return usersDtos;
    }

    public  List<UsersDto> getUsersByCodcas(String codcas) {
        List<UsersDto> usersDtos = new ArrayList<>();
        List<Users> users= usersRep.findByCodcas(codcas);
        for (Users user : users) {
            List<Rol> roles =  rolRep.listaRolesByUsername(user.getUsername());
            usersDtos.add(user(user,roles));
        }
        return usersDtos;
    }

    public UsersDto getUserById(Integer id) {
        Users user = usersRep.findById(id).orElse(null);
        assert user != null;
        List<Rol> roles =  rolRep.listaRolesByUsername(user.getUsername());
        return user(user,roles);
    }

    private UsersDto user (Users  user, List<Rol> roles) {
        UsersDto usersDto = new UsersDto();
        usersDto.setId(user.getId());
        usersDto.setName(user.getName());
        usersDto.setLastname(user.getLastname());
        usersDto.setDni(user.getDni());
        usersDto.setCodcas(user.getCodcas());
        usersDto.setCodred(user.getCodred());
        usersDto.setUsername(user.getUsername());
        usersDto.setPassword("");
        List<String> roleNames = new ArrayList<>();
        for (Rol role : roles) {
          roleNames.add(role.getName());
        }
        usersDto.setRoles(roleNames);
        usersDto.setFlag(user.getFlag());
        usersDto.setFirstLogin(user.getFirstLogin());
        usersDto.setDate_creation(user.getDate_creation());
        usersDto.setDate_update(user.getDate_update());
        return usersDto;
    }





}
