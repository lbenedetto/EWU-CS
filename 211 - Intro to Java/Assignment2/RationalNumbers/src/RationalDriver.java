import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class RationalDriver {
    public static void main(String[] args) {
        ArrayList<Rational> arrayList = new ArrayList<>(6);
        arrayList.add(new Rational(2, 3));
        arrayList.add(new Rational(2, 18));
        arrayList.add(new Rational(3, 12));
        arrayList.add(new Rational(9, 3));
        arrayList.add(new Rational(2, 5));
        arrayList.add(new Rational(22, 7));
        Scanner kb = new Scanner(System.in);
        int input;
        int index;
        int index1;
        do {
            print("1. Display the value of a Rational object\n" +
                    "2. Change the value of a Rational object\n" +
                    "3. Add two Rational objects together and display the sum as a new Rational object\n" +
                    "4. Subtract two Rational objects and display the difference as a new Rational object\n" +
                    "5. Sort the array of Rational number objects. \n" +
                    "6. Determine if a particular Rational object is in the ArrayList\n" +
                    "7. Print the array of Rational number objects to the screen.\n\n" +
                    "8. Quit\n" +
                    "Enter choice: ");
            input = readInt(kb);
            while (input < 1 || input > 8) {
                println("Please enter valid choice");
                print("Enter choice: ");
                input = readInt(kb);
            }
            switch (input) {
                case 1:
                    print("Enter index of Rational object to display: ");
                    index = readIndex(kb, arrayList);
                    println(arrayList.get(index).toString());
                    break;
                case 2:
                    print("Enter index of Rational object to be changed: ");
                    index = readIndex(kb, arrayList);
                    print("Enter numerator: ");
                    int numerator = readInt(kb);
                    print("Enter denominator: ");
                    int denominator = readInt(kb);
                    arrayList.set(index, new Rational(numerator, denominator));
                    break;
                case 3:
                    print("Enter index of first Rational object to sum: ");
                    index = readIndex(kb, arrayList);
                    print("Enter index of second Rational object to sum: ");
                    index1 = readIndex(kb, arrayList);
                    Rational temp = arrayList.get(index).add(arrayList.get(index1));
                    println(temp.toString());
                    break;
                case 4:
                    print("Enter index of first Rational object to subtract: ");
                    index = readIndex(kb, arrayList);
                    print("Enter index of second Rational object to subtract: ");
                    index1 = readIndex(kb, arrayList);
                    temp = arrayList.get(index).subtract(arrayList.get(index1));
                    println(temp.toString());
                    break;
                case 5:
                    arrayList.sort(Comparator.<Rational>naturalOrder());
                    break;
                case 6:
                    print("Enter numerator: ");
                    numerator = readInt(kb);
                    print("Enter denominator: ");
                    denominator = readInt(kb);
                    Rational find = new Rational(numerator,denominator);
                    if (arrayList.contains(find))println("The array list contains " + find.toString());
                    else println("The array list does not contain " + find.toString());
                    break;
                case 7:
                    println(arrayList.toString());
                    break;
            }
        } while (input != 8);
    }

    //Less typing to print lines
    public static void print(String s) {
        System.out.print(s);
    }

    public static void println(String s) {
        System.out.println(s);
    }

    public static int readInt(final Scanner kb) {
        while (!kb.hasNextInt()) {
            kb.next();
            print("Try again: ");
        }
        int out = kb.nextInt();
        return out;
    }

    public static int readIndex(final Scanner kb, ArrayList<Rational> arrayList) {
        int index;
        do {
            index = readInt(kb);
            if (index < 0 || index > arrayList.size() - 1) {
                println("Invalid index");
                print("Enter index of Rational object to display: ");
            }
        } while (index < 0 || index > arrayList.size() - 1);
        return index;
    }
}
