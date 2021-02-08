package hotel;

import java.io.Serializable;

public class Receipt implements Serializable {

    private int dailyCharge;
    private int nightsStayed;
    private int total;
    private String services;
    private int billedServices;

    public Receipt() {
    }

    public Receipt(int dailyCharge, int nightsStayed, int total, String services, int billedServices) {
        this.dailyCharge = dailyCharge;
        this.nightsStayed = nightsStayed;
        this.total = total;
        this.services = services;
        this.billedServices = billedServices;
    }

    public int getDailyCharge() {
        return dailyCharge;
    }

    public void setDailyCharge(int dailyCharge) {
        this.dailyCharge = dailyCharge;
    }

    public int getNightsStayed() {
        return nightsStayed;
    }

    public void setNightsStayed(int nightsStayed) {
        this.nightsStayed = nightsStayed;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public int getBilledServices() {
        return billedServices;
    }

    public void setBilledServices(int billedServices) {
        this.billedServices = billedServices;
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "dailyCharge=" + dailyCharge +
                ", nightsStayed=" + nightsStayed +
                ", total=" + total +
                ", services='" + services + '\'' +
                ", billedServices=" + billedServices +
                '}';
    }
}
