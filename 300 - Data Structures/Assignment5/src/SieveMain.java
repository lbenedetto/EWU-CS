// This program computes all the prime numbers up to a given integer n.  It
// uses the classic "Sieve of Eratosthenes" to do so.

import java.util.*;

public class SieveMain {
	public static void main(String[] args) {
		System.out.println("This program computes all prime numbers up to a");
		System.out.println("maximum using the Sieve of Eratosthenes.");
		System.out.println();
		Scanner console = new Scanner(System.in);
		Sieve s = new Sieve();
		for (; ; ) {
			System.out.print("Maximum n to compute (0 to quit)? ");
			int max = console.nextInt();
			if (max == 0)
				break;
			System.out.println();
			s.computeTo(max);
			s.reportResults();
			int percent = s.getCount() * 100 / s.getMax();
			System.out.println("% of primes = " + percent);
			System.out.println(s.seconds);
		}
	}
}