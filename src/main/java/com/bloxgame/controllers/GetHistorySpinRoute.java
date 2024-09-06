package com.bloxgame.controllers;

import com.bloxgame.model.SpinResult;
import com.bloxgame.service.GameService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

@AllArgsConstructor
public class GetHistorySpinRoute implements Route {

    private GameService gameService;
    private Gson gson;

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String userId = request.headers("userId");
        List<SpinResult> results = gameService.getUserHistory(userId);

        response.type("application/json");

        return gson.toJson(results);
    }
}
