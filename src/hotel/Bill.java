package hotel;

import java.sql.SQLException;
import java.util.ArrayList;

public class Bill {

    final private ArrayList<BillableService> billItems = new ArrayList<>();
    private int roomNumber;
    private boolean completed = false;

    public Bill(int roomNumber) {
        this.roomNumber = roomNumber;
        try {
            Database.getInstance().newBill(roomNumber);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void add(BillableService service) {
        billItems.add(service);
    }

    public boolean remove(BillableService service) {
        try {
            billItems.remove(service);

            return true;
        } catch (NullPointerException e) {
            System.out.println(service.toString() + " could not be found on the bill.");
            return false;
        }
    }

    public ArrayList<BillableService> getBillItems() {
        return billItems;
    }

    public int getBillableItemsTotal() {
        return billItems.stream().mapToInt(BillableService::getPrice).sum();
    }

    private String billRow(String type, String name, int price) {
        return "# " + type + ": " + name + "    " + price + " kr";
    }

    public void printBill() {

        // TODO (Oscar) - Beräkna rumskostnad baserat på hur många dagar dem bott vid checkout

        System.out.println("Customer bill");
        billItems.forEach(item -> System.out.println(billRow(item.getServiceType(), item.toString(), item.getPrice())));
        System.out.println("\n# Total: " + getBillableItemsTotal() + " kr");
    }

}
