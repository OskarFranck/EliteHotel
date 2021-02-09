package hotel;

import static hotel.Input.*;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class CustomerHelper {

    public static List<Customer> customers = new ArrayList<>();
    public static String firstName;
    public static String lastName;
    public static String phoneNumber;

    public static void registerNewCustomer() {
        while (true) {
            System.out.println(Main.printBold("\nAdd new customer"));
            firstName = askString("Enter first name: ");
            if (firstName.equals("0")) { return; } // exit code

            lastName = askString("Enter last name: ");
            if (lastName.equals("0")) { return; } // exit code

            phoneNumber = askString("Enter phone number: ");

            Customer cust = new Customer(firstName, lastName, phoneNumber);
            System.out.println("New customer added to List Customers: " + cust.toString());

            while (true) {
                String choice = Input.askString("\nAdd another customer? (Y/N): ");
                if (choice.equalsIgnoreCase("Y")) {
                    break; // Restart mainLoop
                } else if (choice.equalsIgnoreCase("N")) {
                    return; // Stop method
                } else if (!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n")) {
                    System.out.println("Unknown input. Try again!");
                }
            }
        }
    }

    public static void listAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("No customers in register. \n");
        } else {
            customers.stream().forEach(System.out::println);
        }
    }

    public static void updateCustomer(Customer customer) {
        System.out.println("Update customer:");
        System.out.println(customer);

        while (true) {
            System.out.println("\n1. Change first name");
            System.out.println("2. Change last name");
            System.out.println("3. Change phone number");
            System.out.println("0. Go back\n");
            int choice = Input.askInt("Choose from menu to continue: ");
            switch (choice) {
                case 1:
                    customer.setFirstName(askString("Enter new first name: "));
                    break;
                case 2:
                    customer.setLastName(askString("Enter new last name: "));
                    break;
                case 3:
                    customer.setPhoneNumber(askString("Enter new phone number: "));
                    break;
                case 0:
                    try {
                        Database.getInstance().updateCustomer(customer);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return;
                default:
                    System.out.println("Wrong input. Try again!");
            }
        }
    }

    public static void deleteCustomer(Customer customer) {
        Room room = RoomHelper.findCustomersRoom(customer);
        try {
            while (true) {
                String input = askString(Main.printBold("Do you really want to delete customer #" + customer.getId() + " " + customer.getFullName() + "? (Y/N): "));
                if (input.equalsIgnoreCase("y")) {
                    if (room == null) {
                        Database.getInstance().deleteCustomer(customer);
                        CustomerHelper.customers.remove(customer);
                        return;
                    } else {
                        System.out.println(Main.printBoldRed("Customer is booked to a room, can't delete customer until after checkout"));
                    }
                } else if (input.equalsIgnoreCase("n")) {
                    return;
                } else {
                    System.out.println("Unknown input. Try again!");
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
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
        int choice;
        boolean run = true;
        do {
            System.out.println(Main.printBold("\nSearch and select customer to continue"));
            System.out.println("1. Select by ID");
            System.out.println("2. Search and select by name");
            System.out.println("3. Search and select by phone number");
            System.out.println("0. Cancel and go back\n");

            choice = Input.askInt("Choose from menu to continue: ");

            switch (choice) {
                case 1:
                    Customer customer = selectCustomerByIDMenu();
                    if (customer != null) {
                        System.out.println(Main.printBold("\n" + customer.getFullName() + " selected.\n"));
                        return customer;
                    }
                    break;
                case 2:
                    Customer customerSearch = modularSearchAndSelectCustomerMenu(false);
                    if (customerSearch != null) {
                        System.out.println(Main.printBold("\n" + customerSearch.getFullName() + " selected.\n"));
                        return customerSearch;
                    }
                    break;
                case 3:
                    Customer customerSearchPhone = modularSearchAndSelectCustomerMenu(true);
                    if (customerSearchPhone != null) {
                        System.out.println(Main.printBold("\n" + customerSearchPhone.getFullName() + " selected.\n"));
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
            System.out.println(Main.printBold("\nSelect by ID. Enter 0 to cancel."));
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
            System.out.println(Main.printBold("\nSearch by "+stringToken+". Enter 0 to cancel"));
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
                System.out.print(Main.printBold("One match found: "));
                if (!byPhoneNumber) {
                    System.out.println(Main.printBold(results.get(0).getFullName()));
                } else {
                    System.out.println(Main.printBold(results.get(0).getPhoneNumber() + ", " + results.get(0).getFullName()));
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
                System.out.println(Main.printBold("\nMultiple "+stringToken+"s found matching search:"));
                for (int i = 0; i < results.size(); i++) {
                    if (!byPhoneNumber) {
                        System.out.println(i+1 + ". " + results.get(i).getFullName() + ", ID: " + results.get(i).getId());
                    } else {
                        System.out.println(i+1 + ". " + results.get(i).getPhoneNumber() + ", " + results.get(i).getFullName() + ", ID: " + results.get(i).getId());
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

