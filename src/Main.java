import Models.Game;
import Models.Player;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String loadPath = null;
        String savePath = null;
        if (args.length >= 2)
            loadPath = args[1];
        if (args.length >= 1)
            savePath = args[0];
        System.out.println(Arrays.toString(args));

        Game game;
        if (loadPath != null)
            game = Game.loadFromFile(loadPath);
        else {
            Player player1 = new Player("Danial", Color.RED);
            Player player2 = new Player("Jack", Color.RED);
            game = new Game(player1, player2);
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            int col = scanner.nextInt();
            Player winner = game.getWinner();
            if (winner != null) {
                System.out.println(winner);
                break;
            } else if (col == -1)
                game.relax();
            else if (col == -2)
                while (game.relax())
                    System.out.println("relaxed");
            else if (col == -3)
                break;
            else
                System.out.println(game.addCell(col));
            System.out.println(game);
        }
        if (savePath != null)
            game.saveToFile(savePath);
    }
}
