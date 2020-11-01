package domain.mongo;

import domain.Constants;
import domain.Player;

public class SaveLoadAdapter {

    private static final String where = Constants.database;
    private static SaveLoadAdapter saveLoadAdapter = new SaveLoadAdapter();
    FileManager fm = FileManager.getInstance();
    Database db = Database.getInstance();

    public static SaveLoadAdapter getInstance() {
        return saveLoadAdapter;
    }

    private void SaveLoadAdapter() {
    }

    public void saveGame(GameData data) {
        if (where == "mongo") {
            db.insertGameData(data);
        } else {
            fm.saveGame(data, data.getName());
        }
    }

    public GameData loadGame(String name) {
        if (where == "mongo") {
            return db.getGameData(name);
        } else {
            return fm.loadGame(name);
        }
    }

    public void saveMap(MapData data) {
        if (where == "mongo") {
            db.insertMapData(data);
        } else {
            fm.saveMap(data, data.getName());
        }
    }

    public MapData loadMap(String name) {
        if (where == "mongo") {
            return db.getMapData(name);
        } else {
            return fm.loadMap(name);
        }
    }

    public void saveUser(Player user) throws Exception {
        if (where == "mongo") {
            db.saveUser(user);
        } else {
            fm.saveUser(user);
        }
    }

    public Player getUser(String username, String password) {
        if (where == "mongo") {
            return db.getUser(username, password);
        } else {
            return fm.getUser(username, password);
        }
    }
}
