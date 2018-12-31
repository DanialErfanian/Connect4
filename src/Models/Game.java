package Models;

import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Game {
    private final Player[] players = new Player[2];
    private final int mapHeight = 6;
    private final int mapWidth = 7;
    private boolean turn = false;
    private Cell[][] cells = new Cell[mapHeight][mapWidth];
    private boolean done = false;
    private Timer timer;
    private TimerTask timerTask;

    public Game(Player player1, Player player2) {
        players[0] = player1;
        players[1] = player2;
        new Thread(this::resetTimer).start();
    }

    private void resetTimer() {
        if (relax(false)) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resetTimer();
            return;
        }
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

    private boolean columnIsValid(int column) {
        return column >= 0 && column < mapWidth;
    }

    private Player getPlayer() {
        if (done)
            return null;
        if (turn)
            return players[1];
        return players[0];
    }

    public boolean addCell(int column) {
        return addCell(new Cell(getPlayer()), column);
    }

    private boolean addCell(Cell cell, int column) {
        if (done) return false;
        if (relax(false)) return false;
        if (!columnIsValid(column) || cells[0][column] != null)
            return false;
        this.cells[0][column] = cell;
        changeTurn();
        return true;
    }

    public boolean relax() {
        return relax(true);
    }

    private void addRandomCell() {
        if (timer != null)
            timer.cancel();
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

    public boolean relax(boolean commit) {
        boolean relaxed = false;
        for (int column = 0; column < mapWidth; column++) {
            for (int row = mapHeight - 2; row >= 0; row--) {
                if (cells[row][column] != null && cells[row + 1][column] == null) {
                    if (commit) {
                        cells[row + 1][column] = cells[row][column];
                        cells[row][column] = null;
                    }
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
        new Thread(this::resetTimer).start();
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

    public Player getWinner() {
        if (relax(false))
            return null;
        for (int column = 0; column < mapWidth; column++)
            for (int row = 0; row < mapHeight; row++)
                if (getCellPlayer(row, column) != null) {
                    boolean flag = false;
                    if (getCellPlayer(row + 1, column) == getCellPlayer(row, column))
                        if (getCellPlayer(row + 2, column) == getCellPlayer(row, column))
                            if (getCellPlayer(row + 3, column) == getCellPlayer(row, column))
                                flag = true;
                    if (getCellPlayer(row, column + 1) == getCellPlayer(row, column))
                        if (getCellPlayer(row, column + 2) == getCellPlayer(row, column))
                            if (getCellPlayer(row, column + 3) == getCellPlayer(row, column))
                                flag = true;
                    if (getCellPlayer(row + 1, column + 1) == getCellPlayer(row, column))
                        if (getCellPlayer(row + 2, column + 2) == getCellPlayer(row, column))
                            if (getCellPlayer(row + 3, column + 3) == getCellPlayer(row, column))
                                flag = true;
                    if (getCellPlayer(row - 1, column + 1) == getCellPlayer(row, column))
                        if (getCellPlayer(row - 2, column + 2) == getCellPlayer(row, column))
                            if (getCellPlayer(row - 3, column + 3) == getCellPlayer(row, column))
                                flag = true;
                    if (flag) {
                        done = true;
                        return getCellPlayer(row, column);
                    }
                }
        return null;
    }

    public Player getCellPlayer(int row, int col) {
        try {
            return cells[row][col].getPlayer();
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }


    public static Game loadFromFile(String path) {
        try {
            String text = new String(Files.readAllBytes(Paths.get(path)));
            YaGson mapper = new YaGsonBuilder().setPrettyPrinting().create();
            return mapper.fromJson(text, Game.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveToFile(String path) {
        YaGson mapper = new YaGsonBuilder().setPrettyPrinting().create();
        File file = new File(path);
        try {
            PrintStream result = new PrintStream(file);
            result.println(mapper.toJson(this, Game.class));
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public String getCurrentTurnName() {
        if (done)
            return "Game is done";
        if (turn)
            return players[1].getName();
        else
            return players[0].getName();
    }

    public TimerTask getTimerTask() {
        return timerTask;
    }
}
