package com.bloxgame.controllers;

import com.bloxgame.model.SpinResult;
import com.bloxgame.service.GameService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;
import spark.Route;


@AllArgsConstructor
public class GetSpinResultRoute implements Route {

    private GameService gameService;
    private Gson gson;

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String gameId = request.params(":id");
        SpinResult spinResult = gameService.getGame(gameId);

        response.type("application/json");

        return gson.toJson(spinResult);
    }
}

