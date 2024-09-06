package com.bloxgame.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bloxgame.model.SpinResult;
import com.bloxgame.model.User;
import com.bloxgame.repository.SpinResultRepository;
import com.bloxgame.service.GameService;
import com.bloxgame.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameServiceTest {

    private SpinResultRepository mockSpinResultRepository;
    private UserService mockUserService;
    private GameService gameService;

    @BeforeEach
    public void setUp() {
        mockSpinResultRepository = mock(SpinResultRepository.class);
        mockUserService = mock(UserService.class);
        gameService = new GameService(mockSpinResultRepository, mockUserService);
    }

    @Test
    public void testSpinForUser() {
        User mockUser = new User("testUserId", "testUsername", "testPassword", "seed123", "serverSeed123", "serverSeedHashed123", 1);
        when(mockUserService.getUser("testUserId")).thenReturn(mockUser);

        GameService spyGameService = spy(gameService);
        doReturn(5).when(spyGameService).spin(any(Random.class));

        SpinResult mockSpinResult = new SpinResult(5, true, "testUserId", "seed123", "serverSeed123", 1);
        doNothing().when(mockUserService).updateUser(mockUser);
        doNothing().when(mockSpinResultRepository).addGame(anyString(), any(SpinResult.class));

        SpinResult result = spyGameService.spinForUser("testUserId");

        assertNotNull(result);
        assertEquals(5, result.getNumber());
        assertTrue(result.isPrime());

        verify(mockUserService).updateUser(mockUser);
        verify(mockSpinResultRepository).addGame(eq("testUserId"), any(SpinResult.class));
    }

    @Test
    public void testGetUserHistory() {
        SpinResult result1 = new SpinResult(5, true, "testUserId", "seed123", "serverSeed123", 1);
        SpinResult result2 = new SpinResult(10, false, "testUserId", "seed123", "serverSeed123", 2);
        List<SpinResult> mockResults = Arrays.asList(result1, result2);

        when(mockSpinResultRepository.getGames("testUserId")).thenReturn(mockResults);

        List<SpinResult> results = gameService.getUserHistory("testUserId");

        assertEquals(2, results.size());
        assertEquals(5, results.get(0).getNumber());
        assertEquals(10, results.get(1).getNumber());
    }

    @Test
    public void testGetGame() {
        SpinResult mockSpinResult = new SpinResult(5, true, "testUserId", "seed123", "serverSeed123", 1);
        when(mockSpinResultRepository.getGame("gameId123")).thenReturn(mockSpinResult);

        SpinResult result = gameService.getGame("gameId123");

        assertNotNull(result);
        assertEquals(5, result.getNumber());
        assertTrue(result.isPrime());
    }

    @Test
    public void testIsPrime() throws Exception {
        Method isPrimeMethod = GameService.class.getDeclaredMethod("isPrime", int.class);
        isPrimeMethod.setAccessible(true);

        boolean result = (boolean) isPrimeMethod.invoke(gameService, 5);
        assertTrue(result);

        result = (boolean) isPrimeMethod.invoke(gameService, 4);
        assertFalse(result);
    }

    @Test
    public void testSpin() throws Exception {
        Method spinMethod = GameService.class.getDeclaredMethod("spin", Random.class);
        spinMethod.setAccessible(true);

        int result = (int) spinMethod.invoke(gameService, new Random(1));
        assertTrue(result >= 1 && result <= 20);
    }
}
