package com.filmer.filmerbackend.ServicesImpl;


import com.filmer.filmerbackend.Entities.UserSensitiveData;
import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Repositories.UserSensitiveDataRepository;
import com.filmer.filmerbackend.Repositories.UsersRepository;
import com.filmer.filmerbackend.Services.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final UserSensitiveDataRepository userSensitiveDataRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, UserSensitiveDataRepository userSensitiveDataRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.userSensitiveDataRepository = userSensitiveDataRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<Users> findUserByNick(String nick) {
        return usersRepository.findByNick(nick);
    }

    @Override
    public Optional<Users> findUserByEmail(String email) {
        return userSensitiveDataRepository.findByEmail(email)
                .flatMap(userSensitiveData -> usersRepository.findById(userSensitiveData.getUser().getId_user()));
    }

    @Override
    public boolean authenticateUser(String email, String password) {
        Optional<UserSensitiveData> userSensitiveData = userSensitiveDataRepository.findByEmail(email);
        return userSensitiveData.isPresent() && passwordEncoder.matches(password, userSensitiveData.get().getPassword());
    }

    @Override
    public String registerUser(String username, String email, String password) {
        if (usersRepository.findByNick(username).isPresent() || userSensitiveDataRepository.findByEmail(email).isPresent()) {
            return "User already exists";
        }

        Users user = new Users();
        user.setNick(username);
        Users savedUser = usersRepository.save(user);

        UserSensitiveData sensitiveData = new UserSensitiveData();
        sensitiveData.setEmail(email);
        sensitiveData.setPassword(passwordEncoder.encode(password));
        sensitiveData.setUser(savedUser);
        userSensitiveDataRepository.save(sensitiveData);

        return "User registered successfully";
    }

    @Override
    public List<Users> getFriendsByUserId(int userId) {
        return usersRepository.findFriendsByUserId(userId);
    }
}
