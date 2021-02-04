package hotel;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
	// write your code here

        // Du når nu databasen genom att ropa på den genom "Database.getInstance()..."
        // Database i sig är inte statisk, men du når den och alla metoder som om den vore det-
        // -genom att använda .getInstance()

        // Exempel:
        // Database.getInstance().getAllCustomers();

        // Första gången du ropar på .getInstance() så initsierar den kopplingen mot databasen.
        // Mer om hur Singleton fungerar: https://www.geeksforgeeks.org/singleton-class-java/

        // TODO -- Se alla lediga rum
        // TODO -- Se alla lediga rum av en viss typ
        MainMenu.mainMenu();
    }

}
