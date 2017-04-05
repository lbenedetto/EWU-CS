import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Tribblenacci {
	public static long[] generation;

	public static void main(String[] args) {
		generation = new long[68];
		generation[0] = 1;
		generation[1] = 1;
		generation[2] = 2;
		generation[3] = 4;
		try {
			BufferedReader bf = new BufferedReader(new FileReader("Generations.in"));
			int i, g;
			String line;
			for (i = Integer.parseInt(bf.readLine()); i > 0; i--) {
				g = Integer.parseInt(bf.readLine());
				System.out.println(calc(g));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static long calc(int g) {
		if (generation[g] != 0) {
			return generation[g];
		} else {
			long out = calc(g - 1) + calc(g - 2) + calc(g - 3) + calc(g - 4);
			generation[g] = out;
			return out;
		}
	}
}
