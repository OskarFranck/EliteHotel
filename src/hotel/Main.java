package hotel;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
	// write your code here



        Database.getInstance(); // init database before main loop

        // TODO - Ta bort demo data här
        // ## Fyller programmet och databasen med demo kunder, rum och bokningar - kan köras en gång
        //RoomHelper.addCustomersToDataBase();
 //       RoomHelper.addRoomsToDataBase();

        // TODO (Oscar) - Testing here
        //while (true) {
        //    CustomerHelper.searchAndSelectCustomerMenu();
        //}
   //     RoomHelper.listAllBookings();

        // TODO - Eget Exception som kastas när användaren försöker boka ett upptaget rum

        // TODO - Vid incheckning, skapa en ny bill och knyt den till rummet, och lägg till i BillMap
        // TODO - BookRoom titta vad som händer med bill
        // TODO - Hantera OrderFood så maten läggs till på en bill
        // TODO - Vid RoomUpgrade, flytta bill från gamla till nya rummet och uppdatera BillMap nyckel
        // TODO - Vid utcheckning, skriver man bill till databasen, ta bort den från billMap

        MainMenu.mainMenu();
    }

}
