import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
//Takes 35.283249244 seconds to run with n = 1000000
//Sieve is faster, even though it probably shouldn't be
public class Sieve2 extends ArrayDeque<Integer> {
	boolean illegalState;
	int lastN;
	double seconds;

	public Sieve2() {
		super();
		illegalState = true;
		seconds = 0;
	}

	/**
	 * Compute primes to N
	 *
	 * @param n int
	 * @throws IllegalArgumentException if n is less than
	 * @throws OutOfMemoryError         if n is too large
	 */
	public void computeTo(int n) throws IllegalArgumentException, OutOfMemoryError {
		if (n < 2) throw new IllegalArgumentException("n less than 2");
		clear();
		double before = System.nanoTime();
		Queue<Integer> queue = new ArrayDeque<>();
		for (int i = 2; i <= n; i++)
			queue.add(i);
		Integer p;
		do {
			p = queue.poll();
			add(p);
			Iterator<Integer> iterator = queue.iterator();
			while (iterator.hasNext()) {
				Integer i = iterator.next();
				if (i % p == 0) iterator.remove();
			}
		} while (p < Math.sqrt(n));
		queue.forEach(this::add);
		illegalState = false;
		lastN = n;
		double after = System.nanoTime();
		seconds = (after - before) / 1000000000;
	}

	/**
	 * Print all the primes
	 *
	 * @throws IllegalStateException if no call to computeTo has been made
	 */
	public void reportResults() throws IllegalStateException {
		if (illegalState) {
			throw new IllegalStateException("No call to computeTo made");
		}
		int row = 0;
		for (Integer i : this) {
			row++;
			if (row >= 12) {
				row = 0;
				System.out.println();
			}
			System.out.print(i + " ");
		}
		System.out.println();
	}

	/**
	 * Get the last value of N
	 *
	 * @return int
	 * @throws IllegalStateException if no call to computeTo has been made
	 */
	public int getMax() throws IllegalStateException {
		if (illegalState) {
			throw new IllegalStateException("No call to computeTo made");
		}
		return lastN;
	}

	/**
	 * Get how many primes have been found
	 *
	 * @return int
	 * @throws IllegalStateException if no call to computeTo has been made
	 */
	public int getCount() throws IllegalStateException {
		if (illegalState) {
			throw new IllegalStateException("No call to computeTo made");
		}
		return size();
	}
}
