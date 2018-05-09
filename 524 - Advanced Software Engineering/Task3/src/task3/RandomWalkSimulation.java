package task3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class RandomWalkSimulation {
	private int iterations;
	private int seed;

	RandomWalkSimulation(int iterations, int seed) {
		this.iterations = iterations;
		this.seed = seed;
	}

	public void simulate(int steps) throws Exception {
		var stdevFile = new BufferedWriter(new FileWriter(new File(String.format("%d-steps-std.gnu", steps))));
		var meanFile = new BufferedWriter(new FileWriter(new File(String.format("%d-steps-me.txt", steps))));
		for (int valueBits = 1; valueBits < 32; valueBits++) {
			for (int shiftBits = 0; shiftBits < 32; shiftBits++) {
				if (shiftBits + valueBits > 32) continue;
				var errors = new ArrayList<Float>();
				for (int i = 0; i < iterations; i++) {
					var walker = new RandomWalk(steps, valueBits, shiftBits);
					var error = walker.execute(seed);
					errors.add(error);
				}
				var std = standardDeviation(errors);
				var me = mean(errors);
				float min = Float.MAX_VALUE;
				float max = Float.MIN_VALUE;
				for (Float f : errors) {
					if (f > max) max = f;
					if (f < min) min = f;
				}
				System.out.printf("vb:%s, sb:%s, min:%s\n", valueBits, shiftBits, std);
				System.out.printf("vb:%s, sb:%s, max:%s\n", valueBits, shiftBits, std);
				System.out.printf("vb:%s, sb:%s, stdev:%s\n", valueBits, shiftBits, std);
				System.out.printf("vb:%s, sb:%s, mean:%s\n", valueBits, shiftBits, me);
				stdevFile.write(String.format("%s %s %s\n", valueBits, shiftBits, std));
				meanFile.write(String.format("%s %s %s\n", valueBits, shiftBits, me));
			}
		}
		stdevFile.close();
		meanFile.close();
	}

	private static float mean(ArrayList<Float> table) {
		float total = 0;
		for (Float aTable : table) {
			float currentNum = aTable;
			total += currentNum;
		}
		return total / table.size();
	}

	private static double standardDeviation(ArrayList<Float> table) {
		float mean = mean(table);
		float temp = 0;
		for (Float aTable : table) {
			float val = aTable;
			double squrDiffToMean = Math.pow(val - mean, 2);
			temp += squrDiffToMean;
		}
		double meanOfDiffs = (double) temp / (double) (table.size());
		return Math.sqrt(meanOfDiffs);
	}

	public static void main(String[] args) throws Exception {
		new RandomWalkSimulation(1000, 1000).simulate(1);
		new RandomWalkSimulation(1000, 1000).simulate(10);
		new RandomWalkSimulation(1000, 1000).simulate(100);
	}
}
