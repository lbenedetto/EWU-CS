public class OctalInteger extends LongInteger {
    public OctalInteger(Long value) {
        super(value);
    }

    public String toString() {
        return value < 0 ? "-" + toOctalString(Math.abs(value)) + "(Octal)" : toOctalString(value) + "(Octal)";
    }

    public static OctalInteger parseOctalValue(String value) {
        if (value.charAt(0) == '-')
            return new OctalInteger(0 - toDecimal(value.substring(1)));
        return new OctalInteger(toDecimal(value));
    }

    public static Long toDecimal(String s) {
        char[] charArray = s.toCharArray();
        Long decimal = 0L;
        for (int i = 0; i < charArray.length; i++) {
            decimal += Character.getNumericValue(charArray[i]);
            if (i != charArray.length - 1)
                decimal *= 8;
        }
        return decimal;
    }
}