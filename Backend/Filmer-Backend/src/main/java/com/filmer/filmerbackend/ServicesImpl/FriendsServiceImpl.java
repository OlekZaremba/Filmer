package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Repositories.UsersRepository;
import com.filmer.filmerbackend.Services.FriendsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendsServiceImpl implements FriendsService {

    private final UsersRepository usersRepository;

    public FriendsServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public List<Users> getFriendsByUserId(int userId) {
        return usersRepository.findFriendsByUserId(userId);
    }
}
