import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

//=============================================================================================================================================================

/**
 * Performs two-dimensional interpolation as defined in the task writeup.
 */
public class Lookup2D extends A_Lookup {
	private Pair.TwoDPair[] data;
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Creates a two-dimensional lookup.
	 *
	 * @param filespec - the fully qualified filename of the data file
	 * @throws IOException if there is a file error
	 */
	public Lookup2D(final String filespec) throws IOException {
		var lines = Files.readAllLines(Paths.get(filespec));
		data = new Pair.TwoDPair[Integer.parseInt(lines.remove(0))];
		lines.remove(0);
		var cols = parseInts(lines.remove(0));
		int i = 0;
		for (String line : lines) {
			var values = parseInts(line);
			data[i++] = new Pair.TwoDPair(values[0], new Lookup1D(cols, values));
		}
	}


	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Determines the interpolated dependent variable from the independent variables.
	 *
	 * @param independentVariable1 - the independent variable in column order of the data file
	 * @param independentVariable2 - the independent variable in row order in the data file
	 * @return the dependent variable
	 */
	public double resolveDependentVariable(final double independentVariable1, final double independentVariable2) {
		return resolveDependentVariable(data, independentVariable1, independentVariable2);
	}

}
