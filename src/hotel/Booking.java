package hotel;

public class Booking {

    private final int bookingId;
    private final int roomNumber;
    private final int customer;
    private final String checkInDate;
    private String checkOutDate;

    public Booking(int bookingId, int roomNumber, int customer, String checkInDate, String checkOutDate) {
        this.bookingId = bookingId;
        this.roomNumber = roomNumber;
        this.customer = customer;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Booking(int bookingId, int roomNumber, int customer, String checkInDate) {
        this.bookingId = bookingId;
        this.roomNumber = roomNumber;
        this.customer = customer;
        this.checkInDate = checkInDate;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getCustomer() {
        return customer;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    @Override
    public String toString() {
        return "Booking: Booking ID: " + bookingId + ", Room number: " + roomNumber + ", Customer: " + customer;
    }
}
