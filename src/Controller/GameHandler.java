package Controller;

import Models.Game;

public class GameHandler {
    private Game game;

    public void go(){
        if(game.relax()) {
            go();
            return;
        }


    }
}
