package interpolation;


//=============================================================================================================================================================

/**
 * Performs one-dimensional interpolation as defined in the task writeup.
 */
public class Lookup1D extends A_Lookup {
	private Pair.Pair1D[] coordinates;

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	public Lookup1D(double[] rowLabels, double[] row) {
		coordinates = new Pair.Pair1D[row.length - 1];
		for (int i = 1; i < row.length; i++) {
			//In this case, 1D-Pair is a mapping of longitude to a point
			coordinates[i - 1] = new Pair.Pair1D(rowLabels[i], row[i]);
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Determines the interpolated dependent variable from the independent variable.
	 *
	 * @param independentVariable - the independent variable in column one of the data file
	 * @return the dependent variable
	 */
	public double resolveDependentVariable(final double independentVariable) {
		return interpolate(coordinates, independentVariable);
	}


}
