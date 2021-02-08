package hotel;

public class MainMenu {

    public static void mainMenu() {
        int choice;
        boolean run = true;
        do {
            System.out.println(Main.printBold("Start menu Elite Hotel"));
            System.out.println("1. Receptionist");
            System.out.println("2. Customer");
            System.out.println("0. Exit program\n");

            choice = Input.askInt("Choose from menu to continue: ");

            switch (choice) {
                case 1:
                    receptionistMenu();
                    break;
                case 2:
                    customerMenu();
                    break;
                case 0:
                    // CustomerHelper.customersToDatabase(); // Behövs inte längre
                    System.out.println("\nTack och välkommen åter!");
                    run = false;
                    break;
                default:
                    run = false;
                    break;
            }
        } while (choice < 0 || choice > 2 || run); //info tex felaktig inmating
    }

    private static void receptionistMenu() {
        int choice;
        boolean run = true;
        do {
            System.out.println(Main.printBold("\nReceptionist menu"));
            System.out.println("1. Register new customer");
            System.out.println("2. Handle customers");
            System.out.println("3. Book room");
            System.out.println("4. Upgrade or change room");
            System.out.println("5. Order food");
            System.out.println("6. Checkout");
            System.out.println("7. Show bill");
            System.out.println("0. Back to main menu\n");

            choice = Input.askInt("Choose form menu to continue: ");

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
                    orderFood(selectRoom("Select room to order food too: "));
                    break;
                case 6:
                    RoomHelper.checkOut(selectRoom("Select room to checkout: "));
                    break;
                case 7:
                   // showBill();
                    // TODO - Show bill from database or from hashmap
                    break;
                default:
                    run = false;
                    break;
            }
        } while (choice < 0 || choice > 7 || run);

    }

    private static void handleCustomers() {
        int choice;
        boolean run = true;
        do {
            System.out.println(Main.printBold("\nHandle customers"));
            System.out.println("1. Display customer details");
            System.out.println("2. List all customers");
            System.out.println("3. Update customer info");
            System.out.println("4. Delete customer");
            System.out.println("0. Back to employee menu\n");

            choice = Input.askInt("Choose from menu to continue: ");

            switch (choice) {
                case 1:
                    Customer displayCustomer = CustomerHelper.searchAndSelectCustomerMenu();
                    if (displayCustomer != null) {
                        System.out.println(Main.printBold("Customer details:"));
                        System.out.println("ID: " + displayCustomer.getId());
                        System.out.println("Name: " + displayCustomer.getFullName());
                        System.out.println("Phone number: " + displayCustomer.getPhoneNumber());
                        Room customerRoom = RoomHelper.findCustomersRoom(displayCustomer);
                        System.out.println("Currently checked-in: " + ((customerRoom != null) ? "Yes, Room #" + customerRoom.getRoomNumber() : "No"));
                    }
                    break;
                case 2:
                    CustomerHelper.listAllCustomers();
                    break;
                case 3:
                    Customer updateCustomer = CustomerHelper.searchAndSelectCustomerMenu();
                    if (updateCustomer != null) {
                        CustomerHelper.updateCustomer(updateCustomer);
                    }
                    break;
                case 4:
                    Customer deleteCustomer = CustomerHelper.searchAndSelectCustomerMenu();
                    if (deleteCustomer != null) {
                        CustomerHelper.deleteCustomer(deleteCustomer);
                    }
                    break;
                default:
                    run = false;
                    break;
            }
        } while (choice < 0 || choice > 4 || run);
    }

    private static void customerMenu() {
        System.out.println(Main.printBold("Customer menu\n"));

        int choice;
        boolean run = true;
        do {
            System.out.println("1. Show rooms");
            System.out.println("2. Book room");
            System.out.println("3. Order food");
            System.out.println("4. Checkout");
            System.out.println("0. Back to main menu\n");

            choice = Input.askInt("Choose from menu to continue: ");

            switch (choice) {
                case 1:
                    showRooms();
                    break;
                case 2:
                    RoomHelper.bookRoom();
                    break;
                case 3:
                    orderFood(selectRoom("Select room to order food too: "));
                    break;
                case 4:
                    RoomHelper.checkOut(selectRoom("Select room to checkout: "));
                    break;
                default:
                    run = false;
                    break;
            }
        } while (choice < 0 || choice > 4 || run);
    }

    private static void showRooms() {

        for (RoomType type: RoomType.values()) {
            System.out.println(Main.printBold("\n" + type.print()));
            System.out.println("Bed: " + type.typeOfBed() + " size");
            System.out.println("No. of beds: " + type.getNumberOfBeds());
            System.out.println("Has AC: " + ((type.hasAC()) ? "Yes" : "No"));
            System.out.println("Breakfast included: " + ((type.isBreakfastIncluded()) ? "Yes" : "No"));
            System.out.println("Daily charge: " + type.getDailyCharge() + " SEK");
        }
        System.out.println("");
    }

    private static void orderFood(Room room) {
        if (room == null) {
            return;
        } // Jump out if no room was selected previous
        int roomNumber = room.getRoomNumber();
        Bill roomBill = RoomHelper.getActiveBillMap().get(room.getRoomNumber());
        if (roomBill == null) {
            System.err.println("Error: room has no bill - something has gone very wrong!");
            return;
        }

        System.out.println("Order food menu\n");
        int choice;
        boolean run = true;
        do {
            System.out.println("1. Order a Sandwich - (" + Food.FoodMenuItem.SANDWICH.getPrice() + " kr)");
            System.out.println("2. Order Pasta - (" + Food.FoodMenuItem.PASTA.getPrice() + " kr)");
            System.out.println("3. Order Noodles - (" + Food.FoodMenuItem.NOODLES.getPrice() + " kr)");
            System.out.println("4. Order Drink - (" + Food.FoodMenuItem.DRINK.getPrice() + " kr)");
            System.out.println("0. Go back to main menu\n");

            choice = Input.askInt("Choose from menu to continue: ");

            switch (choice) {
                case 1:
                    System.out.println("A sandwich has been ordered for Room #" + roomNumber + ".\n");
                    roomBill.add(new Food(Food.FoodMenuItem.SANDWICH));
                    break;
                case 2:
                    System.out.println("A pasta dish has been ordered for Room #" + roomNumber + ".\n");
                    roomBill.add(new Food(Food.FoodMenuItem.PASTA));
                    break;
                case 3:
                    System.out.println("A noodles has been ordered for Room #" + roomNumber + ".\n");
                    roomBill.add(new Food(Food.FoodMenuItem.NOODLES));
                    break;
                case 4:
                    System.out.println("A drink has been ordered for Room #" + roomNumber + ".\n");
                    roomBill.add(new Food(Food.FoodMenuItem.DRINK));
                    break;
                default:
                    run = false;
                    break;
            }
        } while (choice < 0 || choice > 4 || run);
    }

    private static Room selectRoom(String menuHeader) {
        System.out.println(menuHeader);

        while (true) {
            System.out.println("\n1. Enter your room number");
            System.out.println("2. View all currently booked rooms");
            System.out.println("3. Select room from customer booking");
            System.out.println("0. Cancel and go back");
            switch (Input.getUserInputInt()) {
                case 1:
                    int inputNumber = Input.askInt("Room number: ");
                    if (RoomHelper.getRoomMap().get(inputNumber) == null) {
                        System.err.println("Room #" + inputNumber + " does not exists. Please try again!");
                        break;
                    }
                    return RoomHelper.getRoomMap().get(inputNumber);
                case 2:
                    System.out.println("\nAll currently booked rooms:");
                    RoomHelper.getAvailableOrUnavailableRooms(false).forEach(room -> {
                        if (room.getRenter() == null) {
                            System.out.println("Room #" + room.getRoomNumber() + ", Unknown guest");
                        } else {
                            System.out.println("Room #" + room.getRoomNumber() + ", " + room.getRenter().getLastName());
                        }
                    });
                    break;
                case 3:
                    Customer customer = CustomerHelper.searchAndSelectCustomerMenu();
                    if (customer == null) {
                        break;
                    }

                    Room room = RoomHelper.findCustomersRoom(customer);
                    if (room == null) {
                        System.out.println("Customer is not checked into any room. Try again.");
                        break;
                    }

                    System.out.println("Room #" + room.getRoomNumber() + " found.");
                    String input = Input.askString("Continue with this room? (Y/N): ");
                    while (true) {
                        if (input.equalsIgnoreCase("y")) {
                            return room;
                        } else if (input.equalsIgnoreCase("n")) {
                            return null;
                        } else {
                            System.out.println("Unknown command. Try again!");
                        }
                    }
                case 0:
                    return null;
            }
        }
    }
}
