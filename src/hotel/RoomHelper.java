package hotel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RoomHelper {
    static List<Booking> bookings = new ArrayList<>();

    public static boolean roomAlreadyBooked(int addRoomNumber) {
        int searchRoomNumber = 0;
        try {
            ResultSet rs = Database.getInstance().getAllBookings();
            while (rs.next()) {
                searchRoomNumber = rs.getInt("roomNumber");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return searchRoomNumber != addRoomNumber;
    }

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

        try {
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

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
