package Models;

class Cell {
    private Player player;

    Cell(Player player) {
        this.player = player;
    }

    Player getPlayer() {
        return player;
    }
}
