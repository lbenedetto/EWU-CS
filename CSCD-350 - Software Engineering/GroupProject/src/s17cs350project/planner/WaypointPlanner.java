package s17cs350project.planner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaypointPlanner {
	private E_AxisNative[] axesNative;
	private E_Unit unitNative;
	private BufferedReader bufferedReader;

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
		axesNative = new E_AxisNative[]{axisA, axisB, axisC};
		this.unitNative = unitNative;
		bufferedReader = new BufferedReader(new InputStreamReader(instream));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WaypointPlanner that = (WaypointPlanner) o;
		return Arrays.equals(axesNative, that.axesNative)
				&& unitNative == that.unitNative
				&& (bufferedReader != null ? bufferedReader.equals(that.bufferedReader) : that.bufferedReader == null);
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
	public double calculateDistance(E_AxisCombinationNeutral axes, boolean isCanonicalElseNative, E_Unit unit) {
		return calculateDistances(axes, isCanonicalElseNative, unit).stream().reduce(0.0, Double::sum);
	}

	/**
	 * Calculates the intermediate distances between coordinates along the path of coordinates. [30]
	 *
	 * @param axes                  the axes of the coordinates to account for in calculating the distance
	 * @param isCanonicalElseNative whether to use the canonical order of the components in each coordinate triple as defined in the constructor or the original native order from the input stream
	 * @param unit                  the unit of the distances
	 * @return the intermediate distances
	 */
	public List<Double> calculateDistances(E_AxisCombinationNeutral axes, boolean isCanonicalElseNative, E_Unit unit) {
		ArrayList<Double> distances = new ArrayList<>();
		List<Coordinates> coordinates = getCoordinates(isCanonicalElseNative, unit);
		Coordinates prev = null;
		for (Coordinates curr : coordinates) {
			if (prev == null) prev = curr;
			distances.add(prev.distanceTo(curr, axes));
		}
		return distances;
	}

	/**
	 * Gets the definition of how to interpret the A axis. [1]
	 *
	 * @return the definition
	 */
	public E_AxisNative getAxisA() {
		return axesNative[0];
	}

	/**
	 * Gets the definition of how to interpret the B axis. [1]
	 *
	 * @return the definition
	 */
	public E_AxisNative getAxisB() {
		return axesNative[1];
	}

	/**
	 * Gets the definition of how to interpret the C axis. [1]
	 *
	 * @return the definition
	 */
	public E_AxisNative getAxisC() {
		return axesNative[2];
	}

	/**
	 * Returns the coordinates read from the input stream. [5]
	 *
	 * @param isCanonicalElseNative whether to use the canonical order of the components in each coordinate triple as
	 *                              defined in the constructor or the original native order from the input stream
	 * @param unit                  the unit of the coordinates
	 * @return the coordinates
	 */
	public List<Coordinates> getCoordinates(boolean isCanonicalElseNative, E_Unit unit) {
		//If its canonical, take them as is. If its native, convert them from the specified unit to the nativeUnit
		ArrayList<Coordinates> coords = new ArrayList<>();
		bufferedReader.lines().forEach(line -> {
			double[] c = Arrays.stream(line.split(","))
					.map(String::trim)
					.mapToDouble(Double::parseDouble)
					.map(v -> convert(v, isCanonicalElseNative, unit))
					.toArray();
			coords.add(new Coordinates(axesNative[0].orient(c[0]), axesNative[1].orient(c[1]), axesNative[2].orient(c[2])));
		});
		return coords;
	}

	/**
	 * Converts the specified double to the specified unit depending on whether we are in canonical or native mode
	 *
	 * @param v                     the value to be converted
	 * @param isCanonicalElseNative the mode we are in
	 * @param unit                  the target unit
	 * @return converted (or not) value
	 */
	private double convert(double v, boolean isCanonicalElseNative, E_Unit unit) {
		if (!isCanonicalElseNative)
			return unit.convertTo(v, unitNative);
		return v;
	}

	/**
	 * Gets the unit of the native coordinate system. [1]
	 *
	 * @return the unit
	 */
	public E_Unit getUnitNative() {
		return unitNative;
	}

	/**
	 * Defines a generic triple of coordinates defined as (first,second,third). Each word corresponds to the position,
	 * but the interpretation depends on which axis is mapped to it; for example, (z,y,x) or (A,B,C).
	 */
	public static class Coordinates {
		double first;
		double second;
		double third;

		/**
		 * Creates a triple [3]
		 *
		 * @param first  the first coordinate
		 * @param second the second coordinate
		 * @param third  the third coordinate
		 */
		public Coordinates(double first, double second, double third) {
			this.first = first;
			this.second = second;
			this.third = third;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Coordinates that = (Coordinates) o;
			return Double.compare(that.first, first) == 0 && Double.compare(that.second, second) == 0 && Double.compare(that.third, third) == 0;
		}

		/**
		 * Outputs the triple in the form (<i>first</i> <i>second</i> <i>third</i>). [1]
		 *
		 * @return the string
		 */
		@Override
		public String toString() {
			return String.format("(%s %s %s)", first, second, third);
		}

		/**
		 * Calculates the distance between this point and the other point, along the specified axes
		 *
		 * @param other The other point
		 * @param axes  the axes to calculate along
		 * @return the distance between the two points
		 */
		double distanceTo(Coordinates other, E_AxisCombinationNeutral axes) {
			switch (axes) {
				case FIRST:
					return other.first - first;
				case SECOND:
					return other.second - second;
				case THIRD:
					return other.third - third;
				case FIRST_SECOND:
					return (other.first + other.second) - (first + second);
				case FIRST_THIRD:
					return (other.first + other.third) - (first + third);
				case SECOND_THIRD:
					return (other.second + other.third) - (second + third);
				case FIRST_SECOND_THIRD:
					return (other.first + other.second + other.third) - (first + second + third);
			}
			return 0.0;//Unreachable
		}
	}

	/**
	 * Defines which components of a coordinate triple to use in distance calculations. [7]
	 */
	public enum E_AxisCombinationNeutral {
		FIRST,                  //use the first component only; e.g., (x2 - x1)
		SECOND,                 //use the second component only; e.g., (y2 - y1)
		THIRD,                  //use the third component only; e.g., (z2 - z1)
		FIRST_SECOND,           //use the first and second components only; e.g., ((x2,y2) - (x1,y1))
		FIRST_THIRD,            //use the first and third components only
		SECOND_THIRD,           //use the second and third components only
		FIRST_SECOND_THIRD,     //use all components; e.g., ((x2,y2,z2) - (x1,y1,z1))
	}

	/**
	 * Defines how to interpret a lettered axis; i.e., axisA, axisB, or axisC. [6]
	 */
	public enum E_AxisNative {
		//TODO: Use this somewhere, somehow
		X_MINUS(-1),    //interprets the lettered axis as x with negative values going in the lettered positive direction
		X_PLUS(1),     //interprets the lettered axis as x with positive values going in the lettered positive direction
		Y_MINUS(-1),    //interprets the lettered axis as y with negative values going in the lettered positive direction
		Y_PLUS(1),     //interprets the lettered axis as y with positive values going in the lettered positive direction
		Z_MINUS(-1),    //interprets the lettered axis as z with negative values going in the lettered positive direction
		Z_PLUS(1);      //interprets the lettered axis as z with positive values going in the lettered positive direction

		int inverter;

		E_AxisNative(int inverter) {
			this.inverter = inverter;
		}

		double orient(double v) {
			return v * inverter;
		}
	}

	/**
	 * Defines the unit of the native coordinate system. [4]
	 */
	public enum E_Unit {
		FEET, KILOMETERS, METERS, MILES;

		private double convertToKilometers(double in) {
			switch (this) {
				case FEET:
					return in * 0.0003048;
				case METERS:
					return in * .001;
				case MILES:
					return in * 1.60934;
			}
			return in;
		}

		private double convertToMeters(double in) {
			switch (this) {
				case MILES:
					return in * 1609.34;
				case FEET:
					return in * 0.3048;
				case KILOMETERS:
					return in * 1000.0;
			}
			return in;
		}

		private double convertToMiles(double in) {
			switch (this) {
				case METERS:
					return in * 0.000621371;
				case FEET:
					return in * 0.000189394;
				case KILOMETERS:
					return in * 0.621371;
			}
			return in;
		}

		private double convertToFeet(double in) {
			switch (this) {
				case MILES:
					return in * 5280.0;
				case METERS:
					return in * 3.28084;
				case KILOMETERS:
					return in * 3280.84;
			}
			return in;
		}

		/**
		 * Converts a value from this format to the target format
		 *
		 * @param in     value to be converted
		 * @param target unit to be converted to
		 * @return converted value
		 */
		public double convertTo(double in, E_Unit target) {
			switch (target) {
				case FEET:
					return convertToFeet(in);
				case KILOMETERS:
					return convertToKilometers(in);
				case METERS:
					return convertToMeters(in);
				case MILES:
					return convertToMiles(in);
			}
			return in;
		}
	}
}
