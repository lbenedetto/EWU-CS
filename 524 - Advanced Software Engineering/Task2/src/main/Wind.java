public class Wind {
	private int speed;
	private int direction;

	private Wind(int direction, int speed) {
		this.speed = speed;
		this.direction = direction;
	}
	private static char[] chars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N'};

	//This is efficient but ugly
	public static Wind decodeChar1(char c){
		if(c == '.') return new Wind(0,0);
		//z is an edge case because it is actually much higher of a value than the char after it
		if(c == 'z') return new Wind(225, 10);
		if ('a' <= c && c <= 'e') return new Wind(0, 10 + (c - 'a') * 10);
		if ('f' <= c && c <= 'j') return new Wind(45, 10 + (c - 'f') * 10);
		if ('k' <= c && c <= 'o') return new Wind(90, 10 + (c - 'k') * 10);
		if ('p' <= c && c <= 't') return new Wind(135, 10 + (c - 'p') * 10);
		if ('u' <= c && c <= 'y') return new Wind(180, 10 + (c - 'u') * 10);
		//use @ instead of z because @ is what actually comes before A
		if ('A' <= c && c <= 'D') return new Wind(225, 10 + (c - '@') * 10);
		if ('E' <= c && c <= 'I') return new Wind(270, 10 + (c - 'E') * 10);
		if ('J' <= c && c <= 'N') return new Wind(315, 10 + (c - 'J') * 10);
		throw new RuntimeException("Invalid char passed to decodeChar");
	}

	//This is pretty but inefficient
	public static Wind decodeChar2(char c){
		if(c == '.') return new Wind(0,0);
		int i = 0;
		for (int d = 0; d < 360; d += 45) {
			for (int s = 10; s < 60; s += 10) {
				if(chars[i++] == c) return new Wind(d, s);
			}
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
