package Controller;

import Models.Player;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ScoreboardController implements Initializable {

    public TableView scoreboard;
    public TableColumn scoreboardName;
    public TableColumn scoreboardWins;
    public TableColumn scoreboardLosses;
    public TableColumn scoreboardDraws;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Player> players = Player.getPlayersStatistics();
        scoreboardName.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
        scoreboardWins.setCellValueFactory(new PropertyValueFactory<Player, Integer>("wins"));
        scoreboardLosses.setCellValueFactory(new PropertyValueFactory<Player, Integer>("losses"));
        scoreboardDraws.setCellValueFactory(new PropertyValueFactory<Player, Integer>("draws"));
        scoreboard.setItems(FXCollections.observableArrayList(players));
    }
}
