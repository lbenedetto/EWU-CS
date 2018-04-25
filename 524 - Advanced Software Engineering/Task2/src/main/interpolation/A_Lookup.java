package interpolation;//=============================================================================================================================================================

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

	double interpolate(Pair[] coordinates, double... v) {
		Pair open = coordinates[0];
		if (v[0] < open.x) throw new RuntimeException("independentVariable was smaller than allowed");
		for (Pair close : coordinates) {
			if (close.x < v[0]) {
				open = close;
			} else {
				return interpolate(v[0], open.x, close.x, open.interpolate(v), close.interpolate(v));
			}

		}
		throw new RuntimeException("independentVariable was larger than allowed");
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
