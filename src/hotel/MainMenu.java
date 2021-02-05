package hotel;

import java.sql.SQLException;
import hotel.CustomerHelper.*;
public class MainMenu {

    public static void mainMenu() throws SQLException{
        System.out.println("Start menu Elite Hotel\n");
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
    private static void receptionistMenu() throws SQLException{
        System.out.println("Receptionist menu\n");

        int choice;
        boolean run = false; //ändrat från true
        do {
            System.out.println("1. Register new customer");
            System.out.println("2. Handle customers");
            System.out.println("3. Book room");
            System.out.println("4. Upgrade room");
            System.out.println("5. Order food");
            System.out.println("6. Checkout");
            System.out.println("7. Show bill");
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
                    RoomHelper.bookRoom();
                    break;
                case 4:
                    RoomHelper.upgradeRoom();
                    break;
                case 5:
                    orderFood(selectRoomForFoodOrder());
                    break;
                case 6:
                    RoomHelper.checkOut();
                    break;
                case 7:
                    showBill();
                    break;
                default:
                    run = false;
                    break;
            }
        } while (choice < 0 || choice > 7 || run);

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

    private static void customerMenu() throws SQLException {
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
                    RoomHelper.bookRoom();
                    break;
                case 3:
                    orderFood(selectRoomForFoodOrder());
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
    private static void orderFood(Room room) {
        if (room == null) { return; } // Jump out if no room was selected previous
        int roomNumber = room.getRoomNumber();

        // TODO - Fix "Bill to Room" connection and sort adding items to the bill given a Room object.

        System.out.println("Order food menu\n");
        int choice;
        boolean run = true;
        do {
            System.out.println("1. Order a Sandwich - (" + Food.FoodMenuItem.SANDWICH.getPrice() + " kr)");
            System.out.println("2. Order Pasta - (" + Food.FoodMenuItem.PASTA.getPrice() + " kr)");
            System.out.println("3. Order Noodles - (" + Food.FoodMenuItem.NOODLES.getPrice() + " kr)");
            System.out.println("4. Order Drink - (" + Food.FoodMenuItem.DRINK.getPrice() + " kr)");
            System.out.println("5. Go back to main menu\n");

            choice = Input.askInt("Choose from menu to continue");

            switch (choice) {
                case 1:
                    System.out.println("A sandwich has been ordered for Room #" + roomNumber + ".\n");
                    // TODO - Add a sandwich object to the customer bill
                    break;
                case 2:
                    System.out.println("A pasta dish has been ordered for Room #" + roomNumber + ".\n");
                    // TODO - Add a pasta object to the customer bill
                    break;
                case 3:
                    System.out.println("A noodles has been ordered for Room #" + roomNumber + ".\n");
                    // TODO - Add a noodle object to the customer bill
                    break;
                case 4:
                    System.out.println("A drink has been ordered for Room #" + roomNumber + ".\n");
                    // TODO - Add a drink object to the customer bill
                    break;
                default:
                    run = false;
                    break;
            }
        } while (choice < 1 || choice > 5 || run);
    }

    private static Room selectRoomForFoodOrder() {
        System.out.println("Select your room first for food order");

        while (true) {
            System.out.println("\n1. Enter your room number");
            System.out.println("2. View all currently booked rooms");
            System.out.println("0. Cancel and go back");
            switch (Input.getUserInputInt()) {
                case 1:
                    int inputNumber = Input.askInt("Room number: ");
                    if (RoomHelper.getRoomMap().get(inputNumber) == null) {
                        System.err.println("Room #" + inputNumber +" does not exists. Please try again!");
                        break;
                    }
                    return RoomHelper.getRoomMap().get(inputNumber);
                case 2:
                    System.out.println("\nAll currently booked rooms:");
                    RoomHelper.getAvailableRooms(false).forEach(room -> {
                        if (room.getRenter() == null) {
                            System.out.println("Room #" + room.getRoomNumber() + ", Unknown guest");
                        } else {
                            System.out.println("Room #" + room.getRoomNumber() + ", " + room.getRenter().getLastName());
                        }
                    });
                    break;
                case 0:
                    return null;
            }
        }
    }

    private static void checkout() {

    }
}
