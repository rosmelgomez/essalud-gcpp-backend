package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.dtos.UserLoginDto;
import essalud.gcpp.essaludgcppbackend.dtos.UsersDto;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.UsersService;
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

    public UserController(AuthenticationManager authenticationManager, UsersService usersService) {
        this.authenticationManager = authenticationManager;
        this.usersService = usersService;
    }

    @GetMapping("/getInfoUser/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public UsersDto getUser(@PathVariable String username) {
        return  usersService.getUserByUsername(username);
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
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public ResponseEntity<?> verifyPassword(@RequestBody UserLoginDto user) throws Exception {
        authenticateUser(user.getUsername(), user.getPassword());
        return  ResponseEntity.ok().body("password verified");
    }

    @PutMapping("/changePassword")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
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
}
