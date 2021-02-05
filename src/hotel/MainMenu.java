package hotel;

import java.sql.SQLException;
import hotel.CustomerHelper.*;
public class MainMenu {

    public static void EmployeeMenu() throws SQLException {
        System.out.println("Start menu Elite Hotel\n");
        CustomerHelper.loadCustomers();
        int choice;
        boolean run = true; 
        do {
            System.out.println("1. Receptionist");
            System.out.println("2. Customer");
            System.out.println("0. Exit program\n");

            choice = Input.askInt("Choose from menu to continue");

            switch (choice) {
                case 1:
                    receptionistMenu();
                    break;
                case 2:
                    customerMenu();
                    break;
                case 0:
                    CustomerHelper.customersToDatabase();
                    System.out.println("Tack och välkommen åter");
                    run = false;
                    break;   
                default:
                    run = false; 
                    break;
            }
        } while (choice < 0 || choice > 2 || run); //info tex felaktig inmating
    }
    
    private static void receptionistMenu() throws SQLException {
        System.out.println("Employee menu\n");

        int choice;
        boolean run = false; //ändrat från true
        do {
            System.out.println("1. Register new customer");
            System.out.println("2. Handle customers");
            System.out.println("3. Book/upgrade room");
            System.out.println("4. Order food");
            System.out.println("5. Checkout"); 
            System.out.println("6. Show bill");
            System.out.println("0. Back to main menu\n");

            choice = Input.askInt("Choose for menu to continue");

            switch (choice) {
                case 1:
                    CustomerHelper.registerNewCustomer();
                    break;
                case 2:
                    handleCustomers();
                    break;
                case 3:
                    bookOrUpgradeRoom();
                    break;
                case 4:
                    orderFood();
                    break;
                case 5:
                    checkout();
                    break;
                case 6:
                    showBill();
                    break;
                default:
                    run = false;
                    break;
            }
        } while (choice < 0 || choice > 6 || run);

    }
    private static void registerNewCustomer() {
        
    }
    private static void bookOrUpgradeRoom() {

    }
    private static void showBill() {

    }
    private static void handleCustomers() throws SQLException {
        System.out.println("Handle customers\n");

        int choice;
        boolean run = true;
        do {
            System.out.println("1. Search customer");
            System.out.println("2. List all customers");
            System.out.println("3. Update customer info");
            System.out.println("4. Delete customer");
            System.out.println("0. Back to employee menu\n");

            choice = Input.askInt("Choose from menu to continue");

            switch (choice) {
                case 1:
                    CustomerHelper.searchCustomerID();
                    break;
                case 2:
                    CustomerHelper.listAllCustomers();
                   // Database.getInstance().getStartingPointIdGenerator();
                    break;
                case 3:
                    CustomerHelper.updateCustomer();
                    break;
                 case 4:
                    CustomerHelper.deleteCustomer();
                    break;   
                default:
                    run = false;
                    break;
            }
        } while (choice < 0 || choice > 4 || run);
    }
    private static void searchCustomer() {

    }
    private static void updateCustomer() {

    }
    private static void deleteCustomer() {

    }

    private static void customerMenu() {
        System.out.println("Customer menu\n");

        int choice;
        boolean run = true;
        do {
            System.out.println("1. Show rooms");
            System.out.println("2. Book room");
            System.out.println("3. Order food");
            System.out.println("4. Checkout");
            System.out.println("0. Back to main menu\n");

            choice = Input.askInt("Choose from menu to continue");

            switch (choice) {
                case 1:
                    showRooms();
                    break;
                case 2:
                    bookRoom();
                    break;
                case 3:
                    orderFood();
                    break;
                case 4:
                    checkout();
                    break;
                default:
                    run = false;
                    break;
            }
        } while (choice < 0 || choice > 4 || run);
    }
    private static void showRooms() {

    }
    private static void bookRoom() {

    }
    private static void orderFood() {
        // TODO - Select Room / Customer first
        System.out.println("Order food menu\n");
        int choice;
        boolean run = true;
        do {
            System.out.println("1. Sandwich - (" + Food.FoodMenuItem.SANDWICH.getPrice() + " kr)");
            System.out.println("2. Pasta - (" + Food.FoodMenuItem.PASTA.getPrice() + " kr)");
            System.out.println("3. Noodles - (" + Food.FoodMenuItem.NOODLES.getPrice() + " kr)");
            System.out.println("4. Drink - (" + Food.FoodMenuItem.DRINK.getPrice() + " kr)");
            System.out.println("5. Back to main menu\n");

            choice = Input.askInt("Choose from menu to continue");

            switch (choice) {
                case 1:
                    System.out.println("A sandwich has been added to the bill.");
                    // TODO - Add a sandwich object to the customer bill
                    break;
                case 2:
                    System.out.println("A pasta dish has been added to the bill.");
                    // TODO - Add a pasta object to the customer bill
                    break;
                case 3:
                    System.out.println("A noodles has been added to the bill.");
                    // TODO - Add a noodle object to the customer bill
                    break;
                case 4:
                    System.out.println("A drink has been added to the bill.");
                    // TODO - Add a drink object to the customer bill
                    break;
                default:
                    run = false;
                    break;
            }
        } while (choice < 1 || choice > 5 || run);
    }
    private static void checkout() {

    }
}
