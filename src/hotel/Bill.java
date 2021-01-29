package hotel;

import java.util.ArrayList;

public class Bill {

    final private ArrayList<Billable> billItems = new ArrayList<>();

    public void add(Billable service) {
        billItems.add(service);
    }

    public boolean remove(Billable service) {
        try {
            billItems.remove(service);

            return true;
        } catch (NullPointerException e) {
            System.out.println(service.toString() + " could not be found on the bill.");
            return false;
        }
    }

    public ArrayList<Billable> getBillItems() {
        return billItems;
    }

    public int getTotal() {
        return billItems.stream().mapToInt(Billable::getPrice).sum();
    }

    private String billRow(String type, String name, int price) {
        return "# " + type + ": " + name + "    " + price + " kr";
    }

    public void printBill() {
        System.out.println("Customer bill");
        billItems.forEach(item -> System.out.println(billRow(item.getServiceType(), item.toString(), item.getPrice())));
        System.out.println("\n# Total: " + getTotal() + " kr");
    }

}
