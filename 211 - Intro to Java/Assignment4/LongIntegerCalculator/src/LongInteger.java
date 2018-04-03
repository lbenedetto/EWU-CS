public abstract class LongInteger {
    protected Long value;

    public LongInteger(Long value) {
        this.value = value;
    }

    public abstract String toString();

    public Long getValue() {
        return value;
    }

    public void setValue(Long a) {
        value = a;
    }

    public void add(LongInteger b) {
        setValue(value + b.getValue());
    }

    public void subtract(LongInteger b) {
        setValue(value - b.getValue());
    }

    public void multiply(LongInteger b) {
        setValue(value * b.getValue());
    }

    public void divide(LongInteger b) {
        setValue(value / b.getValue());
    }

    public String toBinaryString(Long n) {
        if (n == 0) return "0";
        String binary = "";
        Long rem;

        while (n > 0) {
            rem = n % 2;
            n /= 2;
            binary = rem + binary;
        }
        return binary;
    }

    //Hmm... Wonder what happens if I replace the 2 with an 8...
    //And lo, an octal converter was born
    public String toOctalString(Long n) {
        if (n == 0) return "0";
        String octal = "";
        Long rem;
        while (n > 0) {
            rem = n % 8;
            n /= 8;
            octal = rem + octal;
        }
        return octal;
    }

    //So I guess if I replace the 8 with a 16...
    public String toHexString(Long n) {
        if (n == 0) return "0";
        //And map the output to its hex equivalent...
        String hexValues = "0123456789ABCDEF";
        String hex = "";
        Long rem;
        while (n > 0) {
            rem = n % 16;
            n /= 16;
            hex = hexValues.charAt(rem.intValue()) + hex;
        }
        //I'll have a functioning hex converter!
        return hex;
    }


}
