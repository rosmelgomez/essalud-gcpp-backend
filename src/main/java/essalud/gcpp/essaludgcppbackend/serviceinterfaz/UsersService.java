package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.dtos.UsersDto;
import essalud.gcpp.essaludgcppbackend.model.Users;
import essalud.gcpp.essaludgcppbackend.repositories.UsersRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public void saveUser(UsersDto usersDto) {
        Users user = usersRepository.findByUsername(usersDto.getUsername());
        if (user == null) {
            Users newUser = new Users();
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(usersDto.getPassword());
            newUser.setName(usersDto.getUsername());
            newUser.setLastname(usersDto.getLastname());
            newUser.setCenasicod(usersDto.getCenasicod());
            newUser.setRedasiscod(usersDto.getRedasiscod());
            newUser.setUsername(usersDto.getUsername());
            newUser.setPassword(encodedPassword);
            newUser.setRol(usersDto.getRol());
            newUser.setFlag(usersDto.getFlag());
            newUser.setDate_creation(LocalDateTime.now());
            newUser.setDate_update(LocalDateTime.now());
            usersRepository.save(newUser);
        }
    }

    public UsersDto getUserByUsername(String username) {
        Users user = usersRepository.findByUsername(username);
        return  user(user);
    }

    public List<UsersDto> getAllUsers() {
        List<Users> users = usersRepository.findAll();
        List<UsersDto> usersDtos = new ArrayList<>();
        for (Users user : users) {
            usersDtos.add(user(user));
        }
        return usersDtos;
    }

    private UsersDto user (Users  user) {
        UsersDto usersDto = new UsersDto();
        usersDto.setUsername(user.getUsername());
        usersDto.setLastname(user.getLastname());
        usersDto.setCenasicod(user.getCenasicod());
        usersDto.setRedasiscod(user.getRedasiscod());
        usersDto.setUsername(user.getUsername());
        usersDto.setPassword("");
        usersDto.setRol(user.getRol());
        usersDto.setFlag(user.getFlag());
        usersDto.setDate_creation(user.getDate_creation());
        usersDto.setDate_update(user.getDate_update());
        return usersDto;
    }

}
