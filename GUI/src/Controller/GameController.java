package Controller;

import Engine.GameBoard;
import Engine.GameEngine;
import Engine.XMLFileError;
import Engine.robotChoice;
import GUI.GameGUI;
import GUI.LoadXMLTask;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.ArrayList;
import static java.lang.Thread.sleep;

public class GameController {

    private SimpleStringProperty selectedFileProperty;
    private SimpleBooleanProperty isFileSelected;
    private Stage primaryStage;
    private GameEngine game;
    private GameGUI gui;
    private Double buttonSize = 30D;
    private Double rectangleSize=50D;
    private Double circleSize=20D;
    private int rows;
    private int cols;
    private BooleanProperty boardLoaded;
    private BooleanProperty isGameActive;
    private BooleanProperty replayMode;
    private Alert computerTurnAlert;
    private GridPane gameBoardGridPane;
    private boolean showAnimation;



    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setGui(GameGUI gui) {
        this.gui = gui;
    }

    public GameController() {
        this.selectedFileProperty = new SimpleStringProperty();
        this.isFileSelected= new SimpleBooleanProperty(false);
        this.game = new GameEngine();
        this.boardLoaded = new SimpleBooleanProperty(false);
        this.isGameActive = new SimpleBooleanProperty(false);
        this.replayMode = new SimpleBooleanProperty(false);
        this.computerTurnAlert = new Alert(Alert.AlertType.INFORMATION);
        ImageView aiAlert=new ImageView("/fxml/ai.gif");
        aiAlert.setFitWidth(160);
        aiAlert.setFitHeight(80);
        this.computerTurnAlert.setGraphic(aiAlert);
        this.computerTurnAlert.setHeaderText(null);
        this.computerTurnAlert.setTitle("Computer Turn");
        this.showAnimation = false;
    }

    @FXML
    private Button startGameButton;

    @FXML
    private Button LoadXMLButton;

    @FXML
    private Button endGameButton;

    @FXML
    private Button replayModeButton;

    @FXML
    private Button exitButton;

    @FXML
    private GridPane playersInfoGrid;

    @FXML
    private Label playerIdLabel1;

    @FXML
    private ScrollPane mainGameScrollPane;

    @FXML
    private StackPane resignPane;

    @FXML
    private Label playerNum1;

    @FXML
    private Circle playerColor1;

    @FXML
    private Circle playerColor2;

    @FXML
    private Circle playerColor3;

    @FXML
    private Circle playerColor4;

    @FXML
    private Circle playerColor5;

    @FXML
    private Circle playerColor6;

    @FXML
    private ImageView playerType1;

    @FXML
    private ImageView playerType2;

    @FXML
    private ImageView playerType3;

    @FXML
    private ImageView playerType4;

    @FXML
    private ImageView playerType5;

    @FXML
    private ImageView playerType6;

    @FXML
    private Label moveCountLabel1;

    @FXML
    private Label playerIdLabel2;

    @FXML
    private Label playerNum2;

    @FXML
    private Label moveCountLabel2;

    @FXML
    private Label playerIdLabel3;

    @FXML
    private Label playerNum3;

    @FXML
    private Label moveCountLabel3;

    @FXML
    private Label playerIdLabel4;

    @FXML
    private Label playerNum4;

    @FXML
    private Label moveCountLabel4;

    @FXML
    private MenuButton cssStylesMenu;

    @FXML
    private MenuItem menuItemLightOption;

    @FXML
    private MenuItem menuItemDarkOption;

    @FXML
    private MenuItem menuItemDisneyOption;

    @FXML
    private Label playerIdLabel5;

    @FXML
    private Label playerNum5;

    @FXML
    private Label playerIdLabel6;

    @FXML
    private Label playerNum6;

    @FXML
    private Label moveCountLabel5;

    @FXML
    private Label moveCountLabel6;

    @FXML
    private CheckBox animationCheckBox;

    @FXML
    private BorderPane gameBorderPane;

    @FXML
    private Label targetLabel;

    @FXML
    private ImageView activePlayer1;

    @FXML
    private ImageView activePlayer2;

    @FXML
    private HBox menuBar;

    @FXML
    private ImageView activePlayer3;

    @FXML
    private ImageView activePlayer4;

    @FXML
    private ImageView activePlayer5;

    @FXML
    private ImageView activePlayer6;

    @FXML
    private VBox replayBar;

    @FXML
    private Button prevMoveButton;

    @FXML
    private Button endReplayModeButton;

    @FXML
    private Button nextMoveButton;

    @FXML
    private void initialize() {
        startGameButton.disableProperty().bind(boardLoaded.not());
        endGameButton.disableProperty().bind(isGameActive.not());
        LoadXMLButton.disableProperty().bind(isGameActive);
        replayModeButton.disableProperty().bind(isGameActive.not());
        resignButton.disableProperty().bind(isGameActive.not());
    }


    @FXML
    private Button resignButton;

    @FXML
    public void openXMLFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML Game File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files","*.xml"));
        File selectedFiles=fileChooser.showOpenDialog(primaryStage);
        if (selectedFiles==null)
            return;

        String absolutePath=selectedFiles.getAbsolutePath();
        selectedFileProperty.set(absolutePath);
        isFileSelected.set(true);
        LoadXMLTask XMLLoader = new LoadXMLTask(game, absolutePath);
        //XMLFileError fileLoaded = game.openFile(absolutePath);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Loading File");
        alert.setTitle("Nothing special to write here");

        Thread loadXMLThread = new Thread(XMLLoader);

        ImageView img = new ImageView("/fxml/loadingFile.gif");
        img.setFitHeight(90);
        img.setFitWidth(120);
        alert.setGraphic(img);

        alert.contentTextProperty().bind(XMLLoader.messageProperty());

        alert.show();
        loadXMLThread.start();

        XMLLoader.setOnSucceeded((event) ->{
        if (XMLLoader.getValue() == XMLFileError.ALL_GOOD) {
            game.loadBoard();
            game.setPopout();
            game.loadPlayers();
            game.setWinningN();
            game.setBoardLoaded(true);
            this.initPlayerInfo();
            this.initBoardGUI();
            this.updateButtonsAccess();
            boardLoaded.setValue(true);
            targetLabel.setVisible(true);
            targetLabel.setText("Current Game Target: "+game.getWinningN());

            alert.close();
        }
        else {
            alert.close();
            gui.showAlertMessage(XMLLoader.getValue().toString());
        }
        });

    }

    public void updateButtonsAccess() {
        for (int i = 0; i < game.getBoard().getNumOfCols(); i++) {
            gameBoardGridPane.lookup("#arrowDown" + i).setVisible(game.getValidColumnsTop().get(i) && game.isActiveGame() && !game.isReplayMode());
            //gameBoardGridPane.lookup("#arrowDown" + i).setDisable(!game.getValidColumnsTop().get(i) && game.isActiveGame());
            gameBoardGridPane.lookup("#arrowDown" + i).setDisable(false);

            if (game.isPopout()) {
                gameBoardGridPane.lookup("#arrowUp" + i).setVisible(game.getValidColumnsBottom().get(i) && game.isActiveGame() && !game.isReplayMode());
                //gameBoardGridPane.lookup("#arrowUp" + i).setDisable(!game.getValidColumnsBottom().get(i) && game.isActiveGame());
                gameBoardGridPane.lookup("#arrowUp" + i).setDisable(false);
            }
        }
    }


    private void disableArrowButtons(){
        for (int i=0; i<game.getBoard().getNumOfCols(); i++){
            gameBoardGridPane.lookup("#arrowDown"+i).setDisable(true);
            if (game.isPopout())
                gameBoardGridPane.lookup("#arrowUp"+i).setDisable(true);
        }
    }



    public void fixActivePlayers(int currActivePlayer){
        activePlayer1.setVisible(currActivePlayer==0 ? true : false);
        activePlayer2.setVisible(currActivePlayer==1 ? true : false);
        activePlayer3.setVisible(currActivePlayer==2 ? true : false);
        activePlayer4.setVisible(currActivePlayer==3 ? true : false);
        activePlayer5.setVisible(currActivePlayer==4 ? true : false);
        activePlayer6.setVisible(currActivePlayer==5 ? true : false);
    }

    public void showNextActivePlayer(){
        int currPlayer=game.getCurrPlayer()+1;
        int prevPlayer=currPlayer ==1 ? game.getNumOfPlayers() : currPlayer-1;
        playersInfoGrid.lookup("#activePlayer"+currPlayer).setVisible(true);
        playersInfoGrid.lookup("#activePlayer"+prevPlayer).setVisible(false);
    }

    public void showPrevActivePlayer(){
        int currPlayer=game.getCurrPlayer()+1;
        int prevPlayer=currPlayer ==1 ? game.getNumOfPlayers() : currPlayer-1;
        playersInfoGrid.lookup("#activePlayer"+currPlayer).setVisible(false);
        playersInfoGrid.lookup("#activePlayer"+prevPlayer).setVisible(true);
    }



    private void initBoardGUI(){
        this.gameBoardGridPane = new GridPane();
        GameBoard board = game.getBoard();
        this.rows = board.getNumOfRows();
        this.cols = board.getNumOfCols();


        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(1/this.rows);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(1/this.cols);



        gameBoardGridPane.getRowConstraints().addAll(rowConstraints, rowConstraints);
        gameBoardGridPane.getColumnConstraints().addAll(columnConstraints, columnConstraints);


        gameBoardGridPane.setAlignment(Pos.CENTER);
        gameBoardGridPane.setHgap(0);
        gameBoardGridPane.setVgap(0);

        //create a row for the buttons in the top of the grid
        for (int i=0; i<this.cols; i++){
            ImageView image = new ImageView("/fxml/ArrowDownButton.png");
            image.setFitHeight(buttonSize);
            image.setFitWidth(buttonSize);
            Button btn = new Button("",image);
            btn.setShape(new Circle(buttonSize));
            btn.setId("arrowDown"+i);
            btn.getStyleClass().add("ArrowDownButtons");
            btn.setDisable(true);

            final int col=i;
            btn.setOnAction(e -> {
                if(showAnimation) {
                    animationPlay(col, 0, game.getBoard().getColumns()[col].getFirstAvailable() + 1, false, game.getCurrPlayer()+1);
                }

                else {
                    game.playMove(col, false);
                    updateGUIBoard(game.getBoard().getBoardMatrix());
                    endMoveGUI();
                }});


            GridPane.setHalignment(btn, HPos.CENTER); // To align horizontally in the cell
            GridPane.setValignment(btn, VPos.CENTER); // To align vertically in the cell
            GridPane.setRowIndex(btn,0);
            GridPane.setColumnIndex(btn,i);
            gameBoardGridPane.getChildren().add(btn);
        }



        for (int i=1; i<=this.rows; i++) {
            for (int j = 0; j<this.cols; j++){
                StackPane cell = new StackPane();
                cell.setId("gridCell"+i+"-"+j);
                Shape rec = new Rectangle(rectangleSize,rectangleSize);
                rec.getStyleClass().add("gridCellRec");

                Circle circle=new Circle(circleSize);
                circle.setId("circle"+i+"-"+j);
                circle.setCenterX(rectangleSize/2);
                circle.setCenterY(rectangleSize/2);
                circle.getStyleClass().add("player0");

                cell.getChildren().addAll(rec,circle);

                GridPane.setRowIndex(cell,i);
                GridPane.setColumnIndex(cell,j);
                gameBoardGridPane.getChildren().add(cell);
            }
        }

        //create a row for the buttons in the bottom of the grid if needed

        if (game.isPopout()) {
            for (int i = 0; i < this.cols; i++) {
                ImageView image = new ImageView("/fxml/ArrowUpButton.png");
                image.setFitHeight(buttonSize);
                image.setFitWidth(buttonSize);
                Button btn = new Button("", image);
                btn.setShape(new Circle(buttonSize));
                btn.setId("arrowUp" + i);
                btn.getStyleClass().add("ArrowUpButtons");

                btn.setDisable(true);
                final int col=i;
                btn.setOnAction(e -> {
                    if(showAnimation){
                        for (int a=game.getBoard().getNumOfRows(); a>game.getBoard().getColumns()[col].getFirstAvailable()+1; a--) {
                            animationPlay(col,a,1, true, game.getBoard().getColumns()[col].getCell(a-1).getPlayer());
                        }
                    }
                    else {
                        game.playMove(col, true);
                        updateGUIBoard(game.getBoard().getBoardMatrix());
                        endMoveGUI();
                    }

                });

                GridPane.setHalignment(btn, HPos.CENTER); // To align horizontally in the cell
                GridPane.setValignment(btn, VPos.CENTER); // To align vertically in the cell
                GridPane.setRowIndex(btn, rows+1);
                GridPane.setColumnIndex(btn, i);
                gameBoardGridPane.getChildren().add(btn);
            }
        }
        gameBorderPane.setCenter(gameBoardGridPane);
    }



    private void animationPlay(int col, int row, int moveRow, boolean isPopout, int playerClass){
        gameBorderPane.setDisable(true);
        Circle emptyDisk = new Circle(circleSize);
        if(isPopout){
            emptyDisk.getStyleClass().add("player0");
            GridPane.setHalignment(emptyDisk, HPos.CENTER); // To align horizontally in the cell
            GridPane.setValignment(emptyDisk, VPos.CENTER); // To align vertically in the cell
            GridPane.setRowIndex(emptyDisk, row);
            GridPane.setColumnIndex(emptyDisk, col);
            gameBoardGridPane.getChildren().add(emptyDisk);

        }
        Circle newDisk = new Circle(circleSize);
        newDisk.getStyleClass().add("player" + playerClass);

        GridPane.setHalignment(newDisk, HPos.CENTER); // To align horizontally in the cell
        GridPane.setValignment(newDisk, VPos.CENTER); // To align vertically in the cell
        GridPane.setRowIndex(newDisk, row);
        GridPane.setColumnIndex(newDisk, col);
        gameBoardGridPane.getChildren().add(newDisk);


        Path path = new Path();
        path.getElements().addAll(new MoveTo(0, 0), new VLineTo(moveRow * rectangleSize));
        PathTransition pt = new PathTransition(Duration.millis(500), path, newDisk);
        pt.play();

        pt.setOnFinished(e1 -> {
            if(isPopout)
                gameBoardGridPane.getChildren().remove(emptyDisk);
            gameBoardGridPane.getChildren().remove(newDisk);
            gameBorderPane.setDisable(false);
            if(!isPopout || (isPopout && row == game.getBoard().getColumns()[col].getFirstAvailable()+2)) { //if its not Pop Out, play the move inside the function
                game.playMove(col, isPopout);
                updateGUIBoard(game.getBoard().getBoardMatrix());
                endMoveGUI();
            }
        });
    }

    private void endMoveGUI() {
        if (!game.isActiveGame())
            this.endGame();
        else {
            this.updateButtonsAccess();
            //this.showNextActivePlayer();
            fixActivePlayers(game.getCurrPlayer());
            playComputerTurns();
        }
    }


    public void updateGUIBoard(int[][] boardMatrix){
        for (int i=1; i<=this.rows; i++){
            for (int j=0; j<this.cols; j++){
                StackPane currCell = (StackPane) this.gameBoardGridPane.lookup("#gridCell"+i+"-"+j);
                Node currCircle= currCell.getChildren().get(currCell.getChildren().size()-1);
                currCircle.getStyleClass().clear();
                currCircle.getStyleClass().add("player"+boardMatrix[i-1][j]);
            }
        }
    }

    public void showComputerAlert(boolean bol){
        if (bol)
            this.computerTurnAlert.show();
        else
            this.computerTurnAlert.close();
    }

    public void playComputerTurns() {
        if(!game.isCurrentPlayerHuman()) {

            gameBorderPane.setDisable(true);
            ComputerTurnTask turn = new ComputerTurnTask(this.game, this);
            Thread computerTurn = new Thread(turn);

            this.computerTurnAlert.contentTextProperty().bind(turn.messageProperty());
            this.showComputerAlert(true);

            computerTurn.start();

            turn.setOnSucceeded(e ->
            {
                robotChoice choice = turn.getValue();
                gameBorderPane.setDisable(false);
                this.showComputerAlert(false);

                if(showAnimation) {
                    if(!choice.isPopuot())
                        animationPlay(choice.getColumn(), 0, game.getBoard().getColumns()[choice.getColumn()].getFirstAvailable() + 1, choice.isPopuot(), game.getCurrPlayer()+1);

                    else{
                        for (int a=game.getBoard().getNumOfRows(); a>game.getBoard().getColumns()[choice.getColumn()].getFirstAvailable()+1; a--) {
                            animationPlay(choice.getColumn(), a, 1, true, game.getBoard().getColumns()[choice.getColumn()].getCell(a - 1).getPlayer());
                        }
//                        game.playMove(choice.getColumn(), choice.isPopuot());
//                        updateGUIBoard(game.getBoard().getBoardMatrix());
//                        endMoveGUI();
                    }
                }

                else{ game.playMove(choice.getColumn(), choice.isPopuot());
                    updateGUIBoard(game.getBoard().getBoardMatrix());
                    endMoveGUI();}
            });
        }
    }

    public void setLabelText(Parent node, String labelName, String labelValue){
        Label lbl = (Label)node.lookup(labelName);
        lbl.setText(labelValue);
        lbl.setVisible(true);
    }


    public void setPlayerType(Parent node, String imagePlayerNum, boolean isHuman){
        ImageView img= (ImageView) node.lookup(imagePlayerNum);
        String imagePath = isHuman ? "/fxml/humanType1.png" : "/fxml/computerType.png";
        Image imageObject=new Image(imagePath);
        img.setImage(imageObject);
        img.setVisible(true);
    }

    public void setNodeVisability(Node node, String nodeName, boolean visible){
        Node nod= node.lookup(nodeName);
        nod.setVisible(visible);
    }


    public void initPlayerInfo(){
        playersInfoGrid.setVisible(true);
        List<Engine.Player> players=game.getPlayers();

        for (int i =0;i<players.size();i++){
            Engine.Player currPlayer=players.get(i);
            Short playerID = currPlayer.getPlayerID();

            //set players texts
            setLabelText(playersInfoGrid, "#playerNum"+(i+1),currPlayer.getPlayerName());
            setLabelText(playersInfoGrid, "#playerIdLabel"+(i+1),playerID.toString());

            //set players type
            setPlayerType(playersInfoGrid, "#playerType"+(i+1),currPlayer.isHuman());

            //show player color icon
            setNodeVisability(playersInfoGrid, "#playerColor"+(i+1), true);

            //show and bind player move count to label
            Label playerMoveCountLabel =(Label)playersInfoGrid.lookup("#moveCountLabel"+(i+1));
            playerMoveCountLabel.textProperty().bind(currPlayer.getPlayerMoveCountProperty().asString());
            playerMoveCountLabel.setVisible(true);
        }

        for (int i=6; i>players.size(); i--) {
            setNodeVisability(playersInfoGrid, "#playerNum" + (i), false);
            setNodeVisability(playersInfoGrid, "#moveCountLabel"+(i), false);
            setNodeVisability(playersInfoGrid, "#playerIdLabel"+(i), false);
            setNodeVisability(playersInfoGrid, "#playerColor"+(i), false);
            setNodeVisability(playersInfoGrid, "#playerType"+(i), false);
        }


    }

    @FXML
    void startGame(ActionEvent event) {
        startGameButton.setText("Restart Game");
        restartClassesPlayersGrid();
        isGameActive.set(true);
        game.startGame();
        updateGUIBoard(game.getBoard().getBoardMatrix());
        this.updateButtonsAccess();
        activePlayer1.setVisible(true);
        activePlayer2.setVisible(false);
        activePlayer3.setVisible(false);
        activePlayer4.setVisible(false);
        activePlayer5.setVisible(false);
        activePlayer6.setVisible(false);
        this.playComputerTurns();
    }


    @FXML
    void endGameButton(ActionEvent event) {
        this.endGame();
    }

    public void endGame(){
        startGameButton.setText("Start Game");
        isGameActive.set(false);
        disableArrowButtons();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        if(game.getWinners().isEmpty()) {
            alert.setHeaderText(null);
            alert.setContentText("Game tied");
            alert.setTitle("Game Ended");

            ImageView img = new ImageView("/fxml/gameEnded.png");
            img.setFitHeight(90);
            img.setFitWidth(90);
            alert.setGraphic(img);

            alert.show();
        }
        else{
            alert.setHeaderText(null);

            String text=game.getWinners().size()>1 ? "The winners are:" : "The winner is:";
            alert.setContentText("Game Over! " +text +"\n"+game.returnWinnersIds());
            alert.setTitle("Game Ended");
            ImageView img = new ImageView("/fxml/winner.jpg");
            img.setFitHeight(120);
            img.setFitWidth(120);
            alert.setGraphic(img);
            alert.show();
        }

        alert.setOnHidden(e->{
            this.restartClassesPlayersGrid();
            game.endGame();
            this.updateButtonsAccess();
            updateGUIBoard(game.getBoard().getBoardMatrix());

            activePlayer1.setVisible(false);
            activePlayer2.setVisible(false);
            activePlayer3.setVisible(false);
            activePlayer4.setVisible(false);
            activePlayer5.setVisible(false);
            activePlayer6.setVisible(false);

        });
    }

    @FXML
    void exitGame(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void prevMove(ActionEvent event) {
        this.showPrevActivePlayer();
        //fixActivePlayers(game.getCurrPlayer());
        int flag = game.prevMove();
        this.updateGUIBoard(game.getBoard().getBoardMatrix());
        this.nextMoveButton.setDisable(false);
        if(flag == 0)
            this.prevMoveButton.setDisable(true);
    }

    @FXML
    void nextMove(ActionEvent event) {
        int flag = game.nextMove();
        this.showNextActivePlayer();
        //fixActivePlayers(game.getCurrPlayer());
        this.updateGUIBoard(game.getBoard().getBoardMatrix());
        this.prevMoveButton.setDisable(false);
        if(flag == 0)
            this.nextMoveButton.setDisable(true);
    }


    @FXML
    void endReplayMode(ActionEvent event) {
        this.resignPane.setDisable(false);
        this.game.finishReplayMode();
        this.updateButtonsAccess();
        this.replayBar.setVisible(false);
        this.menuBar.setDisable(false);
        this.game.setReplayMode(false);
        this.updateGUIBoard(game.getBoard().getBoardMatrix());
        fixActivePlayers(game.getCurrPlayer());
        this.gameBoardGridPane.setDisable(false);

    }

    @FXML
    void startReplayMode(ActionEvent event) {
        this.resignPane.setDisable(true);
        this.prevMoveButton.setDisable(game.getMoves().isEmpty());
        this.replayBar.setVisible(true);
        this.menuBar.setDisable(true);
        this.nextMoveButton.setDisable(true);
        this.game.setReplayMode(true);
        this.gameBoardGridPane.setDisable(true);
        this.updateButtonsAccess();
    }

    public void disablePlayerFromGrid(int resignedPlayer){
        Node playerNum =playersInfoGrid.lookup("#playerNum"+resignedPlayer);
        Node playerMoveCount =playersInfoGrid.lookup("#moveCountLabel"+resignedPlayer);
        Node playerID =playersInfoGrid.lookup("#playerIdLabel"+resignedPlayer);
        Node playerColor =playersInfoGrid.lookup("#playerColor"+resignedPlayer);
        Node playerType =playersInfoGrid.lookup("#playerType"+resignedPlayer);

        playerNum.getStyleClass().add("resignedPlayer");
        playerMoveCount.getStyleClass().add("resignedPlayer");
        playerID.getStyleClass().add("resignedPlayer");
        playerColor.getStyleClass().add("resignedPlayer");
        playerType.getStyleClass().add("resignedPlayer");
    }

    public void restartClassesPlayersGrid(){
        playerNum1.getStyleClass().remove("resignedPlayer");
        playerNum2.getStyleClass().remove("resignedPlayer");
        playerNum3.getStyleClass().remove("resignedPlayer");
        playerNum4.getStyleClass().remove("resignedPlayer");
        playerNum5.getStyleClass().remove("resignedPlayer");
        playerNum6.getStyleClass().remove("resignedPlayer");

        playerIdLabel1.getStyleClass().remove("resignedPlayer");
        playerIdLabel2.getStyleClass().remove("resignedPlayer");
        playerIdLabel3.getStyleClass().remove("resignedPlayer");
        playerIdLabel4.getStyleClass().remove("resignedPlayer");
        playerIdLabel5.getStyleClass().remove("resignedPlayer");
        playerIdLabel6.getStyleClass().remove("resignedPlayer");

        playerColor1.getStyleClass().remove("resignedPlayer");
        playerColor2.getStyleClass().remove("resignedPlayer");
        playerColor3.getStyleClass().remove("resignedPlayer");
        playerColor4.getStyleClass().remove("resignedPlayer");
        playerColor5.getStyleClass().remove("resignedPlayer");
        playerColor6.getStyleClass().remove("resignedPlayer");

        moveCountLabel1.getStyleClass().remove("resignedPlayer");
        moveCountLabel2.getStyleClass().remove("resignedPlayer");
        moveCountLabel3.getStyleClass().remove("resignedPlayer");
        moveCountLabel4.getStyleClass().remove("resignedPlayer");
        moveCountLabel5.getStyleClass().remove("resignedPlayer");
        moveCountLabel6.getStyleClass().remove("resignedPlayer");

        playerType1.getStyleClass().remove("resignedPlayer");
        playerType2.getStyleClass().remove("resignedPlayer");
        playerType3.getStyleClass().remove("resignedPlayer");
        playerType4.getStyleClass().remove("resignedPlayer");
        playerType5.getStyleClass().remove("resignedPlayer");
        playerType6.getStyleClass().remove("resignedPlayer");
    }


    @FXML
    void resignPlayer(ActionEvent event) {
        this.disablePlayerFromGrid(game.getCurrPlayer()+1);
        this.game.resignCurrentPlayer();
        updateGUIBoard(game.getBoard().getBoardMatrix());
        this.updateButtonsAccess();
        endMoveGUI();
    }



    @FXML
    void loadDarkCSS(ActionEvent event) {
        this.mainGameScrollPane.getStylesheets().clear();
        this.mainGameScrollPane.getStylesheets().add(getClass().getResource("/fxml/CSS/Dark.css").toExternalForm());
        this.cssStylesMenu.setText("Dark");
    }

    @FXML
    void loadDisneyCSS(ActionEvent event) {
        this.mainGameScrollPane.getStylesheets().clear();
        this.mainGameScrollPane.getStylesheets().add(getClass().getResource("/fxml/CSS/Disney.css").toExternalForm());
        this.cssStylesMenu.setText("Disney");
    }

    @FXML
    void loadLightCSS(ActionEvent event) {
        this.mainGameScrollPane.getStylesheets().clear();
        this.mainGameScrollPane.getStylesheets().add(getClass().getResource("/fxml/CSS/Style.css").toExternalForm());
        this.cssStylesMenu.setText("Light");
    }

    @FXML
    void toggleAnimation(ActionEvent event) {
        this.showAnimation = !(this.showAnimation);
    }
}

