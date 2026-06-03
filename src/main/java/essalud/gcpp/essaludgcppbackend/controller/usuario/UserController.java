package essalud.gcpp.essaludgcppbackend.controller.usuario;

import essalud.gcpp.essaludgcppbackend.dtos.usuario.RolDto;
import essalud.gcpp.essaludgcppbackend.dtos.usuario.UserLoginDto;
import essalud.gcpp.essaludgcppbackend.dtos.usuario.UsersDto;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.usuario.RolService;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.usuario.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UsersService usersService;
    private final RolService rolService;

    public UserController(AuthenticationManager authenticationManager, UsersService usersService,  RolService rolService) {
        this.authenticationManager = authenticationManager;
        this.usersService = usersService;
        this.rolService = rolService;
    }

    @GetMapping("/getUser/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public UsersDto getUserByUsername(@PathVariable String username) {
        return  usersService.getUserByUsernameOrEmail(username);
    }

    @GetMapping("/getUserByDni/{dni}")
    public UsersDto getUser(@PathVariable String dni) {
        return  usersService.getUserByDni(dni);
    }

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void saveUser(@RequestBody UsersDto user) {
        usersService.saveUser(user);
    }

    @GetMapping("/getUsers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UsersDto> getUsers() {
        return usersService.getAllUsers();
    }

    @PutMapping("/updateUser")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateUser(@RequestBody UsersDto user) {
        usersService.updateUser(user);
    }

    @DeleteMapping("/deleteUser/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable Integer id ) {
        usersService.deleteUser(id);
    }

    @PostMapping("/verifyPassword")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public ResponseEntity<?> verifyPassword(@RequestBody UserLoginDto user) throws Exception {
        authenticateUser(user.getUsername(), user.getPassword());
        return  ResponseEntity.ok().body("password verified");
    }

    @PutMapping("/changePassword")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public void changePassword(@RequestBody UserLoginDto user) {
        usersService.changePassword(user);
    }

    private void authenticateUser(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }catch(DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        }catch(BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

    }

    @GetMapping("/getUsersByCodred/{code_red}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<UsersDto> getUserByCodred(@PathVariable String code_red) {
        return usersService.getUsersByCod_red(code_red);
    }

    @GetMapping("/getUsersByCodcas/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<UsersDto> getUsersByCodcas(@PathVariable String codcas) {
        return usersService.getUsersByCodcas(codcas);
    }


    @GetMapping("/getUsersById/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public UsersDto getUsersById(@PathVariable Integer id) {
        return usersService.getUserById(id);
    }

    @GetMapping("/getRoles")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<RolDto> getRoles() {
        return rolService.getRols();
    }

}
