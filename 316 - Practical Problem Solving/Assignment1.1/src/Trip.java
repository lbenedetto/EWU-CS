import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Trip {
	public static void main(String[] args) {
		try {
			String filename = null;
			if (args.length >= 1)
				filename = args[0];
			if (filename == null) filename = "input.txt";
			BufferedReader file = new BufferedReader(new FileReader(filename));
			int i = Integer.parseInt(file.readLine());
			while (i != 0) {
				double[] students = new double[i];
				while (i > 0) {
					students[i - 1] = Double.parseDouble(file.readLine());
					i--;
				}
				double total = 0;
				double negDelta = 0;
				double posDelta = 0;
				for (i = 0; i < students.length; i++) {
					total += students[i];
				}
				double average = total / students.length;
				for (i = 0; i < students.length; i++) {
					double diff = (double) (long) ((students[i] - average) * 100.0) / 100.0;
					if (diff < 0)
						negDelta += diff;
					else
						posDelta += diff;
				}
				double exchange = (-negDelta > posDelta) ? -negDelta : posDelta;
				System.out.printf("$%.2f\n", exchange);
				i = Integer.parseInt(file.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
