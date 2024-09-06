package com.bloxgame.controllers;

import com.bloxgame.model.User;
import com.bloxgame.service.UserService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class GetUserSeedRoute implements Route {

    private UserService userService;
    private Gson gson;

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String userId = request.headers("userId");

        User user = userService.getUser(userId);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("id", user.getId());
        responseMap.put("userSeed", user.getSeed());
        responseMap.put("serverSeedHashed", user.getServerSeedHashed());
        responseMap.put("nonce", user.getNonce());

        response.type("application/json");

        return gson.toJson(responseMap);
    }
}
