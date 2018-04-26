package task2.interpolation;//=============================================================================================================================================================

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
	 * Performs linear task2.interpolation to determine the dependent variable based on the independent variable.
	 *
	 * @param iv      - the independent variable, which must lie inclusively between {@code independentVariableOpen} and
	 *                {@code independentVariableClose}
	 * @param ivOpen  - the value of the independent data element at or immediately before {@code independentVariable}
	 * @param ivClose - the value of the independent data element at or immediately after {@code independentVariable}
	 * @param dvOpen  - the value of the dependent data element corresponding to {@code independentVariableOpen}
	 * @param dvClose - the value of the dependent data element corresponding to {@code independentVariableClose}
	 * @return the interpolated value
	 */
	protected double interpolate(final double iv,
	                             final double ivOpen,
	                             final double ivClose,
	                             final double dvOpen,
	                             final double dvClose) {
		// add your task2.interpolation code here
		if (ivClose == ivOpen) return dvOpen;

		var d1 = iv - ivOpen;
		var d2 = ivClose - ivOpen;
		var d3 = dvClose - dvOpen;
		var d4 = d1 / d2;
		var d5 = d4 * d3;
		return d5 + dvOpen;
	}

	double interpolate(Pair[] coordinates, double... v) {
		Pair open = coordinates[0];
		if (v[0] > open.x) throw new RuntimeException("independentVariable was larger than allowed");
		for (Pair close : coordinates) {
			if (v[0] < close.x) {
				open = close;
			} else {
				return interpolate(v[0], open.x, close.x, open.interpolate(v), close.interpolate(v));
			}

		}
		throw new RuntimeException("independentVariable was smaller than allowed");
	}

	public static abstract class Pair {
		double x;//Independent variable (lat, lng, or alt)

		public abstract double interpolate(double... iv);

		public static class Pair1D extends Pair {
			double y;//Dependent variable (speed or direction)

			Pair1D(double x, double y) {
				this.x = x;
				this.y = y;
			}

			@Override
			public double interpolate(double... iv) {
				return y;
			}
		}

		public static class Pair2D extends Pair {
			Lookup1D y;//Dependent variables. If x is lat, then y would be lng/alt pairs

			Pair2D(double x, Lookup1D y) {
				this.x = x;
				this.y = y;
			}

			public double interpolate(double... iv) {
				return y.resolveDependentVariable(iv[1]);
			}
		}

		public static class Pair3D extends Pair {
			Lookup2D y;

			Pair3D(double x, Lookup2D y) {
				this.x = x;
				this.y = y;
			}

			public double interpolate(double... iv) {
				return y.resolveDependentVariable(iv[1], iv[2]);
			}
		}
	}
}
