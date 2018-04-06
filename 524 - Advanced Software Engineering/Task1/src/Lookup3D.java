import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
//=============================================================================================================================================================

/**
 * Performs three-dimensional interpolation as defined in the task writeup.
 */
public class Lookup3D extends A_Lookup {
	private ThreeDPair[] data;
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Creates a three-dimensional lookup.
	 *
	 * @param filespec - the fully qualified filename of the registry of data files
	 * @throws IOException if there is a file error
	 */
	public Lookup3D(final String filespec) throws IOException {
		var lines = Files.readAllLines(Paths.get(filespec));
		data = new ThreeDPair[Integer.parseInt(lines.remove(0))];
		int i = 0;
		for(String line : lines){
			var values = line.split(",");
			data[i++] = new ThreeDPair(Integer.parseInt(values[0]), new Lookup2D(values[1]));
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
		// add your 3D interpolation lookup here
		//Find the two x coord bounds
		//Interpolate their y values with iV1
		//Interpolate the answer with iV2
		ThreeDPair open = data[0];
		if (independentVariable1 < open.x) throw new RuntimeException("independentVariable was smaller than allowed");
		for (ThreeDPair close : data) {
			if (close.x < independentVariable3) {
				open = close;
			} else {
				double y1 = open.y.resolveDependentVariable(independentVariable1, independentVariable2);
				double y2 = close.y.resolveDependentVariable(independentVariable1, independentVariable2);
				return interpolate(independentVariable3, open.x, close.x, y1, y2);
			}
		}
		throw new RuntimeException("independentVariable was larger than allowed");
	}

	public static class ThreeDPair{
		double x;
		Lookup2D y;

		ThreeDPair(double x, Lookup2D y) {
			this.x = x;
			this.y = y;
		}
	}
}
