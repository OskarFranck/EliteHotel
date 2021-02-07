package hotel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

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

    public static int getOnlyExistingCustId() {
        Customer cust;
        do {
            int customerId = Input.askInt("Enter customer ID: ");
            cust = CustomerHelper.getCustomer(customerId);
            if (cust == null) {
                System.out.println("Cant find customer, try again");
            }
        } while (cust == null);
        return cust.getId();
    }

    public static int getOnlyExistingRoomNumber() {
        int existingRoom;
        Room room;
        do {
            existingRoom = Input.askInt("Enter room number: ");
            room = roomMap.get(existingRoom);
            if (room == null) {
                System.out.println("Cant find room, try again");
            }
        } while (room == null);

        return room.getRoomNumber();
    }

    public static void bookRoom() {
        // TODO ändra så metod kolla att input data går att använda direkt efter input
        bookRoomMenu();

        int addRoomNumber;
        int customerId;
        LocalDate checkInDate;

        Customer cust = CustomerHelper.searchAndSelectCustomerMenu();
        if (cust == null) {
            return;
        }
        customerId = cust.getId();
        addRoomNumber = getOnlyExistingRoomNumber();
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
            System.out.println("Booking added to hashMap");
        } else {
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
        getAvailableRooms(true).forEach(room -> {
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
                getAvailableRooms(true, RoomType.STANDARD_SINGLE).forEach(room -> {
                    if (room.getRenter() == null) {
                        System.out.println("Room #" + room.getRoomNumber() + ", Available");
                    }
                });
                break;
            case 2:
                getAvailableRooms(true, RoomType.STANDARD_DOUBLE).forEach(room -> {
                    if (room.getRenter() == null) {
                        System.out.println("Room #" + room.getRoomNumber() + ", Available");
                    }
                });
                break;
            case 3:
                getAvailableRooms(true, RoomType.LUXURY_SINGLE).forEach(room -> {
                    if (room.getRenter() == null) {
                        System.out.println("Room #" + room.getRoomNumber() + ", Available");
                    }
                });
                break;
            case 4:
                getAvailableRooms(true, RoomType.LUXURY_DOUBLE).forEach(room -> {
                    if (room.getRenter() == null) {
                        System.out.println("Room #" + room.getRoomNumber() + ", Available");
                    }
                });
                break;
            case 5:
                getAvailableRooms(true, RoomType.DELUXE_DOUBLE).forEach(room -> {
                    if (room.getRenter() == null) {
                        System.out.println("Room #" + room.getRoomNumber() + ", Available");
                    }
                });
                break;
        }
    }

    public static void upgradeRoom() {

        getAvailableRooms(false).forEach(room -> {
            if (room.getRenter() == null) {
                System.out.println("Room #" + room.getRoomNumber() + ", Unknown guest");
            } else {
                System.out.println("Room #" + room.getRoomNumber() + ", " + room.getRenter().getLastName());
            }
        });

        int currentRoomNumber = Input.askInt("Enter current room number: ");

        getAvailableRooms(true).forEach(room -> {
            if (room.getRenter() == null) {
                System.out.println("Room #" + room.getRoomNumber() + " " + room.getRoomType() + ", Available");
            } else {
                System.out.println("Room #" + room.getRoomNumber() + ", " + room.getRenter().getLastName());
            }
        });

        int upgradedRoomNumber = Input.askInt("Enter new room number: ");

        //TODO Badly handled nullpointerexception
        upgradeRoomDB(currentRoomNumber ,upgradedRoomNumber);
        upgradeRoomHM(currentRoomNumber, upgradedRoomNumber);

    }

    public static void upgradeRoomDB(int currentRoomNumber, int upgradedRoomNumber) {

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
        int bookingId = Input.askInt("Enter booking id for checkout: ");
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
