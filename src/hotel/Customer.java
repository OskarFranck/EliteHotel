
package hotel;


public class Customer {
    
    private int id;
   // private int roomNr;
   // private String food;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    
    static int idGenerator = 1;
    
    public Customer() {
    }

    public Customer(String firstName, String lastName, String phoneNumber) {
        this.id = idGenerator;
        idGenerator ++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
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
        return "Customer: " + "Id: " + id + ",  First name: " + firstName + ", "
                + "Last name: " + lastName + ", Phone number: " + phoneNumber ;
    }
    
    
    
    
    
}
