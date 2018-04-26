package interpolation;

//=============================================================================================================================================================

/**
 * Performs two-dimensional interpolation as defined in the task writeup.
 */
public class Lookup2D extends A_Lookup {
	private Pair.Pair2D[] coordinates;
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	public Lookup2D(double[][] plane) {
		coordinates = new Pair.Pair2D[plane.length - 1];
		for (int i = 1; i < plane.length; i++) {
			//In this case, 2D-Pair is a mapping of latitude to a 1D row (rowLabel,row)
			coordinates[i-1] = new Pair.Pair2D(plane[i][0], new Lookup1D(plane[0], plane[i]));
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Determines the interpolated dependent variable from the independent variables.
	 *
	 * @param latitude - the independent variable in column order of the data file
	 * @param longitude - the independent variable in row order in the data file
	 * @return the dependent variable
	 */
	public double resolveDependentVariable(final double latitude, final double longitude) {
		//TODO: Maybe fix these having to be backwards?
		return interpolate(coordinates, longitude, latitude);
	}

}
