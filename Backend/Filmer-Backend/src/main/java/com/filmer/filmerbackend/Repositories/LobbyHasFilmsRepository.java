package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.LobbyHasFilms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LobbyHasFilmsRepository extends JpaRepository<LobbyHasFilms, Integer> {

}
