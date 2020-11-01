package domain.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.*;
import domain.Constants;
import domain.Player;
import org.bson.Document;

import java.util.Arrays;
import java.util.Date;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class Database {
    static Database db = new Database();
    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<Document> gameDataCollection;
    MongoCollection<Document> mapDataCollection;
    MongoCollection<Document> usersCollection;

    String auth_user = "brickingBad";
    char[] encoded_pwd = "Qwerty123".toCharArray();
    String host_name = "ds241723.mlab.com";
    int port_no = 41723;
    String db_name = "heroku_8z9dw9w1";
    MongoCredential credential = MongoCredential.createScramSha1Credential(auth_user, db_name, encoded_pwd);
    ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/brickingBad");
    MongoClientSettings settings = MongoClientSettings.builder()
            .applyToClusterSettings(builder ->
                    builder.hosts(Arrays.asList(new ServerAddress(host_name, port_no))))
            .credential(credential)
            .retryWrites(false)
            .build();

    public static Database getInstance() {
        return db;
    }

    private void Database() {

    }

    public void setupDatabase() {
        if (Constants.prod) {
            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase(db_name);
        } else {
            mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase("brickingBad");
        }
        gameDataCollection = database.getCollection("gameData");
        mapDataCollection = database.getCollection("mapData");
        usersCollection = database.getCollection("users");
    }

    public void insertMapData(MapData data) {
        try {
            String parsed = data.parse();
            Document doc = new Document("data", parsed)
                    .append("name", data.getName())
                    .append("creationDate", new Date());
            mapDataCollection.insertOne(doc);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void insertGameData(GameData data) {
        try {
            String parsed = data.parse();
            Document doc = new Document("data", parsed)
                    .append("name", data.getName())
                    .append("creationDate", new Date());
            gameDataCollection.insertOne(doc);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public MapData getMapData(String name) {
        try {
            Document data = mapDataCollection.find(eq("name", name)).sort(orderBy(descending("creationDate"))).first();
            String doc = data.get("data").toString();
            return MapData.unParse(doc);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public GameData getGameData(String name) {
        try {
            Document data = gameDataCollection.find(eq("name", name)).sort(orderBy(descending("creationDate"))).first();
            String doc = data.get("data").toString();
            return GameData.unParse(doc);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public Player getUser(String username, String password) {
        try {
            Document doc = usersCollection.find(and(eq("username", username), (eq("password", password)))).first();
            try {
                int score = Integer.parseInt(doc.get("score").toString());
                Player user = new Player(username, password, score);
                return user;
            } catch (NumberFormatException e) {
                System.out.println("Couldn't convert life or score to integer.\n" + e.toString());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public void saveUser(Player user) throws Exception {
        try {
            Document doc = new Document("username", user.getUsername())
                    .append("password", user.getPassword())
                    .append("score", user.getHighScore());
            usersCollection.insertOne(doc);
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new Exception();
        }
    }

    public Object[][] getHighScoreTable() {
        String[][] highScores = new String[10][3];
        int counter = 0;
        MongoCursor<Document> docs = usersCollection.find().limit(10).sort(orderBy(descending("score"))).iterator();
        try {
            while (docs.hasNext()) {
                Document user = docs.next();
                highScores[counter][0] = "" + (counter + 1);
                highScores[counter][1] = user.get("username").toString();
                highScores[counter][2] = user.get("score").toString();
                counter++;
            }
        } finally {
            docs.close();
            return highScores;
        }
    }

    public void updateUserScore(Player user, int score) {
        usersCollection.updateOne(and(eq("username", user.getUsername()), (eq("password", user.getPassword()))), combine(
                set("score", score)));
    }

//    public void update(String collection, String name, MapData data) {
//        mapDatacollection.updateOne(eq("name", name), combine(
//                set("bricks", data.getBricks())
//                set("paddle", data.getPaddle()),
//                set("ball", data.getBall())
//        ));
//    }
//
//    public void delete(String collection, String name) {
//        mapDatacollection.deleteOne(eq("name", name));
//    }
}
