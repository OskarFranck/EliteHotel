package hotel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RestoreFromDatabase {

    public static void restoreAll(boolean debug) {
        restoreRooms(debug);
        restoreRoomBookingStatus(debug);
        restoreAllBills(debug);
    }

    private static void restoreRooms(boolean debug) {
        int restored = 0;
        try {
            ResultSet resultSet = Database.getInstance().getAllRooms();
            while (resultSet.next()) {
                Room room = new Room(resultSet.getInt("roomNumber"), RoomType.valueOf(resultSet.getString("roomType")));
                RoomHelper.addRoomToMap(room);
                restored++;
            }
        } catch (SQLException e) {
            System.err.println("Error: Could not read rooms from database!");
            e.printStackTrace();
        }
        if (debug) { System.out.println("RESTORE: " + restored + " rooms restored from database"); }
    }

    public static void restoreRoomBookingStatus(boolean debug) {
        int restored = 0;
        int skipped = 0;
        try {
            ResultSet resultSet = Database.getInstance().getAllBookings();
            while(resultSet.next()) {
                int bookingId = resultSet.getInt("bookingId");
                int roomNumber = resultSet.getInt("roomNumber");

                if (resultSet.getString("checkInDate") != null && resultSet.getString("checkOutDate") == null) {

                    Room room = RoomHelper.getRoomMap().get(roomNumber);
                    if (room == null) {
                        System.err.println("Warning: When loading bookings - room #" + roomNumber + " exists in the database, but not in the current program. Skipping loading booking #" + bookingId);
                        continue;
                    } else {
                        room.setRented(true);
                    }

                    int customerId = resultSet.getInt("customerId");
                    Customer customer = CustomerHelper.customers.stream().filter(customers -> customers.getId() == customerId).findFirst().orElse(null);
                    if (customer == null) {
                        System.err.println("Warning: Could not restore booking for customer id #" + customerId + ". Not found in customer list.");
                    }
                    room.setRenter(customer);
                } else {
                    skipped++;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: Could not read expected data from database!");
            e.printStackTrace();
        }
        if (debug) { System.out.println("RESTORE: " + restored + " active bookings restored from database, " + skipped + " old non-active bookings ignored."); }
    }

    public static void restoreAllBills(boolean debug) {
        int restored = 0;
        try {
            ResultSet resultSet = Database.getInstance().getAllBills();
            while (resultSet.next()) {
                // Check if bill has been marked as "complete", then ignore restoring
                if (resultSet.getString("complete") == null || !resultSet.getBoolean("complete")) {
                    Database.getInstance().restoreSingleBill(resultSet.getInt("billId"));
                    restored++;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: Could not get bill data from database");
        }
        if (debug) { System.out.println("RESTORE: " + restored + " active bills restored from database"); }
    }

}
