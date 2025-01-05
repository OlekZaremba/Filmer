package com.filmer.filmerbackend.ServicesImpl;


import com.filmer.filmerbackend.Entities.UserSensitiveData;
import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Repositories.UserSensitiveDataRepository;
import com.filmer.filmerbackend.Repositories.UsersRepository;
import com.filmer.filmerbackend.Services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementacja interfejsu UsersService do zarządzania użytkownikami i ich danymi.
 */
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

    /**
     * Wyszukuje użytkownika na podstawie jego pseudonimu.
     *
     * @param nick pseudonim użytkownika
     * @return obiekt Optional zawierający użytkownika, jeśli został znaleziony
     */
    @Override
    public Optional<Users> findUserByNick(String nick) {
        return usersRepository.findByNick(nick);
    }

    /**
     * Wyszukuje użytkownika na podstawie jego adresu e-mail.
     *
     * @param email adres e-mail użytkownika
     * @return obiekt Optional zawierający użytkownika, jeśli został znaleziony
     */
    @Override
    public Optional<Users> findUserByEmail(String email) {
        return userSensitiveDataRepository.findByEmail(email)
                .flatMap(userSensitiveData -> usersRepository.findById(userSensitiveData.getUser().getId_user()));
    }

    /**
     * Uwierzytelnia użytkownika na podstawie adresu e-mail i hasła.
     *
     * @param email    adres e-mail użytkownika
     * @param password hasło użytkownika
     * @return true, jeśli uwierzytelnienie zakończyło się sukcesem, false w przeciwnym razie
     */
    @Override
    public boolean authenticateUser(String email, String password) {
        Optional<UserSensitiveData> userSensitiveData = userSensitiveDataRepository.findByEmail(email);
        return userSensitiveData.isPresent() && passwordEncoder.matches(password, userSensitiveData.get().getPassword());
    }

    /**
     * Rejestruje nowego użytkownika.
     *
     * @param username pseudonim użytkownika
     * @param email    adres e-mail użytkownika
     * @param password hasło użytkownika
     * @return komunikat o statusie rejestracji
     */
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

    /**
     * Pobiera listę znajomych dla określonego użytkownika.
     *
     * @param userId identyfikator użytkownika
     * @return lista znajomych użytkownika
     */
    @Override
    public List<Users> getFriendsByUserId(int userId) {
        return usersRepository.findFriendsByUserId(userId);
    }
}
