package Engine;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.Serializable;

public class Player implements Plays,Serializable {
    private GameBoard board;
    private boolean isHuman;
    //private int playerMoveCount;
    private IntegerProperty playerMoveCount;
    private int playerNumber;
    private short playerID;
    private String playerName;
    private boolean isResigned;


    public IntegerProperty getPlayerMoveCountProperty() {
        return playerMoveCount;
    }

    public String getPlayerName() { return this.playerName;}

    public void setBoard(GameBoard board) {
        this.board = board;
    }

    public Player(GameBoard board, int playerNumber, boolean isHuman, short playerID, String playerName) {
        this.board = board;
        this.playerNumber = playerNumber;
        this.playerMoveCount=new SimpleIntegerProperty(0);
        this.playerID=playerID;
        this.isHuman=isHuman;
        this.playerName=playerName;
        this.isResigned=false;
    }


    public boolean isResigned() {
        return isResigned;
    }

    public void setResigned(boolean resigned) {
        isResigned = resigned;
    }

    public void decreaseMoveCount() {
        Platform.runLater(()->this.playerMoveCount.setValue(this.playerMoveCount.getValue()-1));
        }

    public int getPlayerMoveCount() {
        return playerMoveCount.getValue();
    }

    public void resetMoveCount(){
        this.playerMoveCount.setValue(0);
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public short getPlayerID() {
        return playerID;
    }


    public int pickColumnToPlay(){
        GameBoard.Column[] columns =board.getColumns();
        for (int i=0; i<columns.length; i++){
            if (columns[i].getFirstAvailable()!=-1)
                return i;
        }
        return -1;
    }


    public int pickColumnToPop(int playerNum){
        GameBoard.Column[] columns =board.getColumns();
        for (int i=0; i<columns.length; i++){
            if (columns[i].getBottomPlayerNum()==playerNum)
                return i;
        }
        return -1;
    }

    public void play(int column) {
        board.addNewDisk(column, this.playerNumber);
        Platform.runLater(()->this.playerMoveCount.setValue(this.playerMoveCount.getValue()+1));
    }

    public void pop(int column) {
        board.popDisk(column);
        Platform.runLater(()->this.playerMoveCount.setValue(this.playerMoveCount.getValue()+1));
    }




}