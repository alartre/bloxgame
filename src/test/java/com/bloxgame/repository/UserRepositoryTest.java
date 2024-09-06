package com.bloxgame.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bloxgame.config.H2Config;
import com.bloxgame.model.User;
import com.bloxgame.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepositoryTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private UserRepository userRepository;
    private MockedStatic<H2Config> h2ConfigMock;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        h2ConfigMock = Mockito.mockStatic(H2Config.class);
        when(H2Config.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        userRepository = new UserRepository();
    }

    @AfterEach
    public void tearDown() {
        h2ConfigMock.close();
    }

    @Test
    public void testCreateUser() throws SQLException {
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        User createdUser = userRepository.createUser("testUsername", "testPassword");

        assertNotNull(createdUser.getId());
        verify(mockPreparedStatement).setString(1, createdUser.getId());
        verify(mockPreparedStatement).setString(2, "testUsername");
        verify(mockPreparedStatement).setString(3, "testPassword");
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testCreateUserAlreadyExists() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userRepository.createUser("testUsername", "testPassword");
        });

        assertEquals("There is a user with that username", exception.getMessage());
    }

    @Test
    public void testGetUserById() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("id")).thenReturn("testUserId");
        when(mockResultSet.getString("username")).thenReturn("testUsername");
        when(mockResultSet.getString("password")).thenReturn("testPassword");
        when(mockResultSet.getString("seed")).thenReturn("userSeed123");
        when(mockResultSet.getString("serverSeed")).thenReturn("serverSeed123");
        when(mockResultSet.getString("serverSeedHashed")).thenReturn("serverSeedHashed123");
        when(mockResultSet.getInt("nonce")).thenReturn(1);

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        User user = userRepository.getUser("testUserId");

        assertNotNull(user);
        assertEquals("testUserId", user.getId());
        assertEquals("testUsername", user.getUsername());
        assertEquals("testPassword", user.getPassword());
    }

    @Test
    public void testGetUserByUsername() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("id")).thenReturn("testUserId");
        when(mockResultSet.getString("username")).thenReturn("testUsername");
        when(mockResultSet.getString("password")).thenReturn("testPassword");
        when(mockResultSet.getString("seed")).thenReturn("userSeed123");
        when(mockResultSet.getString("serverSeed")).thenReturn("serverSeed123");
        when(mockResultSet.getString("serverSeedHashed")).thenReturn("serverSeedHashed123");
        when(mockResultSet.getInt("nonce")).thenReturn(1);

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        User user = userRepository.getUserByUsername("testUsername");

        assertNotNull(user);
        assertEquals("testUserId", user.getId());
        assertEquals("testUsername", user.getUsername());
        assertEquals("testPassword", user.getPassword());
    }

    @Test
    public void testSetUserSeed() throws SQLException {
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("id")).thenReturn("testUserId");
        when(mockResultSet.getString("seed")).thenReturn("newSeed123");

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        User updatedUser = userRepository.setUserSeed("newSeed123", "testUserId");

        assertNotNull(updatedUser);
        assertEquals("newSeed123", updatedUser.getSeed());
        verify(mockPreparedStatement).setString(1, "newSeed123");
        verify(mockPreparedStatement).setString(2, "testUserId");
    }

    @Test
    public void testUpdateUserServerSeeds() throws SQLException {
        User mockUser = new User("testUserId", "testUsername", "testPassword", "seed123", "newServerSeed123", "newServerSeedHashed123", 2);

        userRepository.updateUserServerSeeds(mockUser);

        verify(mockPreparedStatement).setString(1, "newServerSeed123");
        verify(mockPreparedStatement).setString(2, "newServerSeedHashed123");
        verify(mockPreparedStatement).setInt(3, 2);
        verify(mockPreparedStatement).setString(4, "testUserId");
        verify(mockPreparedStatement).executeUpdate();
    }
}
