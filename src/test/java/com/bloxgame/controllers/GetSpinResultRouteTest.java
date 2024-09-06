package com.bloxgame.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bloxgame.model.SpinResult;
import com.bloxgame.service.GameService;
import com.bloxgame.controllers.GetSpinResultRoute;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

public class GetSpinResultRouteTest {

    private GameService mockGameService;
    private Gson gson;
    private Request mockRequest;
    private Response mockResponse;
    private GetSpinResultRoute getSpinResultRoute;

    @BeforeEach
    public void setUp() {
        mockGameService = mock(GameService.class);
        gson = new Gson();
        mockRequest = mock(Request.class);
        mockResponse = mock(Response.class);
        getSpinResultRoute = new GetSpinResultRoute(mockGameService, gson);
    }

    @Test
    public void testHandle() throws Exception {
        when(mockRequest.params(":id")).thenReturn("gameId123");

        SpinResult mockSpinResult = new SpinResult("gameId123", 15, true, "userId123", "seed123", "serverSeed123", 1);
        when(mockGameService.getGame("gameId123")).thenReturn(mockSpinResult);

        String expectedResponse = gson.toJson(mockSpinResult);

        Object result = getSpinResultRoute.handle(mockRequest, mockResponse);

        verify(mockResponse).type("application/json");
        assertEquals(expectedResponse, result);
    }
}
