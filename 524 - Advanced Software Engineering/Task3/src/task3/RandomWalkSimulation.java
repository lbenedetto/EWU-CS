package task3;

public class RandomWalkSimulation {
	private int iterations;
	private int seed;

	RandomWalkSimulation(int iterations, int seed) {
		this.iterations = iterations;
		this.seed = seed;
	}

	public void simulate(int steps) {
		for (int i = 0; i < iterations; i++) {
			var walker = new RandomWalk(steps, 0, 0);
			System.out.println(walker.execute(seed));
		}
	}
}
