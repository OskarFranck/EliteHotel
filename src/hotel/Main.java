package hotel;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
	// write your code here



        Database.getInstance(); // init database before main loop

        // TODO - Ta bort demo data här
        // ## Fyller programmet och databasen med demo kunder, rum och bokningar - kan köras en gång
//        RoomHelper.addCustomersToDataBase();
//        RoomHelper.addRoomsToDataBase();

        // TODO (Oscar) - Testing here
        //while (true) {
        //    CustomerHelper.searchAndSelectCustomerMenu();
        //}
   //     RoomHelper.listAllBookings();

        // TODO - Eget Exception som kastas när användaren försöker boka ett upptaget rum

        // TODO - Vid RoomUpgrade, flytta bill från gamla till nya rummet och uppdatera BillMap nyckel (KLAR?)
        // TODO - Vid utcheckning, skriver man bill till databasen, ta bort den från billMap (KLAR?)

        MainMenu.mainMenu();
    }

    public static String printBold(String input) {
        return "\033[1m"+input+"\033[0m";
    }

    public static String printBoldRed(String input) {
        return "\u001B[31m"+input+"\u001B[0m";
    }

}
