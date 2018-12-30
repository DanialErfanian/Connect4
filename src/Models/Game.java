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


public class Game {
    private final Player[] players = new Player[2];
    private final int mapHeight = 6;
    private final int mapWidth = 7;
    private boolean turn = false;
    private Cell[][] cells = new Cell[mapHeight][mapWidth];

    public Game(Player player1, Player player2) {
        players[0] = player1;
        players[1] = player2;
    }

    private boolean columnIsValid(int column) {
        return column >= 0 && column < mapWidth;
    }

    private Player getPlayer() {
        if (turn)
            return players[1];
        return players[0];
    }

    public boolean addCell(int column) {
        return addCell(new Cell(getPlayer()), column);
    }

    private boolean addCell(Cell cell, int column) {
        if (relax()) return false;
        if (!columnIsValid(column) || cells[0][column] != null)
            return false;
        this.cells[0][column] = cell;
        changeTurn();
        return true;
    }

    public boolean relax() {
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

    public Player getWinner() {
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
                    if (flag)
                        return getCellPlayer(row, column);
                }
        return null;
    }

    private Player getCellPlayer(int row, int col) {
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

}

