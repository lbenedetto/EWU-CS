package s17cs350project.planner;

import java.io.InputStream;
import java.util.List;

public class WaypointPlanner {
	/**
	 * Creates planner. Defines axes and reads the coordinates [3]
	 *
	 * @param axisA      definition of how to interpret the A axis
	 * @param axisB      definition of how to interpret the B axis
	 * @param axisC      definition of how to interpret the C axis
	 * @param unitNative the unit of the native coordinate system
	 * @param instream   the input stream containing the coordinates
	 */
	public WaypointPlanner(E_AxisNative axisA, E_AxisNative axisB, E_AxisNative axisC, E_Unit unitNative, InputStream instream) {

	}

	/**
	 * Calculates the total distance along the path of coordinates [5]
	 *
	 * @param axes                  the axes of the coordinates to account for in calculating the distance
	 * @param isCanonicalElseNative whether to use the canonical order of the components in each coordinate triple as
	 *                              defined in the constructor or the original native order from the input stream
	 * @param unit                  the unit of the distances
	 * @return the total distance
	 */
	double calculateDistance(E_AxisCombinationNeutral axes, boolean isCanonicalElseNative, E_Unit unit) {
		return 0.0;
	}

	/**
	 * Calculates the intermediate distances between coordinates along the path of coordinates. [30]
	 *
	 * @param axes                  the axes of the coordinates to account for in calculating the distance
	 * @param isCanonicalElseNative whether to use the canonical order of the components in each coordinate triple as defined in the constructor or the original native order from the input stream
	 * @param unit                  the unit of the distances
	 * @return the intermediate distances
	 */
	List<Double> calculateDistances(E_AxisCombinationNeutral axes, boolean isCanonicalElseNative, E_Unit unit) {
		return null;
	}

	/**
	 * Gets the definition of how to interpret the A axis. [1]
	 *
	 * @return the definition
	 */
	public E_AxisNative getAxisA() {
		return null;
	}

	/**
	 * Gets the definition of how to interpret the B axis. [1]
	 *
	 * @return the definition
	 */
	public E_AxisNative getAxisB() {
		return null;
	}

	/**
	 * Gets the definition of how to interpret the C axis. [1]
	 *
	 * @return the definition
	 */
	public E_AxisNative getAxisC() {
		return null;
	}

	/**
	 * Returns the coordinates read from the input stream. [5]
	 *
	 * @param isCanonicalElseNative whether to use the canonical order of the components in each coordinate triple as
	 *                              defined in the constructor or the original native order from the input stream
	 * @param unit                  the unit of the coordinates
	 * @return the coordinates
	 */
	public Coordinates getCoordinates(boolean isCanonicalElseNative, E_Unit unit) {
		return null;
	}

	/**
	 * Gets the unit of the native coordinate system. [1]
	 *
	 * @return the unit
	 */
	public E_Unit getUnitNative() {
		return null;
	}

	/**
	 * Defines a generic triple of coordinates defined as (first,second,third). Each word corresponds to the position,
	 * but the interpretation depends on which axis is mapped to it; for example, (z,y,x) or (A,B,C).
	 */
	public static class Coordinates {
		/**
		 * Creates a triple [3]
		 *
		 * @param first  the first coordinate
		 * @param second the second coordinate
		 * @param third  the third coordinate
		 */
		Coordinates(double first, double second, double third) {

		}

		/**
		 * Outputs the triple in the form (<i>first</i> <i>second</i> <i>third</i>). [1]
		 *
		 * @return the string
		 */
		@Override
		public String toString() {
			return null;
		}
	}

	/**
	 * Defines which components of a coordinate triple to use in distance calculations. [7]
	 */
	public enum E_AxisCombinationNeutral {
		FIRST,                  //use the first component only; e.g., (x2 - x1)
		FIRST_SECOND,           //use the second component only; e.g., (y2 - y1)
		FIRSTS_SECOND_THRID,    //use the third component only; e.g., (z2 - z1)
		FIRST_THIRD,            //use the first and second components only; e.g., ((x2,y2) - (x1,y1))
		SECOND,                 //use the first and third components only
		SECOND_THIRD,           //use the second and third components only
		THIRD                   //use all components; e.g., ((x2,y2,z2) - (x1,y1,z1))
	}

	/**
	 * Defines how to interpret a lettered axis; i.e., axisA, axisB, or axisC. [6]
	 */
	public enum E_AxisNative {
		X_MINUS,    //interprets the lettered axis as x with negative values going in the lettered positive direction
		X_PLUS,     //interprets the lettered axis as x with positive values going in the lettered positive direction
		Y_MINUS,    //interprets the lettered axis as y with negative values going in the lettered positive direction
		Y_PLUS,     //interprets the lettered axis as y with positive values going in the lettered positive direction
		Z_MINUS,    //interprets the lettered axis as z with negative values going in the lettered positive direction
		Z_PLUS      //interprets the lettered axis as z with positive values going in the lettered positive direction
	}

	/**
	 * Defines the unit of the native coordinate system. [4]
	 */
	public enum E_Unit {
		FEET, KILOMETERS, METERS, MILES
	}
}
