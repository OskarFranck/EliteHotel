package hotel;

import java.io.*;
import java.util.Scanner;

public class Receipt implements Serializable {

    private int dailyCharge;
    private int nightsStayed;
    private int total;
    private String services;
    private int servicesBilled;

    public Receipt() {
    }


    public Receipt(int dailyCharge, int nightsStayed, int total, String services, int billedServices) {
        this.dailyCharge = dailyCharge;
        this.nightsStayed = nightsStayed;
        this.total = total;
        this.services = services;
        this.servicesBilled = billedServices;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "dailyCharge=" + dailyCharge +
                ", nightsStayed=" + nightsStayed +
                ", total=" + total +
                ", services='" + services + '\'' +
                ", billedServices=" + servicesBilled +
                '}';
    }

    public static void readReceipt() throws IOException{
        File file = new File("receipt.txt");
        Scanner sc = new Scanner(file);

        while (sc.hasNext()) {
            System.out.println(sc.nextLine());
        }
    }

    public static void writeReceipt(int roomNumber, int customerId) throws IOException{
        String name = RoomHelper.printAllStoredBills(roomNumber, customerId);

        File file = new File("receipt.txt");
        FileWriter writer = new FileWriter(file, true);
        writer.write(System.lineSeparator() + name);
        writer.flush();
        writer.close();

    }

}
