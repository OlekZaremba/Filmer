package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Dtos.SendInviteRequest;
import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Services.FriendsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FriendsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendsService friendsService;

    @BeforeEach
    void setup() {
        Mockito.reset(friendsService);
    }

    @Test
    void testGetFriends() throws Exception {
        Users user1 = new Users(1, "JohnDoe", null, null);
        Users user2 = new Users(2, "JaneDoe", null, null);

        Mockito.when(friendsService.getFriendsByUserId(1)).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/friends/api/1/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nick", is("JohnDoe")))
                .andExpect(jsonPath("$[1].nick", is("JaneDoe")));
    }

    @Test
    void testAddFriend() throws Exception {
        Mockito.doNothing().when(friendsService).addFriend(1, 2);

        mockMvc.perform(post("/friends/api/1/addFriend/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Użytkownik został dodany do znajomych."));
    }

    @Test
    void testSearchUsers() throws Exception {
        Users user = new Users(3, "SearchUser", null, null);
        Mockito.when(friendsService.searchUsersByNick("Search")).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/friends/api/search").param("nick", "Search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nick", is("SearchUser")));
    }

    @Test
    void testUploadProfilePicture() throws Exception {
        Mockito.doNothing().when(friendsService).uploadProfilePicture(eq(1), any());

        mockMvc.perform(multipart("/friends/api/1/uploadProfilePicture")
                        .file("file", "dummy content".getBytes()))
                .andExpect(status().isOk())
                .andExpect(content().string("Zdjęcie profilowe zostało zaktualizowane."));
    }

    @Test
    void testGetProfilePicture() throws Exception {
        byte[] picture = "dummy image data".getBytes();
        Mockito.when(friendsService.getProfilePicture(1)).thenReturn(picture);

        mockMvc.perform(get("/friends/api/1/profilePicture"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "image/jpeg"))
                .andExpect(content().bytes(picture));
    }


//    @Test
//    void testSendInvite() throws Exception {
//        SendInviteRequest request = new SendInviteRequest(1, "http://example.com/lobby");
//        Mockito.doNothing().when(friendsService).sendInviteEmail(1, "http://example.com/lobby");
//
//        mockMvc.perform(post("/friends/api/sendInvite")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{" +
//                                "\"friendId\": 1," +
//                                "\"lobbyLink\": \"http://example.com/lobby\"}"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Zaproszenie zostało wysłane."));
//    }
}
