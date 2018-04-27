import org.junit.jupiter.api.Test;
import task2.WindLookup;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class WindLookupTest {
	private static char[] chars = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N'};
	private static double[] ALTITUDES = new double[]{0, 3000, 6000, 9000, 12000, 15000};
	private static final double EPSILON = 0.0000001;

	@Test
	void invalidInterpolationTest() throws Exception {
		double[] d = humanReadableCoordinatesToStupidCoordinates(0, 0, 1.5, 5.5, .5);
		System.out.println(Arrays.toString(d));
		assertThrows(
				RuntimeException.class,
				() -> {
					var wl = new WindLookup("simpleInput");
					wl.interpolateDirection(d[0], d[1], d[2], d[3], d[4], d[5], -1);
				},
				"independentVariable was smaller than allowed"
		);
	}

	@Test
	void interpolateDirectionTest() throws Exception {
		double[] d = humanReadableCoordinatesToStupidCoordinates(0, 0, 1.5, 5.5, .5);
		System.out.println(Arrays.toString(d));
		double actual = new WindLookup("simpleInput").interpolateDirection(d[0], d[1], d[2], d[3], d[4], d[5], d[6]);
		assertEquals(157.5, actual, EPSILON);
		/*
		 * 0->  22.5  <-45   180-> 202.5 <-225
		 *       |                  |
		 *       v                  v
		 *       67.5 ->  157.5 <- 247.5
		 *       ^                  ^
		 *       |                  |
		 * 90-> 112.5 <-135  270-> 292.5 <-315
		 */
	}

	@Test
	void interpolateSpeedTest() throws Exception {
		double[] d = humanReadableCoordinatesToStupidCoordinates(0, 0, 1.5, 5.5, .5);
		System.out.println(Arrays.toString(d));
		double actual = new WindLookup("simpleInput").interpolateSpeed(d[0], d[1], d[2], d[3], d[4], d[5], d[6]);
		assertEquals(26.25, actual, EPSILON);
		/*
		 * 10-> 15 <-20     50-> 30 <-10
		 *       |               |
		 *       v               v
		 *       25 -> 26.25 <- 27.5
		 *       ^               ^
		 *       |               |
		 * 30-> 35 <-40     20-> 25 <- 30
		 */
	}

	/**
	 * Bottom left origin
	 *
	 * @param x left-right axis
	 * @param y up-down axis
	 * @param z layer
	 * @return double[] representing dms of lat and lng, and the altitude
	 */
	private double[] humanReadableCoordinatesToStupidCoordinates(int originX, int originY, double x, double y, double z) {
		double[] ret = new double[7];
		var d = decToDMS(originX + (((11.0 - x) * 5.0) / 60.0));
		ret[0] = d[0];
		ret[1] = d[1];
		ret[2] = d[2];
		d = decToDMS(originY + (((11.0 - y) * 5.0) / 60.0));
		ret[3] = d[0];
		ret[4] = d[1];
		ret[5] = d[2];
		ret[6] = (5 - z) * 3000;
		return ret;
	}

	private double[] decToDMS(double x) {
		double d = Math.floor(x);
		double m = Math.floor((x - d) * 60);
		double s = (x - d - (m / 60)) * 3600;
		return new double[]{d, m, s};
	}

	@Test
	void testCoordinateConverter() {
		System.out.println(Arrays.toString(humanReadableCoordinatesToStupidCoordinates(0, 0, 0, 5.5, 0)));
//		assertEquals(new double[]{0, .2, 3, 0, 5, 6, 1500}, humanReadableCoordinatesToStupidCoordinates(0,0, 0, 5.5, 1.5));
	}

	@Test
	void decodeValidChar() {
		System.out.printf("testing %s, expecting %d,%d\n", '.', 0, 0);
		compareWind(WindLookup.decodeChar('.'), new int[]{0, 0});
		int i = 0;
		for (int d = 0; d < 360; d += 45) {
			for (int s = 10; s < 60; s += 10) {
				System.out.printf("testing %s, expecting %d,%d\n", chars[i], d, s);
				compareWind(WindLookup.decodeChar(chars[i]), new int[]{d, s});
				i++;
			}
		}
	}

	private void compareWind(int[] actual, int[] expected) {
		assertArrayEquals(expected, actual);
	}

	@Test
	void decodeInvalidChar() {
		for (char c = 0; c < 127; c++) {
			if (('A' <= c && c <= 'N') || ('a' <= c && c <= 'z') || c == '.') continue;
			System.out.printf("Testing invalid char '%s'\n", c);
			final char in = c;
			assertThrows(RuntimeException.class, () -> WindLookup.decodeChar(in));
		}
	}

	private static String printCube(double[][][] cube) {
		var b = new StringBuilder();
		for (int i = 0; i < cube.length; i++) {
			for (int j = 0; j < cube[i].length; j++) {
				b.append(Arrays.toString(cube[i][j])).append("\n");
			}
			b.append("\n");
		}
		return b.toString();
	}
}
