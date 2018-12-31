package sample;

import Models.Game;
import Models.Player;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class GameStarter {
    public TextField secondPlayerName;
    public TextField firstPlayerName;

    public void startGame() throws Exception {
        System.out.println(String.format("player1: %s\nplayer2: %s\nStarted the game.",
                firstPlayerName.getText(),
                secondPlayerName.getText()));

        Scene scene = firstPlayerName.getScene();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameUI.fxml"));
        System.out.println("GameUI loaded successfully.");
        Parent root = fxmlLoader.load();
        scene.getStylesheets().clear();
        scene.getStylesheets().add("file:///home/danial/Desktop/Uni/AP/Connect4/css/style.css");
        scene.setRoot(root);
        System.out.println("Root changed successfully.");
        System.out.println("Loaded styles:");
        System.out.println(scene.getStylesheets());
        System.out.println("Game starting...");
        Player player1 = new Player(firstPlayerName.getText(), Color.RED);
        Player player2 = new Player(secondPlayerName.getText(), Color.YELLOW);
        Game game = new Game(player1, player2);
        GameController controller = fxmlLoader.getController();
        controller.initialize(game);
        System.out.println("Game started.");
    }

}
