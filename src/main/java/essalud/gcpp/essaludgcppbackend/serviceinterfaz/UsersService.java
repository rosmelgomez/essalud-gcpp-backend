package essalud.gcpp.essaludgcppbackend.serviceinterfaz;

import essalud.gcpp.essaludgcppbackend.dtos.UserLoginDto;
import essalud.gcpp.essaludgcppbackend.dtos.UsersDto;
import essalud.gcpp.essaludgcppbackend.model.Rol;
import essalud.gcpp.essaludgcppbackend.model.Users;
import essalud.gcpp.essaludgcppbackend.repositories.UsersRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            List<Rol> roles = new ArrayList<>();
            Users newUser = new Users();
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(usersDto.getPassword());
            /*newUser.setName(usersDto.getUsername());
            newUser.setLastname(usersDto.getLastname());
            newUser.setDni(usersDto.getDni());
            newUser.setCodcas(usersDto.getCodcas());
            newUser.setCode_red(usersDto.getCode_red());
            */
            newUser.setUsername(usersDto.getUsername());
            newUser.setPassword(encodedPassword);
            //newUser.setRol(usersDto.getRol());
            newUser.setFlag(usersDto.getFlag());
            newUser.setDate_creation(LocalDateTime.now());
            newUser.setDate_update(LocalDateTime.now());
            usersRepository.save(newUser);
        }
    }

    @Transactional(readOnly = true)
    public Users getUserByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public List<UsersDto> getAllUsers() {
        List<Users> users = usersRepository.findAll();
        List<UsersDto> usersDtos = new ArrayList<>();
        for (Users user : users) {
            usersDtos.add(user(user));
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

    public void updateUser(UsersDto usersDto) {
        Users user = usersRepository.findById(usersDto.getId()).orElse(null);
        if (user != null) {
            /*user.setName(usersDto.getName());
            user.setLastname(usersDto.getLastname());
            user.setDni(usersDto.getDni());
            user.setCodcas(usersDto.getCodcas());
            user.setCode_red(usersDto.getCode_red());
             */

            user.setUsername(usersDto.getUsername());
            user.setFlag(usersDto.getFlag());
            //user.setRol(usersDto.getRol());
            if (usersDto.getPassword() != null) {
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode(usersDto.getPassword());
                user.setPassword(encodedPassword);
            }
            usersRepository.save(user);
        }
    }

    public void deleteUser(Integer id) {
        usersRepository.findById(id).ifPresent(usersRepository::delete);
    }

    private UsersDto user (Users  user) {
        UsersDto usersDto = new UsersDto();
        usersDto.setId(user.getId());
        /*
        usersDto.setName(user.getName());
        usersDto.setLastname(user.getLastname());
        usersDto.setDni(user.getDni());
        usersDto.setCodcas(user.getCodcas());
        usersDto.setCode_red(user.getCode_red());
         */
        usersDto.setUsername(user.getUsername());
        usersDto.setPassword("");
        //usersDto.setRol(user.getRol());
        usersDto.setFlag(user.getFlag());
        usersDto.setDate_creation(user.getDate_creation());
        usersDto.setDate_update(user.getDate_update());
        return usersDto;
    }


}
