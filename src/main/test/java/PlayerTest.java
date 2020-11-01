import domain.Player;
import domain.listeners.PlayerListener;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlayerTest {
    Player player;

    @Before
    public void setUp() {
        player = new Player("Test1");
        player.addListener(new PlayerListener() {
            @Override
            public void onPlayerEvent(String name, Object o) {
//                System.out.println(name);
            }
        });
    }

    @Test
    public void testUpdateScore() {
        player.updateScore(10);
        assertEquals(10, player.getHighScore());
        assertTrue(player.repOk());
    }

    @Test
    public void testStartNewGame() {
        player.startNewGame();
        assertEquals(0, player.getHighScore());
        assertTrue(player.repOk());
    }

    @Test
    public void testGetUsername() {
        assertEquals("Test1", player.getUsername());
        assertTrue(player.repOk());
    }

    @Test
    public void testGetScore() {
        assertEquals(0, player.getHighScore());
        assertTrue(player.repOk());
    }
}





