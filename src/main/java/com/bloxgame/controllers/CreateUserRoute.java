package com.bloxgame.controllers;

import com.bloxgame.model.User;
import com.bloxgame.service.UserService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Map;

@AllArgsConstructor
public class CreateUserRoute implements Route {

    private UserService userService;
    private Gson gson;


    @Override
    public Object handle(Request request, Response response) throws Exception {
        String username = request.queryParams("username");
        String password = request.queryParams("password");

        User user = userService.createUser(username, password);

        response.type("application/json");

        return gson.toJson(Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "seed", user.getSeed(),
                "serverSeedHashed", user.getServerSeedHashed(),
                "nonce", user.getNonce()
        ));
    }
}
