package task2.interpolation;

//=============================================================================================================================================================

/**
 * Performs three-dimensional task2.task2.interpolation as defined in the task writeup.
 */
public class Lookup3D extends A_Lookup {
	private Pair.Pair3D[] coordinates;
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Creates a three-dimensional lookup.
	 * threeD[alt][lat][lng]
	 */
	public Lookup3D(double[][][] cube) {
		coordinates = new Pair.Pair3D[cube.length];
		for (int i = 0; i < cube.length; i++) {
			//In this case, 3D-Pair is a mapping of altitude to a 2D plane
			coordinates[i] = new Pair.Pair3D(cube[i][0][0], new Lookup2D(cube[i]));
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Determines the interpolated dependent variable from the independent variables.
	 *
	 * @param altitude - the independent variable in column order of the data file
	 * @param latitude - the independent variable in row order in the data file
	 * @param longitude - the independent variable referring to the data file to use for the third dimension
	 * @return the dependent variable
	 */
	public double resolveDependentVariable(final double altitude, final double latitude, final double longitude) {
		return interpolate(coordinates, altitude, latitude, longitude);
	}
}
