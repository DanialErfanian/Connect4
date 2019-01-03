package Controller;

import Models.Cell;
import Models.Player;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

// TODO: GameStarter UI change initial page size
// TODO: merge backgrounds
// TODO: add "back to main page" button

public class Game {
    private final Player[] players = new Player[2];
    private final int mapHeight = 6;
    private final int mapWidth = 7;
    private boolean turn = false;
    private boolean done = false;
    private boolean stable = true;
    private Cell[][] cells = new Cell[mapHeight][mapWidth];
    private Timer timer;
    private TimerTask timerTask;

    Game(Player player1, Player player2) {
        players[0] = player1;
        players[1] = player2;
        resetTimer();
    }

    int getMapHeight() {
        return mapHeight;
    }

    int getMapWidth() {
        return mapWidth;
    }

    private boolean columnIsValid(int column) {
        return column >= 0 && column < mapWidth;
    }


    private void resetTimer() {
        if (timer != null)
            timer.cancel();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                addRandomCell();
            }
        };
        timer.schedule(timerTask, 10 * 1000);
    }

    private Player getPlayer() {
        return turn ? players[1] : players[0];
    }

    boolean addCell(int column) {
        return addCell(new Cell(getPlayer()), column);
    }

    private boolean addCell(Cell cell, int column) {
        if (done || !stable || !columnIsValid(column) || cells[0][column] != null)
            return false;
        this.cells[0][column] = cell;
        changeTurn();
        return true;
    }

    private void addRandomCell() {
        if (done)
            return;
        int num = new Random().nextInt(mapWidth);
        int i;
        for (i = 0; num > 0 && i < mapWidth * mapWidth; i++)
            if (cells[0][i % mapWidth] == null)
                num--;
        if (num > 0) done = true;
        else addCell(i);
    }

    private boolean relax() {
        boolean relaxed = false;
        for (int column = 0; column < mapWidth; column++) {
            for (int row = mapHeight - 2; row >= 0; row--) {
                if (cells[row][column] != null && cells[row + 1][column] == null) {
                    cells[row + 1][column] = cells[row][column];
                    cells[row][column] = null;
                    relaxed = true;
                }
            }
        }
        return relaxed;
    }


    private void changeTurn() {
        if (done)
            return;
        turn ^= true;
    }

    @Override
    public String toString() {
        int[][] cells = new int[mapHeight][mapWidth];
        for (int i = 0; i < mapHeight; i++)
            for (int j = 0; j < mapWidth; j++) {
                cells[i][j] = 0;
                Cell cell = this.cells[i][j];
                if (cell != null)
                    if (cell.getPlayer() == players[0])
                        cells[i][j] = 1;
                    else
                        cells[i][j] = 2;
            }
        String array = Arrays.deepToString(cells);
        array = array.replaceAll("], ", "]\n");
        array = array.substring(1);
        return String.format("Game: \n%s", array);
    }

    Player getWinner() {
        int[][] dx = new int[][]{{1, 2, 3}, {0, 0, 0}, {1, 2, 3}, {-1, -2, -3}};
        int[][] dy = new int[][]{{0, 0, 0}, {1, 2, 3}, {1, 2, 3}, {+1, +2, +3}};
        for (int column = 0; column < mapWidth; column++)
            for (int row = 0; row < mapHeight; row++)
                if (getCellPlayer(row, column) != null)
                    for (int l = 0; l < 4; l++) {
                        boolean flag = true;
                        for (int i = 0; flag && i < 3; i++)
                            flag = getCellPlayer(row + dx[l][i], column + dy[l][i]) == getCellPlayer(row, column);
                        if (flag) {
                            done = true;
                            for (int i = 0; i < 3; i++)
                                getCell(row + dx[l][i], column + dy[l][i]).setAsWiningState();
                            getCell(row, column).setAsWiningState();
                            return getCellPlayer(row, column);
                        }
                    }
        return null;
    }

    Cell getCell(int row, int col) {
        try {
            return cells[row][col];
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }

    private Player getCellPlayer(int row, int col) {
        try {
            return cells[row][col].getPlayer();
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }

    String getCurrentTurnName() {
        if (turn)
            return players[1].getName();
        else
            return players[0].getName();
    }

    TimerTask getTimerTask() {
        return timerTask;
    }

    boolean isDone() {
        return done;
    }

    boolean clock() {
        boolean now = !relax();
        if (!stable && now)
            resetTimer();
        stable = now;
        if (!done) {
            Player winner = getWinner();
            if (winner != null) {
                Player.updateStatistics(players[0], players[1], winner);
                done = true;
            }
        }
        boolean lastDone = done;
        done = stable;
        for (int col = 0; col < mapWidth; col++)
            if (cells[0][col] == null)
                done = false;
        if (!lastDone && done) Player.updateStatistics(players[0], players[1], null);
        return stable;
    }

    boolean isStable() {
        return stable;
    }

    Color getColor(int row, int column) {
        assert (cells[row][column] != null);
        if (cells[row][column].getPlayer() == players[0])
            return Color.RED;
        else
            return Color.YELLOW;
    }
}
