import java.util.Scanner;

public class Interface {
    static Scanner kb = new Scanner(System.in);

    public static void showln(String s) {
        System.out.println(s);
    }

    public static void show(String s) {
        System.out.print(s);
    }

    public static String readString() {
        kb.nextLine();
        return kb.nextLine();
    }

    public static boolean readBoolean() {
        while (!(kb.nextLine().equalsIgnoreCase("true") || kb.nextLine().equalsIgnoreCase("false"))) {
            kb.next();
            show("Try again: ");
        }
        if (kb.nextLine().equalsIgnoreCase("true")) {
            return true;
        } else return false;
    }

    public static char readChar() {
        while (!kb.hasNextLine()) {
            kb.nextLine();
            show("Try again: ");
        }
        return kb.next().charAt(0);
    }

    public static int readInt() {
        while (!kb.hasNextInt()) {
            kb.next();
            show("Try again: ");
        }
        return kb.nextInt();
    }

    public static long readLong() {
        while (!kb.hasNextLong()) {
            kb.next();
            show("Try again: ");
        }
        return kb.nextLong();
    }

    public static float readFloat() {
        while (!kb.hasNextFloat()) {
            kb.next();
            show("Try again: ");
        }
        return kb.nextFloat();
    }

    public static double readDouble() {
        while (!kb.hasNextDouble()) {
            kb.next();
            show("Try again: ");
        }
        return kb.nextDouble();
    }
}
