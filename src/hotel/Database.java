package hotel;

import java.sql.*;
import java.time.LocalDate;

public class Database {

    private final String url;
    private Connection sqlConnection;
    public static Database singletonInstance = null;

    /**
     * Holds a JDBC connection to local MySQL database server.
     * @param databaseName String name of local database
     * @param databaseUser String name of MySQL user (default is 'root')
     * @param databasePassword String password of MySQL user
     * @throws SQLException -
     */
    Database(String databaseName, String databaseUser, String databasePassword) throws SQLException {
        this.url = "jdbc:mysql://localhost:3306/" + databaseName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        this.sqlConnection = DriverManager.getConnection(url, databaseUser, databasePassword);
    }

    /**
     * Get a singleton instance/reference of class. Initializes up to one instance.
     * @return Static reference to singleton object
     * @throws SQLException -
     */
    public static Database getInstance() throws SQLException {
        if (singletonInstance == null) {
            singletonInstance = new Database("elitehotel", DatabaseCredentials.databaseUser, DatabaseCredentials.databasePassword);

            // Restore database data into application memory on launch
            RestoreFromDatabase.restoreAll(true);
        }
        return singletonInstance;
    }

    public void close() throws SQLException {
        sqlConnection.close();
    }

    /**
     * Retrieves the whole Customer table from the database as a ResultSet
     * @return ResultSet of Customer table
     * @throws SQLException -
     */
    public ResultSet getAllCustomers() throws SQLException {
        return sqlConnection.createStatement().executeQuery("SELECT * FROM Customer");
    }

    public boolean deleteCustomer(int Id) throws SQLException {
        try{
            String delCust = "DELETE FROM Customer WHERE customerId = ?";
            PreparedStatement statement = sqlConnection.prepareStatement(delCust);
            statement.setInt(1, Id);
            statement.executeUpdate();
            System.out.println( "DB uppdaterad, id " + Id + " borttagen");
            System.out.println("");
        return true;
        }catch (SQLException e){
            System.out.println(e);
            return false;
        }
    }

    //Hämtar högsta id-värde som finns i customer-tabellen
    //spara i int som kan hämtas till idGenerator i customer}
    public int getStartingPointIdGenerator() throws SQLException {
        ResultSet rs = sqlConnection.createStatement().executeQuery("SELECT MAX(customerID) AS max FROM Customer");
        rs.next();
        //System.out.println(" Här kommer maxvärdet ; ");
        int maxId = rs.getInt("max");
        //System.out.println(maxId);
        return maxId;
    }

    /**
     * Attempts to add the data from a Customer to the database.
     * @param firstName String of Customer first name, NOT empty
     * @param lastName String of Customer last name, NOT empty
     * @param phoneNumber String of Customer phone number, can be null
     * @return boolean success/failure
     * @throws SQLException -
     */
    public boolean addCustomer(int id, String firstName, String lastName, String phoneNumber) throws SQLException {
        if (firstName.isEmpty() || lastName.isEmpty()) {
            return false;
        }

        try {
            PreparedStatement statement = sqlConnection.prepareStatement("INSERT INTO Customer VALUES (?,?,?,?)");
            statement.setInt(1, id);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, phoneNumber);
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            // System.err.println("Error: Customer already exists in database!");
            return false;
        }
    }

    /**
     * Attempts to add the data from a Customer-object to the database.
     * @param customer Customer-object to add to database.
     * @return boolean success/failure
     * @throws SQLException -
     */
    public boolean addCustomer(Customer customer) {
        if (customer.getFirstName().isEmpty() || customer.getLastName().isEmpty()) {
            return false;
        }

        try {
            PreparedStatement statement = sqlConnection.prepareStatement("INSERT INTO Customer VALUES (?,?,?,?)");
            statement.setInt(1, customer.getId());
            statement.setString(2, customer.getFirstName());
            statement.setString(3, customer.getLastName());
            statement.setString(4, customer.getPhoneNumber());
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCustomer(Customer updatedCustomer) {
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("UPDATE Customer SET firstName = ?, lastName = ?, phoneNumber = ? WHERE customerId = ?");
            statement.setString(1, updatedCustomer.getFirstName());
            statement.setString(2, updatedCustomer.getLastName());
            statement.setString(3, updatedCustomer.getPhoneNumber());
            statement.setInt(4, updatedCustomer.getId());
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
        e.printStackTrace();
        return false;
        }
    }

    /**
     * Retrieves the whole Room table from the database as a ResultSet
     * @return ResultSet of Customer table
     * @throws SQLException -
     */
    public ResultSet getAllRooms() throws SQLException {
        return sqlConnection.createStatement().executeQuery("SELECT * FROM Room");
    }

    /**
     * Attempts to add the data from a Room to the database.
     * @param roomNumber Unique identity number of the room to be added.
     * @param type RoomType enum describing the type of room this is
     * @return boolean success/failure
     * @throws SQLException -
     */
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

    /**
     * Attempts to add the data from a Room to the database.
     * @param room Room-object to add to database.
     * @return boolean success/failure
     * @throws SQLException -
     */
    public boolean addRoom(Room room) throws SQLException {
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("INSERT INTO Room VALUES (?,?)");
            statement.setInt(1, room.getRoomNumber());
            statement.setString(2, room.getRoomType().toString());
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Error: Room already exists in database!");
            return false;
        }
    }

    /**
     * Retrieves the whole Booking table from the database as a ResultSet
     * @return ResultSet of Customer table
     * @throws SQLException -
     */
    public ResultSet getAllBookings() throws SQLException {
        return sqlConnection.createStatement().executeQuery("SELECT * FROM Booking");
    }

    /**
     * Attempts to add a booking to the database.
     * @param roomNumber Unique identity number of the room to be added, first generated by the database.
     * @param customerId Unique identity number of the customer to be added.
     * @param checkInDate LocalDate-object of the check-in date. "LocalDate.now()" can be used.
     * @return boolean success/failure
     * @throws SQLException -
     */
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

    /**
     * Attempts to retrieve a single booking from a booking id
     * @param bookingId Unique identity number of the booking, first generated by the database.
     * @return ResultSet of a single row from the Booking table
     * @throws SQLException -
     */
    public ResultSet getBooking(int bookingId) throws SQLException {
        PreparedStatement query = sqlConnection.prepareStatement("SELECT * FROM Booking WHERE bookingId = ?");
        query.setInt(1, bookingId);
        return query.executeQuery();
    }

    public ResultSet getSingleBooking (int roomNumber, int customerId) throws SQLException{
        PreparedStatement query = sqlConnection.prepareStatement("SELECT * FROM booking WHERE roomNumber = ? and customerId = ? and checkOutDate is null LIMIT 1");
        query.setInt(1, roomNumber);
        query.setInt(2, customerId);
        return query.executeQuery();
    }

    /**
     * Check if a booking id already exists in the database.
     * @param bookingId Unique identity number of the booking, first generated by the database.
     * @return boolean (true = the booking id does not exist yet, false = it already exists)
     * @throws SQLException -
     */
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

    /**
     * Upgrades a booking by updating the room number of target booking in the database.
     * @param bookingId Unique identity number of the target booking, first generated by the database.
     * @param newRoomNumber New room number to upgrade/change to.
     * @return boolean success/failure
     * @throws SQLException -
     */
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

    /**
     * Adds a checkout date to an existing booking in the database. Used during the checkout process.
     * @param bookingId Unique identity number of the target booking, first generated by the database.
     * @param checkOutDate LocalDate-object of the checkout-date, most likely "LocalDate.now()" should be used.
     * @return boolean success/failure
     * @throws SQLException -
     */
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
            PreparedStatement statement = sqlConnection.prepareStatement("INSERT INTO Bill (roomNumber, complete) VALUES (?,?)");
            statement.setInt(1, roomNumber);
            statement.setBoolean(2, false);
            statement.executeUpdate();

            ResultSet rs = sqlConnection.createStatement().executeQuery("SELECT LAST_INSERT_ID()");
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Error: Could not add bill to database - does roomNumber exist in database?");
            e.printStackTrace();
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
     * Get single bill and all food data from the database
     * @param billId Unique identity number of target bill
     * @return ResultSet of database query with 3 columns [billId (int), roomNumber (int), foodItemType (String), complete (Boolean)]
     * @throws SQLException -
     */
    public ResultSet getSingleBillData(int billId) throws SQLException {
        PreparedStatement statement = sqlConnection.prepareStatement("SELECT * FROM billView WHERE billId = ?");
        statement.setInt(1, billId);
        return statement.executeQuery();
    }

    /**
     * Attempts to restore a single Bill-object from the data in the database. Requires the ID of the bill.
     * @param billId Unique identity number of target bill
     * @return A new Bill-object based on the retrieved data, or null if anything went wrong.
     */
    public void restoreSingleBill(int billId) {
        try {
            Bill bill = null;
            ResultSet resultSet = getSingleBillData(billId);

            // Loop through all rows in the result
            while (resultSet.next()) {
                if (bill == null) {
                    // Create the Bill-object with the retrieved Room number, once.
                    bill = new Bill(resultSet.getInt("roomNumber"), resultSet.getInt("billId"));
                    if(resultSet.getBoolean("complete")) {
                        bill.setCompleted(true);
                    }
                }
                String foodItemType = resultSet.getString("foodItemType");
                bill.restoreAdd(new Food(Food.FoodMenuItem.valueOf(foodItemType))); // TODO - Här både läser och skriver den till databas (KLAR?)
            }

            if (bill == null) {
                System.err.println("Error: No bill to restore with given ID");
                return;
            }

            RoomHelper.getRoomMap().get(bill.getRoomNumber()).setRoomBill(bill);
            RoomHelper.addBillToMap(bill);
        } catch (SQLException e) {
            System.err.println("Error: Could not get bill from database");
            e.printStackTrace();
        }
    }

    /**
     * Get all bills and their data from the database
     * @return ResultSet of database query with 3 columns [billId (int), roomNumber (int), complete (Boolean)]
     * @throws SQLException -
     */
    public ResultSet getAllBills() throws SQLException {
        return sqlConnection.createStatement().executeQuery("SELECT * FROM bill");
    }

    /**
     * Set a bill as complete in the database during a checkout
     * @param billId Unique identity number of target bill
     * @return boolean for success/failure
     * @throws SQLException
     */
    public boolean checkOutBill(int billId) throws SQLException {
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("UPDATE Bill SET complete = ? WHERE billId = ?");
            statement.setBoolean(1, true);
            statement.setInt(2, billId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error: Could not checkout bill in database");
            e.printStackTrace();
            return false;
        }
    }

    public boolean upgradeBill(int billId, int newRoomNumber) throws SQLException {
        try {
            PreparedStatement statement = sqlConnection.prepareStatement("UPDATE Bill SET roomNumber = ? WHERE billId = ?");
            statement.setInt(1, newRoomNumber);
            statement.setInt(2, billId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error: Could not upgrade bill in database");
            e.printStackTrace();
            return false;
        }

    }

}