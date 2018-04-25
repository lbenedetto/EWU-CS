import interpolation.Lookup1D;
import interpolation.Lookup2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LookupTest {
	@Test
	void test1DLookup() {
		assertEquals(15.0, new Lookup1D(new double[]{10, 20, 30}, new double[]{1, 2, 3}).resolveDependentVariable(1.5));
	}

	@Test
	void test2DLookup(){
		assertEquals(30.0, new Lookup2D(
				new double[][]{
					new double[]{0, 1, 2, 3},
					new double[]{1, 10, 20, 30},
					new double[]{2, 40, 50, 60},
					new double[]{3, 70, 80, 90},
				}).resolveDependentVariable(1.5, 1.5));
	}

}