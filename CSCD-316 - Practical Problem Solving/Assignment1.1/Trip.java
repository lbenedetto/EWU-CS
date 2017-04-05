import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Trip {
	public static void main(String[] args) {
		double total;
		double exchange;
		double average;
		double diff;
		double negDelta;
		double posDelta;
		int i;
		try {
			String filename = null;
			if (args.length >= 1)
				filename = args[0];
			if (filename == null) filename = "input.txt";
			BufferedReader file = new BufferedReader(new FileReader(filename));
			i = Integer.parseInt(file.readLine());
			while (i != 0) {
				double[] students = new double[i];
				while (i > 0) {
					students[i - 1] = Double.parseDouble(file.readLine());
					i--;
				}
				total = 0;
				negDelta = 0;
				posDelta = 0;
				for (i = 0; i < students.length; i++) {
					total += students[i];
				}
				average = total / students.length;
				for (i = 0; i < students.length; i++) {
					diff = (double) (long) ((students[i] - average) * 100.0) / 100.0;
					if (diff < 0)
						negDelta += diff;
					else
						posDelta += diff;
				}
				exchange = (-negDelta > posDelta) ? -negDelta : posDelta;
				System.out.printf("$%.2f\n", exchange);
				i = Integer.parseInt(file.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
