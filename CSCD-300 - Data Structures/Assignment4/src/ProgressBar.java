class ProgressBar implements Runnable {
	private final int interval;
	private boolean running;
	private boolean tookAWhile;

	public ProgressBar() {
		this.interval = 5000;
		running = true;
		tookAWhile = false;
	}

	public void stop() {
		running = false;
	}

	public boolean didTakeAWhile() {
		return tookAWhile;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			System.out.print("Computing exponents");
			tookAWhile = true;
			Thread.sleep(1000);
			System.out.print(" - This may take a while");
			while (running) {
				Thread.sleep(interval);
				System.out.print(".");
			}
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
}