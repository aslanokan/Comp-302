package domain;

public class Time {
    private static Time instance;
    private double time;
    private double prevTime;
    private double deltaTime;

    private double systemTime;
    private double systemPrevTime;
    private double systemDeltaTime;

    private boolean isPaused = true;

    private Time() {
        time = 0;
        prevTime = time;
        deltaTime = 0;
    }

    public void setTime(double time) {
        this.time = time;
        update();
    }

    public static Time getInstance() {
        if (instance == null) {
            instance = new Time();
        }
        return instance;
    }

    private void updateSystemTime() {
        systemPrevTime = systemTime;
        systemTime = System.currentTimeMillis();
        systemDeltaTime = systemTime - systemPrevTime;
    }

    /**
     * EFFECTS: Updates the time so that time() and deltaTime() will return new values
     */
    public void update() {
        if (!isPaused) {
            updateSystemTime();
            prevTime = time;
            time += systemDeltaTime;
            deltaTime = systemDeltaTime;
        }
    }

    /**
     * @return Change in time since last update() in milliseconds
     */
    public double deltaTime() {
        return deltaTime;
    }

    /**
     * @return Time since first update() in milliseconds
     */
    public double time() {
        return time;
    }

    /**
     * EFFECTS: Pauses the timer. update() will not change internal state when paused.
     */
    public void pause() {
        isPaused = true;
    }

    /**
     * EFFECTS: Resumes the timer. Calls to update() will continue updating internal state.
     */
    public void unPause() {
        isPaused = false;
        // update to ensure systemDeltaTime isn't too large
        updateSystemTime();
    }


}
