package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity(name = "friends_list")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FriendsList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_friends_list;

    @Column(name = "user1")
    private int user1;

    @Column(name = "user2")
    private int user2;

    @Column(name = "created_at")
    private Date createdAt;
}
