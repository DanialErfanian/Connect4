package Controller;

import Models.Cell;
import Models.Player;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class GameController {
    public Label label;
    public Label timer;
    public VBox table;

    private Game game;
    private int currentFrame = 0;
    private boolean isAlive = true;

    void initialize(Game game) {
        this.game = game;
        startGameUpdater();
        setCircleOnAction();
    }

    private void startGameUpdater() {
        Thread updater = new Thread(() -> {
            final int counter = 10000000;
            for (int i = 1; this.isAlive && i <= counter; i++) {
                Platform.runLater(this::update);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        updater.start();
    }

    private void setCircleOnAction() {
        ObservableList<Node> nodes = table.getChildren();
        for (int row = 0; row < game.getMapHeight(); row++) {
            HBox hBox = (HBox) nodes.get(row);
            for (int column = 0; column < game.getMapWidth(); column++) {
                Circle circle = (Circle) hBox.getChildren().get(column);
                int finalColumn = column;
                circle.setOnMouseClicked(mouseEvent -> game.addCell(finalColumn));
            }
        }
    }

    private void update() {
        //System.out.println("update UI graphic...");
        updateGame();
        updateTimer();
        updateLabel();
        currentFrame++;
    }

    private void updateGame() {
        //System.out.println("Updating game...");
        ObservableList<Node> nodes = table.getChildren();
        for (int row = 0; row < game.getMapHeight(); row++) {
            HBox hBox = (HBox) nodes.get(row);
            for (int column = 0; column < game.getMapWidth(); column++) {
                Circle circle = (Circle) hBox.getChildren().get(column);
                Cell cell = game.getCell(row, column);
                if (cell == null || cell.isWiningState() && currentFrame % 6 < 3)
                    circle.setFill(Color.WHITE);
                else
                    circle.setFill(game.getColor(row, column));
            }
        }
    }

    private void updateLabel() {
        boolean stable = game.clock();
        Player winner = game.getWinner();
        if (winner != null)
            label.setText(String.format("winner is %s", winner.getName()));
        else if (game.isDone())
            label.setText("Tie.");
        else if (stable)
            label.setText(String.format("%s's turn.", game.getCurrentTurnName()));
        else
            label.setText("Waiting...");
    }

    private void updateTimer() {
        //System.out.println("Updating timer...");
        long now = System.currentTimeMillis();
        long finish = 0;
        if (game.getTimerTask() != null)
            finish = game.getTimerTask().scheduledExecutionTime();
        long remain = finish - now;
        if (remain < 0)
            remain = 0;
        if (game.isDone())
            timer.setText("GL & HF");
        else if (game.isStable())
            timer.setText(remain / 1000. + "s");
    }

    public void backToMainPage() throws IOException {
        isAlive = false;
        MainPageController.show((Stage) label.getScene().getWindow());
    }

    public void backToGameStarter() throws IOException {
        isAlive = false;
        GameStarter.show((Stage) label.getScene().getWindow());
    }
}
