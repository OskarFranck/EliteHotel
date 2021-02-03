package hotel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoomHelper {

    public static void bookRoom() throws SQLException {

        int roomNumber = Input.askInt("Enter room number: ");
        int customerId = Input.askInt("Enter customer id: ");
        LocalDate checkInDate = LocalDate.now();

//        if (addBooking(roomNumber,customerId,checkInDate)) {
//            System.out.println("Booking added to system");
//        }
    }

    public static void upgradeRoom() {
        int bookingId = Input.askInt("Enter booking Id: ");
        int upgradedRoomNumber = Input.askInt("Enter new room number: ");

    }
    public static void orderFood() {

    }

    public static void checkOut() {
        //TODO Fråga efter kund/rums nummer
        //TODO Hämta booking från databas (Query till databas eller hämta hela tabellen och filtrera i java)
        //TODO Ropa på rumskvitto metod (från Bill class) med rumnummret och printa kvitto
        //TODO Ropa på databasen och sätt utcheckingsdatum

    }
    public void addRooms() {
        Room roomOne = new Room(101, RoomType.STANDARD_SINGLE);
        Room roomTwo = new Room(102, RoomType.STANDARD_SINGLE);
        Room roomThree = new Room(103, RoomType.STANDARD_DOUBLE);
        Room roomFour = new Room(104, RoomType.STANDARD_DOUBLE);
        Room roomFive = new Room(105, RoomType.STANDARD_DOUBLE);
        Room roomSix = new Room(206, RoomType.STANDARD_SINGLE);
        Room roomSeven = new Room(207, RoomType.STANDARD_DOUBLE);
        Room roomEight = new Room(208, RoomType.LUXURY_DOUBLE);
        Room roomNine = new Room(209, RoomType.LUXURY_DOUBLE);
        Room roomTen = new Room(210, RoomType.LUXURY_SINGLE);
        Room roomEleven = new Room(311, RoomType.LUXURY_SINGLE);
        Room roomTwelve = new Room(312, RoomType.LUXURY_DOUBLE);
        Room roomThirteen = new Room(313, RoomType.LUXURY_DOUBLE);
        Room roomFourteen = new Room(314, RoomType.DELUXE_DOUBLE);
        Room roomFifteen = new Room(315, RoomType.DELUXE_DOUBLE);

        List<Room> rooms = Arrays.asList(roomOne, roomTwo, roomThree, roomFour, roomFive, roomSix, roomSeven, roomEight, roomNine, roomTen, roomEleven, roomTwelve, roomThirteen, roomFourteen, roomFifteen);



    }
}
