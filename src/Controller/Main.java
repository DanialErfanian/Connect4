package Controller;

import Models.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../Views/MainPage.fxml"));
        primaryStage.setTitle("Connect4");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(windowEvent -> {
            Player.saveStatistics();
            System.out.println("Closing application...");
            primaryStage.close();
            Platform.exit();
            System.exit(0);
        });
        primaryStage.getIcons().add(new Image(new File("src/Views/background.jpg").toURI().toString()));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
