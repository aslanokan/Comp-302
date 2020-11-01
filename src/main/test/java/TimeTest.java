import domain.Time;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeTest {
    private static Time time;

    @BeforeClass
    public static void setupClass() {
        time = Time.getInstance();
        time.unPause();
        time.update();
    }

    @Before
    public void setup() {
        // make sure Time is unpaused before each test
        time.unPause();
    }

    @Test
    public void testTime() {
        try {
            time.update();
            double before = time.time();
            int delta = 1000;

            Thread.sleep(delta);

            time.update();
            double after = time.time();
            assertEquals(before + delta, after, 10);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeltaTime() {
        try {
            time.update();
            double before = time.time();

            Thread.sleep(1000);

            time.update();
            double after = time.time();
            double deltaTime = time.deltaTime();

            assertEquals(deltaTime, after - before, 10);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPause() {
        try {
            time.update();
            time.pause();

            double before = time.time();

            Thread.sleep(1000);

            time.update();
            double after = time.time();

            assertEquals(before, after, 0);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testPauseUnPause() {
        try {
            time.update();
            time.pause();

            Thread.sleep(1000);
            time.update();

            time.unPause();

            double before = time.time();
            int delta = 1000;
            Thread.sleep(delta);
            time.update();
            double after = time.time();

            assertEquals(before + delta, after, 10);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
