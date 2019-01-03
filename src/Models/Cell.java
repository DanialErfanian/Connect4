package Models;

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

    public boolean isWiningState() {
        return isWiningState;
    }
}
