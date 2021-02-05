package hotel;
import java.util.Scanner;

public class Input {
    public static Scanner sc = new Scanner(System.in);

    public static int getUserInputInt() {
        String userInput;
        int parseInput = 0;
        boolean runInput = true;

        do {
            try {
                userInput = sc.nextLine();
                parseInput = Integer.parseInt(userInput);
                runInput = false;
            } catch (Exception e) {
                System.out.println("Wrong input, Try again: ");
            }
        } while (runInput);
        return parseInput;
    }

    public static String getUserInputString() {
        String userInput = null;
        boolean run = true;

        do {
            try {
                userInput = sc.nextLine();
                run = false;
            } catch (Exception e) {
                System.out.println("Wrong input, Try again: ");
            }
        } while (run);
        return userInput;
    }
    
    public static double getUserInputDouble() {
        String userInput;
        double parseInput = 0;
        boolean run = true;

        do {
            try {
                userInput = sc.nextLine();
                parseInput = Double.parseDouble(userInput);
                run = false;
            } catch (Exception e) {
                System.out.println("Wrong input, Try again: ");
            }
        } while (run);
        return parseInput;
    }

    public static String askString(String print) {
        System.out.println(print);
        return Input.getUserInputString();
    }

    public static int askInt(String print) {
        System.out.print(print);
        return Input.getUserInputInt();
    }

    public static double askDouble(String print) {
        System.out.println(print);
        return Input.getUserInputDouble();
    }
}