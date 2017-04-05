import java.util.InputMismatchException;

public class Rational implements Comparable<Rational> {
    private int numerator, denominator;

    public Rational() {
        numerator = 1;
        denominator = 1;
    }

    public Rational(int n, int d) {
        numerator = n;
        denominator = d;
        reduce();
    }

    public int getNumerator() {
        return numerator;
    }

    public void setNumerator(int numerator) {
        this.numerator = numerator;
        reduce();
    }

    public int getDenominator() {
        return denominator;
    }

    public void setDenominator(int denominator) {
        this.denominator = denominator;
        reduce();
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    @Override
    public int hashCode() {
        int result = getNumerator();
        result = 31 * result + getDenominator();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rational)) return false;
        Rational rational = (Rational) o;
        if (getNumerator() != rational.getNumerator()) return false;
        return denominator == rational.denominator;
    }

    @Override
    public int compareTo(Rational o) {
        if (o.getClass().getSimpleName().equals(this.getClass().getSimpleName())) {
            Rational that = (Rational) o;
            if (this.equals(that)) return 0;
            double R1 = (double)numerator/(double)denominator;
            double R2 = (double)that.getNumerator()/(double)that.getDenominator();
            if (R1 > R2) return 1;
            else return -1;
        }
        throw new InputMismatchException();
    }

    public Rational add(Rational r2) {
        Rational r = new Rational((numerator * r2.getDenominator()) + (r2.getNumerator() * denominator), (denominator * r2.getDenominator()));
        return r;
    }

    public Rational subtract(Rational r2) {
        Rational r = new Rational((numerator * r2.getDenominator()) - (r2.getNumerator() * denominator), (denominator * r2.getDenominator()));
        return r;
    }

    public void reduce() {
        int gcd = gcd(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;
    }

    //recursive implementation
    //taken from http://introcs.cs.princeton.edu/java/23recursion/Euclid.java.html
    public static int gcd(int p, int q) {
        if (q == 0) return p;
        else return gcd(q, p % q);
    }
}
