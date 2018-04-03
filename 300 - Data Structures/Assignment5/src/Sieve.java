import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
//Takes 0.81746045 seconds to run with n = 1000000
//Sieve is faster, even though it probably shouldn't be
public class Sieve extends LinkedList<Integer> implements Queue<Integer> {
	boolean illegalState;
	int lastN;
	double seconds;

	/**
	 * Create a new Sieve object
	 */
	public Sieve() {
		super();
		illegalState = true;
		seconds = 0;
	}

	/**
	 * Computes primes to n
	 *
	 * @param n int
	 * @throws IllegalArgumentException if n is less than 2
	 * @throws OutOfMemoryError
	 */
	public void computeTo(int n) throws IllegalArgumentException, OutOfMemoryError {
		if (n < 2) throw new IllegalArgumentException("n less than 2");
		clear();
		double before = System.nanoTime();
		Queue<Integer> queue = new LinkedList<>();
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
		seconds = (after - before) / 1000000000.0;
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
