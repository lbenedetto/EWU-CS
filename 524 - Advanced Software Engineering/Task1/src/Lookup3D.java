import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
//=============================================================================================================================================================

/**
 * Performs three-dimensional interpolation as defined in the task writeup.
 */
public class Lookup3D extends A_Lookup {
	private Pair.ThreeDPair[] data;
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Creates a three-dimensional lookup.
	 *
	 * @param filespec - the fully qualified filename of the registry of data files
	 * @throws IOException if there is a file error
	 */
	public Lookup3D(final String filespec) throws IOException {
		var lines = Files.readAllLines(Paths.get(filespec));
		data = new Pair.ThreeDPair[Integer.parseInt(lines.remove(0))];
		int i = 0;
		for(String line : lines){
			var values = line.split(",");
			data[i++] = new Pair.ThreeDPair(Integer.parseInt(values[0]), new Lookup2D(values[1]));
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Determines the interpolated dependent variable from the independent variables.
	 *
	 * @param independentVariable1 - the independent variable in column order of the data file
	 * @param independentVariable2 - the independent variable in row order in the data file
	 * @param independentVariable3 - the independent variable referring to the data file to use for the third dimension
	 * @return the dependent variable
	 */
	public double resolveDependentVariable(final double independentVariable1, final double independentVariable2, final double independentVariable3) {
		return resolveDependentVariable(data, independentVariable3, independentVariable1, independentVariable2);
	}
}
