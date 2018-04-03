public class DecimalInteger extends LongInteger {
    public DecimalInteger(Long value) {
        super(value);
    }

    public String toString() {
        return value + "(Decimal)";
    }

    public static DecimalInteger parseDecimalValue(String value) {
        if (value.charAt(0) == '-')
            return new DecimalInteger(0 - Long.parseLong(value.substring(1)));
        return new DecimalInteger(Long.parseLong(value));
    }
}