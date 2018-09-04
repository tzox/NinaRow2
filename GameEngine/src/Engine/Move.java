package Engine;

import java.io.Serializable;

public class Move implements Serializable {
    private int moveNum;
    private int movePlayer;
    private int moveCol;
    private int moveRow;
    private boolean isPopout;


    public boolean isPopout() {
        return isPopout;
    }

    public int getMoveNum() {
        return moveNum;
    }

    public int getMovePlayer() {
        return movePlayer;
    }

    public int getMoveCol() {
        return moveCol;
    }

    public int getMoveRow() {
        return moveRow;
    }

    public Move(int moveNum, int movePlayer, int moveCol, int moveRow, boolean isPopout) {
        this.moveNum = moveNum;
        this.movePlayer = movePlayer;
        this.moveCol = moveCol;
        this.moveRow = moveRow;
        this.isPopout=isPopout;
    }

    @Override
    public String toString() {
        return "Move{" +
                "moveNum=" + moveNum +
                ", movePlayer=" + movePlayer +
                ", moveCol=" + moveCol +
                ", moveRow=" + moveRow +
                ", isPopout=" +isPopout +
                '}';
    }
}
