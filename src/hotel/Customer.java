package hotel;

import java.sql.SQLException;

public class Customer implements Printable {

    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    static int idGen;
    
    //Denna konstruktor används när customers hämtas från databasen
    public Customer(int dbId, String dbFName, String dbLName,String dbPhoneNr){
        this.id = dbId;
        this.firstName = dbFName;
        this.lastName = dbLName;
        this.phoneNumber = dbPhoneNr;
    }
    
    //Den här konstruktorn används när nya customers skapas från menyval
    //Hämtar högsta kundId:t från DB och använder som startpunkt till idgeneratorn.
    public Customer(String firstName, String lastName, String phoneNumber) {
        ++idGen;
        this.id = idGen;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;

        // Lägg till customer i listan
        CustomerHelper.customers.add(this);
        try {
            Database.getInstance().addCustomer(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Customer: " + "Id: " + id + ", First name: " + firstName + ", "
                + "Last name: " + lastName + ", Phone number: " + phoneNumber ;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public void printToConsole() {
        System.out.println("Customer: #" + id + " " + getFullName() + ", phone number: " + phoneNumber);
    }

}
