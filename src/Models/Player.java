package Models;

import javafx.scene.paint.Color;

public class Player {
    private String name;
    private Color color;


    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    @Override
    public String toString() {
        return String.format("Player %s with color %s", name, color);
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
