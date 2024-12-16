-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema filmer
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema filmer
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `filmer` DEFAULT CHARACTER SET latin1 ;
USE `filmer` ;

-- -----------------------------------------------------
-- Table `filmer`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filmer`.`users` (
                                                `id_user` INT(11) NOT NULL AUTO_INCREMENT,
                                                `nick` VARCHAR(45) NOT NULL,
                                                `profile_picture` LONGBLOB NULL DEFAULT NULL,
                                                PRIMARY KEY (`id_user`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 7
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `filmer`.`lobby`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filmer`.`lobby` (
                                                `id_lobby` INT(11) NOT NULL AUTO_INCREMENT,
                                                `users_id_user` INT(11) NOT NULL,
                                                `lobby_creation_date` DATE NOT NULL,
                                                `is_ready` BOOLEAN DEFAULT FALSE,
                                                PRIMARY KEY (`id_lobby`),
                                                INDEX `idx_users_id_user` (`users_id_user` ASC),
                                                CONSTRAINT `fk_lobby_user`
                                                    FOREIGN KEY (`users_id_user`)
                                                        REFERENCES `filmer`.`users` (`id_user`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;



-- -----------------------------------------------------
-- Table `filmer`.`film_director`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filmer`.`film_director` (
                                                        `id_film_director` INT(11) NOT NULL AUTO_INCREMENT,
                                                        `name` VARCHAR(45) NOT NULL,
                                                        PRIMARY KEY (`id_film_director`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `filmer`.`film_studio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filmer`.`film_studio` (
                                                      `id_film_studio` INT(11) NOT NULL AUTO_INCREMENT,
                                                      `studio_name` VARCHAR(45) NOT NULL,
                                                      PRIMARY KEY (`id_film_studio`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `filmer`.`film_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filmer`.`film_type` (
                                                    `id_film_type` INT(11) NOT NULL AUTO_INCREMENT,
                                                    `film_type` VARCHAR(45) NOT NULL,
                                                    PRIMARY KEY (`id_film_type`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `filmer`.`movie_sources`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filmer`.`movie_sources` (
                                                        `id_movie_source` INT(11) NOT NULL AUTO_INCREMENT,
                                                        `source_name` VARCHAR(45) NOT NULL,
                                                        PRIMARY KEY (`id_movie_source`),
                                                        UNIQUE INDEX `source_name` (`source_name` ASC))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `filmer`.`films`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filmer`.`films` (
                                                `id_film` INT(11) NOT NULL AUTO_INCREMENT,
                                                `film_name` VARCHAR(45) NOT NULL,
                                                `film_image` MEDIUMBLOB NOT NULL,
                                                `film_desc` TEXT NOT NULL,
                                                `film_director_id` INT(11) NOT NULL,
                                                `film_studio_id` INT(11) NOT NULL,
                                                `film_type_id` INT(11) NOT NULL,
                                                `source_id` INT(11) NOT NULL,
                                                `genre_id` INT(11) NOT NULL,
                                                PRIMARY KEY (`id_film`),
                                                INDEX `idx_film_director_id` (`film_director_id` ASC),
                                                INDEX `idx_film_studio_id` (`film_studio_id` ASC),
                                                INDEX `idx_film_type_id` (`film_type_id` ASC),
                                                INDEX `fk_films_source` (`source_id` ASC),
                                                INDEX `fk_films_genre` (`genre_id` ASC),
                                                CONSTRAINT `fk_films_film_director`
                                                    FOREIGN KEY (`film_director_id`)
                                                        REFERENCES `filmer`.`film_director` (`id_film_director`),
                                                CONSTRAINT `fk_films_film_studio`
                                                    FOREIGN KEY (`film_studio_id`)
                                                        REFERENCES `filmer`.`film_studio` (`id_film_studio`),
                                                CONSTRAINT `fk_films_film_type`
                                                    FOREIGN KEY (`film_type_id`)
                                                        REFERENCES `filmer`.`film_type` (`id_film_type`),
                                                CONSTRAINT `fk_films_source`
                                                    FOREIGN KEY (`source_id`)
                                                        REFERENCES `filmer`.`movie_sources` (`id_movie_source`),
                                                CONSTRAINT `fk_films_genre`
                                                    FOREIGN KEY (`genre_id`)
                                                        REFERENCES `filmer`.`film_genre` (`id_genre`)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `filmer`.`film_bans`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filmer`.`film_bans` (
                                                    `id_film_ban` INT(11) NOT NULL AUTO_INCREMENT,
                                                    `lobby_id` INT(11) NOT NULL,
                                                    `film_id` INT(11) NOT NULL,
                                                    `user_id` INT(11) NOT NULL,
                                                    `ban_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
                                                    PRIMARY KEY (`id_film_ban`),
                                                    UNIQUE INDEX `lobby_id` (`lobby_id` ASC, `film_id` ASC, `user_id` ASC),
                                                    INDEX `film_id` (`film_id` ASC),
                                                    INDEX `user_id` (`user_id` ASC),
                                                    CONSTRAINT `film_bans_ibfk_1`
                                                        FOREIGN KEY (`lobby_id`)
                                                            REFERENCES `filmer`.`lobby` (`id_lobby`),
                                                    CONSTRAINT `film_bans_ibfk_2`
                                                        FOREIGN KEY (`film_id`)
                                                            REFERENCES `filmer`.`films` (`id_film`),
                                                    CONSTRAINT `film_bans_ibfk_3`
                                                        FOREIGN KEY (`user_id`)
                                                            REFERENCES `filmer`.`users` (`id_user`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `filmer`.`friends_list`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filmer`.`friends_list` (
                                                       `id_friends_list` INT(11) NOT NULL AUTO_INCREMENT,
                                                       `user1` INT(11) NOT NULL,
                                                       `user2` INT(11) NOT NULL,
                                                       `created_at` DATETIME(6) NULL DEFAULT NULL,
                                                       PRIMARY KEY (`id_friends_list`, `user2`, `user1`),
                                                       INDEX `idx_user1` (`user1` ASC),
                                                       INDEX `idx_user2` (`user2` ASC),
                                                       CONSTRAINT `fk_friends_list_user1`
                                                           FOREIGN KEY (`user1`)
                                                               REFERENCES `filmer`.`users` (`id_user`),
                                                       CONSTRAINT `fk_friends_list_user2`
                                                           FOREIGN KEY (`user2`)
                                                               REFERENCES `filmer`.`users` (`id_user`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 11
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `filmer`.`lobby_has_films`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filmer`.`lobby_has_films` (
                                                          `id_lobby_has_films` INT(11) NOT NULL AUTO_INCREMENT,
                                                          `lobby_id_lobby` INT(11) NOT NULL,
                                                          `films_id_film` INT(11) NOT NULL,
                                                          PRIMARY KEY (`id_lobby_has_films`),
                                                          INDEX `idx_lobby_id_lobby` (`lobby_id_lobby` ASC),
                                                          INDEX `idx_films_id_film` (`films_id_film` ASC),
                                                          CONSTRAINT `fk_lobby_has_films_film`
                                                              FOREIGN KEY (`films_id_film`)
                                                                  REFERENCES `filmer`.`films` (`id_film`),
                                                          CONSTRAINT `fk_lobby_has_films_lobby`
                                                              FOREIGN KEY (`lobby_id_lobby`)
                                                                  REFERENCES `filmer`.`lobby` (`id_lobby`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `filmer`.`lobby_results`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filmer`.`lobby_results` (
                                                        `id_lobby_result` INT(11) NOT NULL AUTO_INCREMENT,
                                                        `lobby_id` INT(11) NOT NULL,
                                                        `film_id` INT(11) NOT NULL,
                                                        `position` ENUM('first', 'second', 'third') NOT NULL,
                                                        PRIMARY KEY (`id_lobby_result`),
                                                        UNIQUE INDEX `lobby_id` (`lobby_id` ASC, `position` ASC),
                                                        INDEX `film_id` (`film_id` ASC),
                                                        CONSTRAINT `lobby_results_ibfk_1`
                                                            FOREIGN KEY (`lobby_id`)
                                                                REFERENCES `filmer`.`lobby` (`id_lobby`),
                                                        CONSTRAINT `lobby_results_ibfk_2`
                                                            FOREIGN KEY (`film_id`)
                                                                REFERENCES `filmer`.`films` (`id_film`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `filmer`.`lobby_sources`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filmer`.`lobby_sources` (
                                                        `id_lobby_source` INT(11) NOT NULL AUTO_INCREMENT,
                                                        `lobby_id` INT(11) NOT NULL,
                                                        `source_id` INT(11) NOT NULL,
                                                        PRIMARY KEY (`id_lobby_source`),
                                                        UNIQUE INDEX `lobby_id` (`lobby_id` ASC, `source_id` ASC),
                                                        INDEX `source_id` (`source_id` ASC),
                                                        CONSTRAINT `lobby_sources_ibfk_1`
                                                            FOREIGN KEY (`lobby_id`)
                                                                REFERENCES `filmer`.`lobby` (`id_lobby`),
                                                        CONSTRAINT `lobby_sources_ibfk_2`
                                                            FOREIGN KEY (`source_id`)
                                                                REFERENCES `filmer`.`movie_sources` (`id_movie_source`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `filmer`.`lobby_users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filmer`.`lobby_users` (
                                                      `id_lobby_user` INT(11) NOT NULL AUTO_INCREMENT,
                                                      `lobby_id` INT(11) NOT NULL,
                                                      `user_id` INT(11) NOT NULL,
                                                      PRIMARY KEY (`id_lobby_user`),
                                                      UNIQUE INDEX `lobby_id` (`lobby_id` ASC, `user_id` ASC),
                                                      INDEX `user_id` (`user_id` ASC),
                                                      CONSTRAINT `lobby_users_ibfk_1`
                                                          FOREIGN KEY (`lobby_id`)
                                                              REFERENCES `filmer`.`lobby` (`id_lobby`),
                                                      CONSTRAINT `lobby_users_ibfk_2`
                                                          FOREIGN KEY (`user_id`)
                                                              REFERENCES `filmer`.`users` (`id_user`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `filmer`.`user_sensitive_data`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filmer`.`user_sensitive_data` (
                                                              `id_user_sensitive_data` INT(11) NOT NULL AUTO_INCREMENT,
                                                              `email` VARCHAR(45) NOT NULL,
                                                              `password` VARCHAR(300) NOT NULL,
                                                              `users_id_user` INT(11) NOT NULL,
                                                              PRIMARY KEY (`id_user_sensitive_data`, `users_id_user`),
                                                              INDEX `idx_users_id_user` (`users_id_user` ASC),
                                                              CONSTRAINT `fk_user_sensitive_data_user`
                                                                  FOREIGN KEY (`users_id_user`)
                                                                      REFERENCES `filmer`.`users` (`id_user`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 5
    DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `filmer`.`watched_movies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filmer`.`watched_movies` (
                                                         `id_watched_movies` INT(11) NOT NULL AUTO_INCREMENT,
                                                         `films_id_film` INT(11) NOT NULL,
                                                         `users_id_user` INT(11) NOT NULL,
                                                         PRIMARY KEY (`id_watched_movies`),
                                                         INDEX `idx_films_id_film` (`films_id_film` ASC),
                                                         INDEX `idx_users_id_user` (`users_id_user` ASC),
                                                         INDEX `idx_user_film` (`users_id_user` ASC, `films_id_film` ASC),
                                                         CONSTRAINT `fk_watched_movies_film`
                                                             FOREIGN KEY (`films_id_film`)
                                                                 REFERENCES `filmer`.`films` (`id_film`),
                                                         CONSTRAINT `fk_watched_movies_user`
                                                             FOREIGN KEY (`users_id_user`)
                                                                 REFERENCES `filmer`.`users` (`id_user`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8;


CREATE TABLE IF NOT EXISTS `filmer`.`film_genre` (
                                                     `id_genre` INT(11) NOT NULL AUTO_INCREMENT,
                                                     `genre_name` VARCHAR(255) NOT NULL,
                                                     PRIMARY KEY (`id_genre`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `filmer`.`user_preferences` (
                                                           `id_user_preference` INT(11) NOT NULL AUTO_INCREMENT,
                                                           `lobby_id` INT(11) NOT NULL,
                                                           `user_id` INT(11) NOT NULL,
                                                           `streaming_platform_id` INT(11) NULL,
                                                           `genre_id` INT(11) NULL,
                                                           `type_id` INT(11) NULL,
                                                           `is_ready` BOOLEAN DEFAULT FALSE,
                                                           PRIMARY KEY (`id_user_preference`),
                                                           UNIQUE INDEX `lobby_user_unique` (`lobby_id`, `user_id`),
                                                           CONSTRAINT `fk_user_preferences_lobby` FOREIGN KEY (`lobby_id`) REFERENCES `filmer`.`lobby` (`id_lobby`),
                                                           CONSTRAINT `fk_user_preferences_user` FOREIGN KEY (`user_id`) REFERENCES `filmer`.`users` (`id_user`),
                                                           CONSTRAINT `fk_user_preferences_streaming_platform` FOREIGN KEY (`streaming_platform_id`) REFERENCES `filmer`.`movie_sources` (`id_movie_source`),
                                                           CONSTRAINT `fk_user_preferences_genre` FOREIGN KEY (`genre_id`) REFERENCES `filmer`.`film_genre` (`id_genre`),
                                                           CONSTRAINT `fk_user_preferences_type` FOREIGN KEY (`type_id`) REFERENCES `filmer`.`film_type` (`id_film_type`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;


INSERT INTO `filmer`.`film_genre` (`genre_name`) VALUES
                                                     ('Dramat'),
                                                     ('Komedia'),
                                                     ('Horror'),
                                                     ('Thriller'),
                                                     ('Psychologiczny');

INSERT INTO `filmer`.`movie_sources` (`source_name`) VALUES
                                                         ('Netflix'),
                                                         ('Disney+'),
                                                         ('Max'),
                                                         ('Showtime'),
                                                         ('Amazon');

INSERT INTO `filmer`.`film_type` (`film_type`) VALUES
                                                   ('Film'),
                                                   ('Serial');

INSERT INTO `films` (`id_film`, `film_name`, `film_image`, `film_desc`, `film_director_id`, `film_studio_id`, `film_type_id`, `source_id`, `genre_id`) VALUES
(1, 'Inception', NULL, 'A skilled thief is offered a chance to erase his criminal past by planting an idea into the mind of a C.E.O.', 1, 1, 1, 1, 1),
(2, 'Pulp Fiction', NULL, 'The lives of two mob hitmen, a boxer, a gangster\'s wife, and a pair of diner bandits intertwine.', 3, 2, 1, 1, 2),
(3, 'The Godfather', NULL, 'The aging patriarch of an organized crime dynasty transfers control to his reluctant son.', 10, 3, 1, 1, 1),
(4, 'Avatar', NULL, 'A paraplegic Marine dispatched to Pandora finds himself torn between following orders and protecting an alien civilization.', 4, 3, 1, 2, 1),
(5, 'Jaws', NULL, 'A killer shark unleashes chaos on a beach community.', 2, 2, 1, 3, 3),
(6, 'The Dark Knight', NULL, 'Batman w starciu z Jokerem, w mrocznej historii pełnej zwrotów akcji.', 1, 1, 1, 1, 1),
(7, 'Forrest Gump', NULL, 'Historia człowieka, którego prosta natura prowadzi przez niezwykłe wydarzenia.', 2, 2, 1, 5, 1),
(8, 'Interstellar', NULL, 'Grupa astronautów wyrusza na misję w celu znalezienia nowego domu dla ludzkości.', 1, 1, 1, 3, 1),
(9, 'Parasite', NULL, 'Rodzina oszustów infiltruje bogate domostwo, co prowadzi do nieoczekiwanych wydarzeń.', 5, 4, 1, 2, 4),
(10, 'The Shawshank Redemption', NULL, 'Skazany niesłusznie mężczyzna nawiązuje wyjątkową przyjaźń w więzieniu.', 5, 3, 1, 1, 1),
(11, 'Inglourious Basterds', NULL, 'Historia grupy żydowskich żołnierzy podczas II wojny światowej, planujących zamach na nazistów.', 3, 2, 1, 4, 1),
(12, 'The Matrix', NULL, 'Haker odkrywa, że świat, w którym żyje, to komputerowa symulacja i dołącza do walki o wolność ludzkości.', 4, 3, 1, 1, 3),
(13, 'Gladiator', NULL, 'Rzymski generał staje się gladiatorem, szukając zemsty na cesarzu, który zamordował jego rodzinę.', 6, 4, 1, 5, 1),
(14, 'Titanic', NULL, 'Epicka opowieść o miłości i tragedii na pokładzie słynnego statku Titanic.', 4, 1, 1, 3, 1),
(15, 'The Wolf of Wall Street', NULL, 'Historia Jordana Belforta, maklera giełdowego, który wciąga się w świat pieniędzy, luksusu i korupcji.', 5, 5, 1, 2, 2),
(16, 'Fight Club', NULL, 'Młody mężczyzna i tajemniczy Tyler Durden tworzą podziemny klub walki, który szybko wymyka się spod kontroli.', 8, 4, 1, 1, 4),
(17, 'The Lord of the Rings: The Fellowship of the ', NULL, 'Drużyna przyjaciół wyrusza w niebezpieczną podróż, by zniszczyć pierścień władzy.', 2, 1, 1, 2, 1),
(18, 'The Lion King', NULL, 'Historia dorastania młodego lwa Simby, który walczy o swoje miejsce w królestwie.', 2, 5, 1, 1, 2),
(19, 'Star Wars: A New Hope', NULL, 'Luke Skywalker wyrusza na misję, by ocalić galaktykę przed Imperium.', 15, 3, 1, 5, 1),
(20, 'The Silence of the Lambs', NULL, 'Agentka FBI prosi Hannibala Lectera o pomoc w schwytaniu seryjnego mordercy.', 5, 2, 1, 4, 3),
(21, 'Saving Private Ryan', NULL, 'Grupa żołnierzy wyrusza na niebezpieczną misję, by uratować ostatniego żyjącego brata z rodziny.', 2, 1, 1, 3, 1),
(22, 'Coco', NULL, 'Młody chłopiec Miguel odkrywa tajemnice swojej rodziny podczas niezwykłej podróży do Krainy Zmarłych.', 3, 5, 1, 1, 2),
(23, 'Schindler’s List', NULL, 'Historia Oskara Schindlera, który ratuje setki Żydów przed zagładą w czasie II wojny światowej.', 5, 1, 1, 3, 1),
(24, 'Mad Max: Fury Road', NULL, 'Postapokaliptyczna opowieść o walce o przetrwanie w świecie pustynnym, pełnym chaosu.', 4, 2, 1, 2, 3),
(25, 'Joker', NULL, 'Opowieść o trudnym życiu Arthura Flecka, które doprowadza go do przemiany w Jokera.', 1, 4, 1, 1, 4),
(26, 'The Avengers', NULL, 'Grupa superbohaterów łączy siły, aby powstrzymać Lokiego przed zdobyciem władzy nad Ziemią.', 4, 1, 1, 3, 2),
(27, 'La La Land', NULL, 'Porywająca opowieść o miłości, marzeniach i pasji dwóch artystów w Los Angeles.', 5, 2, 1, 5, 2),
(28, 'The Green Mile', NULL, 'Strażnik więzienny odkrywa nadprzyrodzone zdolności jednego z osadzonych.', 5, 3, 1, 1, 1),
(29, 'Black Panther', NULL, 'Król T’Challa staje przed wyzwaniem ochrony Wakandy i jej tajemniczej technologii.', 1, 4, 1, 2, 2),
(30, 'The Social Network', NULL, 'Historia powstania Facebooka i walk o prawa do własności intelektualnej.', 3, 3, 1, 4, 4),
(31, 'Deadpool', NULL, 'Niepokorny antybohater walczy z przestępcami, wplatając w to cięty humor i brutalne akcje.', 4, 2, 1, 3, 3),
(32, 'Shutter Island', NULL, 'Dwóch detektywów bada tajemnicze zniknięcie pacjentki z zakładu psychiatrycznego.', 5, 4, 1, 1, 4),
(33, 'The Godfather: Part II', NULL, 'Kontynuacja losów rodziny Corleone, przedstawiająca początki działalności młodego Vita.', 5, 3, 1, 5, 1),
(34, 'Up', NULL, 'Starszy mężczyzna i młody skaut wyruszają w podróż balonowym domem do Ameryki Południowej.', 2, 5, 1, 1, 2),
(35, 'Once Upon a Time in Hollywood', NULL, 'Historia aktora i jego kaskadera w latach 60. w Hollywood, przeplatana prawdziwymi wydarzeniami.', 3, 4, 1, 2, 4),
(36, 'The Truman Show', NULL, 'Mężczyzna odkrywa, że całe jego życie jest transmitowane jako popularne reality show.', 2, 3, 1, 5, 4),
(37, 'Avengers: Endgame', NULL, 'Bohaterowie walczą o ocalenie wszechświata i cofnięcie skutków działań Thanosa.', 4, 1, 1, 3, 2),
(38, 'The Pursuit of Happyness', NULL, 'Ojciec samotnie wychowujący syna walczy o przetrwanie i realizację swoich marzeń.', 5, 2, 1, 4, 1),
(39, 'Django Unchained', NULL, 'Wyzwolony niewolnik i łowca nagród wyruszają, by uratować żonę tego pierwszego.', 3, 1, 1, 1, 4),
(40, 'The Great Gatsby', NULL, 'Historia milionera Jaya Gatsby’ego i jego obsesji na punkcie dawnej miłości.', 5, 4, 1, 2, 1),
(41, 'The Incredibles', NULL, 'Rodzina superbohaterów próbuje wrócić do akcji, ukrywając swoje moce przed światem.', 2, 5, 1, 1, 2),
(42, 'Bohemian Rhapsody', NULL, 'Biografia Freddiego Mercury’ego i zespołu Queen, ukazująca ich drogę na szczyt.', 4, 4, 1, 5, 2),
(43, 'The Revenant', NULL, 'Myśliwy walczy o przetrwanie po ataku niedźwiedzia i zdradzie swojego towarzysza.', 1, 3, 1, 2, 3),
(44, 'The Witcher', NULL, 'Geralt z Rivii, samotny łowca potworów, próbuje odnaleźć swoje przeznaczenie.', 3, 5, 2, 4, 4),
(45, 'Inception', NULL, 'Ekspert od włamań do snów wyrusza na misję wszczepienia pomysłu w podświadomość miliardera.', 1, 1, 1, 3, 4),
(46, 'Harry Potter and the Sorcerer\'s Stone', NULL, 'Chłopiec odkrywa, że jest czarodziejem, i wyrusza do Szkoły Magii i Czarodziejstwa w Hogwarcie.', 2, 1, 1, 2, 2),
(47, 'The Hobbit: An Unexpected Journey', NULL, 'Bilbo Baggins wyrusza na wyprawę z krasnoludami, aby odzyskać ich królestwo.', 4, 3, 1, 1, 1),
(48, 'Zootopia', NULL, 'Królik policjant i cyniczny lis detektyw rozwiązują tajemnicę w metropolii zwierząt.', 3, 5, 1, 1, 2),
(49, 'Frozen', NULL, 'Dwójka sióstr walczy o ocalenie swojego królestwa, które zostało pokryte wiecznym lodem.', 2, 5, 1, 4, 2),
(50, 'Jurassic Park', NULL, 'Naukowcy ożywiają dinozaury, ale park rozrywki szybko zamienia się w koszmar.', 5, 2, 1, 3, 3),
(51, 'Pirates of the Caribbean: The Curse of the Bl', NULL, 'Kapitan Jack Sparrow wyrusza na przygodę, by odzyskać swój statek.', 3, 4, 1, 5, 2),
(52, 'The Hunger Games', NULL, 'Młoda dziewczyna staje do walki na śmierć i życie w dystopijnym państwie.', 4, 3, 1, 1, 4),
(53, 'Inside Out', NULL, 'Personifikacje emocji pomagają młodej dziewczynie poradzić sobie ze zmianami w życiu.', 2, 5, 1, 4, 2),
(54, 'The Shape of Water', NULL, 'Niezwykła historia miłości między niemą kobietą a wodnym stworzeniem.', 9, 4, 1, 3, 4),
(55, 'Logan', NULL, 'Zmęczony życiem Wolverine staje przed ostatnim wyzwaniem, aby chronić młodą mutantkę.', 1, 1, 1, 2, 3),
(56, 'Black Swan', NULL, 'Historia baletnicy, która traci kontrolę nad własnym umysłem w drodze do perfekcji.', 8, 4, 1, 3, 5),
(57, 'Shutter Island', NULL, 'Detektyw odkrywa mroczne sekrety wyspy zamieszkałej przez niebezpiecznych pacjentów.', 5, 2, 1, 1, 5),
(58, 'The Sixth Sense', NULL, 'Psycholog dziecięcy pomaga chłopcu, który widzi zmarłych.', 9, 3, 1, 4, 5),
(59, 'Gone Girl', NULL, 'Mąż staje się głównym podejrzanym w sprawie zaginięcia swojej żony.', 8, 4, 1, 5, 5),
(60, 'Oldboy', NULL, 'Mężczyzna zostaje porwany na 15 lat i próbuje rozwikłać tajemnicę swojego oprawcy.', 10, 5, 1, 3, 5),
(61, 'The Machinist', NULL, 'Bezsenność prowadzi pracownika fabryki do granic szaleństwa i paranoi.', 8, 1, 1, 2, 5),
(62, 'Prisoners', NULL, 'Ojciec rozpoczyna desperackie poszukiwania swojej zaginionej córki.', 12, 3, 1, 1, 5),
(63, 'Memento', NULL, 'Mężczyzna z zanikiem pamięci krótkotrwałej próbuje rozwiązać zagadkę morderstwa swojej żony.', 1, 2, 1, 4, 5),
(64, 'The Others', NULL, 'Kobieta podejrzewa, że w jej domu znajdują się duchy.', 13, 4, 1, 2, 5),
(65, 'Requiem for a Dream', NULL, 'Opowieść o uzależnieniach i ich wpływie na życie czterech osób.', 8, 5, 1, 5, 5);

INSERT INTO `film_director` (`id_film_director`, `name`) VALUES
(1, 'Christopher Nolan'),
(2, 'Steven Spielberg'),
(3, 'Quentin Tarantino'),
(4, 'James Cameron'),
(5, 'Martin Scorsese'),
(6, 'Ridley Scott'),
(7, 'Peter Jackson'),
(8, 'David Fincher'),
(9, 'Guillermo del Toro'),
(10, 'Francis Ford Coppola'),
(11, 'Greta Gerwig'),
(12, 'Denis Villeneuve'),
(13, 'Alfred Hitchcock'),
(14, 'Stanley Kubrick'),
(15, 'George Lucas');


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
