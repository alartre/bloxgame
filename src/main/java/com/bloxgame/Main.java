package com.bloxgame;

import com.bloxgame.config.H2Config;
import com.bloxgame.controllers.*;
import com.bloxgame.repository.SpinResultRepository;
import com.bloxgame.repository.UserRepository;
import com.bloxgame.service.GameService;
import com.bloxgame.service.UserService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static spark.Spark.*;

@Slf4j
public class Main {
    public static void main(String[] args) {

        H2Config.createTables();

        Gson gson = new Gson();

        SpinResultRepository spinResultRepository = new SpinResultRepository();
        UserRepository userRepository = new UserRepository();

        UserService userService = new UserService(userRepository);
        GameService gameService = new GameService(spinResultRepository, userService);

        CreateUserRoute createUserRoute = new CreateUserRoute(userService, gson);
        UpdateUserSeedRoute updateUserSeedRoute = new UpdateUserSeedRoute(userService, gson);
        SpinRoute spinRoute = new SpinRoute(gameService, gson);
        GetHistorySpinRoute getHistorySpinRoute = new GetHistorySpinRoute(gameService, gson);
        GetSpinResultRoute getSpinResultRoute = new GetSpinResultRoute(gameService, gson);
        GetUserSeedRoute getUserSeedRoute = new GetUserSeedRoute(userService, gson);

        exception(Exception.class, (e, request, response) -> {
            response.type("application/json");
            response.status(500);
            String error = e.getMessage() != null ? e.getMessage():"";
            response.body(gson.toJson(Map.of("error", error)));
        });

        path("/user", () -> {
            post("/createUser", createUserRoute);
            put("/updateSeed", updateUserSeedRoute);
            get("", getUserSeedRoute);
        });
        path("/spin", () -> {
            get("/history", getHistorySpinRoute);
            get("/:id", getSpinResultRoute);
            post("", spinRoute);


        });



    }
}
