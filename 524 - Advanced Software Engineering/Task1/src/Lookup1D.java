import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

//=============================================================================================================================================================

/**
 * Performs one-dimensional interpolation as defined in the task writeup.
 */
public class Lookup1D extends A_Lookup {
	private Pair.OneDPair[] data;
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Creates a one-dimensional lookup.
	 *
	 * @param filespec - the fully qualified filename of the data file
	 * @throws IOException if there is a file error
	 */
	public Lookup1D(final String filespec) throws IOException {
		var lines = Files.readAllLines(Paths.get(filespec));
		data = new Pair.OneDPair[Integer.parseInt(lines.remove(0))];
		var i = 0;
		for (String line : lines) {
			var values = parseInts(line);
			data[i++] = new Pair.OneDPair(values[0], values[1]);
		}
	}

	public Lookup1D(double[] row, double[] col) {
		data = new Pair.OneDPair[row.length - 1];
		for (int i = 1; i < row.length; i++) {
			data[i - 1] = new Pair.OneDPair(row[i], col[i]);
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
		// add your 1D interpolation lookup here
		return resolveDependentVariable(data, independentVariable);
	}


}
