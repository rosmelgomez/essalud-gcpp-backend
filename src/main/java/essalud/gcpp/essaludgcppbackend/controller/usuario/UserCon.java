package essalud.gcpp.essaludgcppbackend.controller.usuario;

import essalud.gcpp.essaludgcppbackend.dtos.usuario.RolDto;
import essalud.gcpp.essaludgcppbackend.dtos.usuario.UserLoginDto;
import essalud.gcpp.essaludgcppbackend.dtos.usuario.UsersDto;
import essalud.gcpp.essaludgcppbackend.service.usuario.RolServ;
import essalud.gcpp.essaludgcppbackend.service.usuario.UsersServ;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserCon {

    private final AuthenticationManager authenticationManager;
    private final UsersServ usersServ;
    private final RolServ rolServ;

    public UserCon(AuthenticationManager authenticationManager, UsersServ usersServ,
                   RolServ rolServ) {
        this.authenticationManager = authenticationManager;
        this.usersServ = usersServ;
        this.rolServ = rolServ;
    }

    @GetMapping("/getUser/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public UsersDto getUserByUsername(@PathVariable String username) {
        return usersServ.getUserByUsernameOrEmail(username);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public UsersDto getCurrentUser() {
        return usersServ.getUserByUsernameOrEmail(Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName());
    }

    @GetMapping("/getUserByDni/{dni}")
    public UsersDto getUser(@PathVariable String dni) {
        return usersServ.getUserByDni(dni);
    }

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void saveUser(@RequestBody UsersDto user) {
        usersServ.saveUser(user);
    }

    @GetMapping("/getUsers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UsersDto> getUsers() {
        return usersServ.getAllUsers();
    }

    @PutMapping("/updateUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateUser(@RequestBody UsersDto user) {
        usersServ.updateUser(user);
    }

    @DeleteMapping("/deleteUser/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable Integer id) {
        usersServ.deleteUser(id);
    }

    @PostMapping("/verifyPassword")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public void verifyPassword(@RequestBody UserLoginDto user) throws Exception {
        authenticateUser(user.getUsername(), user.getPassword());
    }

    @PutMapping("/changePassword")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public void changePassword(@RequestBody UserLoginDto user) {
        usersServ.changePassword(user);
    }

    private void authenticateUser(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

    }

    @GetMapping("/getUsersByCodred/{code_red}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<UsersDto> getUserByCodred(@PathVariable String code_red) {
        return usersServ.getUsersByCod_red(code_red);
    }

    @GetMapping("/getUsersByCodcas/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<UsersDto> getUsersByCodcas(@PathVariable String codcas) {
        return usersServ.getUsersByCodcas(codcas);
    }

    @GetMapping("/getUsersById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public UsersDto getUsersById(@PathVariable Integer id) {
        return usersServ.getUserById(id);
    }

    @GetMapping("/getRoles")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<RolDto> getRoles() {
        return rolServ.getRols();
    }

}
