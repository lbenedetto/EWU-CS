import java.util.Arrays;
import java.util.Scanner;

/**
 * Interacts with the user to acquire a phrase and produce all of the substrings of that phrase.
 * Prompts the user to repeat
 *
 * <b>
 * Extra Credit:
 * </b>
 * <pre>
 * None Attempted
 * </pre>
 *
 * <b>
 * History:
 * </b>
 * <pre>
 * 11/12/2015 - Created program
 * </pre>
 *
 * @author Lars Benedetto
 * @see "No Borrowed Code"
 */
public class SubstringTester {
    public static boolean enableLogging = false;
    static Scanner kb = new Scanner(System.in);
    public static void main(String[] args) {
        String quit;
        do {

            System.out.print("Enter a string: ");
            String str = readString();
            substringify(str.split(""), 0, str.length());
            System.out.println("Done");
            System.out.println("Would you like to go again?");
            System.out.print("Any key to quit, yes to continue: ");
            quit = readString();
        }while(quit.equalsIgnoreCase("yes"));
        System.out.println("Exiting...");
    }

    /**
     * Prints all the substrings of an array
     *
     * @param str - Array of one letter long strings
     * @param from - Starting index for use in copyOfRange
     * @param to - Ending index for use in copyOfRange
     * @see "No Borrowed Code"
     */
    public static void substringify(String[] str, int from, int to) {
        if(enableLogging)System.out.println(from + "," + to);
        if (to > from) {
            String[] subArray = Arrays.copyOfRange(str, from, to);
            if (subArray.length != 0)
                System.out.println(String.join("", subArray));
            substringify(str, from, to - 1);
        }
        if (to == from) {
            substringify(str, from + 1, str.length);
        }
        if(from>to){
            System.out.println();
        }

    }

    /**
     * Clears the input buffer and returns a string
     *
     * @return String, data entered by user
     * @see "No Borrowed Code"
     */
    public static String readString() {
        if (!kb.hasNextLine()) {
            kb.nextLine();
        }
        return kb.nextLine();
    }
}