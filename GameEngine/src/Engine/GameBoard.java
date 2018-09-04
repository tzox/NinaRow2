package Engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameBoard implements Serializable {
    private int numOfRows;
    private int numOfCols;
    private Column[] columns;
    private int[][] boardMatrix;
    private boolean isCircular;

    public void updateMatrixCell(int row, int col, int playerNumber){
        boardMatrix[row][col]=playerNumber;
    }

    public void updateMatrixCellPop(int col){

        for (int i=numOfRows-1; i>0; i--)
            boardMatrix[i][col]=boardMatrix[i-1][col];

        boardMatrix[0][col]=0;
    }


    public void undoPopFromMatrixCell(int column, int playerNum){
        for (int i=0; i<numOfRows-1; i++)
            boardMatrix[i][column]=boardMatrix[i+1][column];
        boardMatrix[numOfRows-1][column] = playerNum;
    }

    public int getNumOfCols() {
        return numOfCols;
    }

    public void addNewDisk(int column, int playerNumber) {
        //boardMatrix
        columns[column].setCell(playerNumber, true);
    }

    public void removeDisk(int column){
        columns[column].setCell(0, false);
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public GameBoard(int numOfRows, int numOfCols, boolean isCircular) {
        this.numOfRows = numOfRows;
        this.numOfCols = numOfCols;
        this.columns = new Column[numOfCols];
        this.boardMatrix = new int[numOfRows][];
        this.isCircular=isCircular;

        for (int i = 0; i < numOfCols; i++)
            columns[i] = new Column(numOfRows);

        for (int i = 0; i < numOfRows; i++)
            this.boardMatrix[i] = new int[numOfCols];


        //Connect the cells to their neighbors
        for (int i = 0; i < this.numOfRows; i++) {
            for (int j = 0; j < this.numOfCols; j++) {

                //setting the right neighbor
                if (j!=this.numOfCols-1)
                    columns[j].getCell(i).setCellAround(columns[j+1].getCell(i),Direction.RIGHT.dirNum);
                else if(isCircular)
                    columns[j].getCell(i).setCellAround(columns[0].getCell(i),Direction.RIGHT.dirNum);

                //setting the left neighbor
                if (j!=0)
                    columns[j].getCell(i).setCellAround(columns[j-1].getCell(i),Direction.LEFT.dirNum);
                else if(isCircular)
                    columns[j].getCell(i).setCellAround(columns[numOfCols-1].getCell(i),Direction.LEFT.dirNum);

                //setting the up neighbor
                if (i!=0)
                    columns[j].getCell(i).setCellAround(columns[j].getCell(i-1),Direction.UP.dirNum);
                else if(isCircular)
                    columns[j].getCell(i).setCellAround(columns[j].getCell(numOfRows-1),Direction.UP.dirNum);


                //setting the down neighbor
                if (i!=this.numOfRows-1)
                    columns[j].getCell(i).setCellAround(columns[j].getCell(i+1),Direction.DOWN.dirNum);
                else if(isCircular)
                    columns[j].getCell(i).setCellAround(columns[j].getCell(0),Direction.DOWN.dirNum);


                //setting the down-left neighbor
                if (j!=0 & i!=this.numOfRows-1)
                    columns[j].getCell(i).setCellAround(columns[j-1].getCell(i+1),Direction.DOWN_LEFT.dirNum);

                //setting the down-right neighbor
                if (j!=this.numOfCols-1 & i!=this.numOfRows-1)
                    columns[j].getCell(i).setCellAround(columns[j+1].getCell(i+1),Direction.DOWN_RIGHT.dirNum);


                //setting the up-right neighbor
                if (j!=this.numOfCols-1 & i!=0)
                    columns[j].getCell(i).setCellAround(columns[j+1].getCell(i-1),Direction.UP_RIGHT.dirNum);


                //setting the up-left neighbor
                if (j!=0 & i!=0)
                    columns[j].getCell(i).setCellAround(columns[j-1].getCell(i-1),Direction.UP_LEFT.dirNum);

            }
        }
    }



    public void resetGameBoard(){

        //Resetting the gameBoardMatrix
        for (int i=0; i<this.numOfRows; i++)
            for (int j=0; j<this.numOfCols; j++)
                this.boardMatrix[i][j]=0;

        //Resetting the gameBoard data structure (Columns and Cells)
        for (int i=0; i<this.numOfCols; i++)
            this.columns[i].resetColumn();

    }

    public void popDisk(int column){
        columns[column].popFromColumn();
    }

    public int[][] getBoardMatrix () {
            return this.boardMatrix;
        }

    public Column[] getColumns() {
        return columns;
    }


    public void updateBoardMatrixFromBoardStructure(){
        for (int i=0; i<this.numOfRows; i++)
            for (int j=0; j<this.numOfCols; j++)
                this.updateMatrixCell(i,j,this.columns[j].getCell(i).getPlayer());
    }


    public void removePlayerFromBoard(int playerToRemove){
        for (int i=0; i<this.columns.length; i++)
            this.columns[i].removePlayerFromColumn(playerToRemove+1);

        this.updateBoardMatrixFromBoardStructure();
    }

    public int getColumnFirstAvailable(int colIndex){
        return columns[colIndex].getFirstAvailable();
    }

    public enum Direction {
            UP(1), DOWN(6), LEFT(3), RIGHT(4), UP_LEFT(0), UP_RIGHT(2), DOWN_LEFT(5), DOWN_RIGHT(7);

        public final int dirNum;

         Direction(int dirNum) {
            this.dirNum = dirNum;
        }
    }

    /*-----------------Column Class--------------------*/
    public class Column implements Serializable {
            private int firstAvailable;
            private Cell[] cells;


            public void removePlayerFromColumn(int playerToRemove) {
                List<Integer> remainingDisks=new ArrayList<>();

                //keep the disks to remain in the column in a temporary arraylist
                for (int i=cells.length-1; i>firstAvailable; i--)
                    if (cells[i].getPlayer()!=0 && cells[i].getPlayer()!=playerToRemove)
                        remainingDisks.add(cells[i].getPlayer());

                //update the first-available data member of the column
                this.firstAvailable=cells.length-remainingDisks.size()-1;

                //put the remaining disks in the beginning and zeros at the rest
                for (int i=0; i<cells.length; i++)
                    cells[cells.length-i-1].setCellPlayer(i<remainingDisks.size()?remainingDisks.get(i):0);
            }


            public int getBottomPlayerNum(){
                return cells[cells.length-1].getPlayer();
            }


            public void popFromColumn(){
                for (int i=this.cells.length-1; i>0 && 0!=cells[i].getPlayer(); i--)
                    cells[i].setCellPlayer(cells[i-1].getPlayer());
                cells[0].setCellPlayer(0);
                firstAvailable++;
            }

        public void undoPopFromColumn(int playerNum){
            for (int i=0; i<cells.length-1; i++)
                cells[i].setCellPlayer(cells[i+1].getPlayer());

            cells[cells.length-1].setCellPlayer(playerNum);
            firstAvailable--;
        }


            public void resetColumn(){
                for (int i=0; i<cells.length; i++)
                    cells[i].setCellPlayer(0);
                firstAvailable= cells.length-1;
            }


            public Column(int numOfCells) {
                this.cells = new Cell[numOfCells];
                this.firstAvailable = numOfCells-1;


                for (int i = 0; i < numOfCells; i++) {
                    cells[i] = new Cell(0);

                }
            }

            public void setCell(int player, boolean toAdd){

                if (!toAdd)
                    cells[this.firstAvailable+1].setCellPlayer(player);
                else
                    cells[this.firstAvailable].setCellPlayer(player);

                if(toAdd)
                    firstAvailable--;
                else
                    firstAvailable++;
            }

            public int getFirstAvailable() {
                return firstAvailable;
            }

            public Cell getCell(int cellIndex){
                return cells[cellIndex];
            }

            int printCell(int index) {
                return cells[index].getPlayer();
            }

            /* -----------------Cell Class-------------------*/
            public class Cell implements Serializable {
                private int player;

                public Cell getNeighborCell(Direction dir) {
                    return cellsAround[dir.dirNum];
                }

                private Cell[] cellsAround = new Cell[8];

                public void setCellAround(Cell neighbor, int dir) {
                    this.cellsAround[dir] = neighbor;
                }


                public Cell(int player) {
                    this.player = player;
                }

                public int getPlayer() {
                    return player;
                }

                public void setCellPlayer(int player){
                    this.player=player;
                }
            }
        }
    }
