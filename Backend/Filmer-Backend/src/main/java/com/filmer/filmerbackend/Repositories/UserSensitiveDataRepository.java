package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.UserSensitiveData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSensitiveDataRepository extends JpaRepository<UserSensitiveData, Long> {
    Optional<UserSensitiveData> findByEmail(String email);
}
