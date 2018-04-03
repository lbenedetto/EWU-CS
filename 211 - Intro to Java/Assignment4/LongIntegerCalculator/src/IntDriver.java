//Lars Benedetto 11/4/2015
//Attempted all extra credit
//Negative values and my own convert methods
import java.util.regex.Pattern;

public class IntDriver {
    public static String base = "Decimal";
    public static String operator = "";
    public static String option;
    public static LongInteger ValueA = null;
    public static LongInteger ValueB = null;
    public static boolean showAnswer = false;

    public static void main(String[] args) {
        do {
            try {
                showMenu(base);
                option = Interface.readString().toLowerCase();
                //If the input was not something that changes the settings
                if (!settingsUpdate()) {
                    //Then it must be a number
                    //So interpret the input as a number of the current base
                    interpretInput();
                }
                if (showAnswer) {
                    //If the = sign was entered, then perform the operation
                    updateValues();
                }
            } catch (NumberFormatException e) {
                Interface.showln(e.getMessage());
            } catch (ArithmeticException e) {
                Interface.showln("Cannot divide by zero");
                showAnswer = false;
            }
        } while (!option.equals("q"));
        Interface.showln("Closing program");
    }

    public static void interpretInput() {
        switch (base) {
            case ("Binary"):
                //Use regex to make sure it can be interpreted in this base
                if (!Pattern.matches("-?[10]+", option))
                    throw new NumberFormatException("BinaryInteger expected, user entered \"" + option + "\"");
                if (ValueA == null || operator.equals(""))
                    ValueA = BinaryInteger.parseBinaryValue(option);
                else
                    ValueB = BinaryInteger.parseBinaryValue(option);
                break;
            case ("Octal"):
                if (!Pattern.matches("-?[01234567]+", option))
                    throw new NumberFormatException("OctalInteger expected, user entered \"" + option + "\"");
                if (ValueA == null || operator.equals(""))
                    ValueA = OctalInteger.parseOctalValue(option);
                else
                    ValueB = OctalInteger.parseOctalValue(option);
                break;
            case ("Hexadecimal"):
                if (!Pattern.matches("-?[0123456789ABCDEF]+", option.toUpperCase()))
                    throw new NumberFormatException("HexInteger expected, user entered \"" + option + "\"");
                if (ValueA == null || operator.equals(""))
                    ValueA = HexInteger.parseHexValue(option);
                else
                    ValueB = HexInteger.parseHexValue(option);
                break;
            case ("Decimal"):
                if (!Pattern.matches("-?[0123456789]+", option))
                    throw new NumberFormatException("DecimalInteger expected, user entered \"" + option + "\"");
                if (ValueA == null || operator.equals(""))
                    ValueA = DecimalInteger.parseDecimalValue(option);
                else
                    ValueB = DecimalInteger.parseDecimalValue(option);
                break;
        }
    }

    public static boolean settingsUpdate() {
        switch (option) {
            case ("bin"):
                base = "Binary";
                //Convert existing values to the new Base
                try {
                    ValueA = new BinaryInteger(ValueA.getValue());
                    ValueB = new BinaryInteger(ValueB.getValue());
                } catch (NullPointerException e) {
                    //Ignore it
                }
                break;
            case ("oct"):
                base = "Octal";
                //Convert existing values to the new Base
                try {
                    ValueA = new OctalInteger(ValueA.getValue());
                    ValueB = new OctalInteger(ValueB.getValue());
                } catch (NullPointerException e) {
                    //Ignore it
                }
                break;
            case ("dcm"):
                base = "Decimal";
                //Convert existing values to the new Base
                try {
                    ValueA = new DecimalInteger(ValueA.getValue());
                    ValueB = new DecimalInteger(ValueB.getValue());
                } catch (NullPointerException e) {
                    //Ignore it
                }
                break;
            case ("hex"):
                base = "Hexadecimal";
                //Convert existing values to the new Base
                try {
                    ValueA = new HexInteger(ValueA.getValue());
                    ValueB = new HexInteger(ValueB.getValue());
                } catch (NullPointerException e) {
                    //Ignore it
                }
                break;
            case ("+"):
                operator = "+";
                break;
            case ("-"):
                operator = "-";
                break;
            case ("*"):
                operator = "*";
                break;
            case ("/"):
                operator = "/";
                break;
            case ("="):
                showAnswer = true;
                break;
            case ("q"):
                break;
            case ("rst"):
                ValueA = null;
                ValueB = null;
                operator = "";
                base = "Decimal";
                showAnswer = false;
                break;
            default:
                return false;
        }
        return true;
    }

    public static void updateValues() {
        switch (operator) {
            case ("+"):
                ValueA.add(ValueB);
                break;
            case ("-"):
                ValueA.subtract(ValueB);
                break;
            case ("*"):
                ValueA.multiply(ValueB);
                break;
            case ("/"):
                ValueA.divide(ValueB);
                break;
        }
        //And reset everything except ValueA back to default.
        operator = "";
        ValueB = null;
        showAnswer = false;
    }

    public static void showMenu(String base) {
        Interface.showln("");
        Interface.showln(base + " mode");
        Interface.showln("");
        showCurrentState();
        Interface.showln("Bin ~ Binary         + ~ Add");
        Interface.showln("Oct ~ Octal          - ~ Subtract");
        Interface.showln("Dcm ~ Decimal        * ~ Multiply");
        Interface.showln("Hex ~ Hexadecimal    / ~ Divide");
        Interface.showln("Rst ~ Reset          = ~ Equals");
        Interface.showln("Q ~ Quit");
        Interface.show("Option or value --> ");
    }

    public static void showCurrentState() {
        try {
            Interface.show(ValueA.toString() + " ");
            Interface.show(operator + " ");
            Interface.showln(ValueB.toString());
        } catch (NullPointerException e) {
            Interface.showln("");
        }
    }
}
