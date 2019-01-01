package Models;

import javafx.scene.paint.Color;

public class Cell {
    private Player player;
    private boolean isWiningState = false;

    public Cell(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setAsWiningState() {
        isWiningState = true;
    }

    public Color getColor() {
        return player.getColor();
    }

    public boolean isWiningState() {
        return isWiningState;
    }
}
