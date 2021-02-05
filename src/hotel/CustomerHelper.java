package hotel;

import static hotel.Input.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class CustomerHelper {

    public static List<Customer> customers = new ArrayList<>();
    // public static List<Customer> custFromDB = new ArrayList<>();
    public static String firstName;
    public static String lastName;
    public static String phoneNumber;

    public static void registerNewCustomer() throws SQLException {
        boolean goOn = true;
        while (goOn) {
            boolean run = true;
            firstName = askString("Enter first name:"); //Lägga till kontroll endast bokstäver, eller iaf ej blankt?

            lastName = askString("Enter last name:");
            phoneNumber = askString("Enter phone number"); //Begränsning antal tecken? regex bara 0-9 och vissa tecken?

            Customer cust = new Customer(firstName, lastName, phoneNumber);
            //customers.add(cust); // Görs nu direkt i Customer-klassen i constructorn
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

    public static Customer getCustomer(int id) {
        return customers.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    public static ArrayList<Customer> findCustomersMatchingName(String name) {
        return customers.stream()
                .filter(c -> c.getFullName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<Customer> findCustomersMatchingPhoneNumber(String phoneNumber) {
        return customers.stream()
                .filter(c -> c.getPhoneNumber().contains(phoneNumber))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static Customer searchAndSelectCustomerMenu() {
        System.out.println("Search and select customer to continue\n");

        int choice;
        boolean run = true;
        do {
            System.out.println("1. Select by ID");
            System.out.println("2. Search and select by name");
            System.out.println("3. Search and select by phone number");
            System.out.println("0. Cancel and go back\n");

            choice = Input.askInt("Choose for menu to continue: ");

            switch (choice) {
                case 1:
                    Customer customer = selectCustomerByIDMenu();
                    if (customer != null) {
                        System.out.println("\n" + customer.getFullName() + " selected.\n");
                        return customer;
                    }
                    break;
                case 2:
                    Customer customerSearch = modularSearchAndSelectCustomerMenu(false);
                    if (customerSearch != null) {
                        System.out.println("\n" + customerSearch.getFullName() + " selected.\n");
                        return customerSearch;
                    }
                    break;
                case 3:
                    Customer customerSearchPhone = modularSearchAndSelectCustomerMenu(true);
                    if (customerSearchPhone != null) {
                        System.out.println("\n" + customerSearchPhone.getFullName() + " selected.\n");
                        return customerSearchPhone;
                    }
                    break;
                default:
                    run = false;
                    break;
            }
        } while (choice < 0 || choice > 7 || run);
        return null;
    }

    private static boolean stopPhrase(String inputString) {
        String input = inputString.toLowerCase();
        return (input.equals("0") || input.equals("exit") || input.equals("cancel") || input.equals("stop") || input.equals("back"));
    }

    private static Customer selectCustomerByIDMenu() {
        while (true) {
            System.out.println("\nSelect by ID. Enter 0 to cancel.");
            System.out.print("Customer ID: ");
            int input = Input.getUserInputInt();

            if (input == 0) { return null; } // User cancels

            Customer customer = CustomerHelper.getCustomer(input);
            if (customer == null) {
                System.err.println("No customer found with ID #" + input + ". Try again!");
            } else {
                return customer;
            }
        }
    }

    // Call with "byPhoneNumber" as true to search by phone number instead of by name
    private static Customer modularSearchAndSelectCustomerMenu(boolean byPhoneNumber) {
        String stringToken = "name";
        if (byPhoneNumber) { stringToken = "phone number"; };

        while (true) {
            System.out.println("\nSearch by "+stringToken+". Enter 0 to cancel");
            System.out.print("Customer "+stringToken+": ");
            String input = Input.getUserInputString();

            if (stopPhrase(input)) {  return null; } // User cancels

            ArrayList<Customer> results;
            if (!byPhoneNumber) {
                results = CustomerHelper.findCustomersMatchingName(input);
            } else {
                results = CustomerHelper.findCustomersMatchingPhoneNumber(input);
            }

            if (results.size() == 0) {
                System.err.println("No "+stringToken+"s found with \"" + input + "\". Try again!");
            } else if (results.size() == 1) {
                System.out.print("One match found: ");
                if (!byPhoneNumber) {
                    System.out.println(results.get(0).getFullName());
                } else {
                    System.out.println(results.get(0).getPhoneNumber() + ", " + results.get(0).getFullName());
                }
                System.out.print("Continue with this selection? (Y/N): ");
                while (true) {
                    String inputYesNO = Input.getUserInputString();
                    if (inputYesNO.equalsIgnoreCase("y") || inputYesNO.equalsIgnoreCase("yes")) {
                        return results.get(0);
                    } else if (inputYesNO.equalsIgnoreCase("n") || inputYesNO.equalsIgnoreCase("no")) {
                        System.out.println("Selection canceled. Will restart search.");
                        break;
                    }
                    System.out.println("Unknown command. Please enter \"yes\" or \"no\".");
                }

            } else {
                System.out.println("Multiple "+stringToken+"s found matching search:");
                for (int i = 0; i < results.size(); i++) {
                    if (!byPhoneNumber) {
                        System.out.println(i+1 + ". " + results.get(i).getFullName());
                    } else {
                        System.out.println(i+1 + ". " + results.get(i).getPhoneNumber() + ", " + results.get(i).getFullName());
                    }
                }
                while (true) {
                    System.out.print("Select: ");
                    int inputSelection = Input.getUserInputInt();
                    if (inputSelection < 1 || inputSelection > results.size()) {
                        System.err.println("No selection with that number.");
                        continue;
                    }
                    return results.get((inputSelection-1));
                }
            }
        }
    }
}

