package hotel;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try {
            Database.getInstance(); // init database before main loop
            MainMenu.mainMenu();
            Database.getInstance().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String printBold(String input) {
        return "\033[1m"+input+"\033[0m";
    }

    public static String printBoldRed(String input) {
        return "\u001B[31m"+input+"\u001B[0m";
    }

}
