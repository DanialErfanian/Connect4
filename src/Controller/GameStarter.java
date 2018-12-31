package Controller;

import Models.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameStarter {
    public TextField secondPlayerName;
    public TextField firstPlayerName;
    public BorderPane rootPane;

    @FXML
    public void startGame() throws Exception {
        System.out.println(String.format("player1: %s\nplayer2: %s\nstarted the game.",
                firstPlayerName.getText(),
                secondPlayerName.getText()));
        Stage stage = (Stage) rootPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/GameUI.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add("file:///home/danial/Desktop/Uni/AP/Connect4/css/style.css");
        GameController controller = loader.getController();
        Player player1 = new Player(firstPlayerName.getText(), Color.RED);
        Player player2 = new Player(secondPlayerName.getText(), Color.YELLOW);
        Game game = new Game(player1, player2);
        controller.initialize(game);
        System.out.println("Game started.");
        stage.setScene(scene);
        stage.setOnCloseRequest(windowEvent -> {
            System.out.println("here");
            stage.close();
            Platform.exit();
            System.exit(0);
        });
    }
}
