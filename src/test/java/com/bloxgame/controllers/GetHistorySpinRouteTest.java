package com.bloxgame.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bloxgame.model.SpinResult;
import com.bloxgame.service.GameService;
import com.bloxgame.controllers.GetHistorySpinRoute;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.List;

public class GetHistorySpinRouteTest {

    private GameService mockGameService;
    private Gson gson;
    private Request mockRequest;
    private Response mockResponse;
    private GetHistorySpinRoute getHistorySpinRoute;

    @BeforeEach
    public void setUp() {
        mockGameService = mock(GameService.class);
        gson = new Gson();
        mockRequest = mock(Request.class);
        mockResponse = mock(Response.class);
        getHistorySpinRoute = new GetHistorySpinRoute(mockGameService, gson);
    }

    @Test
    public void testHandle() throws Exception {
        when(mockRequest.headers("userId")).thenReturn("testUserId");

        SpinResult result1 = new SpinResult("1", 5, true, "testUserId", "seed1", "serverSeed1", 1);
        SpinResult result2 = new SpinResult("2", 10, false, "testUserId", "seed2", "serverSeed2", 2);
        List<SpinResult> mockResults = Arrays.asList(result1, result2);

        when(mockGameService.getUserHistory("testUserId")).thenReturn(mockResults);

        String expectedResponse = gson.toJson(mockResults);

        Object result = getHistorySpinRoute.handle(mockRequest, mockResponse);

        verify(mockResponse).type("application/json");
        assertEquals(expectedResponse, result);
    }
}
