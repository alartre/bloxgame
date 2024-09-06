package com.bloxgame.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bloxgame.model.User;
import com.bloxgame.service.UserService;
import com.bloxgame.controllers.GetUserSeedRoute;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class GetUserSeedRouteTest {

    private UserService mockUserService;
    private Gson gson;
    private Request mockRequest;
    private Response mockResponse;
    private GetUserSeedRoute getUserSeedRoute;

    @BeforeEach
    public void setUp() {
        mockUserService = mock(UserService.class);
        gson = new Gson();
        mockRequest = mock(Request.class);
        mockResponse = mock(Response.class);
        getUserSeedRoute = new GetUserSeedRoute(mockUserService, gson);
    }

    @Test
    public void testHandle() throws Exception {
        when(mockRequest.headers("userId")).thenReturn("testUserId");

        User mockUser = new User("testUserId", "testUser", "testpassword", "userSeed123", "serverSeed123", "serverSeedHashed123", 5);
        when(mockUserService.getUser("testUserId")).thenReturn(mockUser);

        Map<String, Object> expectedResponseMap = new HashMap<>();
        expectedResponseMap.put("id", "testUserId");
        expectedResponseMap.put("userSeed", "userSeed123");
        expectedResponseMap.put("serverSeedHashed", "serverSeedHashed123");
        expectedResponseMap.put("nonce", 5);

        String expectedResponse = gson.toJson(expectedResponseMap);

        Object result = getUserSeedRoute.handle(mockRequest, mockResponse);

        verify(mockResponse).type("application/json");
        assertEquals(expectedResponse, result);
    }
}
