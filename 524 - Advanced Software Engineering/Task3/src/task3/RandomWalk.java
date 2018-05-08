package task3;

import java.util.Random;

public class RandomWalk {
	private int steps;
	private int valueBits;
	private int shiftBits;

	RandomWalk(int steps, int valueBits, int shiftBits) {
		this.steps = steps;
		this.shiftBits = shiftBits;
		this.valueBits = valueBits;
	}

	public float execute(int seed) {
		float fx = 0;
		float fy = 0;
		float fz = 0;
		Real rx = new Real(valueBits, shiftBits, 0f);
		Real ry = new Real(valueBits, shiftBits, 0f);
		Real rz = new Real(valueBits, shiftBits, 0f);

		var random = new Random(seed);

		for (int i = 0; i < steps; i++) {
			float nx = getNextRandomFloat(random);
			float ny = getNextRandomFloat(random);
			float nz = getNextRandomFloat(random);
			fx += nx;
			fy += ny;
			fz += nz;
			rx = rx.add(new Real(valueBits, shiftBits, nx));
			ry = ry.add(new Real(valueBits, shiftBits, ny));
			rz = rz.add(new Real(valueBits, shiftBits, nz));
			System.out.printf("diff: %f\n", fx - rx.getValue());
		}
		return (float) Math.sqrt(Math.pow(fx - rx.getValue(), 2) + Math.pow(fy - ry.getValue(), 2) + Math.pow(fz - rz.getValue(), 2));
	}

	private float getNextRandomFloat(Random random) {
		return (random.nextFloat() - .5f) * 1000000f;
	}


}
