package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByNick(String nick);
    @Query("SELECT u FROM users u JOIN friends_list f ON u.id_user = f.user2 WHERE f.user1 = :userId")
    List<Users> findFriendsByUserId(@Param("userId") int userId);


}
