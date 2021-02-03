package hotel;

import java.sql.*;
import java.time.LocalDate;

public class Database {

    private final String url;
    private Connection sqlConnection;
    public static Database singletonInstance = null;

    Database(String databaseName, String databaseUser, String databasePassword) throws SQLException {
        this.url = "jdbc:mysql://localhost:3306/" + databaseName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        this.sqlConnection = DriverManager.getConnection(url, databaseUser, databasePassword);
    }

    public static Database getInstance() throws SQLException {
        if (singletonInstance == null) {
            singletonInstance = new Database("elitehotel", DatabaseCredentials.databaseUser, DatabaseCredentials.databasePassword);
        }
        return singletonInstance;
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
            System.err.println("Error: Customer already exists in database!");
            return false;
        }
    }

    public ResultSet getAllRooms() throws SQLException {
        return sqlConnection.createStatement().executeQuery("SELECT * FROM Room");
    }

    public boolean addRoom(int roomNumber, RoomType type) throws SQLException {
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("INSERT INTO Room VALUES (?,?)");
            statement.setInt(1, roomNumber);
            statement.setString(2, type.toString());
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Error: Room already exists in database!");
            return false;
        }
    }

    public ResultSet getAllBookings() throws SQLException {
        return sqlConnection.createStatement().executeQuery("SELECT * FROM Booking");
    }

    public boolean addBooking(int roomNumber, int customerId, LocalDate checkInDate) throws SQLException {
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("INSERT INTO Booking (roomNumber, customerId, checkInDate) VALUES (?,?,?)");
            statement.setInt(1, roomNumber);
            statement.setInt(2, customerId);
            statement.setDate(3, Date.valueOf(checkInDate));
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Error: Can not add booking to database!");
            return false;
        }
    }

    public ResultSet getBooking(int bookingId) throws SQLException {
        PreparedStatement query = sqlConnection.prepareStatement("SELECT * FROM Booking WHERE bookingId = ?");
        query.setInt(1, bookingId);
        return query.executeQuery();
    }

    private boolean noBookingExists(int bookingId) throws SQLException {
        return !getBooking(bookingId).next();
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
            System.err.println("Error: Can not add booking to database!");
            return false;
        }
    }

    public boolean upgradeBooking(int bookingId, int newRoomNumber) throws SQLException {
        if (noBookingExists(bookingId)) {
            System.err.println("Error: No booking with that ID!");
            return false;
        }

        try {
            PreparedStatement statement = sqlConnection.prepareStatement("UPDATE Booking SET roomNumber = ? WHERE bookingId = ?");
            statement.setInt(1, newRoomNumber);
            statement.setInt(2, bookingId);
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Error: Can not update booking to database!");
            return false;
        }
    }

    public boolean checkOutBooking(int bookingId, LocalDate checkOutDate) throws SQLException {
        if (noBookingExists(bookingId)) {
            System.err.println("Error: No booking with that ID!");
            return false;
        }

        // Get checkOutDate cell from database for this booking
        ResultSet query = getBooking(bookingId);
        query.next();
        String checkOutDateCell = query.getString(5);

        // Verify that a checkOutDate does not already exist for this booking
        if (checkOutDateCell == null) {
            PreparedStatement statement = sqlConnection.prepareStatement("UPDATE Booking SET checkOutDate = ? WHERE bookingId = ?");
            statement.setDate(1, Date.valueOf(checkOutDate));
            statement.setInt(2, bookingId);
            statement.executeUpdate();
            return true;
        } else {
            System.err.println("Error: Booking #" + bookingId + " is already checked out!");
            return false;
        }
    }

}