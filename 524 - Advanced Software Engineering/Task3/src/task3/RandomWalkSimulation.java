package task3;

public class RandomWalkSimulation {
	private int iterations;
	private int seed;

	RandomWalkSimulation(int iterations, int seed) {
		this.iterations = iterations;
		this.seed = seed;
	}

	public void simulate(int steps) {
		for(int valueBits = 20; valueBits < 32; valueBits++){
			for(int shiftBits = 0; shiftBits < 32; shiftBits++){
				if(shiftBits + valueBits > 32) continue;
				for (int i = 0; i < iterations; i++) {
					var walker = new RandomWalk(steps, valueBits, shiftBits);
					System.out.printf("vb:%s, sb:%s, diff:%s\n", valueBits, shiftBits, walker.execute(seed));
				}
			}
		}


	}

	public static void main(String[] args) {
		new RandomWalkSimulation(10, 1000).simulate(10000);
//		var t= new Real(28,  4, 10).add(new Real(28,4,10)).subtract(new Real(28,4,100010));
	}
}
