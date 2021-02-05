package hotel;

import static hotel.Input.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CustomerHelper {

    public static List<Customer> customers = new ArrayList<>();
    // public static List<Customer> custFromDB = new ArrayList<>();
    public static String firstName;
    public static String lastName;
    public static String phoneNumber;

    public static void loadCustomers() throws SQLException {
        try {
            ResultSet r = Database.getInstance().getAllCustomers();
            while (r.next()) {
                int dbId = r.getInt("customer.customerId");
                String dbFName = r.getString("customer.firstName");
                String dbLName = r.getString("customer.lastName");
                String dbPhoneNr = r.getString("customer.phoneNumber");
                Customer dbC = new Customer(dbId, dbFName, dbLName, dbPhoneNr);
                customers.add(dbC);               
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void registerNewCustomer() throws SQLException {
        boolean goOn = true;
        while (goOn) {
            boolean run = true;
            firstName = askString("Enter first name:"); //Lägga till kontroll endast bokstäver, eller iaf ej blankt?

            lastName = askString("Enter last name:");
            phoneNumber = askString("Enter phone number"); //Begränsning antal tecken? regex bara 0-9 och vissa tecken?

            Customer cust = new Customer(firstName, lastName, phoneNumber);
            customers.add(cust);
            //Behöver vi ha denna?
            System.out.println("New customer added to List Customers: " + cust.toString());
            System.out.println("Add another customer ? Y/N"); //Kontroll finns att bara Y eller N.
            while (run) {

                String choice = sc.nextLine();
                if (choice.equalsIgnoreCase("Y")) {
                    goOn = run;
                    run = false;
                } else if (choice.equalsIgnoreCase("N")) {
                    goOn = false;
                    run = false;
                } else if (!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n")) {
                    System.out.println("Please enter Y or N:");
                    run = true;
                }
            }
        }
    }

    public static void searchCustomerLastName() throws SQLException {

        if (customers.isEmpty()) {
            System.out.println("No customers in register JavaAppl.");
        } else {
            System.out.println("Serch for customer");
            System.out.println("Enter last name: ");
            lastName = sc.nextLine();
            customers.stream().filter(c -> c.getLastName().equalsIgnoreCase(lastName)).forEach(System.out::println);
        }
    }

    public static void listAllCustomers() throws SQLException {
        if (customers.isEmpty()) {
           System.out.println("No customers in register. \n");
         } else {
        customers.stream().forEach(System.out::println);
        }
    }
    
    public static void searchCustomerID() {
        System.out.println("Serch for customer");
        int ID = askInt("To search for customer, please enter customer ID");
        if (customers.isEmpty()) {
            System.out.println("No customers in register.");
            System.out.println("");
        } else //finns bara en matchning, något annat bättre sätt?
        {
            customers.stream().filter(c -> c.getId() == ID).forEach(System.out::println);
        }
    }

    public static void updateCustomer() {
        System.out.println("Update customer");
        int ID = askInt("Enter customer ID");

        System.out.println("");
        System.out.println("1. Change first name");
        System.out.println("2. Change last name");
        System.out.println("3. Change phone number");
        System.out.println("");

        int choice = Input.askInt("Choose from menu to continue");

        switch (choice) {

            case 1:
                String fname = askString("Enter new first name");
                for (Customer c : customers) {
                    if (c.getId() == ID) { //använda exeption ?
                        c.setFirstName(fname);
                    }
                }
                break;
            case 2:
                String lname = askString("Enter new last name");
                for (Customer c : customers) {
                    if (c.getId() == ID) { //använda exeption ?
                        c.setLastName(lname);
                    }
                }
                break;
            case 3:
                String number = askString("Enter new phone number");
                for (Customer c : customers) {
                    if (c.getId() == ID) { //använda exeption ?
                        c.setPhoneNumber(number);
                    }
                }
                break;
            default:
                System.out.println("Wrong input");
        }
    }

    public static void deleteCustomer() throws SQLException {      
        int ID = askInt("To delete customer, enter customer ID:");
        if (customers.isEmpty()) {
            System.out.println("No customers in register.");
        } else {            
            customers.removeIf(c -> c.getId() == ID);
            Database.getInstance().deleteCustomer(ID);        
        }
    }

    public static void customersToDatabase() throws SQLException {

        for (Customer c : customers) {
            Database.getInstance().addCustomer(c.getId(), c.getFirstName(), c.getLastName(), c.getPhoneNumber());
        }
    }
}

