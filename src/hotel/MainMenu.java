package hotel;

public class MainMenu {

    public static void EmployeeMenu() {
        System.out.println("Start menu Elite Hotel\n");

        int choice;
        boolean run = true;
        do {
            System.out.println("1. Receptionist");
            System.out.println("2. Customer");
            System.out.println("3. Exit program\n");

            choice = Input.askInt("Choose from menu to continue");

            switch (choice) {
                case 1:
                    receptionistMenu();
                    break;
                case 2:
                    customerMenu();
                    break;
                default:
                    run = false;
                    break;
            }
        } while (choice < 1 || choice > 3 || run);
    }
    private static void receptionistMenu() {
        System.out.println("Employee menu\n");

        int choice;
        boolean run = true;
        do {
            System.out.println("1. Register new customer");
            System.out.println("2. Handle customers");
            System.out.println("3. Book/upgrade room");
            System.out.println("4. Order food");
            System.out.println("5. Checkout");
            System.out.println("6. Show bill");
            System.out.println("7. Back to main menu\n");

            choice = Input.askInt("Choose for menu to continue");

            switch (choice) {
                case 1:
                    registerNewCustomer();
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
        } while (choice < 1 || choice > 7 || run);

    }
    private static void registerNewCustomer() {

    }
    private static void bookOrUpgradeRoom() {

    }
    private static void showBill() {

    }
    private static void handleCustomers() {
        System.out.println("Handle customers\n");

        int choice;
        boolean run = true;
        do {
            System.out.println("1. Search customer");
            System.out.println("2. Update customer info");
            System.out.println("3. Delete customer");
            System.out.println("4. Back to employee menu\n");

            choice = Input.askInt("Choose from menu to continue");

            switch (choice) {
                case 1:
                    searchCustomer();
                    break;
                case 2:
                    updateCustomer();
                    break;
                case 3:
                    deleteCustomer();
                    break;
                default:
                    run = false;
                    break;
            }
        } while (choice < 1 || choice > 4 || run);
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
            System.out.println("5. Back to main menu\n");

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
        } while (choice < 1 || choice > 5 || run);
    }
    private static void showRooms() {

    }
    private static void bookRoom() {

    }
    private static void orderFood() {

    }
    private static void checkout() {

    }
}
