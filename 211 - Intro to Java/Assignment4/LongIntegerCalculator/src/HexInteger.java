public class HexInteger extends LongInteger {
    public HexInteger(Long value) {
        super(value);
    }

    public String toString() {
        return value < 0 ? "-" + toHexString(Math.abs(value)) + "(Hex)" : toHexString(value) + "(Hex)";
    }

    public static HexInteger parseHexValue(String value) {
        if (value.charAt(0) == '-')
            return new HexInteger(0 - toDecimal(value.substring(1)));
        return new HexInteger(toDecimal(value));
    }

    //http://introcs.cs.princeton.edu/java/31datatype/Hex2Decimal.java.html
    public static Long toDecimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        Long val = 0L;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }
}
