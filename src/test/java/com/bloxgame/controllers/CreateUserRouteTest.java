package com.bloxgame.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bloxgame.model.User;
import com.bloxgame.service.UserService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import java.util.Map;

public class CreateUserRouteTest {

    private UserService mockUserService;
    private Gson gson;
    private Request mockRequest;
    private Response mockResponse;
    private CreateUserRoute createUserRoute;

    @BeforeEach
    public void setUp() {
        mockUserService = mock(UserService.class);
        gson = new Gson();
        mockRequest = mock(Request.class);
        mockResponse = mock(Response.class);
        createUserRoute = new CreateUserRoute(mockUserService, gson);
    }

    @Test
    public void testHandle() throws Exception {
        when(mockRequest.queryParams("username")).thenReturn("testUser");
        when(mockRequest.queryParams("password")).thenReturn("testPass");

        User mockUser = new User("1", "testUser", "testpassword", "seed", "serverSeed", "serverSeedHashed", 1);
        when(mockUserService.createUser("testUser", "testPass")).thenReturn(mockUser);

        String expectedResponse = gson.toJson(Map.of(
                "id", "1",
                "username", "testUser",
                "seed", "seed",
                "serverSeedHashed", "serverSeedHashed",
                "nonce", 1
        ));

        Object result = createUserRoute.handle(mockRequest, mockResponse);

        verify(mockResponse).type("application/json");
        assertEquals(expectedResponse, result);
    }
}
