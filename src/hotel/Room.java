package hotel;

public class Room {

    private final int roomNumber;
    private final RoomType roomType;
    private boolean rented = false;
    private Customer renter = null;

    Room(int roomNumber, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public boolean getRented() {
        return rented;
    }

    public Customer getRenter() {
        return renter;
    }

}