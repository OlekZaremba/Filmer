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
                                                PRIMARY KEY (`id_film`),
                                                INDEX `idx_film_director_id` (`film_director_id` ASC),
                                                INDEX `idx_film_studio_id` (`film_studio_id` ASC),
                                                INDEX `idx_film_type_id` (`film_type_id` ASC),
                                                INDEX `fk_films_source` (`source_id` ASC),
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
                                                        REFERENCES `filmer`.`movie_sources` (`id_movie_source`))
    ENGINE = InnoDB
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
                                                           `streaming_platform` INT(11) NULL,
                                                           `genre` INT(11) NULL,
                                                           `type` INT(11) NULL,
                                                           `is_ready` BOOLEAN DEFAULT FALSE,
                                                           PRIMARY KEY (`id_user_preference`),
                                                           UNIQUE INDEX `lobby_user_unique` (`lobby_id`, `user_id`),
                                                           CONSTRAINT `fk_user_preferences_lobby` FOREIGN KEY (`lobby_id`) REFERENCES `filmer`.`lobby` (`id_lobby`),
                                                           CONSTRAINT `fk_user_preferences_user` FOREIGN KEY (`user_id`) REFERENCES `filmer`.`users` (`id_user`),
                                                           CONSTRAINT `fk_user_preferences_streaming_platform` FOREIGN KEY (`streaming_platform`) REFERENCES `filmer`.`movie_sources` (`id_movie_source`),
                                                           CONSTRAINT `fk_user_preferences_genre` FOREIGN KEY (`genre`) REFERENCES `filmer`.`film_genre` (`id_genre`),
                                                           CONSTRAINT `fk_user_preferences_type` FOREIGN KEY (`type`) REFERENCES `filmer`.`film_type` (`id_film_type`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;




SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
