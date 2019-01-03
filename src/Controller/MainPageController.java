package Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageController {


    public Button exit;
    public Button statistics;
    public Button startGame;

    public void showMainPage() throws IOException {
        Stage stage = (Stage) startGame.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/MainPage.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    public void closeStage() {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.getOnCloseRequest().handle(null);
    }

    public void startGame() throws IOException {
        Stage stage = (Stage) startGame.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Views/GameStarter.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
    }

    public void showStatistics() {
        // TODO
    }

}
