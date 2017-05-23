package s17cs350project.planner;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaypointPlanner {
	private E_AxisNative[] axesNative;
	private E_Unit unitNative;
	private static final E_Unit unitCanonical = E_Unit.METERS;
	private BufferedReader bufferedReader;
	private static final int MEGABYTE = 1000000;
	private ArrayList<Coordinates> coordinates;

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
		// validate arguments
		if (axisA == null || axisB == null || axisC == null || unitNative == null || instream == null)
			throw new IllegalArgumentException("null argument");
		if (axisA.axis.equals(axisB.axis) || axisA.axis.equals(axisC.axis) || axisB.axis.equals(axisC.axis))
			throw new IllegalArgumentException("Invalid axis combination");

		axesNative = new E_AxisNative[]{axisA, axisB, axisC};
		this.unitNative = unitNative;
		bufferedReader = new BufferedReader(new InputStreamReader(instream));
		try {
			bufferedReader.mark(MEGABYTE);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		// validate arguments
		if (axes == null || unit == null)
			throw new IllegalArgumentException("null argument");

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
		// validate arguments
		if (axes == null || unit == null)
			throw new IllegalArgumentException("null argument");

		ArrayList<Double> distances = new ArrayList<>();
		getCoordinates(isCanonicalElseNative, unit);
		Coordinates[] coords = coordinates.toArray(new Coordinates[coordinates.size()]);
		for (int i = 1; i < coords.length; i++)
			distances.add(Math.abs(unitCanonical.convertTo(coords[i - 1].distanceTo(coords[i], axes), unit)));
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
		// validate arguments
		if (unit == null)
			throw new IllegalArgumentException("null argument");
		ArrayList<Coordinates> internalCoords = new ArrayList<>();
		ArrayList<Coordinates> coords = new ArrayList<>();
		bufferedReader.lines().forEach(line -> {
			try {
				double[] c = Arrays.stream(line.split(","))
						.map(String::trim)
						.mapToDouble(Double::parseDouble)
						.map(v -> convert(v, unitNative, unitCanonical))
						.toArray();
				if (c.length != 3) throw new IllegalArgumentException("Invalid input stream (wrong length)");
				if (isCanonicalElseNative)
					c = E_AxisNative.map(c, axesNative);
				internalCoords.add(new Coordinates(c[0], c[1], c[2]));
				c = convert(c, unitCanonical, unit);
				coords.add(new Coordinates(c[0], c[1], c[2]));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Invalid input stream (Could not parse digits)");
			}
		});
		try {
			bufferedReader.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
		coordinates = internalCoords;
		return coords;
	}

	/**
	 * Converts the specified double to the specified unit depending on whether we are in canonical or native mode
	 *
	 * @param v          the value to be converted
	 * @param sourceUnit the source unit
	 * @param targetUnit the target unit
	 * @return converted (or not) value
	 */
	private double convert(double v, E_Unit sourceUnit, E_Unit targetUnit) {
		return sourceUnit.convertTo(v, targetUnit);
	}

	private double[] convert(double[] v, E_Unit sourceUnit, E_Unit targetUnit) {
		for (int i = 0; i < v.length; i++)
			v[i] = convert(v[i], sourceUnit, targetUnit);
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
			if (Double.isNaN(first) || Double.isNaN(second) || Double.isNaN(third))
				throw new IllegalArgumentException("double was NaN");
			this.first = first;
			this.second = second;
			this.third = third;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Coordinates that = (Coordinates) o;

			return equalsDelta(that.first, first) && equalsDelta(that.second, second) && equalsDelta(that.third, third);
		}

		/**
		 * helper method for equals
		 * allow difference between doubles
		 *
		 * @param _a first value
		 * @param _b second value
		 * @return if difference between doubles fall within internal delta
		 */
		private boolean equalsDelta(double _a, double _b) {
			final double _delta = 0.0000001;
			double _diff = Double.compare(_a, _b);
			return (Math.abs(_diff) <= _delta) || _a == _b;
		}

		/**
		 * Outputs the triple in the form (first second third). [1]
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
		 * @param o    The other point
		 * @param axes the axes to calculate along
		 * @return the distance between the two points
		 */
		double distanceTo(Coordinates o, E_AxisCombinationNeutral axes) {
			switch (axes) {
				case FIRST:
					return o.first - first;
				case SECOND:
					return o.second - second;
				case THIRD:
					return o.third - third;
				case FIRST_SECOND:
					return distanceBetween2D(first, o.first, second, o.second);
				case FIRST_THIRD:
					return distanceBetween2D(first, o.first, third, o.third);
				case SECOND_THIRD:
					return distanceBetween2D(second, o.second, third, o.third);
				case FIRST_SECOND_THIRD:
					return distanceBetween3D(first, o.first, second, o.second, third, o.third);
			}
			return 0.0;//Unreachable
		}

		static double distanceBetween2D(double x1, double x2, double y1, double y2) {
			return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
		}

		static double distanceBetween3D(double x1, double x2, double y1, double y2, double z1, double z2) {
			return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));
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
		X_MINUS(-1, "X"),    //interprets the lettered axis as x with negative values going in the lettered positive direction
		X_PLUS(1, "X"),     //interprets the lettered axis as x with positive values going in the lettered positive direction
		Y_MINUS(-1, "Y"),    //interprets the lettered axis as y with negative values going in the lettered positive direction
		Y_PLUS(1, "Y"),     //interprets the lettered axis as y with positive values going in the lettered positive direction
		Z_MINUS(-1, "Z"),    //interprets the lettered axis as z with negative values going in the lettered positive direction
		Z_PLUS(1, "Z");      //interprets the lettered axis as z with positive values going in the lettered positive direction

		int inverter;
		String axis;

		E_AxisNative(int inverter, String axis) {
			this.inverter = inverter;
			this.axis = axis;
		}

		double orient(double v) {
			return v * inverter;
		}

		static double[] map(double[] c, E_AxisNative[] axisNatives) {
			double[] out = new double[3];
			for (int i = 0; i < 3; i++) {
				E_AxisNative map = axisNatives[i];
				switch (map.axis) {
					case "X":
						out[i] = map.orient(c[0]);
						break;
					case "Y":
						out[i] = map.orient(c[1]);
						break;
					case "Z":
						out[i] = map.orient(c[2]);
						break;
				}
			}
			return out;
		}

		boolean equals(E_AxisNative that) {
			return this.axis.equals(that.axis);
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
