package domain.scenes;

import domain.BrickingBadApplication;
import domain.Player;
import domain.mongo.SaveLoadAdapter;

public class LoginScene {
    SaveLoadAdapter saveLoadAdapter = SaveLoadAdapter.getInstance();

    public void LoginScene() {
    }

    public boolean singIn(String username, String password) {
        /**
         *  @effects: return true if the user exists, otherwise returns false
         */
        if (username.equals("") || password.equals("")) {
            return false;
        }
        Player player = saveLoadAdapter.getUser(username, password);
        if (player != null) {
            BrickingBadApplication.setUser(player);
            return true;
        }
        return false;
    }

    public boolean signUp(String username, String password) {
        /**
         *  @modifies: creates a new document in the database or a new file
         *  @effects: return true if the user successfully created, otherwise returns false
         */
        if (username == "" || password == "") {
            return false;
        }
        if (saveLoadAdapter.getUser(username, password) != null) {
            return false;
        } else {
            Player user = new Player(username, password, 0);
            try {
                saveLoadAdapter.saveUser(user);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
