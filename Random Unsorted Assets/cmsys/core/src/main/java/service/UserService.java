package service;

import dto.LoginCredentials;
import dto.UserDTO;
import dto.UserRegisterDTO;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loginAccount(LoginCredentials loginCredentials)
    {
        User requestUser = this.userRepository.findByUsernameAndPassword(loginCredentials.getUsername(), loginCredentials.getPassword());

        return requestUser;
    }

    public boolean emailExists(String email)
    {
        User result = userRepository.findByEmail(email);
        return result != null;
    }

    public boolean usernameExists(String username)
    {
        User result = userRepository.findByUsername(username);
        return result != null;
    }

    public List<String> getFirstTen(String username){
        List<User> usersFromDb = userRepository.findFirst10ByUsername(username);

        return usersFromDb.stream().map(User::getUsername).collect(Collectors.toList());
    }

    public User getByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }

    public User registerNewUserAccount(UserRegisterDTO accountDto) throws Exception {
        if (emailExists(accountDto.getEmail())) {
            throw new Exception(
                    "There is an account with that email address: " + accountDto.getEmail());
        }

        if (usernameExists(accountDto.getUsername())) {
            throw new Exception(
                    "There is an account with that email username: " + accountDto.getEmail());
        }

        User user = new User();
        user.setUsername(accountDto.getUsername());
        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setPassword(accountDto.getPassword());
        user.setEmail(accountDto.getEmail());

        return userRepository.save(user);
    }
}
