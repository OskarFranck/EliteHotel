package hotel;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
	// write your code here

        // TODO - Ta bort demo data här
        // ## Fyller programmet och databasen med demo kunder, rum och bokningar - kan köras en gång
        RoomHelper.addCustomersToDataBase();
        RoomHelper.addRoomsToDataBase();

        MainMenu.mainMenu();
    }

}
