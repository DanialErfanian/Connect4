// TODO: change color when winner determined

package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../Views/GameStarter.fxml"));
        primaryStage.setTitle("Connect4");
        primaryStage.setScene(new Scene(root, 1008, 830));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
