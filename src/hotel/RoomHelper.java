package hotel;

public class RoomHelper {

    public static class RoomNotVacantException extends Exception {
        public RoomNotVacantException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class RoomVacantException extends Exception {
        public RoomVacantException(String errorMessage) {
            super(errorMessage);
        }
    }

    private static void addRoom() {

    }
    private static void checkRoomExists() {

    }
}
