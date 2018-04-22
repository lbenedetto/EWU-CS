public class Wind {
	private int speed;
	private int direction;

	private Wind(int direction, int speed) {
		this.speed = speed;
		this.direction = direction;
	}

	public static Wind decodeChar(char c) {
		if (('A' <= c && c <= 'N') || ('a' <= c && c <= 'z') || c == '.') {
			int x = (c < 'a') ? c - 'A' + 26 : c - 'a';
			if (c == '.') x = -1;

			int s = 10 + (x % 5) * 10;
			int d = x / 5 * 45;
			return new Wind(d, s);
		}
		throw new RuntimeException("Invalid char passed to decodeChar");
	}

	public int getSpeed() {
		return speed;
	}

	public int getDirection() {
		return direction;
	}
}
