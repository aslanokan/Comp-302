package domain;

import domain.listeners.PlayerListener;
import domain.mongo.Database;

import java.util.ArrayList;

public class Player {

    /**
     * OVERVIEW: This class is used for keeping track of the current players, life and score of the player.
     * Representation Invariant:
     * score => 0 and 0<= life <=3 and username != null
     */

    private int highScore;
    private String username;
    private String password;
    private ArrayList<PlayerListener> listeners = new ArrayList<PlayerListener>();

    public Player(String username) {
        highScore = 0;
        this.username = username;
    }

    public Player(String username, String password, int highScore) {
        this.username = username;
        this.password = password;
        this.highScore = highScore;
    }

    public void updateScore(int newScore) {
/**
 * @param newScore updated score of the player
 *  @reqiures: newScore should be positive
 *  @modifies: score
 *  @effects: add the given score to current score
 */
        highScore += newScore;
        publishEvent("newScore", highScore);
    }

    public int getHighScore() {
        /**
         *  @return current score
         */
        return highScore;
    }

    public void startNewGame() {
/**
 *  @modifies: life, score
 *  @effects: the life of the player decrement by 1
 */
        highScore = 0;
    }

    public void publishEvent(String name, Object obj) {
/**
 *  @param name name of the event
 *  @param obj changed value of the class
 *  @effects: send the new value to the ui
 */
        for (PlayerListener pl : listeners) {
            pl.onPlayerEvent(name, obj);
        }
    }

    public void addListener(PlayerListener ls) {
/**
 *  @param ls PlayerListener
 *  @modifies: listeners
 *  @effects: add new listener to the listeners array
 */
        listeners.add(ls);
    }

    public String getUsername()
    /**
     *  @return username
     */
    {
        return username;
    }

    public String getPassword()
    /**
     *  @return password
     */
    {
        return password;
    }

    public void compareResult(int score) {
        if (highScore < score) {
            this.highScore = score;
            Database.getInstance().updateUserScore(this, highScore);
        }
    }

    @Override
    public String toString() {
        /**
         *  @return generate string of the object
         */
        return username +
                ":" + password +
                ":" + highScore;
    }

    public boolean repOk() {
        if (username == null) {
            return false;
        } else if (highScore < 0) {
            return false;
        } else return true;
    }
}

