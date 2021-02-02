package hotel;

import java.sql.*;
import java.time.LocalDate;

public class Database {

    private final String url;
    private Connection sqlConnection;

    Database(String databaseName, String databaseUser, String databasePassword) throws SQLException {
        this.url = "jdbc:mysql://localhost:3306/" + databaseName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        this.sqlConnection = DriverManager.getConnection(url, databaseUser, databasePassword);
    }

    public void close() throws SQLException {
        sqlConnection.close();
    }

    public ResultSet getAllCustomers() throws SQLException {
        return sqlConnection.createStatement().executeQuery("SELECT * FROM Customer");
    }

    public boolean addCustomer(int id, String firstName, String lastName, String phoneNumber) throws SQLException {
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("INSERT INTO Customer VALUES (?,?,?,?)");
            statement.setInt(1, id);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, phoneNumber);
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Customer already exists in database!");
            return false;
        }
    }

    public ResultSet getAllRooms() throws SQLException {
        return sqlConnection.createStatement().executeQuery("SELECT * FROM Room");
    }

    /*
    // TODO - Requires RoomType enum to run
    public boolean addRoom(int roomNumber, RoomType type) throws SQLException {
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("INSERT INTO Room VALUES (?,?)");
            statement.setInt(1, roomNumber);
            statement.setString(2, type.toString());
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Room already exists in database!");
            return false;
        }
    }
     */

    public ResultSet getAllBookings() throws SQLException {
        return sqlConnection.createStatement().executeQuery("SELECT * FROM Booking");
    }

    // TODO - Will this autoincrement in database?
    public boolean addBooking(int roomNumber, int customerId, LocalDate checkInDate) throws SQLException {
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("INSERT INTO Booking (roomNumber, customerId, checkInDate) VALUES (?,?,?)");
            statement.setInt(1, roomNumber);
            statement.setInt(2, customerId);
            statement.setDate(3, Date.valueOf(checkInDate));
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Room already exists in database!");
            return false;
        }
    }

    public boolean addBooking(int roomNumber, int customerId, LocalDate checkInDate, LocalDate checkOutDate) throws SQLException {
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("INSERT INTO Booking (roomNumber, customerId, checkInDate, checkOutDate) VALUES (?,?,?,?)");
            statement.setInt(1, roomNumber);
            statement.setInt(2, customerId);
            statement.setDate(3, Date.valueOf(checkInDate));
            statement.setDate(4, Date.valueOf(checkOutDate));
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Room already exists in database!");
            return false;
        }
    }

    public boolean checkOutBooking(int bookingId, LocalDate checkOutDate) throws SQLException {
        PreparedStatement query = sqlConnection.prepareStatement("SELECT checkOutDate FROM Booking WHERE bookingId = ?");
        // Tests if above query for checkOutBooking returns empty
        if (!query.executeQuery().next()) {
            PreparedStatement statement = sqlConnection.prepareStatement("UPDATE Booking SET checkOutDate = ? WHERE bookingId = ?");
            statement.setDate(1, Date.valueOf(checkOutDate));
            statement.setInt(2, bookingId);
            statement.executeUpdate();
            return true;
        } else {
            System.out.println("Booking #" + bookingId + " is already checked out!");
            return false;
        }
    }

}