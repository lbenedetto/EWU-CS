import interpolation.Lookup1D;
import interpolation.Lookup2D;
import interpolation.Lookup3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LookupTest {
	@Test
	void test1DLookup() {
		assertEquals(25, new Lookup1D(
				new double[]{0, 3, 2, 1},
				new double[]{3, 10, 20, 30}
		).resolveDependentVariable(1.5));
	}

	@Test
	void test2DLookup() {
		assertEquals(45.0/2.0, new Lookup2D(
				new double[][]{
						new double[]{0.0, 0.9166666666666666, 0.8333333333333334, 0.75, 0.6666666666666666, 0.5833333333333334, 0.5, 0.4166666666666667, 0.3333333333333333, 0.25, 0.16666666666666666, 0.08333333333333333, 0.0},
						new double[]{0.9166666666666666, 0,0,0,0,0,0,0,0,0,0,0,0},
						new double[]{0.8333333333333334, 0,0,0,0,0,0,0,0,0,0,0,0},
						new double[]{0.75,               0,0,0,0,0,0,0,0,0,0,0,0},
						new double[]{0.6666666666666666, 0,0,0,0,0,0,0,0,0,0,0,0},
						new double[]{0.5833333333333334, 0,0,0,0,0,0,0,0,0,0,0,0},
						new double[]{0.5,                10,0,0,0,0,0,0,0,0,0,0,0},
						new double[]{0.4166666666666667, 45,0,0,0,0,0,0,0,0,0,0,0},
						new double[]{0.3333333333333333, 0,0,0,0,0,0,0,0,0,0,0,0},
						new double[]{0.25,               0,0,0,0,0,0,0,0,0,0,0,0},
						new double[]{0.16666666666666666,0,0,0,0,0,0,0,0,0,0,0,0},
						new double[]{0.08333333333333333,0,0,0,0,0,0,0,0,0,0,0,0},
						new double[]{0.0,                0,0,0,0,0,0,0,0,0,0,0,0}
				}).resolveDependentVariable(2.5, 2.5));
	}

	@Test
	void test3DLookup() {
		assertEquals(30.0, new Lookup3D(
				new double[][][]{
						new double[][]{
								new double[]{0.0, 0.9166666666666666, 0.8333333333333334, 0.75, 0.6666666666666666, 0.5833333333333334, 0.5, 0.4166666666666667, 0.3333333333333333, 0.25, 0.16666666666666666, 0.08333333333333333, 0.0},
								new double[]{0.9166666666666666, 0,0,0,0,0,0,0,0,0,0,0,0},
								new double[]{0.8333333333333334, 0,0,0,0,0,0,0,0,0,0,0,0},
								new double[]{0.75,               0,0,0,0,0,0,0,0,0,0,0,0},
								new double[]{0.6666666666666666, 0,0,0,0,0,0,0,0,0,0,0,0},
								new double[]{0.5833333333333334, 0,0,0,0,0,0,0,0,0,0,0,0},
								new double[]{0.5,                0,0,0,0,0,0,0,0,0,0,0,0},
								new double[]{0.4166666666666667, 0,0,0,0,0,0,0,0,0,0,0,0},
								new double[]{0.3333333333333333, 0,0,0,0,0,0,0,0,0,0,0,0},
								new double[]{0.25,               0,0,0,0,0,0,0,0,0,0,0,0},
								new double[]{0.16666666666666666,0,0,0,0,0,0,0,0,0,0,0,0},
								new double[]{0.08333333333333333,0,0,0,0,0,0,0,0,0,0,0,0},
								new double[]{0.0,                0,0,0,0,0,0,0,0,0,0,0,0}
						},
						new double[][]{
								new double[]{1, 3, 2, 1},
								new double[]{3, 11, 21, 31},
								new double[]{2, 41, 51, 61},
								new double[]{1, 71, 81, 91},
						}
				}).resolveDependentVariable(0, 2.5, 2.5));
	}

}