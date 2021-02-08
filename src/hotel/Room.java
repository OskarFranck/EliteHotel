package hotel;

public class Room implements Printable {

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

    public boolean isRented() {
        return rented;
    }

    public Customer getRenter() {
        return renter;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public void setRenter(Customer renter) {
        this.renter = renter;
    }

    @Override
    public void printToConsole() {
        System.out.println("Room: #" + roomNumber + " " + getRoomType().print() + ((rented) ? ", rented to #" + getRenter().getId() + " " + getRenter().getFullName() + "." : ", available room."));
    }

}
