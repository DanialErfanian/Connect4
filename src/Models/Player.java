package Models;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Player {
    private static final String statisticsFilePath = "statistics.json";
    private static ArrayList<Player> playersStatistics = new ArrayList<>();

    public static ArrayList<Player> getPlayersStatistics() {
        return playersStatistics;
    }

    static {
        loadStatistics();
    }

    private String name;
    private int wins = 0, draws = 0, losses = 0;

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    public static void updateStatistics(Player player1, Player player2, Player winner) {
        System.out.println("updating statistics");
        if (winner == player1) {
            player1.wins++;
            player2.losses++;
        } else if (winner == player2) {
            player2.wins++;
            player1.losses++;
        } else {
            player1.draws++;
            player2.draws++;
        }
    }

    public static Player getOrCreate(String name) {
        for (Player player : playersStatistics)
            if (player.name.equals(name))
                return player;
        return new Player(name);
    }

    private Player(String name) {
        this.name = name;
        playersStatistics.add(this);
    }

    @Override
    public String toString() {
        return String.format("Player<%s>", name);
    }

    public String getName() {
        return name;
    }

    public static void saveStatistics() {
        try {
            System.out.println("Saving statistics...");
            System.out.println(playersStatistics);
            YaGson mapper = new YaGsonBuilder().setPrettyPrinting().create();
            Player[] players = playersStatistics.toArray(new Player[0]);
            String json = mapper.toJson(players, players.getClass());
            File file = new File(statisticsFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(json.getBytes());
            System.out.println("statistics saved.");
        } catch (IOException ignored) {
        }
    }

    private static void loadStatistics() {
        try {
            YaGson mapper = new YaGsonBuilder().setPrettyPrinting().create();
            String json = new String(Files.readAllBytes(Paths.get(statisticsFilePath)));
            Player[] players = mapper.fromJson(json, Player[].class);
            playersStatistics = new ArrayList<>(Arrays.asList(players));
        } catch (IOException ignored) {
        }
    }
}
