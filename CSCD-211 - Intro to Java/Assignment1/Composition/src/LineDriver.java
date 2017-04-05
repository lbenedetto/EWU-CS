import java.util.Scanner;

public class LineDriver {
    public static void main(String[] args) {
        int input = 0;
        Scanner kb = new Scanner(System.in);
        Line lineOne = new Line();
        Line lineTwo = new Line();
        while (input != 6) {
            println("1. Enter coordinates, width and color for first line");
            println("2. Enter coordinates, width and color for second line");
            println("3. Compare the two lines");
            println("4. Display coordinates, width, length and color for first line");
            println("5. Display coordinates, width, length and color for second line");
            println("6. Quit");
            print("Enter choice: ");
            input = readInt(kb);
            while (input < 1 || input > 6) {
                println("Please enter valid choice");
                print("Enter choice: ");
                input = readInt(kb);
            }
            switch (input) {
                case 1:
                    println("Enter information for first line");
                    lineOne = createNewLine(kb);
                    break;
                case 2:
                    println("Enter information for second line");
                    lineTwo = createNewLine(kb);
                    break;
                case 3:
                    if (lineOne.equals(lineTwo))
                        println("Lines are the same");
                    else
                        println("Lines are not the same");
                    break;
                case 4:
                    println(lineOne.toString());
                    break;
                case 5:
                    println(lineTwo.toString());
                    break;
            }
        }
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
        }
        int out = kb.nextInt();
        return out;
    }

    public static Line createNewLine(final Scanner kb) {
        int x1 = -1, y1 = -1, y2 = -1, x2 = -1, width = -1;
        String color = "";
        while (!Line.validatePoints(x1, y1)) {
            print("Enter x1:");
            x1 = readInt(kb);
            print("Enter y1:");
            y1 = readInt(kb);
            if (!Line.validatePoints(x1, y1)) {
                println("Invalid point");
            }
        }
        while (!Line.validatePoints(x2, y2)) {
            print("Enter x2:");
            x2 = readInt(kb);
            print("Enter y2:");
            y2 = readInt(kb);
            if (!Line.validatePoints(x2, y2)) {
                println("Invalid point");
            }
        }
        while (width < 1) {
            print("Enter line width:");
            width = readInt(kb);
            if (width < 1) {
                println("invalid width");
            }
        }
        while (color.equalsIgnoreCase("")) {
            //Not going to validate whether the color entered is real
            print("Enter color: ");
            kb.nextLine();
            color = kb.nextLine();
        }
        return new Line(x1, y1, x2, y2, width, color);
    }
}
