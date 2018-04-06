import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

//=============================================================================================================================================================

/**
 * Defines the simulation that generates the Gnuplot scripts for each part of the task.
 */
public class Simulation {
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------
	private static PrintStream out;
	private static int i = 1;

	/**
	 * Executes the simulation.
	 *
	 * @param arguments - there are no arguments
	 * @throws IOException if there is a file error
	 */
	public static void main(final String[] arguments) {
		runTest(() -> new Simulation().generateGnuplot1D(.1));
		runTest(() -> new Simulation().generateGnuplot2D(.25, 2.5));
		runTest(() -> new Simulation().generateGnuplot3D(.5, 5, 500));
	}

	private static void runTest(Runnable r) {
		try {
			out = new PrintStream(new File("myOutput" + i + "D.gnu"));
			r.run();
			out.close();
			i++;
		} catch (Exception e) {
			//Ignore
		}
	}
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Creates the simulation.
	 */
	public Simulation() {
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Generates the Gnuplot script that demonstrates the performance of the one-dimensional interpolation.
	 *
	 * @throws IOException if there is a file error
	 */
	public void generateGnuplot1D(double step) {
		try {
			out.println("# gnuplot output of 1D data");
			out.println("# copy and save to a file and plot with load 'filename'");
			out.println("# remove 'with lines' to show the data points");
			out.println();
			out.println("reset");
			out.println("set xlabel 'independent'");
			out.println("set ylabel 'dependent'");
			out.println("plot '-' with lines");
			out.println();

			Lookup1D lookup = new Lookup1D("data1d.csv");


			for (double iIndependentVariable = 1; iIndependentVariable <= 10; iIndependentVariable += step) {
				double dependentVariable = lookup.resolveDependentVariable(iIndependentVariable);

				out.println(iIndependentVariable + " " + dependentVariable);
			}

			out.println("end");
		} catch (Exception e) {
			//Ignore
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Generates the Gnuplot script that demonstrates the performance of the two-dimensional interpolation.
	 *
	 * @throws IOException if there is a file error
	 */
	public void generateGnuplot2D(double step1, double step2) {
		try {
			out.println("# gnuplot output of 2D data");
			out.println("# copy and save to a file and plot with load 'filename'");
			out.println("# remove 'with lines' to show the data points");
			out.println();
			out.println("reset");
			out.println("set xlabel 'independent1'");
			out.println("set ylabel 'independent2'");
			out.println("set zlabel 'dependent'");
			out.println("set xyplane at 0");
			out.println("set pm3d at b");
			out.println("splot '-' with lines");
			out.println();

			Lookup2D lookup = new Lookup2D("data2d.csv");

			for (double iIndependentVariable1 = 1; iIndependentVariable1 <= (4 - step1); iIndependentVariable1 += step1) {
				for (double iIndependentVariable2 = 10; iIndependentVariable2 <= (40 - step2); iIndependentVariable2 += step2) {
					// build the mesh structure in Gnuplot format
					double dependentVariableA = lookup.resolveDependentVariable(iIndependentVariable1, iIndependentVariable2);
					double dependentVariableB = lookup.resolveDependentVariable((iIndependentVariable1 + step1), iIndependentVariable2);
					double dependentVariableC = lookup.resolveDependentVariable((iIndependentVariable1 + step1), (iIndependentVariable2 + step2));
					double dependentVariableD = lookup.resolveDependentVariable(iIndependentVariable1, (iIndependentVariable2 + step2));

					out.println(iIndependentVariable1 + " " + iIndependentVariable2 + " " + dependentVariableA);
					out.println((iIndependentVariable1 + step1) + " " + iIndependentVariable2 + " " + dependentVariableB);
					out.println((iIndependentVariable1 + step1) + " " + (iIndependentVariable2 + step2) + " " + dependentVariableC);
					out.println(iIndependentVariable1 + " " + (iIndependentVariable2 + step2) + " " + dependentVariableD);
					out.println(iIndependentVariable1 + " " + iIndependentVariable2 + " " + dependentVariableA);
					out.println();
				}

				out.println();
			}

			out.println("end");
		} catch (Exception e) {
			//Ignore
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Generates the Gnuplot script that demonstrates the performance of the three-dimensional interpolation.
	 *
	 * @throws IOException if there is a file error
	 */
	public void generateGnuplot3D(double step1, double step2, double step3) {
		try {
			out.println("# gnuplot output of 3D data");
			out.println("# copy and save to a file and plot with load 'filename'");
			out.println("# remove 'with lines' to show the data points");
			out.println();
			out.println("reset");
			out.println("set xlabel 'independent1'");
			out.println("set ylabel 'independent2'");
			out.println("set zlabel 'dependent'");
			out.println("set xyplane at 0");
			out.print("splot ");

			int numDatasets = (int) Math.ceil(((3000 - 1000) / step3) + 1);

			for (int iDataset = 0; iDataset < numDatasets; iDataset++) {
				out.print("'-' with lines title columnheader(1), ");
			}

			out.println();
			out.println();

			Lookup3D lookup = new Lookup3D("data3d.csv");

			for (double iIndependentVariable3 = 1000; iIndependentVariable3 <= 3000; iIndependentVariable3 += step3) {
				out.println("\"independent3=" + iIndependentVariable3 + "\"");

				for (double iIndependentVariable1 = 1; iIndependentVariable1 <= (4 - step1); iIndependentVariable1 += step1) {
					for (double iIndependentVariable2 = 10; iIndependentVariable2 <= (40 - step2); iIndependentVariable2 += step2) {
						// build the mesh structure in Gnuplot format
						double dependentVariableA = lookup.resolveDependentVariable(iIndependentVariable1, iIndependentVariable2, iIndependentVariable3);
						double dependentVariableB = lookup.resolveDependentVariable((iIndependentVariable1 + step1), iIndependentVariable2, iIndependentVariable3);
						double dependentVariableC = lookup.resolveDependentVariable((iIndependentVariable1 + step1),
								(iIndependentVariable2 + step2),
								iIndependentVariable3);
						double dependentVariableD = lookup.resolveDependentVariable(iIndependentVariable1, (iIndependentVariable2 + step2), iIndependentVariable3);

						out.println(iIndependentVariable1 + " " + iIndependentVariable2 + " " + dependentVariableA);
						out.println((iIndependentVariable1 + step1) + " " + iIndependentVariable2 + " " + dependentVariableB);
						out.println((iIndependentVariable1 + step1) + " " + (iIndependentVariable2 + step2) + " " + dependentVariableC);
						out.println(iIndependentVariable1 + " " + (iIndependentVariable2 + step2) + " " + dependentVariableD);
						out.println(iIndependentVariable1 + " " + iIndependentVariable2 + " " + dependentVariableA);
						out.println();
					}

					out.println();
				}

				out.println("EOF");
			}
		} catch (Exception e) {
			//Ignore
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Runs a single hardcoded test that demonstrates the performance of the one-dimensional interpolation.
	 *
	 * @throws IOException if there is a file error
	 */
	public void testLookup1D() throws IOException {
		Lookup1D lookup = new Lookup1D("/home/dtappan/workspace/w17cs439-task-3/data1d.csv");

		double result = lookup.resolveDependentVariable(1.1);

		out.println(result);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Runs a single hardcoded test that demonstrates the performance of the two-dimensional interpolation.
	 *
	 * @throws IOException if there is a file error
	 */
	public void testLookup2D() throws IOException {
		Lookup2D lookup = new Lookup2D("/home/dtappan/workspace/w17cs439-task-3/data2d.csv");

		double result = lookup.resolveDependentVariable(1, 10);

		out.println(result);
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Runs a single hardcoded test that demonstrates the performance of the three-dimensional interpolation.
	 *
	 * @throws IOException if there is a file error
	 */
	public void testLookup3D() throws IOException {
		Lookup3D lookup = new Lookup3D("/home/dtappan/workspace/w17cs439-task-3/data3d.csv");

		double result = lookup.resolveDependentVariable(1, 10, 1000);

		out.println(result);
	}
}