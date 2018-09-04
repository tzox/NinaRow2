import java.util.Scanner;

public class Menu {

    private String loadOption="1. Load Board";
    private String startOption="2. Start Game";
    private String restartOption="2. Restart Game";
    private String StatusOption="3. Show Game Status";
    private String playOption="4. Play Turn";
    private String historyOption="5. Show Game History";
    private String undoOption = "6. Undo Last Turn";
    private String writeOption = "7. Save Game";
    private String readOption = "8. Load Last Saved Game";
    private String exitOption="9. Exit Game";


    public int menuInput(boolean isActive){
        Scanner reader = new Scanner(System.in);
        String userInput;
        System.out.println("Please choose one of the following options:");
        System.out.println(loadOption);
        System.out.println(isActive ? restartOption : startOption);
        System.out.println(StatusOption);
        System.out.println(playOption);
        System.out.println(historyOption);
        System.out.println(undoOption);
        System.out.println(writeOption);
        System.out.println(readOption);
        System.out.println(exitOption);

        while (true) {
            userInput = reader.nextLine();
            try {
                int menuOptionSelected = Integer.parseInt(userInput);
                if (menuOptionSelected<1 || menuOptionSelected>9)
                    System.out.println("Please choose a number between 1 and 9.");
                else
                    return menuOptionSelected;
            } catch (Exception e) {
                System.out.println("The value you have entered is not valid. Please enter a whole number.");
            }

        }
    }
}

