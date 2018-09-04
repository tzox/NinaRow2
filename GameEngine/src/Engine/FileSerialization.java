package Engine;

import java.io.*;

public class FileSerialization {

    public static final String FILE_NAME = "GameState.txt";

    public static GameEngine readGameStateFromFile() throws IOException, ClassNotFoundException, FileNotFoundException {
        // Read the array list  from the file
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(FILE_NAME))) {

            GameEngine game =
                    (GameEngine) in.readObject();
            in.close();
            game.setStartTimeSavedGame();
            return game;
        }

    }

    public static void writeGameStateToFile(GameEngine game) throws IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(FILE_NAME))) {
            game.setTimePassedForSavedFile();
            out.writeObject(game);
            out.flush();
            out.close();
        }
    }
}
