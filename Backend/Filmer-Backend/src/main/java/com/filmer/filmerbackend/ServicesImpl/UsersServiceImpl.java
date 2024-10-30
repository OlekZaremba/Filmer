package com.filmer.filmerbackend.ServicesImpl;


import com.filmer.filmerbackend.Entities.UserSensitiveData;
import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Repositories.UserSensitiveDataRepository;
import com.filmer.filmerbackend.Repositories.UsersRepository;
import com.filmer.filmerbackend.Services.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final UserSensitiveDataRepository userSensitiveDataRepository;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, UserSensitiveDataRepository userSensitiveDataRepository) {
        this.usersRepository = usersRepository;
        this.userSensitiveDataRepository = userSensitiveDataRepository;
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
        return userSensitiveData.isPresent() && userSensitiveData.get().getPassword().equals(password);
    }

}
