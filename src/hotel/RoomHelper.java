package hotel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

public class RoomHelper {

    private static final HashMap<Integer, Room> roomMap = new HashMap<>();

    public static HashMap<Integer, Room> getRoomMap() {
        return roomMap;
    }

    public static void addRoomToMap(Room room) {
        if (roomMap.containsKey(room.getRoomNumber())) {
            System.err.println("Error: Room already exists in map");
            return;
        }

        try {
            roomMap.put(room.getRoomNumber(), room);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void restoreRooms() {
        try {
            ResultSet resultSet = Database.getInstance().getAllRooms();
            while (resultSet.next()) {
                Room room = new Room(resultSet.getInt("roomNumber"), RoomType.valueOf(resultSet.getString("roomType")));
                addRoomToMap(room);
            }
        } catch (SQLException e) {
            System.err.println("Error: Could not read rooms from database!");
            e.printStackTrace();
        }
    }

    private static void restoreRoomBookingStatus() {
        try {
            ResultSet resultSet = Database.getInstance().getAllBookings();
            while(resultSet.next()) {
                if (resultSet.getString("checkInDate") != null && resultSet.getString("checkOutDate") == null) {
                    Room room = getRoomMap().get(resultSet.getInt("roomNumber"));
                    room.setRented(true);

                    int customerId = resultSet.getInt("customerId");
                    Customer customer = CustomerHelper.getCustomer(customerId);
                    if (customer == null) {
                        System.err.println("Warning: Could not restore booking for customer id #" + customerId + ". Not found in customer list.");
                    }
                    room.setRenter(customer);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: Could not read expected data from database!");
            e.printStackTrace();
        }
    }

    public static void bookRoom() throws SQLException {

        int roomNumber = Input.askInt("Enter room number: ");
        int customerId = Input.askInt("Enter customer id: ");
        LocalDate checkInDate = LocalDate.now();

        if (Database.getInstance().addBooking(roomNumber,customerId,checkInDate)) {
            System.out.println("Booking added to system");
        }
    }

    public static void upgradeRoom() throws SQLException{
        ResultSet rs = Database.getInstance().getAllBookings();
        while (rs.next()) {
            int bookingId = rs.getInt("bookingId");
            int roomNumber = rs.getInt("roomNumber");
            int customerId = rs.getInt("customerId");
            System.out.println("Booking ID: " + bookingId + ", Room number: " + roomNumber + ", Customer ID: " + customerId);
        }
        int bookingId = Input.askInt("Enter booking Id: ");
        int upgradedRoomNumber = Input.askInt("Enter new room number: ");

        if(Database.getInstance().upgradeBooking(bookingId, upgradedRoomNumber)) {
            System.out.println("Room upgraded");
        }
    }
    public static void orderFood() {

    }

    public static void checkOut() {
        //TODO Fråga efter kund/rums nummer
        //TODO Hämta booking från databas (Query till databas eller hämta hela tabellen och filtrera i java)
        //TODO Ropa på rumskvitto metod (från Bill class) med rumnummret och printa kvitto
        //TODO Ropa på databasen och sätt utcheckingsdatum

    }
    public static void addRoomsToDataBase() throws SQLException{

        Database.getInstance().addRoom(101, RoomType.STANDARD_SINGLE);
        Database.getInstance().addRoom(102, RoomType.STANDARD_SINGLE);
        Database.getInstance().addRoom(103, RoomType.STANDARD_DOUBLE);
        Database.getInstance().addRoom(104, RoomType.STANDARD_DOUBLE);
        Database.getInstance().addRoom(105, RoomType.STANDARD_DOUBLE);
        Database.getInstance().addRoom(201, RoomType.STANDARD_SINGLE);
        Database.getInstance().addRoom(202, RoomType.STANDARD_DOUBLE);
        Database.getInstance().addRoom(203, RoomType.LUXURY_DOUBLE);
        Database.getInstance().addRoom(204, RoomType.LUXURY_DOUBLE);
        Database.getInstance().addRoom(205, RoomType.LUXURY_SINGLE);
        Database.getInstance().addRoom(301, RoomType.LUXURY_SINGLE);
        Database.getInstance().addRoom(302, RoomType.LUXURY_DOUBLE);
        Database.getInstance().addRoom(303, RoomType.LUXURY_DOUBLE);
        Database.getInstance().addRoom(304, RoomType.DELUXE_DOUBLE);
        Database.getInstance().addRoom(305, RoomType.DELUXE_DOUBLE);
        restoreRooms();
        restoreRoomBookingStatus();

    }
    public static void addCustomersToDataBase() throws SQLException {

        Database.getInstance().addCustomer("Oskar", "Franck", "123123");
        Database.getInstance().addCustomer("Egon", "Bergfalk", "123123");
        Database.getInstance().addCustomer("Bella", "Andersson", "123123");
        Database.getInstance().addCustomer("Jack", "Olson", "123123");
        Database.getInstance().addCustomer("Svinto", "Stal", "123123");
        Database.getInstance().addCustomer("Bla", "Bla", "123123");
        Database.getInstance().addCustomer("Magdalena", "Bergqvist", "123123");
        Database.getInstance().addCustomer("Oscar", "Bergstrom", "123123");
        Database.getInstance().addCustomer("Jonas", "Lindgren", "123123");
        Database.getInstance().addCustomer("Elenore", "Franck", "123123");
        Database.getInstance().addCustomer("Sandra", "Nordin", "123123");

    }
}
