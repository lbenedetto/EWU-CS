import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WindTest {
	private static char[] chars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N'};

	@Test
	void decodeValidChar1() {
		compareWind(Wind.decodeChar1('.'), 0, 0);
		int i = 0;
		for (int d = 0; d < 360; d += 45) {
			for (int s = 10; s < 60; s += 10) {
				System.out.printf("testing %s, expecting %d,%d\n", chars[i], d, s);
				compareWind(Wind.decodeChar1(chars[i]), d, s);
				i++;
			}
		}
	}

	@Test
	void decodeValidChar2() {
		compareWind(Wind.decodeChar2('.'), 0, 0);
		int i = 0;
		for (int d = 0; d < 360; d += 45) {
			for (int s = 10; s < 60; s += 10) {
				System.out.printf("testing %s, expecting %d,%d\n", chars[i], d, s);
				compareWind(Wind.decodeChar2(chars[i]), d, s);
				i++;
			}
		}
	}

	private void compareWind(Wind w, int direction, int speed) {
		assertEquals(direction, w.getDirection());
		assertEquals(speed, w.getSpeed());
	}

	@Test
	void decodeInvalidChar(){
		for(char c = 0; c < 127; c++){
			if(('A' <= c && c <= 'N') || ('a' <= c && c <= 'z') || c == '.') continue;
			System.out.printf("Testing invalid char '%s'\n", c);
			final char in = c;
			assertThrows(RuntimeException.class, () -> Wind.decodeChar1(in));
			assertThrows(RuntimeException.class, () -> Wind.decodeChar2(in));
		}
	}
}
