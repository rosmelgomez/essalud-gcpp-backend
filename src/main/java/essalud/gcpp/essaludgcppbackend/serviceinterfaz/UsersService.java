package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.dtos.UserLoginDto;
import essalud.gcpp.essaludgcppbackend.dtos.UsersDto;
import essalud.gcpp.essaludgcppbackend.model.usuario.Rol;
import essalud.gcpp.essaludgcppbackend.model.usuario.UserRol;
import essalud.gcpp.essaludgcppbackend.model.usuario.Users;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.RolRepository;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UserRolRepository;
import essalud.gcpp.essaludgcppbackend.repositories.usuario.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    private final RolRepository rolRepository;
    private final UserRolRepository userRolRepository;
    public UsersService(UsersRepository usersRepository,  RolRepository rolRepository,  UserRolRepository userRolRepository) {
        this.usersRepository = usersRepository;
        this.rolRepository = rolRepository;
        this.userRolRepository = userRolRepository;
    }

    public void saveUser(UsersDto usersDto) {
        Users user = usersRepository.findByUsername(usersDto.getUsername());
        if (user == null) {
            Users newUser = new Users();
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(usersDto.getPassword());

            newUser.setName(usersDto.getUsername());
            newUser.setLastname(usersDto.getLastname());
            newUser.setDni(usersDto.getDni());
            newUser.setCodcas(usersDto.getCodcas());
            newUser.setCode_red(usersDto.getCode_red());
            newUser.setUsername(usersDto.getUsername());
            newUser.setPassword(encodedPassword);
            newUser.setFlag(usersDto.getFlag());
            newUser.setDate_creation(LocalDateTime.now());
            newUser.setDate_update(LocalDateTime.now());
            newUser = usersRepository.save(newUser);

            /// obtener roles y registrar user_rol
             for (String role : usersDto.getRoles()) {
                 Rol rol = rolRepository.findRol(role);
                 UserRol  userRol = new UserRol();
                 userRol.setFlag(true);
                 userRol.setDate_creation(LocalDateTime.now());
                 userRol.setUsuario_rol(newUser);
                 userRol.setRol_user(rol);
                 userRolRepository.save(userRol);
             }

        }
    }

    public UsersDto getUserByUsernameOrEmail(String username) {
        Users user = usersRepository.findByUsername(username);
        if (user == null) {
            user = usersRepository.findByEmail(username);
        }
        List<Rol> roles =  rolRepository.listaRolesByUsername(user.getUsername());
        return user(user, roles);
    }

    public UsersDto getUserByDni(String dni) {
        Users user = usersRepository.findByDni(dni);
        List<Rol> roles =  rolRepository.listaRolesByUsername(user.getUsername());
        return user(user, roles);
    }

    public List<UsersDto> getAllUsers() {
        List<Users> users = usersRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<UsersDto> usersDtos = new ArrayList<>();
        for (Users user : users) {
            List<Rol> roles =  rolRepository.listaRolesByUsername(user.getUsername());
            usersDtos.add(user(user, roles));
        }
        return usersDtos;
    }

    public void changePassword(UserLoginDto userLoginDto) {
        Users user = usersRepository.findByUsername(userLoginDto.getUsername());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userLoginDto.getPassword());
        user.setPassword(encodedPassword);
        user.setDate_update(LocalDateTime.now());
        usersRepository.save(user);
    }

    @Transactional
    public void updateUser(UsersDto usersDto) {

        Users user = usersRepository.findById(usersDto.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setName(usersDto.getName());
        user.setLastname(usersDto.getLastname());
        user.setDni(usersDto.getDni());
        user.setCodcas(usersDto.getCodcas());
        user.setCode_red(usersDto.getCode_red());
        user.setUsername(usersDto.getUsername());
        user.setFlag(usersDto.getFlag());

        if (usersDto.getPassword() != null && !usersDto.getPassword().isEmpty()) {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setPassword(passwordEncoder.encode(usersDto.getPassword()));
        }

        usersRepository.save(user);

        updateRolesUserByName(user, usersDto.getRoles());
    }
    @Transactional
    public void updateRolesUserByName(Users user, List<String> rolesNames) {
        // 1. Obtener roles actuales del usuario (registros de la tabla intermedia UserRol)
        List<UserRol> asignacionesActuales = userRolRepository.findByUserId(user.getId());

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
                userRolRepository.save(asignacion);

                // Opción B: Si prefieres borrarlo físicamente:
                // userRolRepository.delete(asignacion);
            } else {
                // Si ya existe y estaba desactivado, lo volvemos a activar
                if (!asignacion.getFlag()) {
                    asignacion.setFlag(true);
                    userRolRepository.save(asignacion);
                }
            }
        }

        // --- LÓGICA DE ADICIÓN ---
        // Si el rol está en la lista nueva pero NO está en la DB, lo creamos
        for (String nombreNuevo : nuevosNombres) {
            if (!nombresActuales.contains(nombreNuevo)) {
                Rol rol = rolRepository.findRol(nombreNuevo);
                if (rol != null) {
                    UserRol nuevoUserRol = new UserRol();
                    nuevoUserRol.setUsuario_rol(user);
                    nuevoUserRol.setRol_user(rol);
                    nuevoUserRol.setDate_creation(LocalDateTime.now());
                    nuevoUserRol.setFlag(true);
                    userRolRepository.save(nuevoUserRol);
                }
            }
        }
    }

    public void deleteUser(Integer id) {
        Users user = usersRepository.findById(id).orElse(null);
        if (user != null) {
            user.setFlag(false);
            usersRepository.save(user);
        }
    }

    public  List<UsersDto> getUsersByCod_red(String code_red) {
        List<UsersDto> usersDtos = new ArrayList<>();
        List<Users> users= usersRepository.findByCode_red(code_red);
        for (Users user : users) {
            usersDtos.add(user(user,null));
        }
        return usersDtos;
    }

    private UsersDto user (Users  user, List<Rol> roles) {
        UsersDto usersDto = new UsersDto();
        usersDto.setId(user.getId());
        usersDto.setName(user.getName());
        usersDto.setLastname(user.getLastname());
        usersDto.setDni(user.getDni());
        usersDto.setCodcas(user.getCodcas());
        usersDto.setCode_red(user.getCode_red());
        usersDto.setUsername(user.getUsername());
        usersDto.setPassword("");
        List<String> roleNames = new ArrayList<>();
        for (Rol role : roles) {
          roleNames.add(role.getName());
        }
        usersDto.setRoles(roleNames);
        usersDto.setFlag(user.getFlag());
        usersDto.setDate_creation(user.getDate_creation());
        usersDto.setDate_update(user.getDate_update());
        return usersDto;
    }





}
