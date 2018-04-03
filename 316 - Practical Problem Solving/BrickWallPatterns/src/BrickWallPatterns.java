import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;

public class BrickWallPatterns {
	//UVa 900: Brick Wall Patterns
	private static final BigInteger[] f = new BigInteger[50];

	public static void main(String[] args) throws Exception {
		//Pre-compute fibonacci
		f[0] = BigInteger.ONE;
		f[1] = BigInteger.valueOf(2);
		for (int i = 2; i < 50; i++)
			f[i] = f[i - 1].add(f[i - 2]);
		int n;
		BufferedReader in = new BufferedReader(new FileReader("input.txt"));
		n = Integer.parseInt(in.readLine());
		while (n != 0) {
			System.out.println(f[n-1]);
			n = Integer.parseInt(in.readLine());
		}
	}
}
