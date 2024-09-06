package com.bloxgame.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bloxgame.config.H2Config;
import com.bloxgame.model.SpinResult;
import com.bloxgame.repository.SpinResultRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SpinResultRepositoryTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private SpinResultRepository spinResultRepository;
    private MockedStatic<H2Config> h2ConfigMock;

    @BeforeEach
    public void setUp() throws SQLException {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        h2ConfigMock = Mockito.mockStatic(H2Config.class);
        when(H2Config.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        spinResultRepository = new SpinResultRepository();
    }

    @AfterEach
    public void tearDown() {
        h2ConfigMock.close();
    }

    @Test
    public void testAddGame() throws SQLException {
        SpinResult spinResult = new SpinResult("spinId123", 10, true, "userId123", "userSeed123", "serverSeed123", 1);

        spinResultRepository.addGame("userId123", spinResult);

        verify(mockPreparedStatement).setString(1, "spinId123");
        verify(mockPreparedStatement).setInt(2, 10);
        verify(mockPreparedStatement).setBoolean(3, true);
        verify(mockPreparedStatement).setString(4, "userId123");
        verify(mockPreparedStatement).setString(5, "userSeed123");
        verify(mockPreparedStatement).setString(6, "serverSeed123");
        verify(mockPreparedStatement).setInt(7, 1);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void testGetGames() throws SQLException {
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("id")).thenReturn("spinId1").thenReturn("spinId2");
        when(mockResultSet.getInt("number")).thenReturn(10).thenReturn(20);
        when(mockResultSet.getBoolean("isPrime")).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("seed")).thenReturn("userSeed1").thenReturn("userSeed2");
        when(mockResultSet.getString("serverSeed")).thenReturn("serverSeed1").thenReturn("serverSeed2");
        when(mockResultSet.getInt("nonce")).thenReturn(1).thenReturn(2);

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        List<SpinResult> results = spinResultRepository.getGames("userId123");

        assertEquals(2, results.size());
        assertEquals("spinId1", results.get(0).getId());
        assertEquals(10, results.get(0).getNumber());
        assertTrue(results.get(0).isPrime());
        assertEquals("spinId2", results.get(1).getId());
        assertEquals(20, results.get(1).getNumber());
        assertFalse(results.get(1).isPrime());
    }

    @Test
    public void testGetGame() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("id")).thenReturn("spinId123");
        when(mockResultSet.getInt("number")).thenReturn(10);
        when(mockResultSet.getBoolean("isPrime")).thenReturn(true);
        when(mockResultSet.getString("userId")).thenReturn("userId123");
        when(mockResultSet.getString("seed")).thenReturn("userSeed123");
        when(mockResultSet.getString("serverSeed")).thenReturn("serverSeed123");
        when(mockResultSet.getInt("nonce")).thenReturn(1);

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        SpinResult result = spinResultRepository.getGame("spinId123");

        assertNotNull(result);
        assertEquals("spinId123", result.getId());
        assertEquals(10, result.getNumber());
        assertTrue(result.isPrime());
        assertEquals("userId123", result.getUserId());
        assertEquals("userSeed123", result.getUserSeed());
        assertEquals("serverSeed123", result.getServerSeed());
        assertEquals(1, result.getNonce());
    }

    @Test
    public void testGetGameNotFound() throws SQLException {
        when(mockResultSet.next()).thenReturn(false);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            spinResultRepository.getGame("spinIdNotFound");
        });

        assertEquals("Game not found with id spinIdNotFound", exception.getMessage());
    }
}
