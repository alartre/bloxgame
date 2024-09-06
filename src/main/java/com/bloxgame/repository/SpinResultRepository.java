package com.bloxgame.repository;

import com.bloxgame.config.H2Config;
import com.bloxgame.model.SpinResult;
import com.bloxgame.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpinResultRepository {

    public SpinResultRepository() {
        H2Config.createTables();
    }

    public void addGame(String userId, SpinResult result) {
        String sql = "INSERT INTO spin_results (id, number, isPrime, userId, seed, serverSeed, nonce) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = H2Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, result.getId());
            preparedStatement.setInt(2, result.getNumber());
            preparedStatement.setBoolean(3, result.isPrime());
            preparedStatement.setString(4, result.getUserId());
            preparedStatement.setString(5, result.getUserSeed());
            preparedStatement.setString(6, result.getServerSeed());
            preparedStatement.setInt(7, result.getNonce());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public List<SpinResult> getGames(String userId) {
        List<SpinResult> results = new ArrayList<>();
        String sql = "SELECT * FROM spin_results WHERE userId = ?";
        try (Connection connection = H2Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                int number = rs.getInt("number");
                boolean isPrime = rs.getBoolean("isPrime");
                String userSeed = rs.getString("seed");
                String serverSeed = rs.getString("serverSeed");
                int nonce = rs.getInt("nonce");
                results.add(new SpinResult(id, number, isPrime, userId, userSeed, serverSeed, nonce));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return results;
    }

    public SpinResult getGame(String gameId) {
        String sql = "SELECT * FROM spin_results WHERE id = ?";
        try (Connection connection = H2Config.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, gameId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return new SpinResult(
                        rs.getString("id"),
                        rs.getInt("number"),
                        rs.getBoolean("isPrime"),
                        rs.getString("userId"),
                        rs.getString("seed"),
                        rs.getString("serverSeed"),
                        rs.getInt("nonce"));

            } else {
                throw new IllegalArgumentException("Game not found with id " + gameId);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
