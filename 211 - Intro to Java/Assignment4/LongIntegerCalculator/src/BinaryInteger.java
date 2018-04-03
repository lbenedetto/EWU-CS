public class BinaryInteger extends LongInteger {
    public BinaryInteger(Long value) {
        super(value);
    }

    public String toString() {
        return value < 0 ? "-" + toBinaryString(Math.abs(value)) + "(Binary)" : toBinaryString(value) + "(Binary)";
    }

    public static BinaryInteger parseBinaryValue(String value) {
        if (value.charAt(0) == '-')
            return new BinaryInteger(0 - toDecimal(value.substring(1)));
        return new BinaryInteger(toDecimal(value));
    }

    public static Long toDecimal(String s) {
        Long total = 0L;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '1')
                total += (int) Math.pow(2, s.length() - 1 - i);
        }
        return total;
    }
}
