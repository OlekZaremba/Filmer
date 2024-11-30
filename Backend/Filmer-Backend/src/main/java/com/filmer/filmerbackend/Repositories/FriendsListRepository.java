package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.FriendsList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendsListRepository extends JpaRepository<FriendsList, Integer> {
    boolean existsByUser1AndUser2(int user1, int user2);


}
