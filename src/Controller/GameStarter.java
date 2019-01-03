package Controller;

import Models.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GameStarter {
    static void show(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(GameStarter.class.getResource("../Views/GameStarter.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

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
        GameController controller = loader.getController();
        Player player1 = Player.getOrCreate(firstPlayerName.getText());
        Player player2 = Player.getOrCreate(secondPlayerName.getText());
        Game game = new Game(player1, player2);
        controller.initialize(game);
        System.out.println("Game started.");
        stage.setScene(scene);
    }
}
