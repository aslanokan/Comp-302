import domain.mongo.Database;
import domain.scenes.LoginScene;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class LoginSceneTest {
    LoginScene loginScene = new LoginScene();

    @Before
    public void setup() {
        Database db = Database.getInstance();
        db.setupDatabase();
    }

    @Test()
    public void canSingUp() {
        Boolean canSingIn = loginScene.singIn("okan", "123456");
        Boolean canSingUp = loginScene.signUp("okan", "123456");
        if (canSingIn) {
            assertFalse(canSingUp);
        } else {
            assertTrue(canSingUp);
        }
    }

    @Test
    public void canSingIn() {
        loginScene.signUp("okan", "123456");
        Boolean canSingIn = loginScene.singIn("okan", "123456");
        assertTrue(canSingIn);
    }

    @Test
    public void canSingInFalse() {
        loginScene.signUp("okan", "123456");
        Boolean canSingIn = loginScene.singIn("okan", "1234");
        assertFalse(canSingIn);
    }

    @Test
    public void canSingInFalse2() {
        loginScene.signUp("okan", "123456");
        Boolean canSingIn = loginScene.singIn("ok", "123456");
        assertFalse(canSingIn);
    }
}
