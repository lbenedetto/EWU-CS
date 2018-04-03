/**
 * Alex Toenniessen: ajt110195@gmail.com
 * Lars Benedetto: larsbenedetto@gmail.com
 * Richard Philips: mersennetwizter@gmail.com
 */
public class c {

	public static void main(String[] args) {
		for (int n = 166; n < 1000; n++) {
			for (int d = 660; d < 1000; d++) {
				if (n == d) continue;
				Fraction a = new Fraction(n, d);
				Fraction b = cancel(n, d);
				if (a.equals(b)) {
					System.out.println(a.toString() + " = " + b.toString());
				}
			}
		}
	}

	private static Fraction cancel(int n, int d) {
		String ns = String.valueOf(n);
		String ds = String.valueOf(d);
		ns = ns.substring(0, ns.length() - 1);
		ds = ds.substring(1, ds.length());
		return new Fraction(Integer.valueOf(ns), Integer.valueOf(ds));
	}

	private static class Fraction {
		int n;
		int d;
		int nnr;
		int dnr;

		Fraction(int n, int d) {
			nnr = n;
			dnr = d;
			int gcd = GCD(n, d);
			this.n = n / gcd;
			this.d = d / gcd;
		}

		private int GCD(int n, int d) {
			if (d == 0) return n;
			return GCD(d, n % d);
		}

		boolean equals(Fraction that) {
			return this.n == that.n && this.d == that.d;
		}

		public String toString() {
			return nnr + " / " + dnr;
		}
	}
}
