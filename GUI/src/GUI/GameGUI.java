package GUI;

import Controller.GameController;
import Engine.XMLFileError;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.net.URL;



public class GameGUI extends Application {
    private Button btn;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        //System.out.println(javafx.scene.text.Font.getFamilies());

       //load main fxml
        URL mainFXML = getClass().getResource("/fxml/GameFXML.fxml");
        loader.setLocation(mainFXML);
        ScrollPane root = loader.load();

        //wire up controller
        GameController gameController = loader.getController();
        gameController.setPrimaryStage(primaryStage);
        gameController.setGui(this);

        primaryStage.setTitle("Nina row");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public void showAlertMessage(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
