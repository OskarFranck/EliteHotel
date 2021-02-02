package hotel;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
	// write your code here
        Database db = new Database("elitehotel", DatabaseCredentials.databaseUser, DatabaseCredentials.databasePassword);
        MainMenu.EmployeeMenu();
    }

}
