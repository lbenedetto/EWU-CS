package cs524builder.parserdemo;

//=============================================================================================================================================================

/**
 * Creates a builder that reads and executes a component-definition file.
 *
 * @author Dan Tappan [06.02.13]
 */
public class BuilderDemo {
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Executes the builder.
	 *
	 * @param arguments - the fully qualified filename of the definition file
	 */
	public static void main(final String[] arguments) {
		if (arguments.length != 2) {
			throw new RuntimeException("usage: input_filename output_filename ");
		}

		try {
			BuilderParserDemo parser = new BuilderParserDemo(arguments[0], arguments[1]);

			parser.parse();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
