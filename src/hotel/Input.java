package hotel;
import java.util.Scanner;

public class Input {
    public static Scanner sc = new Scanner(System.in);

    public static class InputStringIsBlankException extends Exception {
        public InputStringIsBlankException(String errorMessage) {
            super(errorMessage);
        }
    }


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
                if (userInput.chars().allMatch(Character::isWhitespace)) { throw new InputStringIsBlankException("Input contains only whitespace"); }
                if (userInput.equals("")) { throw new InputStringIsBlankException("Input is empty"); }
                run = false;
            } catch (InputStringIsBlankException e) {
                System.out.println("No input, try again: ");
            } catch (Exception e) {
                System.out.println("Wrong input, try again: ");
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
        System.out.print(print);
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