package com.bloxgame.service;

import com.bloxgame.model.User;
import com.bloxgame.repository.UserRepository;
import org.eclipse.jetty.util.StringUtil;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username, String password) {
        if(StringUtil.isBlank(username) || StringUtil.isBlank(password)) {
            throw new IllegalStateException("Username or password are empty");
        }
       return userRepository.createUser(username, password);
    }

    public void updateUser(User user) {
        if(user == null) {
            throw new IllegalStateException();
        }
        userRepository.updateUserServerSeeds(user);
    }

    public User getUser(String userId) {
        if(StringUtil.isBlank(userId)) {
            throw  new IllegalStateException();
        }
        return userRepository.getUser(userId);
    }

    public User setUserSeed(String seed, String userId) {
        if (StringUtil.isBlank(seed) || StringUtil.isBlank(userId)) {
            throw new IllegalStateException("Seed or userId can't be null");
        }
        return userRepository.setUserSeed(seed, userId);
    }

    /**
    public String getUserSeed(String userId) {
        if(StringUtil.isBlank(userId)) {
            throw new IllegalStateException();
        }
        return userRepository.getUserSeed(userId);
    }
     */

}
