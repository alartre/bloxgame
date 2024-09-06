package com.bloxgame.controllers;

import com.bloxgame.model.User;
import com.bloxgame.service.GameService;
import com.bloxgame.service.UserService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

@AllArgsConstructor
public class UpdateUserSeedRoute implements Route {

    private UserService userService;

    private Gson gson;


    @Override
    public Object handle(Request request, Response response) throws Exception {
        String userId = request.headers("userId");
        String seed = request.queryParams("seed");

        response.type("application/json");

        User user = userService.setUserSeed(seed, userId);

        return gson.toJson(Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "seed", user.getSeed(),
                "serverSeedHashed", user.getServerSeedHashed(),
                "nonce", user.getNonce()
        ));
    }
}
