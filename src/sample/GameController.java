package sample;

import Models.Game;
import Models.Player;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameController {
    public Label label;
    public Label timer;
    public VBox table;
    private Game game;

    void initialize(Game game) {
        this.game = game;

        new Thread(() -> {
            for (int i = 1; i <= 100000; i++) {
                Platform.runLater(this::update);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

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

    private void updateGame() {
        System.out.println("Updating game...");
        ObservableList<Node> nodes = table.getChildren();
        for (int row = 0; row < game.getMapHeight(); row++) {
            HBox hBox = (HBox) nodes.get(row);
            for (int column = 0; column < game.getMapWidth(); column++) {
                Circle circle = (Circle) hBox.getChildren().get(column);
                Player player = game.getCellPlayer(row, column);
                if (player == null)
                    circle.setFill(Color.WHITE);
                else
                    circle.setFill(player.getColor());
            }
        }
    }

    private void update() {
        updateGame();
        updateTimer();
        updateLabel();
    }

    private void updateLabel() {
        boolean relaxed = game.relax();
        System.out.println(game);
        Player winner = game.getWinner();
        if (winner != null)
            label.setText(String.format("winner is %s", winner.getName()));
        else if (!relaxed)
            label.setText(String.format("%s's turn.", game.getCurrentTurnName()));
        else
            label.setText("Waiting...");
    }

    private void updateTimer() {
        System.out.println("Updating timer...");
        long now = System.currentTimeMillis();
        long finish = 0;
        if (game.getTimerTask() != null)
            finish = game.getTimerTask().scheduledExecutionTime();
        long remain = finish - now;
        if (remain < 0)
            remain = 0;
        timer.setText(remain / 1000. + "s");
    }
}
