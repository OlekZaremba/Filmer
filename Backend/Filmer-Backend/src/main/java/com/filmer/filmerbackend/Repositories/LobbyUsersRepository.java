package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.LobbyUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LobbyUsersRepository extends JpaRepository<LobbyUsers, Integer> {
    @Query("SELECT lu FROM LobbyUsers lu WHERE lu.lobby.idLobby = :lobbyId AND lu.user.id_user = :userId")
    Optional<LobbyUsers> findByLobbyAndUser(@Param("lobbyId") int lobbyId, @Param("userId") int userId);
    List<LobbyUsers> findByLobby(int lobbyId);

}
