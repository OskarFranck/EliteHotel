package hotel;

public class Room {

    private final int roomNumber;
    private final RoomType roomType;
    private boolean rented = false;
    private Customer renter = null;
    private Bill roomBill = null;

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

    public Bill getBill() { return roomBill; }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public void setRenter(Customer renter) {
        this.renter = renter;
    }

    public void setRoomBill(Bill bill) {
        this.roomBill = bill;
    }

}
