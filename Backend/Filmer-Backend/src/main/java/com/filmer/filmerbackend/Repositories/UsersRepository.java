package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByNick(String nick);
}
