package com.bloxgame.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bloxgame.model.SpinResult;
import com.bloxgame.service.GameService;
import com.bloxgame.controllers.SpinRoute;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

public class SpinRouteTest {

    private GameService mockGameService;
    private Gson gson;
    private Request mockRequest;
    private Response mockResponse;
    private SpinRoute spinRoute;

    @BeforeEach
    public void setUp() {
        mockGameService = mock(GameService.class);
        gson = new Gson();
        mockRequest = mock(Request.class);
        mockResponse = mock(Response.class);
        spinRoute = new SpinRoute(mockGameService, gson);
    }

    @Test
    public void testHandle() throws Exception {
        when(mockRequest.headers("userId")).thenReturn("testUserId");

        SpinResult mockSpinResult = new SpinResult("spinId123", 10, true, "testUserId", "userSeed123", "serverSeedHashed123", 3);
        when(mockGameService.spinForUser("testUserId")).thenReturn(mockSpinResult);

        String expectedResponse = gson.toJson(mockSpinResult);

        Object result = spinRoute.handle(mockRequest, mockResponse);

        verify(mockResponse).type("application/json");
        assertEquals(expectedResponse, result);
    }
}
