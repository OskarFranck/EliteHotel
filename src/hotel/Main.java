package hotel;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
	// write your code here
        MainMenu.EmployeeMenu();
        Database db = new Database("EliteHotelDB", DatabaseCredentials.databaseUser, DatabaseCredentials.databasePassword);
    }

}
