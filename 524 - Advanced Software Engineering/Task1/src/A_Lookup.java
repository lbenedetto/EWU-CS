//=============================================================================================================================================================

/**
 * Defines the shared elements of the lookup subclasses.
 */
public abstract class A_Lookup {
	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Creates a lookup.
	 */
	public A_Lookup() {
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Performs linear interpolation to determine the dependent variable based on the independent variable.
	 *
	 * @param independentVariable      - the independent variable, which must lie inclusively between {@code independentVariableOpen} and
	 *                                 {@code independentVariableClose}
	 * @param independentVariableOpen  - the value of the independent data element at or immediately before {@code independentVariable}
	 * @param independentVariableClose - the value of the independent data element at or immediately after {@code independentVariable}
	 * @param dependentVariableOpen    - the value of the dependent data element corresponding to {@code independentVariableOpen}
	 * @param dependentVariableClose   - the value of the dependent data element corresponding to {@code independentVariableClose}
	 * @return the interpolated value
	 */
	protected double interpolate(final double independentVariable,
	                             final double independentVariableOpen,
	                             final double independentVariableClose,
	                             final double dependentVariableOpen,
	                             final double dependentVariableClose) {
		// add your interpolation code here
		if(independentVariableClose == independentVariableOpen) return dependentVariableOpen;
		return (((independentVariable - independentVariableOpen) /
				(independentVariableClose - independentVariableOpen)) * (dependentVariableClose - dependentVariableOpen)) + dependentVariableOpen;
	}
}
