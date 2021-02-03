package hotel;

import static hotel.Input.*;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collector;
import static java.util.stream.Collectors.toList;

public class CustomerHelper {
//    static Database db;
//        static {
//            try{
//                db = new Database("elitehotel", DatabaseCredentials.databaseUser, DatabaseCredentials.databasePassword);
//            }catch (SQLException throwable) {
//                throwable.printStackTrace();
//            }
//        }
//        
//        
    //public static Set<Customer> Customers = new HashSet<>(); //måste konvertera för att använda stream
    public static List<Customer> customers = new ArrayList<>();
    public static String firstName;
    public static String lastName;
    public static String phoneNumber;
    public static int ID;
    

    public static void registerNewCustomer() {
        boolean goOn = true;

        while (goOn) {
            boolean run = true;
            firstName = askString("Enter first name:"); //Lägga till kontroll endast bokstäver, eller iaf ej blankt?

            lastName = askString("Enter last name:");
            phoneNumber = askString("Enter phone number"); //Begränsning antal tecken? regex bara 0-9 och vissa tecken?
            Customer cust = new Customer(firstName, lastName, "123456");//obs! Hårdkodat just nu!
            customers.add(cust);
            System.out.println("New customer added: " + cust.toString());
           
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
                    //goOn = true;
                    run = true;
                }
            }
        }
        
    }

    public static void searchCustomerLastName() {

        if (customers.isEmpty()) {
            System.out.println("No customers in register.");
        } else {
            System.out.println("Serch for customer");
            System.out.println("Enter last name: ");
            lastName = sc.nextLine();
            customers.stream().filter(c -> c.getLastName().equalsIgnoreCase(lastName)).forEach(System.out::println);
        }
    }
    
    public static void listAllCustomers (){
         if (customers.isEmpty()) {
            System.out.println("No customers in register. \n");
            
        } else {
    customers.stream().forEach(System.out::println);
             System.out.println("");
        }
    }

    public static void searchCustomerID() {
        System.out.println("Serch for customer");
        ID = askInt("To search for customer, please enter customer ID");
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
        ID = askInt("Enter customer ID");
       
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
    
    public static void deleteCustomer(){   
            //System.out.println("Delete customer, ");
            ID = askInt("To delete customer, enter customer ID:");
                if (customers.isEmpty()) {
                    System.out.println("No customers in register.");
                } else {//finns bara en matchning, något annat bättre sätt?           
               // customers.stream().filter(c -> c.getId() == ID).forEach(c ->, removeif);
                customers.removeIf(c -> c.getId() == ID);
                
            }
                
                
    }
    
}
