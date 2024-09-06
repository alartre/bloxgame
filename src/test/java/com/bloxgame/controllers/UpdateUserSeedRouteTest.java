import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.bloxgame.model.User;
import com.bloxgame.service.UserService;
import com.bloxgame.controllers.UpdateUserSeedRoute;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import java.util.Map;

public class UpdateUserSeedRouteTest {

    private UserService mockUserService;
    private Gson gson;
    private Request mockRequest;
    private Response mockResponse;
    private UpdateUserSeedRoute updateUserSeedRoute;

    @BeforeEach
    public void setUp() {
        mockUserService = mock(UserService.class);
        gson = new Gson();
        mockRequest = mock(Request.class);
        mockResponse = mock(Response.class);
        updateUserSeedRoute = new UpdateUserSeedRoute(mockUserService, gson);
    }

    @Test
    public void testHandle() throws Exception {
        when(mockRequest.headers("userId")).thenReturn("testUserId");
        when(mockRequest.queryParams("seed")).thenReturn("newUserSeed");

        User mockUser = new User("testUserId", "testUsername", "testPassword", "newUserSeed", "serverSeed123", "serverSeedHashed123", 5);
        when(mockUserService.setUserSeed("newUserSeed", "testUserId")).thenReturn(mockUser);

        Map<String, Object> expectedResponseMap = Map.of(
                "id", "testUserId",
                "username", "testUsername",
                "seed", "newUserSeed",
                "serverSeedHashed", "serverSeedHashed123",
                "nonce", 5
        );

        String expectedResponse = gson.toJson(expectedResponseMap);

        Object result = updateUserSeedRoute.handle(mockRequest, mockResponse);

        verify(mockResponse).type("application/json");
        assertEquals(expectedResponse, result);
    }
}
