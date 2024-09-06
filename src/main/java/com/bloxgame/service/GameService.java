package com.bloxgame.service;


import com.bloxgame.model.SpinResult;
import com.bloxgame.model.User;
import com.bloxgame.repository.SpinResultRepository;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class GameService {
    private final SpinResultRepository spinResultRepository;
    private final UserService userService;

    public GameService(SpinResultRepository spinResultRepository, UserService userService) {
        this.spinResultRepository = spinResultRepository;
        this.userService = userService;
    }


    public SpinResult spinForUser(String userId) {
        User user = userService.getUser(userId);
        SecureRandom secureRandom = new SecureRandom((user.getSeed() + user.getServerSeed() + user.getNonce()).getBytes());

        int number = spin(secureRandom);

        boolean isPrime = isPrime(number);

        SpinResult spinResult = new SpinResult(number, isPrime, userId, user.getSeed(), user.getServerSeed(), user.getNonce());

        user.rotateServerSeed();

        userService.updateUser(user);

        spinResultRepository.addGame(userId, spinResult);

        return spinResult;
    }

    public List<SpinResult> getUserHistory(String userId) {
        return spinResultRepository.getGames(userId);
    }

    public SpinResult getGame(String gameId) {
        return spinResultRepository.getGame(gameId);
    }

    public int spin(Random random) {
        return random.nextInt(20) + 1;
    }

    private boolean isPrime(int number) {

        if(number <= 1) return false;
        for(int i = 2; i <= Math.sqrt(number); i++) {
            if(number % i == 0) return false;
        }
        return true;
    }
}
