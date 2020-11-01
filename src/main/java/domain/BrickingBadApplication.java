package domain;

import domain.mongo.Database;
import ui.BrickingBadFrame;

public class BrickingBadApplication {
    static private BrickingBadFrame frame;
    static private Player user;

    public static void main(String[] args) {
        openApplication();
        openLoginScreen();
    }

    public static void openApplication() {
        Database db = Database.getInstance();
        try {
            db.setupDatabase();
        } catch (Exception e) {
//            System.out.println(e.toString());
            System.out.println("Mongo Connection error.");
        }
        System.out.println("Mongo Successfully connected.");

        frame = new BrickingBadFrame();
    }

    public static void openLoginScreen() {
        frame.openLoginPanel();
    }

    public static void setUser(Player u) {
        user = u;
    }

    public static Player getUser() {
        return user;
    }
}
