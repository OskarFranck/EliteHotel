package hotel;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
	// write your code here



        Database.getInstance(); // init database before main loop

        // TODO - Ta bort demo data här
        // ## Fyller programmet och databasen med demo kunder, rum och bokningar - kan köras en gång

        // TODO (Oscar) - Testing here
        //while (true) {
        //    CustomerHelper.searchAndSelectCustomerMenu();
        //}
   //     RoomHelper.listAllBookings();
        MainMenu.mainMenu();
    }

}
