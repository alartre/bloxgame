package com.bloxgame.repository;

import com.bloxgame.config.H2Config;
import com.bloxgame.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public UserRepository() {
        H2Config.createTables();
    }

    public User createUser(String username, String password) {
        User user = new User(username, password);
        if(getUserByUsername(username) != null) {
            throw new IllegalArgumentException("There is a user with that username");

        }
        String sql = "INSERT INTO users (id, username, password, seed, serverSeed, serverSeedHashed, nonce) VALUES (?,?,?,?,?,?,?)";
        try(Connection connection = H2Config.getConnection();
            PreparedStatement preparedStatement =connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getSeed());
            preparedStatement.setString(5, user.getServerSeed());
            preparedStatement.setString(6, user.getServerSeedHashed());
            preparedStatement.setInt(7, user.getNonce());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }
        return user;
    }

    public void updateUserServerSeeds(User updatedUser) {
        String sql = "Update users SET serverSeed = ?, serverSeedHashed = ?, nonce = ? WHERE id = ?";
        try (Connection connection = H2Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, updatedUser.getServerSeed());
            preparedStatement.setString(2, updatedUser.getServerSeedHashed());
            preparedStatement.setInt(3, updatedUser.getNonce());
            preparedStatement.setString(4, updatedUser.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }

    }

    public User setUserSeed(String seed, String userId)  {
        String sql = "UPDATE users SET seed = ?  WHERE id = ?";
        try (Connection connection = H2Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, seed);
            preparedStatement.setString(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }
        return getUser(userId);
    }

    public User getUser(String userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try(Connection connection = H2Config.getConnection();
            PreparedStatement preparedStatement =connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("seed"),
                        rs.getString("serverSeed"),
                        rs.getString("serverSeedHashed"),
                        rs.getInt("nonce"));

            } else {
                throw new IllegalArgumentException("User not found with ID: " + userId);
            }
        }catch (SQLException e) {
            throw new IllegalStateException("There is an error in the database", e);
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try(Connection connection = H2Config.getConnection();
            PreparedStatement preparedStatement =connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("seed"),
                        rs.getString("serverSeed"),
                        rs.getString("serverSeedHashed"),
                        rs.getInt("nonce"));

            } else {
                return null;
            }
        }catch (SQLException e) {
            throw new IllegalStateException("There is an error in the database", e);
        }
    }

    public String getUserSeed(String userId) {
        User user = getUser(userId);
        return user.getSeed();
    }

}
