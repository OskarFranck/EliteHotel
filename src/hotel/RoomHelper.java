package hotel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class RoomHelper {

    private static final HashMap<Integer, Room> roomMap = new HashMap<>();
    private static final HashMap<Integer, Bill> activeBillMap = new HashMap<>();

    public static HashMap<Integer, Room> getRoomMap() {
        return roomMap;
    }
    public static HashMap<Integer, Bill> getActiveBillMap() {
        return activeBillMap;
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

    public static void addBillToMap(Bill bill) {
        if (activeBillMap.containsKey(bill.getRoomNumber())) {
            System.err.println("Error: Room already has a bill");
            return;
        }

        try {
            activeBillMap.put(bill.getRoomNumber(), bill);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// Not used (remove)
//    public static int getOnlyExistingCustId() {
//        Customer cust;
//        do {
//            int customerId = Input.askInt("Enter customer ID: ");
//            cust = CustomerHelper.getCustomer(customerId);
//            if (cust == null) {
//                System.out.println("Cant find customer, try again");
//            }
//        } while (cust == null);
//        return cust.getId();
//    }

    public static int getValidRoomForUpgrade() {
        while (true) {
            int existingRoom = Input.askInt("Enter room number: ");
            Room room = roomMap.get(existingRoom);
            if (room == null) {
                System.out.println("Cant find room, try again");
                continue; // Restart loop
            }
            if (getAvailableOrUnavailableRooms(true).contains(room)) {
                System.out.println("Room #" +  room.getRoomNumber() + " cant be upgraded, no one stays there");
                continue; // Restart loop
            }
            return room.getRoomNumber();
        }
    }

    public static int getOnlyExistingAndAvailableRoom() {
        while (true) {
            int existingRoom = Input.askInt("Enter room number: ");
            Room room = roomMap.get(existingRoom);
            if (room == null) {
                System.out.println("Cant find room, try again");
                continue; // Restart loop
            }
            if (getAvailableOrUnavailableRooms(false).contains(room)) {
                System.out.println("Room is already booked, try again");
                continue; // Restart loop
            }
            return room.getRoomNumber();
        }
    }

    public static void bookRoom() {
        int addRoomNumber;
        int customerId;
        LocalDate checkInDate;
        Customer cust;

        while (true) {
            cust = CustomerHelper.searchAndSelectCustomerMenu();
            if (cust == null) {
                return; // Go back to main menu
            }

            if (findCustomersRoom(cust) != null) {
                System.out.println("Customer is already booked to room.");
                continue; // Loop again
            }
            break;
        }

        customerId = cust.getId();

        bookRoomMenu();
        addRoomNumber = getOnlyExistingAndAvailableRoom();
        checkInDate = LocalDate.now();

        if (!getRoomMap().get(addRoomNumber).isRented()) {
            try {
                if (Database.getInstance().addBooking(addRoomNumber, customerId, checkInDate)) {
                    System.out.println("Booking added to DB");
                }
            } catch (SQLException throwables) {

            }
            getRoomMap().get(addRoomNumber).setRented(true);
            getRoomMap().get(addRoomNumber).setRenter(CustomerHelper.customers.stream().filter(cs -> cs.getId() == customerId).findFirst().orElse(null));
            Bill bill = new Bill(addRoomNumber); // TODO - Store this somewhere? YES HASHMAP
            System.out.println("Booking added to hashMap");
        } else {
            // TODO - hantera att bokning frågar efter ett available room ist för att gå vidare
            System.out.println("Could not add booking");
        }
    }

    public static void bookRoomMenu() {
        int choice;

        skip:
        do {
            System.out.println("1. List room available by room type");
            System.out.println("2. List all available rooms");
            System.out.println("0. Back to menu");

            choice = Input.askInt("Choose from menu: \n");

            switch (choice) {
                case 1:
                    listRoomsByType();
                    break skip;
                case 2:
                    listAllAvailableRooms();
                    break skip;
                default:
                    return;
            }
        } while (choice < 0 || choice > 2);
    }

    public static void listAllAvailableRooms() {
        getAvailableOrUnavailableRooms(true).forEach(room -> {
            if (room.getRenter() == null) {
                System.out.println("Room #" + room.getRoomNumber() + " " + room.getRoomType() + ", Available");
            } else {
                System.out.println("Room #" + room.getRoomNumber() + ", " + room.getRenter().getLastName());
            }
        });
    }

    public static void listRoomsByType() {
        int choice;

        System.out.println("1. Show all available Standard single rooms");
        System.out.println("2. Show all available Standard double rooms");
        System.out.println("3. Show all available Luxury single rooms");
        System.out.println("4. Show all available Luxury double rooms");
        System.out.println("5. Show all available Deluxe double rooms");

        choice = Input.askInt("Choose from menu: ");
        switch (choice) {
            case 1:
                getAvailableOrUnavailableRooms(true, RoomType.STANDARD_SINGLE).forEach(room -> {
                    if (room.getRenter() == null) {
                        System.out.println("Room #" + room.getRoomNumber() + ", Available");
                    }
                });
                break;
            case 2:
                getAvailableOrUnavailableRooms(true, RoomType.STANDARD_DOUBLE).forEach(room -> {
                    if (room.getRenter() == null) {
                        System.out.println("Room #" + room.getRoomNumber() + ", Available");
                    }
                });
                break;
            case 3:
                getAvailableOrUnavailableRooms(true, RoomType.LUXURY_SINGLE).forEach(room -> {
                    if (room.getRenter() == null) {
                        System.out.println("Room #" + room.getRoomNumber() + ", Available");
                    }
                });
                break;
            case 4:
                getAvailableOrUnavailableRooms(true, RoomType.LUXURY_DOUBLE).forEach(room -> {
                    if (room.getRenter() == null) {
                        System.out.println("Room #" + room.getRoomNumber() + ", Available");
                    }
                });
                break;
            case 5:
                getAvailableOrUnavailableRooms(true, RoomType.DELUXE_DOUBLE).forEach(room -> {
                    if (room.getRenter() == null) {
                        System.out.println("Room #" + room.getRoomNumber() + ", Available");
                    }
                });
                break;
        }
    }

    public static void upgradeRoom() {

        // TODO - Flytta bill vid byte av rum.
        getAvailableOrUnavailableRooms(false).forEach(room -> {
            if (room.getRenter() == null) {
                System.out.println("Room #" + room.getRoomNumber() + ", Unknown guest");
            } else {
                System.out.println("Room #" + room.getRoomNumber() + ", " + room.getRenter().getLastName());
            }
        });

        int currentRoomNumber = getValidRoomForUpgrade();

        getAvailableOrUnavailableRooms(true).forEach(room -> {
            if (room.getRenter() == null) {
                System.out.println("Room #" + room.getRoomNumber() + " " + room.getRoomType() + ", Available");
            } else {
                System.out.println("Room #" + room.getRoomNumber() + ", " + room.getRenter().getLastName());
            }
        });

        int upgradedRoomNumber = getOnlyExistingAndAvailableRoom();

        //TODO Badly handled nullpointerexception
        upgradeRoomDB(currentRoomNumber ,upgradedRoomNumber);
        upgradeRoomHM(currentRoomNumber, upgradedRoomNumber);

        // TODO - Move bill from current room to new room

    }

    public static void upgradeRoomDB(int currentRoomNumber, int upgradedRoomNumber) {

        // TODO Hantera att kunder kan stå på flera rum

        int customerId;
        int bookingId = 0;
        try {
            if (roomMap.get(currentRoomNumber).getRenter() != null) {
                Customer cust = roomMap.get(currentRoomNumber).getRenter();
                customerId = cust.getId();
            } else {
                System.err.println("No room");
                return;
            }
            ResultSet rs = Database.getInstance().getSingleBooking(currentRoomNumber, customerId);
            while (rs.next()) {
                bookingId = rs.getInt("bookingId");
            }
            Database.getInstance().upgradeBooking(bookingId, upgradedRoomNumber);
            // TODO - hantera att inte kunna updatera till ett rum som inte finns eller redan är bokat
            System.out.println("Room updated in Database");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            System.err.println("Cant find booking with matching room number, cant upgrade");
        }
    }

    public static void upgradeRoomHM(int currentRoomNumber, int upgradedRoomNumber) {

        try {
            if (roomMap.get(currentRoomNumber).getRenter() != null && roomMap.get(currentRoomNumber).isRented()) {
                roomMap.get(currentRoomNumber).setRented(false);
                Customer cust = roomMap.get(currentRoomNumber).getRenter();
                roomMap.get(currentRoomNumber).setRenter(null);
                roomMap.get(upgradedRoomNumber).setRenter(cust);
                roomMap.get(upgradedRoomNumber).setRented(true);
                System.out.println("Room updated in hashMap");
            } else {
                System.out.println("Can't upgrade room, no existing booking exists");
            }
        } catch (Exception e) {

        }
    }

    public static void checkOut() {
        //TODO Checkout is done with booking id, still need to get bookingslist as Array from DB
        // TODO Använd rums nummmert för att hämta bill och sätta bill till completed och ta rums referansen och tabort from Billmap
        int bookingId = Input.askInt("Enter booking id for checkout: ");
        LocalDate checkOutDate = LocalDate.now();

        try {
            Database.getInstance().checkOutBooking(bookingId, checkOutDate);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void daysStayed() {

    }

    // TODO metod som kolla hur många dagar som gått mellan in och ut checking

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

            CustomerHelper.customers.add(new Customer("Oskar", "Franck", "123123"));
            CustomerHelper.customers.add(new Customer("Egon", "Bergfalk", "123123"));
            CustomerHelper.customers.add(new Customer("Bella", "Andersson", "123123"));
            CustomerHelper.customers.add(new Customer("Jack", "Olson", "123123"));
            CustomerHelper.customers.add(new Customer("Svinto", "Stal", "123123"));
            CustomerHelper.customers.add(new Customer("Bla", "Bla", "123123"));
            CustomerHelper.customers.add(new Customer("Magdalena", "Bergqvist", "123123"));
            CustomerHelper.customers.add(new Customer("Oscar", "Bergstrom", "123123"));
            CustomerHelper.customers.add(new Customer("Jonas", "Lindgren", "123123"));
            CustomerHelper.customers.add(new Customer("Elenore", "Franck", "123123"));
            CustomerHelper.customers.add(new Customer("Sandra", "Nordin", "123123"));

    }

    /**
     * Returns a list of all Room-objects, filtered to all available or booked rooms
     * @param isAvailable true for only available rooms, false for only booked rooms
     * @return ArrayList of Rooms, empty list if no matches were found
     */
    public static ArrayList<Room> getAvailableOrUnavailableRooms(boolean isAvailable) {
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
    public static ArrayList<Room> getAvailableOrUnavailableRooms(boolean isAvailable, RoomType roomType) {
        return getAvailableOrUnavailableRooms(isAvailable).stream()
                .filter(room -> room.getRoomType() == roomType)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Attempts to find a room that has a booking with that customer. Returns null if no room was found for that customer.
     * @param customer Customer-object to look for using it's ID
     * @return Room with customer as current renter, or null
     */
    public static Room findCustomersRoom(Customer customer) {
        return getAvailableOrUnavailableRooms(false).stream()
                .filter(room -> room.getRenter().getId() == customer.getId())
                .findFirst()
                .orElse(null);
    }

}
