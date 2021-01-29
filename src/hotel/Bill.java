package hotel;

import java.util.ArrayList;

public class Bill {

    final private ArrayList<Billable> sessionBill = new ArrayList<>();

    public void add(Billable service) {
        sessionBill.add(service);
    }

    public boolean remove(Billable service) {
        try {
            sessionBill.remove(service);
            return true;
        } catch (NullPointerException e) {
            System.out.println(service.toString() + " could not be found on the bill.");
            return false;
        }
    }

    public ArrayList<Billable> getSessionBill() {
        return sessionBill;
    }

    public int getTotal() {
        return sessionBill.stream().mapToInt(Billable::getPrice).sum();
    }

    private String billRow(String type, String name, int price) {
        return "# " + type + ": " + name + "    " + price + " kr";
    }

    public void printBill() {
        System.out.println("Customer bill");
        sessionBill.forEach(item -> System.out.println(billRow(item.getServiceType(), item.toString(), item.getPrice())));
        System.out.println("\n# Total: " + getTotal() + " kr");
    }

}
