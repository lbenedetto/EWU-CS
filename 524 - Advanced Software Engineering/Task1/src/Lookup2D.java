import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

//=============================================================================================================================================================

/**
 * Performs two-dimensional interpolation as defined in the task writeup.
 */
public class Lookup2D extends A_Lookup {
	private TwoDPair[] data;
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Creates a two-dimensional lookup.
	 *
	 * @param filespec - the fully qualified filename of the data file
	 * @throws IOException if there is a file error
	 */
	public Lookup2D(final String filespec) throws IOException {
		var lines = Files.readAllLines(Paths.get(filespec));
		data = new TwoDPair[Integer.parseInt(lines.remove(0))];
		lines.remove(0);
		var cols = Util.parseInts(lines.remove(0));
		int i = 0;
		for (String line : lines) {
			var values = Util.parseInts(line);
			data[i++] = new TwoDPair(values[0], new Lookup1D(cols, values));
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
		// add your 2D interpolation lookup here

		//Find the two x coord bounds
		//Interpolate their y values with iV1
		//Interpolate the answer with iV2
		TwoDPair open = data[0];
		if (independentVariable1 < open.x) throw new RuntimeException("independentVariable was smaller than allowed");
		for (TwoDPair close : data) {
			if (close.x < independentVariable1) {
				open = close;
			} else {
				double y1 = open.y.resolveDependentVariable(independentVariable2);
				double y2 = close.y.resolveDependentVariable(independentVariable2);
				return interpolate(independentVariable1, open.x, close.x, y1, y2);
			}
		}
		throw new RuntimeException("independentVariable was larger than allowed");
	}

	public static class TwoDPair {
		double x;
		Lookup1D y;

		TwoDPair(double x, Lookup1D y) {
			this.x = x;
			this.y = y;
		}
	}

}
