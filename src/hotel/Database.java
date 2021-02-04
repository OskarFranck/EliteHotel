package hotel;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

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

    public boolean addCustomer(String firstName, String lastName, String phoneNumber) throws SQLException {
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("INSERT INTO Customer (firstName, lastName, phoneNumber) VALUES (?,?,?)");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, phoneNumber);
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
            statement.executeUpdate();
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

    /** Creates a new bill table in the database from a Room number.
     * @param roomNumber Unique identity number of the room to be added.
     * @return int or 0 if anything went wrong.
     */
    public int newBill(int roomNumber) {
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("INSERT INTO Bill (roomNumber) VALUES (?)");
            statement.setInt(1, roomNumber);
            statement.executeUpdate();

            ResultSet rs = sqlConnection.createStatement().executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error: Could not add to database - does roomNumber exist in database?");
            return 0;
        }
    }

    /**
     * Add food to an existing bill in the database.
     * @param billId Unique identity number of target bill
     * @param food Food-object to add data from
     * @return boolean for success/failure
     */
    public boolean addFoodToBill(int billId, Food food) {
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("INSERT INTO BillFoodItem VALUES (?, ?)");
            statement.setInt(1, billId);
            statement.setString(2, food.getMenuItem().name());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error: Could not add to bill");
            return false;
        }
    }

    /**
     * Get all bills and their data from the database
     * @return ResultSet of database query with 3 columns [billId (int), roomNumber (int), foodItemType (String)]
     * @throws SQLException
     */
    public ResultSet getBillData() throws SQLException {
        return sqlConnection.createStatement().executeQuery("SELECT * FROM billView");
    }

    /**
     * Get single bill and all food data from the database
     * @param billId Unique identity number of target bill
     * @return ResultSet of database query with 3 columns [billId (int), roomNumber (int), foodItemType (String)]
     * @throws SQLException
     */
    public ResultSet getBillData(int billId) throws SQLException {
        PreparedStatement statement = sqlConnection.prepareStatement("SELECT * FROM billView WHERE billId = ?");
        statement.setInt(1, billId);
        return statement.executeQuery();
    }

    /**
     * Attempts to restore a Bill-object from the data in the database. Requires the ID of the bill.
     * @param billId Unique identity number of target bill
     * @return A new Bill-object based on the retrieved data, or null if anything went wrong.
     */
    public Bill restoreBill(int billId) {
        try {
            Bill bill = null;
            ResultSet resultSet = getBillData(billId);

            // Loop through all rows in the result
            while (resultSet.next()) {
                if (bill == null) {
                    // Create the Bill-object with the retrieved Room number, once.
                    bill = new Bill(resultSet.getInt(2));
                }
                String foodItemType = resultSet.getString(3);
                bill.add(new Food(Food.FoodMenuItem.valueOf(foodItemType)));
            }

            if (bill == null) {
                System.err.println("Error: No bill to restore with given ID");
            }

            return bill;
        } catch (SQLException e) {
            System.err.println("Error: Could not get bill from database");
            return null;
        }
    }

}