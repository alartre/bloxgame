package com.bloxgame.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bloxgame.model.User;
import com.bloxgame.repository.UserRepository;
import com.bloxgame.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.eclipse.jetty.util.StringUtil;

public class UserServiceTest {

    private UserRepository mockUserRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        mockUserRepository = mock(UserRepository.class);
        userService = new UserService(mockUserRepository);
    }

    @Test
    public void testCreateUser() {
        when(mockUserRepository.createUser("testUsername", "testPassword"))
            .thenReturn(new User("testUserId", "testUsername", "testPassword", "seed", "serverSeed", "serverSeedHashed", 0));

        User user = userService.createUser("testUsername", "testPassword");

        assertNotNull(user);
        assertEquals("testUserId", user.getId());
        assertEquals("testUsername", user.getUsername());
        assertEquals("testPassword", user.getPassword());
    }

    @Test
    public void testCreateUserWithBlankUsernameOrPassword() {
        assertThrows(IllegalStateException.class, () -> userService.createUser("", "password"));
        assertThrows(IllegalStateException.class, () -> userService.createUser("username", ""));
    }

    @Test
    public void testUpdateUser() {
        User mockUser = new User("testUserId", "testUsername", "testPassword", "seed", "serverSeed", "serverSeedHashed", 0);

        doNothing().when(mockUserRepository).updateUserServerSeeds(mockUser);

        userService.updateUser(mockUser);

        verify(mockUserRepository).updateUserServerSeeds(mockUser);
    }

    @Test
    public void testUpdateUserWithNullUser() {
        assertThrows(IllegalStateException.class, () -> userService.updateUser(null));
    }

    @Test
    public void testGetUser() {
        when(mockUserRepository.getUser("testUserId"))
            .thenReturn(new User("testUserId", "testUsername", "testPassword", "seed", "serverSeed", "serverSeedHashed", 0));

        User user = userService.getUser("testUserId");

        assertNotNull(user);
        assertEquals("testUserId", user.getId());
        assertEquals("testUsername", user.getUsername());
    }

    @Test
    public void testGetUserWithBlankUserId() {
        assertThrows(IllegalStateException.class, () -> userService.getUser(""));
    }

    @Test
    public void testSetUserSeed() {
        User mockUser = new User("testUserId", "testUsername", "testPassword", "newSeed", "serverSeed", "serverSeedHashed", 0);

        when(mockUserRepository.setUserSeed("newSeed", "testUserId")).thenReturn(mockUser);

        User updatedUser = userService.setUserSeed("newSeed", "testUserId");

        assertNotNull(updatedUser);
        assertEquals("newSeed", updatedUser.getSeed());
    }


    @Test
    public void testSetUserSeedWithBlankParams() {
        assertThrows(IllegalStateException.class, () -> userService.setUserSeed("", "userId"));
        assertThrows(IllegalStateException.class, () -> userService.setUserSeed("seed", ""));
    }
}
