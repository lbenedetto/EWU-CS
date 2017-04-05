import java.util.Arrays;
import java.util.Scanner;

public class ArrayUtils {
    public static void main(String[] args) {
        final Scanner kb = new Scanner(System.in);
        print("Enter length of array: ");
        int length = readInt(kb);
        int ara[];
        ara = createAra(length);
        initAra(ara);
        printAra(ara);
        ara = doubleAra(ara);
        printAra(ara);
    }

    public static int[] createAra(int i) {
        return new int[i];
    }

    public static void initAra(int[] ara) {
        for (int i = 0; i < ara.length; i++) {
            ara[i] = i + 100;
        }
    }

    public static int[] doubleAra(int[] ara) {
        int[] ara1 = new int[ara.length * 2];
        for (int i = 0; i < ara1.length; i++) {
            try {
                ara1[i] = ara[i];
            } catch(Exception e) {
                ara1[i] = 0;
            }
        }
        return ara1;
    }
    public static void printAra(int[] ara){
        println(Arrays.toString(ara));
    }
    //Non assignment specific methods
    public static void println(String s) {
        //Less typing
        System.out.println(s);
    }

    public static void print(String s) {
        //Less typing
        System.out.print(s);
    }

    public static int readInt(final Scanner kb) {
        //Safely get an int from user
        while (!kb.hasNextInt()) {
            kb.next();
        }
        int out = kb.nextInt();
        return out;
    }
}
