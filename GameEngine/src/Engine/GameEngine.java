package Engine;

import com.sun.corba.se.impl.encoding.WrapperInputStream;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


public class GameEngine implements Serializable {
    private List<Player> players;
    private int movesLeft;
    private GameBoard board;
    private long startTime;
    private long timePassedForSavedFile;
    private boolean activeGame;
    private boolean boardLoaded;
    private int winningN;
    private List<Move> moves;
    private int currPlayer;
    private  XMLParser xmlParser;
    private boolean isPopout;
    private List<Boolean> validColumnsTop;
    private List<Boolean> validColumnsBottom;
    Set<Short> winners;
    private int currMoveIndex;
    private boolean isReplayMode;
    private int numOfActivePlayers;

    public boolean isReplayMode(){
        return this.isReplayMode;
    }

    public List<Boolean> getValidColumnsTop() {
        return validColumnsTop;
    }

    public List<Boolean> getValidColumnsBottom() {
        return validColumnsBottom;
    }

    void updateValidColumnsTop(){
        for (int i=0; i<this.board.getNumOfCols(); i++)
            validColumnsTop.set(i,board.getColumns()[i].getFirstAvailable() == -1 ? false : true);
    }

    void updateValidColumnsBottom(){
        for (int i=0; i<this.board.getNumOfCols(); i++)
            validColumnsBottom.set(i,board.getColumns()[i].getBottomPlayerNum() == this.currPlayer+1 ? true : false);
    }


    public boolean isPopout() {
        return isPopout;
    }

    public void setPopout() {
        this.isPopout = xmlParser.getGameMode().equals("Popout");
    }

    public void setPopoutTMP(){
        this.isPopout=isPopout ? false : true;
}

    public void loadPlayers(){
        this.players.clear();
        int counter=1;
        for (Engine.XML.Player player : xmlParser.getPlayers()) {
            short playerId=player.getId();
            String playerName=player.getName();
            boolean isHuman=player.getType().equals("Human");

            this.players.add(new Player(this.board, counter++, isHuman, playerId, playerName));
        }
    }

    public XMLFileError openFile(String path){
        File file = new File(path);

        if(!file.exists()){
            return XMLFileError.FILE_DOES_NOT_EXIST;
        }
        else if(!file.getName().endsWith(".xml")){
            return XMLFileError.FILE_IS_NOT_XML;
        }

        xmlParser.receiveXMLPath(file);

        return checkValidXML(xmlParser.getWinningN(), xmlParser.getNumOfRows(), xmlParser.getNumOfCols(), xmlParser.getPlayers());
    }

    public XMLFileError checkValidXML(int target, int rows, int cols, List<Engine.XML.Player> players){
        if (rows > 50 || rows < 5) {
            return XMLFileError.INVALID_NUM_OF_ROWS;
        }

        if (cols > 30 || cols < 6) {
            return XMLFileError.INVALID_NUM_OF_COLS;
        }

        if (target >= rows) {
            return XMLFileError.INVALID_TARGET_ROWS;
        }

        if (target >= cols) {
            return XMLFileError.INVALID_TARGET_COLS;
        }

        if (target < 2){
            return XMLFileError.INVALID_TARGET_MIN;
        }

        if (players.size()<2 || players.size()>6)
            return XMLFileError.INVALID_NUM_OF_PLAYERS;


        //checking if two players share the same ID
//        Set<Short> playerIds= new HashSet<>();
//
//        for (Engine.XML.Player player : players)
//            playerIds.add(player.getId());

        //HashSet<Short> playerIdsSet= players.stream().map(Engine.XML.Player::getId).collect(Collectors.toSet());

        Set<Short> playersSet = new HashSet<>();
        players.forEach(p->playersSet.add(p.getId()));

        if (playersSet.size()!=players.size())
            return XMLFileError.INVALID_PLAYERS_ID;

        return XMLFileError.ALL_GOOD;
    }

    public void resetPlayers(){
        this.players.clear();
    }

    public void setWinningN() {
        this.winningN = xmlParser.getWinningN();
    }

    public void setStartTime(){
        this.startTime=System.currentTimeMillis();
    }

    public void setStartTimeSavedGame(){
        this.startTime = System.currentTimeMillis() - this.timePassedForSavedFile;
    }

    public void setTimePassedForSavedFile(){
        this.timePassedForSavedFile = System.currentTimeMillis() - this.startTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getPlayerMoveCount(int playerNum){
        return players.get(playerNum).getPlayerMoveCount();
    }


    public void winnersAfterResign(){
        for (int i=0; i<this.board.getNumOfCols(); i++)
            this.isWinningPop(i);
    }

    public boolean isPlayerHuman(int playerNum){
        return players.get(playerNum).isHuman();
    }

    public short getPlayerId(int playerNum){
        return players.get(playerNum).getPlayerID();
    }

    public String getPlayerName(int playerNum){
        return players.get(playerNum).getPlayerName();
    }

    public boolean isActiveGame() {
        return activeGame;
    }

    public int getCurrPlayer() {
        return currPlayer;
    }

    public void setCurrPlayer(int currPlayer) {
        this.currPlayer = currPlayer;
    }

    public void advanceCurrPlayer(){
        do
        this.currPlayer=(currPlayer+1) % getNumOfPlayers();
        while (this.players.get(this.currPlayer).isResigned());
    }

    public List<Move> getMoves() {
        return moves;
    }

    public int getWinningN() {
        return winningN;
    }

    public GameEngine() {
        this.activeGame=false;
        this.boardLoaded=false;
        this.players=new ArrayList<Player>(2);
        this.moves=new ArrayList<Move>();
        this.currPlayer=0;
        this.xmlParser = new XMLParser();
        this.winners = new HashSet<>();
        this.isReplayMode = false;


    } //Constructor!

    public int getMovesLeft() {
        return movesLeft;
    }

    public void incMovesLeft() {this.movesLeft++;}

    public void decMovesLeft() {this.movesLeft--;}

    public void resetMoves(){
        this.moves.clear();
    }

    public Set<Short> getWinners() {
        return winners;
    }


    public void addPlayer(int playerNum, boolean isHuman, short playerID, String playerName){
        this.players.add(new Player(this.board, playerNum,isHuman, playerID,playerName));
    }

    public void resetMovesLeft(){
        this.movesLeft=this.board.getNumOfCols()*this.board.getNumOfRows();
    }

    public void loadBoard(){
        boolean isCircular = xmlParser.getGameMode().equals("Circular");
        this.board = new GameBoard(xmlParser.getNumOfRows(), xmlParser.getNumOfCols(), isCircular);
        this.boardLoaded=true;
        this.validColumnsTop= new ArrayList<>(Collections.nCopies(xmlParser.getNumOfCols(), true));
        this.validColumnsBottom=new ArrayList<>(Collections.nCopies(xmlParser.getNumOfCols(), false));
    }


    public void isWinningPop (int column) {
        GameBoard.Column colToCheck = board.getColumns()[column];

        for (int i = board.getNumOfRows() - 1; i > colToCheck.getFirstAvailable(); i--) {
            if (isWinningMove(column, i)) {
                this.winners.add(players.get(colToCheck.getCell(i).getPlayer() - 1).getPlayerID());
            }
        }
    }

    public boolean isWinningMove (int column, int row){
        GameBoard.Column lastMoveColumn = board.getColumns()[column];

        int lastMoveRow = row==-1 ? lastMoveColumn.getFirstAvailable()+1 : row;

        GameBoard.Column.Cell lastMoveCell=lastMoveColumn.getCell(lastMoveRow);

        if(countCurrStreak(lastMoveCell, GameBoard.Direction.UP, GameBoard.Direction.DOWN) >= this.winningN){
            return true;
        }

        if(countCurrStreak(lastMoveCell, GameBoard.Direction.LEFT, GameBoard.Direction.RIGHT) >= this.winningN){
            return true;
        }

        if(countCurrStreak(lastMoveCell, GameBoard.Direction.DOWN_LEFT, GameBoard.Direction.UP_RIGHT) >= this.winningN){
            return true;
        }

        if(countCurrStreak(lastMoveCell, GameBoard.Direction.DOWN_RIGHT, GameBoard.Direction.UP_LEFT) >= this.winningN){
            return true;
        }

        return false;
    }





    public void resignCurrentPlayer(){
        //remove the player from the board
        this.board.removePlayerFromBoard(this.currPlayer);


        this.players.get(this.currPlayer).setResigned(true);
        this.advanceCurrPlayer();
        this.numOfActivePlayers--;

        this.winnersAfterResign();

        updateValidColumnsTop();
        if (this.isPopout)
            updateValidColumnsBottom();

        setActiveGame(!isGameOver());

    }



    private int countCurrStreak(GameBoard.Column.Cell cellToCheck, GameBoard.Direction dir1, GameBoard.Direction dir2){
        GameBoard.Column.Cell currCell = cellToCheck.getNeighborCell(dir1);
        int currCount = 1;
        int currStreak = 1;
        int lastMovePlayer = cellToCheck.getPlayer();

        //check dir1
        while(currCell != null && currCell.getPlayer() == lastMovePlayer && currCount < this.winningN){
            //System.out.println("dir 1: "+dir1.dirNum + " Neighbor: " +currCell.getPlayer());
            currCount++;
            currStreak++;
            currCell=currCell.getNeighborCell(dir1);
        }

        //check dir2
        currCell = cellToCheck.getNeighborCell(dir2);
        currCount=1;
        while(currCell != null && currCell.getPlayer() == lastMovePlayer && currCount < this.winningN){
            //System.out.println("dir 2: "+dir2.dirNum + " Neighbor: " +currCell.getPlayer());
            currCount++;
            currStreak++;
            currCell=currCell.getNeighborCell(dir2);
        }
        //System.out.println("Current streak is: " + currStreak);
        return currStreak;
    }

    public int getNumOfPlayers() {
        return players.size();
    }

    public void setActiveGame(boolean activeGame) {
        this.activeGame = activeGame;
    }

    public void setBoardLoaded(boolean boardLoaded) {
        this.boardLoaded = boardLoaded;
    }

    public void setReplayMode(boolean replayMode) {
        isReplayMode = replayMode;
    }

    public boolean isGameOver() {

        if (this.numOfActivePlayers==1)
            this.winners.add(this.players.get(this.currPlayer).getPlayerID());

        if (!this.winners.isEmpty())
            return true;

        for (boolean bol : validColumnsTop)
            if (bol==true)
                return false;

        for (boolean bol : validColumnsBottom)
            if (bol==true)
                return false;

        return true;
    }

    public boolean isBoardLoaded() {
        return boardLoaded;
    }

    public GameBoard getBoard() {
        return board;
    }

    public boolean isCurrentPlayerHuman(){
        return this.players.get(this.currPlayer).isHuman();
    }


    public boolean playWithPlayer (int columnToPlay) {
        int firstAvailable;
        int finalColumnToPlay;


        finalColumnToPlay=columnToPlay;

        firstAvailable = board.getColumnFirstAvailable(finalColumnToPlay);

        if (firstAvailable != -1) {
                int playerNum = this.players.get(this.currPlayer).getPlayerNumber();
                this.board.updateMatrixCell(firstAvailable, finalColumnToPlay, playerNum);
                this.players.get(this.currPlayer).play(finalColumnToPlay);
                if(!this.isReplayMode)
                    this.moves.add(new Move(moves.size() + 1, playerNum, finalColumnToPlay + 1, firstAvailable + 1,false));
                this.movesLeft--;
                return true;
            }

        return false;
    }

    public boolean popWithPlayer (int columnToPop) {
        int finalColumnToPop;

        finalColumnToPop=columnToPop;


        //NO INPUT CHECK AT THE MOMENT - MUST ENTER VALID COLUMNS

            int playerNum = this.players.get(this.currPlayer).getPlayerNumber();
            this.board.updateMatrixCellPop(finalColumnToPop);
            this.players.get(this.currPlayer).pop(finalColumnToPop);
            if(!this.isReplayMode)
                this.moves.add(new Move(moves.size() + 1, playerNum, finalColumnToPop + 1, 0,true));
            this.movesLeft++;
            return true;

        //return false;
    }



    public void finishReplayMode(){
        while (nextMove()!=0);
        this.isReplayMode=false;
    }

    //return 0 is there are no moves to play, return -1 if the game is not active, return 1 if successfully undo
    public int prevMove(){
        if(!activeGame)
            return -1; // the game is not active



        Move lastMove = this.moves.get(currMoveIndex);
        int prevCol = lastMove.getMoveCol() - 1;
        this.currPlayer = lastMove.getMovePlayer() - 1;

        if(!lastMove.isPopout()) {
            int rowToRemove = lastMove.getMoveRow() - 1;
            this.board.removeDisk(prevCol);
            this.board.updateMatrixCell(rowToRemove, prevCol, 0);
            this.movesLeft++;
        }
        else{
            this.board.undoPopFromMatrixCell(prevCol,this.currPlayer+1);
            this.board.getColumns()[prevCol].undoPopFromColumn(this.currPlayer+1);
            this.movesLeft--;
        }

        this.players.get(lastMove.getMovePlayer() - 1).decreaseMoveCount();
        this.currMoveIndex--;

        if(currMoveIndex==-1)
            return 0; // no moves to undo

        return 1; // undo successfully
    }

    public int nextMove(){
        if(!activeGame)
            return -1; // the game is not active

        if(currMoveIndex==moves.size()-1)
            return 0; // no next move

        Move nextMove = this.moves.get(currMoveIndex+1);
        int columnPlayed = nextMove.getMoveCol()-1;
        int playerPlayed = nextMove.getMovePlayer()-1;
        boolean isMovePopout=nextMove.isPopout();
        this.currPlayer=playerPlayed;

        playMove(columnPlayed,isMovePopout);

        if(currMoveIndex==moves.size()-1)
            return 0; // no next move

        return 1;
    }


    public robotChoice playTurnRobot(){
        boolean movePopout = false;
        int columnToPlay = players.get(currPlayer).pickColumnToPlay();
        if(columnToPlay == -1) {
            columnToPlay = players.get(currPlayer).pickColumnToPop(currPlayer+1);
            movePopout = true;
        }

        return new robotChoice(columnToPlay,movePopout);
    }


    public String returnWinnersIds(){
        StringBuilder winners= new StringBuilder(new String());

        for (Short id : this.winners)
            winners.append(id).append("\n");

        return winners.toString();

    }

    public void playMove(int column, boolean isPopout){
        if (isPopout)
            this.popWithPlayer(column);
        else
            this.playWithPlayer(column);

        if(!isReplayMode) {
            int colToCheck = moves.get(moves.size() - 1).getMoveCol() - 1;

            if (!isPopout) {
                if (isWinningMove(colToCheck, -1))
                    this.winners.add(players.get(currPlayer).getPlayerID());
            } else
                this.isWinningPop(colToCheck);

                setActiveGame(!isGameOver());

        }

        this.currMoveIndex++;

        this.advanceCurrPlayer();
        updateValidColumnsTop();
        if (this.isPopout)
            updateValidColumnsBottom();

        //this.printBoard(board.getBoardMatrix());


    }

    public List<Player> getPlayers() {
        return players;
    }


    public void startGame(){
        this.setActiveGame(true);
        this.numOfActivePlayers=this.players.size();
        //game.setStartTime();
        Collections.fill(this.getValidColumnsBottom(), Boolean.FALSE);
        Collections.fill(this.getValidColumnsTop(), Boolean.TRUE);
        this.resetMoves();
        this.resetMovesLeft();
        this.winners.clear();
        this.setCurrPlayer(0);
        this.currMoveIndex = -1;
        this.getBoard().resetGameBoard();
        for(Player player : this.players) {
            player.resetMoveCount();
            player.setResigned(false);
        }
    }

    public void endGame(){
        this.setActiveGame(false);
        this.getBoard().resetGameBoard();
        for(Player player : this.players)
            player.resetMoveCount();
    }


    public void printBoard(int[][] boardMatrix) {
        int numOfRows = boardMatrix.length;
        int numOfCols = boardMatrix[0].length;
        System.out.print("\t");
        for (int i = 0; i < numOfCols; i++)
            System.out.print(i + 1 + "\t");
        System.out.println();

        for (int i = 0; i < numOfRows; i++) {
            System.out.print(i + 1 + "\t");
            for (int j = 0; j < numOfCols; j++) {
                System.out.print(boardMatrix[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();

    }

}
