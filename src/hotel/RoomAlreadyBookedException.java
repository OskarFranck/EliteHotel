package hotel;

public class RoomAlreadyBookedException extends Exception {
    public RoomAlreadyBookedException(String errorMessage) {
        super(errorMessage);
    }
}
