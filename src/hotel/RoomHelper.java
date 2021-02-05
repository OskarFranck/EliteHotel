package hotel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class RoomHelper {
    static List<Booking> bookings = new ArrayList<>();

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

    public static void restoreRoomBookingStatus() {
        try {
            ResultSet resultSet = Database.getInstance().getAllBookings();
            while(resultSet.next()) {
                int bookingId = resultSet.getInt("bookingId");
                int roomNumber = resultSet.getInt("roomNumber");

                if (resultSet.getString("checkInDate") != null && resultSet.getString("checkOutDate") == null) {

                    Room room = getRoomMap().get(roomNumber);
                    if (room == null) {
                        System.err.println("Warning: When loading bookings - room #" + roomNumber + " exists in the database, but not in the current program. Skipping loading booking #" + bookingId);
                        continue;
                    } else {
                        room.setRented(true);
                    }

                    int customerId = resultSet.getInt("customerId");
                    Customer customer = CustomerHelper.customers.stream().filter(customers -> customers.getId() == customerId).findFirst().orElse(null);
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

    public static void bookRoom2() {

        int addRoomNumber = Input.askInt("Enter room number: ");
        int customerId = Input.askInt("Enter customer id: ");
        LocalDate checkInDate = LocalDate.now();

        try {
            if (roomAlreadyBooked(addRoomNumber)) {
                if (Database.getInstance().addBooking(addRoomNumber, customerId, checkInDate)) {
                    System.out.println("Booking added to system");
                }
            } else {
                System.out.println("Room already booked");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void bookRoom() {

        int addRoomNumber = Input.askInt("Enter room number: ");
        int customerId = Input.askInt("Enter customer id: ");
        LocalDate checkInDate = LocalDate.now();
        int searchRoomNumber = 0;

        try {
            ResultSet rs = Database.getInstance().getAllBookings();
            while (rs.next()) {
                searchRoomNumber = rs.getInt("roomNumber");
            }
            if (searchRoomNumber != addRoomNumber) {
                if (Database.getInstance().addBooking(addRoomNumber, customerId, checkInDate)) {
                    System.out.println("Booking added to system");
                }
            } else {
                System.out.println("Room already booked");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void upgradeRoom() {

        //TODO When upgrading room can be booked more then once. function exists in book room
        searchInArray();

        try {
            int bookingId = Input.askInt("Enter booking Id: ");
            int upgradedRoomNumber = Input.askInt("Enter new room number: ");
            if (roomAlreadyBooked(upgradedRoomNumber)) {
                if (Database.getInstance().upgradeBooking(bookingId, upgradedRoomNumber)) {
                    System.out.println("Room upgraded");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void searchInArray() {

        //TODO bug in search does not exit loop

        System.out.println("Search in booking\n");
        System.out.println("1. Search by customer ID");
        System.out.println("2. Search by lastname");
        System.out.println("3. Show all bookings");
        System.out.println("0. Go back to previous menu");

        int choice;
        boolean run = true;
        do {
            choice = Input.askInt("Enter from above");

            switch (choice) {
                case 1:
                    addBookingsToArray();
                    int customerId = Input.askInt("Enter customer ID");
                    bookings.stream().filter(bs -> bs.getCustomer() == customerId).forEach(System.out::println);
                    break;
                case 2:
                    CustomerHelper.searchCustomerLastName();
                    break;
                case 3:
                    addBookingsToArray();
                    for (Booking booking : bookings) {
                        System.out.println(booking);
                    }
                    break;
                default:
                    run = false;
                    break;
            }
        } while (choice < 0 || choice > 3 || run);

    }

    public static void addBookingsToArray() {
        // add booking table to arraylist
        bookings.clear();
        int bookingId;
        int roomNumber;
        int customerId;
        String checkInDate;
        String checkOutDate;
        try {
            ResultSet rs = Database.getInstance().getAllBookings();
            while (rs.next()) {
                bookingId = rs.getInt("bookingId");
                roomNumber = rs.getInt("roomNumber");
                customerId = rs.getInt("customerId");
                checkInDate = rs.getString("checkInDate");
                checkOutDate = rs.getString("checkOutDate");
                Booking booking = new Booking(bookingId, roomNumber, customerId, checkInDate, checkOutDate);
                bookings.add(booking);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void checkOut() {
        searchInArray();

        int bookingId = Input.askInt("Enter booking id for checkout");
        LocalDate checkOutDate = LocalDate.now();

        try {
            Database.getInstance().checkOutBooking(bookingId, checkOutDate);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void addRoomsToDataBase() {

        try {
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

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void addCustomersToDataBase() {

        Database.getInstance().addCustomer("Oskar", "Franck", "123123");
        CustomerHelper.customers.add(new Customer("Oskar", "Franck", "123123"));
        Database.getInstance().addCustomer("Egon", "Bergfalk", "123123");
        CustomerHelper.customers.add(new Customer("Egon", "Bergfalk", "123123"));
        Database.getInstance().addCustomer("Bella", "Andersson", "123123");
        CustomerHelper.customers.add(new Customer("Bella", "Andersson", "123123"));
        Database.getInstance().addCustomer("Jack", "Olson", "123123");
        CustomerHelper.customers.add(new Customer("Jack", "Olson", "123123"));
        Database.getInstance().addCustomer("Svinto", "Stal", "123123");
        CustomerHelper.customers.add(new Customer("Svinto", "Stal", "123123"));
        Database.getInstance().addCustomer("Bla", "Bla", "123123");
        CustomerHelper.customers.add(new Customer("Bla", "Bla", "123123"));
        Database.getInstance().addCustomer("Magdalena", "Bergqvist", "123123");
        CustomerHelper.customers.add(new Customer("Magdalena", "Bergqvist", "123123"));
        Database.getInstance().addCustomer("Oscar", "Bergstrom", "123123");
        CustomerHelper.customers.add(new Customer("Oscar", "Bergstrom", "123123"));
        Database.getInstance().addCustomer("Jonas", "Lindgren", "123123");
        CustomerHelper.customers.add(new Customer("Jonas", "Lindgren", "123123"));
        Database.getInstance().addCustomer("Elenore", "Franck", "123123");
        CustomerHelper.customers.add(new Customer("Elenore", "Franck", "123123"));
        Database.getInstance().addCustomer("Sandra", "Nordin", "123123");
        CustomerHelper.customers.add(new Customer("Sandra", "Nordin", "123123"));

    }

    /**
     * Returns a list of all Room-objects, filtered to all available or booked rooms
     * @param isAvailable true for only available rooms, false for only booked rooms
     * @return ArrayList of Rooms, empty list if no matches were found
     */
    public static ArrayList<Room> getAvailableRooms(boolean isAvailable) {
        if (isAvailable) {
            return getRoomMap().values().stream()
                    .filter(room -> !room.getRented())
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            return getRoomMap().values().stream()
                    .filter(Room::getRented)
                    .collect(Collectors.toCollection(ArrayList::new));
        }
    }

    /**
     * Returns a list of all Room-objects, filtered to all available or booked rooms, and to a specific RoomType
     * @param isAvailable true for only available rooms, false for only booked rooms
     * @param roomType the RoomType enum to match for
     * @return ArrayList of Rooms, empty list if no matches were found
     */
    public static ArrayList<Room> getAvailableRooms(boolean isAvailable, RoomType roomType) {
        return getAvailableRooms(isAvailable).stream()
                .filter(room -> room.getRoomType() == roomType)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
