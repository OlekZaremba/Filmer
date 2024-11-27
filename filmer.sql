-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Lis 27, 2024 at 12:11 PM
-- Wersja serwera: 10.4.32-MariaDB
-- Wersja PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `filmer`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `films`
--

CREATE TABLE `films` (
  `id_film` int(11) NOT NULL,
  `film_name` varchar(45) NOT NULL,
  `film_image` mediumblob NOT NULL,
  `film_desc` text NOT NULL,
  `film_director_id` int(11) NOT NULL,
  `film_studio_id` int(11) NOT NULL,
  `film_type_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `film_director`
--

CREATE TABLE `film_director` (
  `id_film_director` int(11) NOT NULL,
  `name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `film_studio`
--

CREATE TABLE `film_studio` (
  `id_film_studio` int(11) NOT NULL,
  `studio_name` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `film_type`
--

CREATE TABLE `film_type` (
  `id_film_type` int(11) NOT NULL,
  `film_type` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `friends_list`
--

CREATE TABLE `friends_list` (
  `id_friends_list` int(11) NOT NULL,
  `user1` int(11) NOT NULL,
  `user2` int(11) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `lobby`
--

CREATE TABLE `lobby` (
  `id_lobby` int(11) NOT NULL,
  `first_place` int(11) NOT NULL,
  `second_place` int(11) NOT NULL,
  `third_place` int(11) NOT NULL,
  `users_id_user` int(11) NOT NULL,
  `lobby_creation_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `lobby_has_films`
--

CREATE TABLE `lobby_has_films` (
  `id_lobby_has_films` int(11) NOT NULL,
  `lobby_id_lobby` int(11) NOT NULL,
  `films_id_film` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `id_user` int(11) NOT NULL,
  `nick` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id_user`, `nick`) VALUES
(1, '111'),
(2, '555'),
(3, '999');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `user_sensitive_data`
--

CREATE TABLE `user_sensitive_data` (
  `id_user_sensitive_data` int(11) NOT NULL,
  `email` varchar(45) NOT NULL,
  `password` varchar(300) NOT NULL,
  `users_id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `user_sensitive_data`
--

INSERT INTO `user_sensitive_data` (`id_user_sensitive_data`, `email`, `password`, `users_id_user`) VALUES
(1, '111', '$2a$10$D5Lu.U.K4wKFNH2SFbe6w.MF8aGem6qJviSlBZmonKRZUp73pGuSe', 1),
(2, '555', '$2a$10$toUeH8TIL19Z8jQfwlC5AOiHINtw7sjqY1uku/gkSDsjO2G/9d4Xu', 2),
(3, '999', '$2a$10$axPou7ms.NWvxCaEZwkoh.HCU.eTbNjFEykgNXGA8yh.S3c.y5Z5O', 3);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `watched_movies`
--

CREATE TABLE `watched_movies` (
  `id_watched_movies` int(11) NOT NULL,
  `films_id_film` int(11) NOT NULL,
  `users_id_user` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `films`
--
ALTER TABLE `films`
  ADD PRIMARY KEY (`id_film`),
  ADD KEY `idx_film_director_id` (`film_director_id`),
  ADD KEY `idx_film_studio_id` (`film_studio_id`),
  ADD KEY `idx_film_type_id` (`film_type_id`);

--
-- Indeksy dla tabeli `film_director`
--
ALTER TABLE `film_director`
  ADD PRIMARY KEY (`id_film_director`);

--
-- Indeksy dla tabeli `film_studio`
--
ALTER TABLE `film_studio`
  ADD PRIMARY KEY (`id_film_studio`);

--
-- Indeksy dla tabeli `film_type`
--
ALTER TABLE `film_type`
  ADD PRIMARY KEY (`id_film_type`);

--
-- Indeksy dla tabeli `friends_list`
--
ALTER TABLE `friends_list`
  ADD PRIMARY KEY (`id_friends_list`,`user2`,`user1`),
  ADD KEY `idx_user1` (`user1`),
  ADD KEY `idx_user2` (`user2`);

--
-- Indeksy dla tabeli `lobby`
--
ALTER TABLE `lobby`
  ADD PRIMARY KEY (`id_lobby`),
  ADD KEY `idx_users_id_user` (`users_id_user`),
  ADD KEY `idx_first_place` (`first_place`),
  ADD KEY `idx_second_place` (`second_place`),
  ADD KEY `idx_third_place` (`third_place`);

--
-- Indeksy dla tabeli `lobby_has_films`
--
ALTER TABLE `lobby_has_films`
  ADD PRIMARY KEY (`id_lobby_has_films`),
  ADD KEY `idx_lobby_id_lobby` (`lobby_id_lobby`),
  ADD KEY `idx_films_id_film` (`films_id_film`);

--
-- Indeksy dla tabeli `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_user`);

--
-- Indeksy dla tabeli `user_sensitive_data`
--
ALTER TABLE `user_sensitive_data`
  ADD PRIMARY KEY (`id_user_sensitive_data`,`users_id_user`),
  ADD KEY `idx_users_id_user` (`users_id_user`);

--
-- Indeksy dla tabeli `watched_movies`
--
ALTER TABLE `watched_movies`
  ADD PRIMARY KEY (`id_watched_movies`),
  ADD KEY `idx_films_id_film` (`films_id_film`),
  ADD KEY `idx_users_id_user` (`users_id_user`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `films`
--
ALTER TABLE `films`
  MODIFY `id_film` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `film_director`
--
ALTER TABLE `film_director`
  MODIFY `id_film_director` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `film_studio`
--
ALTER TABLE `film_studio`
  MODIFY `id_film_studio` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `film_type`
--
ALTER TABLE `film_type`
  MODIFY `id_film_type` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `friends_list`
--
ALTER TABLE `friends_list`
  MODIFY `id_friends_list` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `lobby`
--
ALTER TABLE `lobby`
  MODIFY `id_lobby` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `lobby_has_films`
--
ALTER TABLE `lobby_has_films`
  MODIFY `id_lobby_has_films` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `user_sensitive_data`
--
ALTER TABLE `user_sensitive_data`
  MODIFY `id_user_sensitive_data` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `watched_movies`
--
ALTER TABLE `watched_movies`
  MODIFY `id_watched_movies` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `films`
--
ALTER TABLE `films`
  ADD CONSTRAINT `fk_films_film_director` FOREIGN KEY (`film_director_id`) REFERENCES `film_director` (`id_film_director`),
  ADD CONSTRAINT `fk_films_film_studio` FOREIGN KEY (`film_studio_id`) REFERENCES `film_studio` (`id_film_studio`),
  ADD CONSTRAINT `fk_films_film_type` FOREIGN KEY (`film_type_id`) REFERENCES `film_type` (`id_film_type`);

--
-- Constraints for table `friends_list`
--
ALTER TABLE `friends_list`
  ADD CONSTRAINT `fk_friends_list_user1` FOREIGN KEY (`user1`) REFERENCES `users` (`id_user`),
  ADD CONSTRAINT `fk_friends_list_user2` FOREIGN KEY (`user2`) REFERENCES `users` (`id_user`);

--
-- Constraints for table `lobby`
--
ALTER TABLE `lobby`
  ADD CONSTRAINT `fk_lobby_films1` FOREIGN KEY (`first_place`) REFERENCES `films` (`id_film`),
  ADD CONSTRAINT `fk_lobby_films2` FOREIGN KEY (`second_place`) REFERENCES `films` (`id_film`),
  ADD CONSTRAINT `fk_lobby_films3` FOREIGN KEY (`third_place`) REFERENCES `films` (`id_film`),
  ADD CONSTRAINT `fk_lobby_user` FOREIGN KEY (`users_id_user`) REFERENCES `users` (`id_user`);

--
-- Constraints for table `lobby_has_films`
--
ALTER TABLE `lobby_has_films`
  ADD CONSTRAINT `fk_lobby_has_films_film` FOREIGN KEY (`films_id_film`) REFERENCES `films` (`id_film`),
  ADD CONSTRAINT `fk_lobby_has_films_lobby` FOREIGN KEY (`lobby_id_lobby`) REFERENCES `lobby` (`id_lobby`);

--
-- Constraints for table `user_sensitive_data`
--
ALTER TABLE `user_sensitive_data`
  ADD CONSTRAINT `fk_user_sensitive_data_user` FOREIGN KEY (`users_id_user`) REFERENCES `users` (`id_user`);

--
-- Constraints for table `watched_movies`
--
ALTER TABLE `watched_movies`
  ADD CONSTRAINT `fk_watched_movies_film` FOREIGN KEY (`films_id_film`) REFERENCES `films` (`id_film`),
  ADD CONSTRAINT `fk_watched_movies_user` FOREIGN KEY (`users_id_user`) REFERENCES `users` (`id_user`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
