package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.FriendsList;
import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Repositories.FriendsListRepository;
import com.filmer.filmerbackend.Repositories.UsersRepository;
import com.filmer.filmerbackend.Services.FriendsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FriendsServiceImpl implements FriendsService {

    private final UsersRepository usersRepository;
    private final FriendsListRepository friendsListRepository;

    public FriendsServiceImpl(UsersRepository usersRepository, FriendsListRepository friendsListRepository) {
        this.usersRepository = usersRepository;
        this.friendsListRepository = friendsListRepository;
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

        FriendsList friendsList = new FriendsList();
        friendsList.setUser1(userId);
        friendsList.setUser2(friendId);
        friendsList.setCreatedAt(new Date());

        friendsListRepository.save(friendsList);
    }
}
