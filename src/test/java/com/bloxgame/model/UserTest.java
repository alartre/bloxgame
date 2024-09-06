import static org.junit.jupiter.api.Assertions.*;

import com.bloxgame.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.lang.reflect.Method;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("testUsername", "testPassword");
    }

    @Test
    public void testConstructorWithUsernameAndPassword() {
        assertNotNull(user.getId());
        assertEquals("testUsername", user.getUsername());
        assertEquals("testPassword", user.getPassword());
        assertNotNull(user.getSeed());
        assertNotNull(user.getServerSeed());
        assertNotNull(user.getServerSeedHashed());
        assertEquals(1, user.getNonce());
    }

    @Test
    public void testRotateServerSeed() {
        String oldServerSeed = user.getServerSeed();
        String oldServerSeedHashed = user.getServerSeedHashed();
        int oldNonce = user.getNonce();

        user.rotateServerSeed();

        assertNotNull(user.getServerSeed());
        assertNotEquals(oldServerSeed, user.getServerSeed());
        assertNotEquals(oldServerSeedHashed, user.getServerSeedHashed());
        assertEquals(oldNonce + 1, user.getNonce());
    }

    @Test
    public void testHashServerSeed() throws Exception {
        String serverSeed = UUID.randomUUID().toString();

        Method hashServerSeedMethod = User.class.getDeclaredMethod("hashServerSeed", String.class);
        hashServerSeedMethod.setAccessible(true);

        String hashedServerSeed = (String) hashServerSeedMethod.invoke(user, serverSeed);

        assertNotNull(hashedServerSeed);
        assertEquals(64, hashedServerSeed.length());
    }

    @Test
    public void testSetSeed() {
        String newSeed = UUID.randomUUID().toString();
        user.setSeed(newSeed);
        assertEquals(newSeed, user.getSeed());
    }
}
