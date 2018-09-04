package Controller;

import Engine.GameEngine;
import Engine.robotChoice;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class ComputerTurnTask extends Task<robotChoice> {
    private GameEngine game;
    private GameController controller;

    ComputerTurnTask(GameEngine game, GameController controller){
        this.game = game;
        this.controller = controller;
    }

    @Override
    protected robotChoice call() throws Exception {

        updateMessage("Calculating possible moves");
        Thread.sleep(750);
        updateMessage("Choosing column");
        Thread.sleep(750);

        return game.playTurnRobot();
    }
}
