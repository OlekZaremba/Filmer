package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Services.DrawService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class DrawControllerTest {

    @Test
    public void testStartDrawEndpoint() {
        DrawService drawServiceMock = mock(DrawService.class);
        DrawController controller = new DrawController(drawServiceMock);
        controller.startDraw("test-lobby");
    }

    @Test
    public void testSubmitVoteEndpoint() {
        DrawService drawServiceMock = mock(DrawService.class);
        DrawController controller = new DrawController(drawServiceMock);
        controller.submitVote("test-lobby", 1, 2);
    }
}
