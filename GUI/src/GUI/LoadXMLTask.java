package GUI;

import Engine.GameEngine;
import Engine.XMLFileError;
import javafx.concurrent.Task;

import java.io.File;

public class LoadXMLTask extends Task<XMLFileError> {
    private GameEngine game;
    private String fileName;

    static final long sleepTime=1000L;

    public LoadXMLTask(GameEngine game, String fileName){
        this.game = game;
        this.fileName = fileName;
    }

    @Override
    public XMLFileError call() throws Exception {
        updateMessage("Loading...");
        Thread.sleep(sleepTime);

        updateMessage("Validating the file.");
        Thread.sleep(sleepTime);
        return game.openFile(fileName);
    }
}
