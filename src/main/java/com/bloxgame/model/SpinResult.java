package com.bloxgame.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;


@Getter
@AllArgsConstructor
public class SpinResult {

    private String id;
    private final int number;
    private final boolean isPrime;
    private String userId;
    private String userSeed;
    private String serverSeed;
    private int nonce;

    public SpinResult(int number, boolean isPrime, String userId, String userSeed, String serverSeed, int nonce) {
        this.id= UUID.randomUUID().toString();
        this.number=number;
        this.isPrime=isPrime;
        this.userId=userId;
        this.userSeed=userSeed;
        this.serverSeed=serverSeed;
        this.nonce=nonce;
    }

}
