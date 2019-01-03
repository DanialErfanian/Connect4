package Models;

import java.util.ArrayList;

public class Player {
    private static ArrayList<Player> players = new ArrayList<>();
    private String name;

    public static Player getOrCreate(String name) {
        for (Player player : players)
            if (player.name.equals(name))
                return player;
        return new Player(name);
    }

    private Player(String name) {
        this.name = name;
        players.add(this);
    }

    @Override
    public String toString() {
        return String.format("Player<%s>", name);
    }

    public String getName() {
        return name;
    }
}
