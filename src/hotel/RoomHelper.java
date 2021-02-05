package hotel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    public static void bookRoom() throws SQLException {

        int roomNumber = Input.askInt("Enter room number: ");
        int customerId = Input.askInt("Enter customer id: ");
        LocalDate checkInDate = LocalDate.now();

        if (Database.getInstance().addBooking(roomNumber,customerId,checkInDate)) {
            System.out.println("Booking added to system");
        }
    }

    public static void upgradeRoom() {

        //TODO When upgrading room can be booked more then once. function exists in book room
        //TODO hämta gammalt rum från hashmap och sätt setRented till false, koppla bort kund koplingen(till null)
        //TODO Hämta nya rummet från hashmap, på rum sätt setrented till true, och koppla kunden till rummet (renter) hämta från customerHelper
        //TODO Flytta bill från gammla till nya rummet

        ArrayList availableRooms = getAvailableRooms(true);

//        searchInArray();
//
//        try {
//            int bookingId = Input.askInt("Enter booking ID: ");
//            int upgradedRoomNumber = Input.askInt("Enter new room number: ");
//            if (getRoomMap().get(upgradedRoomNumber).isRented()) {
//                if (Database.getInstance().upgradeBooking(bookingId, upgradedRoomNumber)) {
//                    System.out.println("Room upgraded");
//                }
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
    }
    public static void searchInArray() {

        //TODO bug in search does not exit loop
        //TODO hård testa do-while loopen (return och boolean run)

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
                    return;
                case 2:
                    CustomerHelper.searchCustomerLastName();
                    return;
                case 3:
                    addBookingsToArray();
                    for (Booking booking : bookings) {
                        System.out.println(booking);
                    }
                    return;
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

        //TODO komma på ur man ska skicka
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

        try {
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * Returns a list of all Room-objects, filtered to all available or booked rooms
     * @param isAvailable true for only available rooms, false for only booked rooms
     * @return ArrayList of Rooms, empty list if no matches were found
     */
    public static ArrayList<Room> getAvailableRooms(boolean isAvailable) {
        if (isAvailable) {
            return getRoomMap().values().stream()
                    .filter(room -> !room.isRented())
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            return getRoomMap().values().stream()
                    .filter(Room::isRented)
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
