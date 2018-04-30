package task2.interpolation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import task2.WindLookup;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class A_LookupTest {
	private static final double EPSILON = 0.0000001;
	private static Method method;

	@BeforeAll
	static void init() throws Exception {
		method = A_Lookup.class.getDeclaredMethod("interpolate", double.class, double.class, double.class, double.class, double.class);
		method.setAccessible(true);
	}

	@Test
	void testValidInterpolate() throws Exception{
		double x1 = -10;
		double y1 = -10;
		double x2 = 10;
		double y2 = 10;

		for (int iv = (int) x1 * 100; iv <= x2 * 100; iv += 1) {
			double d1 = iv / 100.0;
			double d = (double) method.invoke(null, d1, x1, x2, y1, y2);
			System.out.printf("in:%f, out:%f\n", d1, d);
			assertEquals(d1, d, EPSILON);
		}
	}

	@Test
	void testInvalidInterpolate() throws Exception{
		method.invoke(null, -1, 0,1,10,20);
	}
}