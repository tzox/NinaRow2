import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import Engine.*;


public class GameUI {

    public static final String FILE_NAME = "GameState.txt";

    public String playerSymbols;

    public GameUI() {
        this.playerSymbols = ".XOTUV";
    }

    public void startGame(GameEngine game) {
        if (game.isBoardLoaded()) {
                game.setActiveGame(true);
                game.setStartTime();
                game.resetMoves();
                game.resetMovesLeft();
                game.setCurrPlayer(0);
                game.getBoard().resetGameBoard();
                System.out.println("Game started.");
        } else {
            System.out.println("No board loaded. Please load board before starting");
        }
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
                System.out.print(this.playerSymbols.charAt(boardMatrix[i][j]) + "\t");
            }
            System.out.println();
        }
        System.out.println();

    }

    public void printMoveHistory(List<Move> moves) {
        if (0 == moves.size())
            System.out.println("No moves played yet.");
        else {
            for (Move move : moves) {
                System.out.println("Move Number: " + move.getMoveNum() + ", Player Symbol: " +
                        playerSymbols.charAt(move.getMovePlayer()) + ", Column Played: " +
                        move.getMoveCol());
            }
        }
        System.out.println();
    }

    public void exitGame() {
        System.exit(0);
    }

    public void printGameStatus(GameEngine game) {
        printBoard(game.getBoard().getBoardMatrix());
        System.out.println("N sequence needed: " + game.getWinningN());
        System.out.println("Current game active: " + (game.isActiveGame() ? "Yes" : "No"));
        if (game.isActiveGame()) {
            System.out.println("Current player number: " + (game.getCurrPlayer() + 1));

            //print players symbols
            for (int i = 1; i <= game.getNumOfPlayers(); i++)
                System.out.print("Player " + i + " Symbol: " + playerSymbols.charAt(i) + "\t\t");
            System.out.println();

            //print each player move-count
            for (int i = 1; i <= game.getNumOfPlayers(); i++)
                System.out.print("Player " + i + " Move Count: " + game.getPlayerMoveCount(i - 1) + "\t\t");
            System.out.println();

            //print each player name
            for (int i = 1; i <= game.getNumOfPlayers(); i++)
                System.out.print("Player " + i + " Name: " + game.getPlayerName(i - 1) + "\t\t");
            System.out.println();

            //print each player id
            for (int i = 1; i <= game.getNumOfPlayers(); i++)
                System.out.print("Player " + i + " ID: " + game.getPlayerId(i - 1) + "\t\t");
            System.out.println();

            //is each player human
            for (int i = 1; i <= game.getNumOfPlayers(); i++)
                System.out.print("Player " + i + " Human? " + game.isPlayerHuman(i - 1) + "\t\t");
            System.out.println();


            //print time passed since game started
            long secondsPassed = (System.currentTimeMillis() - game.getStartTime()) / 1000;

            System.out.println("Time passed since start of game: " + String.format("%02d:%02d", secondsPassed / 60, secondsPassed % 60) + "\n");
        }
    }

    public void loadBoardFromXMLFile(GameEngine game) {
        Scanner reader = new Scanner(System.in);

        System.out.println("Please enter path for xml file");
        String xmlPath = reader.nextLine();
        XMLFileError fileLoaded = game.openFile(xmlPath);
        switch (fileLoaded) {
            case FILE_DOES_NOT_EXIST:
                System.out.println("File does not exist :-(");
                break;
            case FILE_IS_NOT_XML:
                System.out.println("The file is not in XML format, shame!");
                break;
            case INVALID_NUM_OF_ROWS:
                System.out.println("XML file has invalid number of rows. Must be between 5 to 50.");
                break;
            case INVALID_NUM_OF_COLS:
                System.out.println("XML file has invalid number of cols. Must be between 6 to 30.");
                break;
            case INVALID_TARGET_ROWS:
                System.out.println("XML file has invalid target number. Must be less than the number of rows.");
                break;
            case INVALID_TARGET_COLS:
                System.out.println("XML file has invalid target number. Must be less than the number of cols.");
                break;
            case INVALID_TARGET_MIN:
                System.out.println("XML file has invalid target number. Must be more then one.");
                break;
            case INVALID_PLAYERS_ID:
                System.out.println("XML file has invalid player Ids. Two players cannot have the same ID.");
                break;
            case INVALID_NUM_OF_PLAYERS:
                System.out.println("XML file has invalid number of players. Must be between 2 to 6.");
                break;
            case ALL_GOOD:
                game.loadBoard();
                game.setPopout();
                game.loadPlayers();
                game.setWinningN();
                game.setBoardLoaded(true);
                this.printGameStatus(game);
                System.out.println("Board loaded successfully.");
                break;
        }
    }


    public void playTurn(GameEngine game) {
        Scanner reader = new Scanner(System.in);
        int columnToPlay;
        String turnInput;

        if (game.isActiveGame()) {
            while (true) {
                if (!game.isCurrentPlayerHuman()) {
                    game.playWithPlayer(0, false);
                    break;
                } else {
                    this.printBoard(game.getBoard().getBoardMatrix());
                    System.out.println("player " + (game.getCurrPlayer() + 1) + ": please choose a column");
                    turnInput = reader.nextLine();
                    try {
                        columnToPlay = Integer.parseInt(turnInput);
                        if (columnToPlay >= 1 && columnToPlay <= game.getBoard().getNumOfCols())
                            if (!game.isPopout()) {
                                if (!game.playWithPlayer(columnToPlay - 1, true)) {
                                    System.out.println("Column is full!");
                                    continue;
                                } else
                                    break;
                            }
                            else{
                                game.popWithPlayer(columnToPlay - 1, true);
                                break;
                            }
                        else
                            System.out.println("Please choose a column between 1 and " + game.getBoard().getNumOfCols());
                            System.out.println();
                    } catch (Exception e) {
                        System.out.println("The value you have entered is not valid. Please enter a whole number.");
                        System.out.println();
                    }
                }
            }

            this.printBoard(game.getBoard().getBoardMatrix());

            if (!game.isCurrentPlayerHuman())
                System.out.println("Player " + (game.getCurrPlayer() + 1) + " played the column: " + game.getMoves().get(game.getMoves().size() - 1).getMoveCol() + "\n");

            int colToCheck=game.getMoves().get(game.getMoves().size() - 1).getMoveCol() - 1;

            if (!game.getMoves().get(game.getMoves().size()-1).isPopout()) {
                if (game.isWinningMove(colToCheck,-1)) {
                    System.out.println("Player " + this.playerSymbols.charAt(game.getCurrPlayer() + 1) + " WINS!!!!");
                    System.out.println();
                    game.setActiveGame(false);
                    return;
                }
            }
            else{
                Set<Integer> winners=game.isWinningPop(colToCheck);
                if (winners.size()!=0){
                    for (int player : winners)
                        System.out.println("Player " + player + " WINS!");
                }
            }

            if (0 == game.getMovesLeft()) {
                System.out.println("Board is full, game tie");
                game.setActiveGame(false);
                return;
            }
            //game.setCurrPlayer((game.getCurrPlayer()+1)%game.getNumOfPlayers());
            game.advanceCurrPlayer();
        } else {
            System.out.println("Not possible to play a turn if no game has started.");
            System.out.println();
        }
    }

    public void runGame() {
        Menu menu = new Menu();
        GameEngine game = new GameEngine();
        int userInput;

        while (true) {
            userInput = menu.menuInput(game.isActiveGame());
            switch (userInput) {
                case 1:
                    if (!game.isActiveGame())
                        this.loadBoardFromXMLFile(game);
                    else
                        System.out.println("Game started. Can't load board in the middle of a game.");

                    System.out.println();
                    break;
                case 2:
                    this.startGame(game);
                    System.out.println();
                    break;
                case 3:
                    if (game.isBoardLoaded())
                        this.printGameStatus(game);
                    else
                        System.out.println("Not possible to show game status if no board has been loaded.");
                    System.out.println();
                    break;
                case 4:
                    this.playTurn(game);
                    break;
                case 5:
                    this.printMoveHistory(game.getMoves());
                    break;
                case 9:
                    System.out.println("Thanks for playing. Goodbye!");
                    this.exitGame();
                    break;
                case 6:
                    switch(game.undoTurn()){
                        case 0:
                            System.out.println("No moves to undo.");
                            break;
                        case -1:
                            System.out.println("Not possible to undo a turn if no game has started.");
                            break;
                        case 1:
                            this.printBoard(game.getBoard().getBoardMatrix());
                            System.out.println();
                            System.out.println("Undo last turn successfully");
                    }
                    System.out.println();
                    break;

                case 7:
                    game.setPopoutTMP();
                    break;

//                    try {
//                        if(game.isActiveGame()) {
//                            FileSerialization.writeGameStateToFile(game);
//                            System.out.println("Game state saved successfully.");
//                        }
//                        else
//                            System.out.println("Game isn't started, please start a game to save one.");
//
//                        System.out.println();
//                        break;
//                    } catch (Exception e) {
//                        System.out.println("Could'nt write game to file.");
//                        System.out.println();
//                        break;
//                    }

                case 8:
                    try {
                        if(!game.isActiveGame()) {
                            game = FileSerialization.readGameStateFromFile();
                            System.out.println("Game state loaded successfully.");
                        }
                        else
                            System.out.println("Game started. Can't load saved game in the middle of a game.");

                        System.out.println();
                        break;
                    }
                    catch (FileNotFoundException e){
                        System.out.println("Didn't find any saved game.");
                        System.out.println();
                        break;
                    }
                    catch (IOException e) {
                        System.out.println("Could'nt read game from file.");
                        System.out.println();
                        break;
                    } catch (ClassNotFoundException e) {
                        System.out.println("Class not found, Could'nt read game from file.");
                        System.out.println();
                        break;
                    }
            }
        }
    }


    public static void main(String[] args) {
        GameUI ui = new GameUI();
        ui.runGame();
    }
}
