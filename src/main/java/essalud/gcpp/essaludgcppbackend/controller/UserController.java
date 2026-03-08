package essalud.gcpp.essaludgcppbackend.controller;

import essalud.gcpp.essaludgcppbackend.dtos.UsersDto;
import essalud.gcpp.essaludgcppbackend.serviceinterfaz.UsersService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ggi")
public class UserController {

    private final UsersService usersService;

    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/getInfoUser/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN','ADMIN_RED','USER')")
    public UsersDto getUser(@PathVariable String username) {
        return  usersService.getUserByUsername(username);
    }

}
