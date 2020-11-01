import domain.Player;
import domain.mongo.Database;
import domain.mongo.SaveLoadAdapter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SaveLoadAdapterTest {

    SaveLoadAdapter saveLoadAdapter = SaveLoadAdapter.getInstance();

    @Before
    public void setup() {
        Database db = Database.getInstance();
        db.setupDatabase();
    }


    @Test()
    @DisplayName("Loads Saved game data and checks if the ball.x is correct.")
    public void saveAndLoadGameTest() {
        GameData save = new GameData("loadGameTest", new ArrayList<Brick>(), new Paddle(1, 1), new Ball(10, 10, 1, 1));
        saveLoadAdapter.saveGame(save);

        GameData load = saveLoadAdapter.loadGame("loadGameTest");
        Ball ball = load.getBall();
        assertEquals(ball.getX(), 1, 10);
    }

    @Test
    @DisplayName("Test if true holds")
    public void saveAndLoadMapTest() {
        ArrayList<Brick> bricks = new ArrayList<>();
        bricks.add(new SimpleBrick(1, 1));
        MapData save = new MapData("loadMapTest", bricks);
        saveLoadAdapter.saveMap(save);

        MapData load = saveLoadAdapter.loadMap("loadMapTest");
        Brick brick = load.getBricks().get(0);
        assertEquals(brick.getX(), 1, 10);
    }

    @Test
    public void saveAndLoadPlayerTest() {
        Player user = new Player("Okan", "123", 99999999, 99999999);
        try {
            saveLoadAdapter.saveUser(user);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        Player saveUser = saveLoadAdapter.getUser("Okan", "123");
        assertEquals(user.getLife(), saveUser.getLife());
    }

    @Test
    public void saveAndLoadPlayerTest2() {
        Player user = new Player("Okan", "123", 99999999, 99999999);
        try {
            saveLoadAdapter.saveUser(user);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        Player saveUser = saveLoadAdapter.getUser("Okan", "");
        assertNull(saveUser);
    }

    @Test
    public void saveAndLoadPlayerTest3() {
        Player user = new Player("Okan", "123", 99999999, 99999999);
        try {
            saveLoadAdapter.saveUser(user);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        Player saveUser = saveLoadAdapter.getUser("okan", "123");
        assertNull(saveUser);
    }
}
