//=============================================================================================================================================================

import java.util.Arrays;

/**
 * Defines the shared elements of the lookup subclasses.
 */
public abstract class A_Lookup {
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Creates a lookup.
	 */
	public A_Lookup() {
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Performs linear interpolation to determine the dependent variable based on the independent variable.
	 *
	 * @param independentVariable      - the independent variable, which must lie inclusively between {@code independentVariableOpen} and
	 *                                 {@code independentVariableClose}
	 * @param independentVariableOpen  - the value of the independent data element at or immediately before {@code independentVariable}
	 * @param independentVariableClose - the value of the independent data element at or immediately after {@code independentVariable}
	 * @param dependentVariableOpen    - the value of the dependent data element corresponding to {@code independentVariableOpen}
	 * @param dependentVariableClose   - the value of the dependent data element corresponding to {@code independentVariableClose}
	 * @return the interpolated value
	 */
	protected double interpolate(final double independentVariable,
	                             final double independentVariableOpen,
	                             final double independentVariableClose,
	                             final double dependentVariableOpen,
	                             final double dependentVariableClose) {
		// add your interpolation code here
		if (independentVariableClose == independentVariableOpen) return dependentVariableOpen;
		return (((independentVariable - independentVariableOpen) /
				(independentVariableClose - independentVariableOpen)) * (dependentVariableClose - dependentVariableOpen)) + dependentVariableOpen;
	}

	double resolveDependentVariable(Pair[] data, double... v) {
		Pair open = data[0];
		if (v[0] < open.x) throw new RuntimeException("independentVariable was smaller than allowed");
		for (Pair close : data) {
			if (close.x < v[0]) {
				open = close;
			} else {
				return interpolate(v[0], open.x, close.x, open.interpolate(v), close.interpolate(v));
			}

		}
		throw new RuntimeException("independentVariable was larger than allowed");
	}

	static double[] parseInts(String s) {
		return Arrays.stream(s.split(",")).mapToDouble(Double::parseDouble).toArray();
	}


	public static abstract class Pair {
		double x;

		public abstract double interpolate(double... iv);

		public static class OneDPair extends Pair {
			double y;

			OneDPair(double x, double y) {
				this.x = x;
				this.y = y;
			}

			@Override
			public double interpolate(double... iv) {
				return y;
			}
		}

		public static class TwoDPair extends Pair {
			Lookup1D y;

			TwoDPair(double x, Lookup1D y) {
				this.x = x;
				this.y = y;
			}

			public double interpolate(double... iv) {
				return y.resolveDependentVariable(iv[1]);
			}
		}

		public static class ThreeDPair extends Pair {
			Lookup2D y;

			ThreeDPair(double x, Lookup2D y) {
				this.x = x;
				this.y = y;
			}

			public double interpolate(double... iv) {
				return y.resolveDependentVariable(iv[1], iv[2]);
			}
		}
	}
}
