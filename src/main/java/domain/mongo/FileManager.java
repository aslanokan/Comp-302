package domain.mongo;

import domain.Player;

import java.io.*;

public class FileManager {
    static FileManager fileManager = new FileManager();

    public static FileManager getInstance() {
        return fileManager;
    }

    private void FileManager() {

    }

    public void saveGame(GameData data, String name) {
        /**
         *  @modifies: generates new file and saves parsed GameData into it
         *  @effects: print the error message if there is an IOException
         */
        try {
            String parsed = data.parse();
            saveFile(parsed, name);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void saveMap(MapData data, String name) {
        /**
         *  @modifies: generates new file and saves parsed MapData into it
         *  @effects: print the error message if there is an IOException
         */
        try {
            String parsed = data.parse();
            saveFile(parsed, name);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public GameData loadGame(String name) {
        /**
         *  @requires: saved file with given name
         *  @effects: print the error message if there is an IOException
         *  @returns: GameData of saved game data with given name.
         */
        try {
            String text = loadFile(name);
            return GameData.unParse(text);
        } catch (IOException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public MapData loadMap(String name) {
        /**
         *  @requires: saved file with given name
         *  @effects: print the error message if there is an IOException
         *  @returns: MapData of saved map data with given name.
         */
        try {
            String text = loadFile(name);
            return MapData.unParse(text);
        } catch (IOException e) {
            System.out.println(e.toString());
            return null;
        }
    }

    private void saveFile(String data, String name) throws IOException {
        /**
         *  @modifies: generates new file with given name and data
         *  @throws: IOException
         */
        BufferedWriter writer = new BufferedWriter(new FileWriter(name));
        writer.write(data);
        writer.close();
    }

    private String loadFile(String name) throws IOException {
        /**
         *  @requires: saved file with given name
         *  @returns: String of save data with given name.
         *  @throws: IOException
         */

        BufferedReader reader = new BufferedReader(new FileReader(name));
        String data = "";
        String temp;
        while ((temp = reader.readLine()) != null) {
            data += temp + "\n";
        }
        reader.close();
        return data;
    }

    public Player getUser(String username, String password) {
        /**
         *  @requires: saved file with given username-password combination
         *  @effects: print the error message if there is an IOException or NumberFormatException
         *  @returns: Player of saved player data with given name and password.
         */
        try {
            String fileName = username + ":" + password;
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String data = "";
            String temp;
            while ((temp = reader.readLine()) != null) {
                data += temp + "\n";
            }
            reader.close();
            String[] splitted = data.split("\n")[0].split(":");
            try {
                int score = Integer.parseInt(splitted[2]);
                Player user = new Player(username, password, score);
                return user;
            } catch (NumberFormatException e) {
                System.out.println("Couldn't convert life or score to integer.\n" + e.toString());
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public void saveUser(Player user) throws Exception {
        /**
         *  @modifies: generates new file and saves parsed Player data into it
         *  @effects: print the error message if there is an IOException
         */
        try {
            String fileName = user.getUsername() + ":" + user.getPassword();
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(user.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println(e.toString());
            throw new Exception();
        }
    }
}
