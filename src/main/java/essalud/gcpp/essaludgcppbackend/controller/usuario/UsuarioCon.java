package essalud.gcpp.essaludgcppbackend.controller.usuario;

import essalud.gcpp.essaludgcppbackend.dtos.usuario.RolDto;
import essalud.gcpp.essaludgcppbackend.dtos.usuario.UsuarioLoginDto;
import essalud.gcpp.essaludgcppbackend.dtos.usuario.UsuarioDto;
import essalud.gcpp.essaludgcppbackend.security.CurrentUser;
import essalud.gcpp.essaludgcppbackend.service.usuario.RolServ;
import essalud.gcpp.essaludgcppbackend.service.usuario.UsuarioServ;
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
public class UsuarioCon {

    private final AuthenticationManager authenticationManager;
    private final UsuarioServ usuarioServ;
    private final RolServ rolServ;

    public UsuarioCon(AuthenticationManager authenticationManager, UsuarioServ usuarioServ,
                      RolServ rolServ) {
        this.authenticationManager = authenticationManager;
        this.usuarioServ = usuarioServ;
        this.rolServ = rolServ;
    }

    @GetMapping("/getbyusername/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public UsuarioDto getUserByUsername(@PathVariable String username) {
        return usuarioServ.getUserByUsernameOrEmail(username);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public UsuarioDto getCurrentUser() {
        return usuarioServ.getUserByUsernameOrEmail(Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName());
    }

    /// Endpoint publico (formulario de solicitud de acceso): solo expone nombre y apellidos.
    @GetMapping("/getbydni/{dni}")
    public UsuarioDto getUser(@PathVariable String dni) {
        return usuarioServ.getPublicUserByDni(dni);
    }

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void saveUser(@RequestBody UsuarioDto user) {
        usuarioServ.saveUser(user);
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UsuarioDto> getUsers() {
        return usuarioServ.getAllUsers();
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateUser(@RequestBody UsuarioDto user) {
        usuarioServ.updateUser(user);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable Integer id) {
        usuarioServ.deleteUser(id);
    }

    @PostMapping("/verifypassword")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public void verifyPassword(@RequestBody UsuarioLoginDto user) throws Exception {
        // Solo se verifica la contrasena del usuario autenticado, nunca la de un tercero.
        authenticateUser(CurrentUser.username(), user.getPassword());
    }

    @PutMapping("/changepassword")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public void changePassword(@RequestBody UsuarioLoginDto user) {
        // El usuario se toma del token: el username del body se ignora.
        usuarioServ.changePassword(CurrentUser.username(), user.getPassword());
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

    @GetMapping("/getallbycodred/{code_red}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<UsuarioDto> getUserByCodred(@PathVariable String code_red) {
        return usuarioServ.getUsersByCod_red(code_red);
    }

    @GetMapping("/getallbycodcas/{codcas}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<UsuarioDto> getUsersByCodcas(@PathVariable String codcas) {
        return usuarioServ.getUsersByCodcas(codcas);
    }

    @GetMapping("/getbyid/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public UsuarioDto getUsersById(@PathVariable Integer id) {
        return usuarioServ.getUserById(id);
    }

    @GetMapping("/getroles")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER','CONSULTA')")
    public List<RolDto> getRoles() {
        return rolServ.getRols();
    }

}
