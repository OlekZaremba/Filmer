package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.FriendsList;
import com.filmer.filmerbackend.Entities.UserSensitiveData;
import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Repositories.FriendsListRepository;
import com.filmer.filmerbackend.Repositories.UserSensitiveDataRepository;
import com.filmer.filmerbackend.Repositories.UsersRepository;
import com.filmer.filmerbackend.Services.FriendsService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Service
public class FriendsServiceImpl implements FriendsService {

    private final UsersRepository usersRepository;
    private final FriendsListRepository friendsListRepository;
    private final UserSensitiveDataRepository userSensitiveDataRepository;
    private final JavaMailSender mailSender;

    public FriendsServiceImpl(UsersRepository usersRepository, FriendsListRepository friendsListRepository, UserSensitiveDataRepository userSensitiveDataRepository, JavaMailSender mailSender) {
        this.usersRepository = usersRepository;
        this.friendsListRepository = friendsListRepository;
        this.userSensitiveDataRepository = userSensitiveDataRepository;
        this.mailSender = mailSender;
    }

    @Override
    public List<Users> getFriendsByUserId(int userId) {
        return usersRepository.findFriendsByUserId(userId);
    }

    @Override
    public List<Users> searchUsersByNick(String nick) {
        return usersRepository.findByPartialNick(nick);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if (userId == friendId) {
            throw new IllegalArgumentException("Nie możesz dodać samego siebie do znajomych.");
        }

        boolean userExists = usersRepository.existsById(userId);
        boolean friendExists = usersRepository.existsById(friendId);
        if (!userExists || !friendExists) {
            throw new IllegalArgumentException("Jeden z użytkowników nie istnieje.");
        }

        boolean relationshipExists = friendsListRepository.existsByUser1AndUser2(userId, friendId)
                || friendsListRepository.existsByUser1AndUser2(friendId, userId);
        if (relationshipExists) {
            throw new IllegalArgumentException("Relacja już istnieje między tymi użytkownikami.");
        }

        Date now = new Date();

        FriendsList friendsList1 = new FriendsList();
        friendsList1.setUser1(userId);
        friendsList1.setUser2(friendId);
        friendsList1.setCreatedAt(now);
        friendsListRepository.save(friendsList1);

        FriendsList friendsList2 = new FriendsList();
        friendsList2.setUser1(friendId);
        friendsList2.setUser2(userId);
        friendsList2.setCreatedAt(now);
        friendsListRepository.save(friendsList2);
    }


    @Override
    public void uploadProfilePicture(int userId, MultipartFile file) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie istnieje."));

        try {
            byte[] imageBytes = file.getBytes();
            user.setProfilePicture(imageBytes);
            usersRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Nie udało się zapisać zdjęcia profilowego.", e);
        }
    }

    @Override
    public byte[] getProfilePicture(int userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie istnieje."));

        return user.getProfilePicture();
    }

    @Override
    public void sendInviteEmail(int friendId, String lobbyLink) {
        Users user = usersRepository.findById(friendId)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie istnieje."));

        UserSensitiveData userSensitiveData = userSensitiveDataRepository.findById(user.getUserSensitiveData().getId_user_sensitive_data())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono danych wrażliwych użytkownika."));

        String friendEmail = userSensitiveData.getEmail();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(friendEmail);
        message.setSubject("Zaproszenie do lobby");
        message.setText("Cześć!\n\nTwój znajomy zaprosił Cię do dołączenia do lobby.\nKliknij poniższy link, aby dołączyć: " + lobbyLink + "\n\nMiłej zabawy!");

        try {
            mailSender.send(message);
            System.out.println("Zaproszenie wysłane do " + friendEmail);
        } catch (Exception e) {
            throw new RuntimeException("Nie udało się wysłać zaproszenia e-mailem.", e);
        }
    }

}
