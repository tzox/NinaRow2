package Engine;

import Engine.XML.GameDescriptor;

import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class XMLParser implements Serializable {

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "Engine.XML";

    private int numOfRows;
    private int numOfCols;
    private int winningN;
    private List<Engine.XML.Player> players;
    String gameMode;

    public int getNumOfRows() {
        return numOfRows;
    }

    public int getNumOfCols() {
        return numOfCols;
    }

    public int getWinningN() {
        return winningN;
    }


    public List<Engine.XML.Player> getPlayers() {
        return players;
    }

    public String getGameMode() {
        return gameMode;
    }

    public boolean receiveXMLPath(File file) {
        InputStream inputStream = null;//XMLParser.class.getResourceAsStream("C:/BoardFiles/ex1-small.xml");
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist.");
        }
        try {
            GameDescriptor gameDesc = deserializeFrom(inputStream);
            this.winningN=gameDesc.getGame().getTarget().intValue();
            this.numOfCols=gameDesc.getGame().getBoard().getColumns().intValue();
            this.numOfRows=gameDesc.getGame().getBoard().getRows();
            this.players=gameDesc.getPlayers().getPlayer();
            this.gameMode=gameDesc.getGame().getVariant();

            return true;
        } catch (JAXBException e) {
            System.out.println("Error occurred during opening the file");
            System.out.println();
            return false;
        }
    }
    private static GameDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (GameDescriptor) u.unmarshal(in);
    }







//    public boolean receiveXMLPath(File file) {
//        try {
//
//            JAXBContext jaxbContext = JAXBContext.newInstance(GameDescriptor.class);
//
//            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//            GameDescriptor gameDesc = (GameDescriptor) jaxbUnmarshaller.unmarshal(file);
//
//            this.winningN=gameDesc.getGame().getTarget().intValue();
//            this.numOfCols=gameDesc.getGame().getBoard().getColumns().intValue();
//            this.numOfRows=gameDesc.getGame().getBoard().getRows();
//            return true;
//
//        } catch (JAXBException e) {
//            System.out.println("Error occurred during opening the file");
//            System.out.println();
//            return false;
//        }
//
//
//    }

}
