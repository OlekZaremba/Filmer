package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Users;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
/**
 * Interfejs serwisu do zarządzania operacjami związanymi z listą znajomych i profilami użytkowników.
 */
public interface FriendsService {

    /**
     * Wyszukuje użytkowników na podstawie części ich pseudonimu.
     *
     * @param nick część pseudonimu użytkownika
     * @return lista użytkowników spełniających kryteria wyszukiwania
     */
    List<Users> searchUsersByNick(String nick);

    /**
     * Pobiera listę znajomych dla danego użytkownika.
     *
     * @param userId identyfikator użytkownika
     * @return lista znajomych powiązanych z użytkownikiem
     */
    List<Users> getFriendsByUserId(int userId);

    /**
     * Dodaje użytkownika do listy znajomych innego użytkownika.
     *
     * @param userId   identyfikator użytkownika, który dodaje znajomego
     * @param friendId identyfikator dodawanego znajomego
     */
    void addFriend(int userId, int friendId);

    /**
     * Przesyła zdjęcie profilowe dla danego użytkownika.
     *
     * @param userId identyfikator użytkownika
     * @param file   plik zawierający zdjęcie profilowe
     */
    void uploadProfilePicture(int userId, MultipartFile file);

    /**
     * Pobiera zdjęcie profilowe danego użytkownika.
     *
     * @param userId identyfikator użytkownika
     * @return tablica bajtów reprezentująca zdjęcie profilowe
     */
    byte[] getProfilePicture(int userId);

    /**
     * Wysyła zaproszenie e-mail do znajomego.
     *
     * @param friendId  identyfikator znajomego
     * @param lobbyLink link do lobby, które ma być przesłane w zaproszeniu
     */
    void sendInviteEmail(int friendId, String lobbyLink);
}
