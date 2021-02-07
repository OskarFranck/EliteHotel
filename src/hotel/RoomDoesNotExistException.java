package hotel;

public class RoomDoesNotExistException extends Exception {
    public RoomDoesNotExistException(String errorMessage) {
        super(errorMessage);
    }
}