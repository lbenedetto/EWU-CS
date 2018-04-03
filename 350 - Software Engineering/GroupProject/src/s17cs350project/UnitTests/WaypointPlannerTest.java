package s17cs350project.UnitTests;

import org.junit.jupiter.api.Test;
import s17cs350project.planner.WaypointPlanner;
import s17cs350project.planner.WaypointPlanner.Coordinates;
import s17cs350project.planner.WaypointPlanner.E_AxisCombinationNeutral;
import s17cs350project.planner.WaypointPlanner.E_AxisNative;
import s17cs350project.planner.WaypointPlanner.E_Unit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static s17cs350project.planner.WaypointPlanner.E_AxisCombinationNeutral.*;
import static s17cs350project.planner.WaypointPlanner.E_AxisNative.*;
import static s17cs350project.planner.WaypointPlanner.E_Unit.*;

class WaypointPlannerTest {
	// delta for comparing double values
	private static final double doubleDelta = 0.000001;

	/**
	 * illegal tests for WaypointPlanner.Coordinates()
	 */
	@Test
	void test_Coordinates_illegal() {
		// Double.NaN
		assertThrows(IllegalArgumentException.class, () -> new Coordinates(Double.NaN, Double.NaN, Double.NaN));
		assertThrows(IllegalArgumentException.class, () -> new Coordinates(0, 0, Double.NaN));
		assertThrows(IllegalArgumentException.class, () -> new Coordinates(0, Double.NaN, 0));
		assertThrows(IllegalArgumentException.class, () -> new Coordinates(Double.NaN, 0, 0));
	}

	/*
	 * Illegal tests for WaypointPlanner()
	 */
	@SuppressWarnings("ConstantConditions")
	@Test
	void test_WaypointPlanner_illegal() throws IOException {

		// test stream to use
		String _input = "1,1,1\n2,2,2\n3,3,3";
		InputStream _stream = new ByteArrayInputStream(_input.getBytes());

		// null
		// brute force all axis/unit combinations with null arguments
		for (int i = 0; i < E_AxisNative.values().length + 1; ++i) {
			for (int j = 0; j < E_AxisNative.values().length + 1; ++j) {
				for (int k = 0; k < E_AxisNative.values().length + 1; ++k) {
					for (int x = 0; x < E_Unit.values().length + 1; ++x) {
						E_AxisNative
								_axisA = (i == 0) ? null : E_AxisNative.values()[i - 1],
								_axisB = (j == 0) ? null : E_AxisNative.values()[j - 1],
								_axisC = (k == 0) ? null : E_AxisNative.values()[k - 1];
						E_Unit _unit = (x == 0) ? null : E_Unit.values()[x - 1];

						assertThrows(IllegalArgumentException.class, () -> new WaypointPlanner(_axisA, _axisB, _axisC, _unit, null));
						if ((_axisA == null) || (_axisB == null) || (_axisC == null) || (_unit == null)) {
							assertThrows(IllegalArgumentException.class, () -> new WaypointPlanner(_axisA, _axisB, _axisC, _unit, _stream));
						}
					}
				}
			}
		}

		// missing/duplicate AXIS
		// brute force all axis
		for (E_AxisNative _axisA : E_AxisNative.values()) {
			for (E_AxisNative _axisB : E_AxisNative.values()) {
				for (E_AxisNative _axisC : E_AxisNative.values()) {
					// check for a duplicate/missing axis
					if (missingAxis(_axisA, _axisB, _axisC)) {
						for (E_Unit _unit : E_Unit.values()) {
							assertThrows(IllegalArgumentException.class, () -> new WaypointPlanner(_axisA, _axisB, _axisC, _unit, _stream));
						}
					}
				}
			}
		}

		// close temp stream used
		_stream.close();

		// bad input stream
		test_WaypointPlanner_illegal_stream("1,1,1\n1,2,3\n1,2");
		test_WaypointPlanner_illegal_stream("1,1,1\n1,2,3\n1,,3");
		test_WaypointPlanner_illegal_stream("1,1,1\n1,23\n1,2,3");
		test_WaypointPlanner_illegal_stream("1,1,1\n1,2,3\n\\1,2,3");
		test_WaypointPlanner_illegal_stream("1,1,1\n1,2.3\n1a,2,3");
		test_WaypointPlanner_illegal_stream("1,1,1..\n1,2.3\n1a,2,3");
		test_WaypointPlanner_illegal_stream("1,1,1\n1,2.3\n1a,2,3");
		test_WaypointPlanner_illegal_stream("1,1,1\n1,2..3\n1,2,3");
		test_WaypointPlanner_illegal_stream("1,1,1a\n1,23\n1,2,3");
//		test_WaypointPlanner_illegal_stream("1,1,1\n1,2,3\n1,2,3");
//		test_WaypointPlanner_illegal_stream("1,1,1\n1,2,3\n1,2,3");
//		test_WaypointPlanner_illegal_stream("1,1,1\n1,2.23,3\n1,2,3");
		test_WaypointPlanner_illegal_stream("1,b,1\n1,a,3\n.01,2,3");
		test_WaypointPlanner_illegal_stream("1,1,1\n1,1.1,2,3\n1,.2,.3");
		test_WaypointPlanner_illegal_stream("1,1,1\n1,2,3\n1,1,2,3");
		test_WaypointPlanner_illegal_stream("a,b,c\nd,e,f");
	}

	/**
	 * helper method for test_WaypointPlanner_illegal()
	 * uses given string to Waypoint constructor (as input stream)
	 */
	private void test_WaypointPlanner_illegal_stream(String _str) throws RuntimeException, IOException {
		InputStream _stream = new ByteArrayInputStream(_str.getBytes());

		assertThrows(IllegalArgumentException.class, () -> new WaypointPlanner(X_PLUS, Y_PLUS, Z_PLUS, METERS, _stream).getCoordinates(true, METERS));

		_stream.close();
	}

	/**
	 * Illegal tests for calculateDistance()
	 */
	@Test
	void test_calculateDistance_illegal() throws IOException {
		InputStream _stream = new ByteArrayInputStream("1,2,3\n3,4,5\n6,7,8".getBytes());
		WaypointPlanner _planner = new WaypointPlanner(X_PLUS, Y_PLUS, Z_PLUS, METERS, _stream);

		// null
		// bruteforce all combinations
		for (int i = 0; i < E_AxisCombinationNeutral.values().length + 1; ++i) {
			for (int j = 0; j < E_Unit.values().length + 1; ++j) {
				E_AxisCombinationNeutral _combination = (i == 0) ? null : E_AxisCombinationNeutral.values()[i - 1];
				E_Unit _unit = (j == 0) ? null : E_Unit.values()[j - 1];

				if ((_combination == null) || (_unit == null)) {
					assertThrows(IllegalArgumentException.class, () -> _planner.calculateDistance(_combination, true, _unit));
					assertThrows(IllegalArgumentException.class, () -> _planner.calculateDistance(_combination, false, _unit));
				}
			}
		}

		_stream.close();
	}

	/**
	 * Illegal tests for calculateDistances()
	 */
	@Test
	void test_calculateDistances_illegal() throws IOException {
		InputStream _stream = new ByteArrayInputStream("1,2,3\n3,4,5\n6,7,8".getBytes());
		WaypointPlanner _planner = new WaypointPlanner(X_PLUS, Y_PLUS, Z_PLUS, METERS, _stream);

		// null
		// bruteforce all combinations
		for (int i = 0; i < E_AxisCombinationNeutral.values().length + 1; ++i) {
			for (int j = 0; j < E_Unit.values().length + 1; ++j) {
				E_AxisCombinationNeutral _combination = (i == 0) ? null : E_AxisCombinationNeutral.values()[i - 1];
				E_Unit _unit = (j == 0) ? null : E_Unit.values()[j - 1];

				if ((_combination == null) || (_unit == null)) {
					assertThrows(IllegalArgumentException.class, () -> _planner.calculateDistances(_combination, true, _unit));
					assertThrows(IllegalArgumentException.class, () -> _planner.calculateDistances(_combination, false, _unit));
				}
			}
		}

		_stream.close();
	}

	/**
	 * Illegal tests for getCoordinates()
	 */
	@Test
	void test_getCoordinates_illegal() throws IOException {
		// null unit
		InputStream _stream = new ByteArrayInputStream("1,2,3\n3,4,5\n6,7,8".getBytes());
		WaypointPlanner _planner = new WaypointPlanner(X_PLUS, Y_PLUS, Z_PLUS, METERS, _stream);
		_stream.close();

		assertThrows(IllegalArgumentException.class, () -> _planner.getCoordinates(false, null));

		// some kind of internal list is not returned
		List<Coordinates> _coordinates = _planner.getCoordinates(false, METERS);
		_coordinates.clear();
		_coordinates = _planner.getCoordinates(false, METERS);

		assertTrue(_coordinates.size() == 3);
	}

	/**
	 * test WaypointPlanner()
	 */
	@Test
	void test_WaypointPlanner() throws IOException {
		InputStream _stream = new ByteArrayInputStream("1,2,3\n3,4,5\n6,7,8".getBytes());

		// attributes test
		// brute force combinations
		for (E_AxisNative _axisA : E_AxisNative.values()) {
			for (E_AxisNative _axisB : E_AxisNative.values()) {
				for (E_AxisNative _axisC : E_AxisNative.values()) {
					// check no missing axis
					if (!missingAxis(_axisA, _axisB, _axisC)) {
						for (E_Unit _unit : E_Unit.values()) {
							// test all set attributes come back correct
							WaypointPlanner _planner = new WaypointPlanner(_axisA, _axisB, _axisC, _unit, _stream);

							assertTrue(_axisA == _planner.getAxisA());
							assertTrue(_axisB == _planner.getAxisB());
							assertTrue(_axisC == _planner.getAxisC());
							assertTrue(_unit == _planner.getUnitNative());
						}
					}
				}
			}
		}

		_stream.close();

		// given non-rewinded stream, cordinates test
		_stream = new ByteArrayInputStream("1,2,3\n3,4,5\n6,7,8".getBytes());
		WaypointPlanner _planner1 = new WaypointPlanner(X_PLUS, Y_PLUS, Z_PLUS, METERS, _stream);
		_stream = new ByteArrayInputStream("1,2,3\n3,4,5\n6,7,8".getBytes());
		WaypointPlanner _planner2 = new WaypointPlanner(X_PLUS, Y_PLUS, Z_PLUS, METERS, _stream);

		_stream.close();

		List<Coordinates> _coordinates1 = _planner1.getCoordinates(true, METERS);
		List<Coordinates> _coordinates2 = _planner2.getCoordinates(true, METERS);

		// not using equals to get more detailed failure reason
		//assertTrue(_coordinates1.equals(_coordinates2));

		validateCoordinates(_coordinates1, _coordinates2);
	}

	/**
	 * helper method
	 * checks if any axis (X,Y,Z) is missing
	 */
	private boolean missingAxis(E_AxisNative _axisA, E_AxisNative _axisB, E_AxisNative _axisC) {
		return _axisA.getCardinalAxis().equals(_axisB.getCardinalAxis())
				|| _axisA.getCardinalAxis().equals(_axisC.getCardinalAxis())
				|| _axisB.getCardinalAxis().equals(_axisC.getCardinalAxis());
	}

	/**
	 * test given in pdf
	 * test 1
	 * may need to adjust double comparison (precision issues)
	 */
	@Test
	void test_pdf1() throws IOException {

		String input = "1 ,2,3 \n 9,7,5 \n -1,-3, -5\n -1,-5,-9 \n   4, 6,2";
		InputStream _instream = new ByteArrayInputStream(input.getBytes());
		WaypointPlanner planner = new WaypointPlanner(X_PLUS, Y_PLUS, Z_PLUS, KILOMETERS, _instream);

		List<Coordinates> coordinatesNative = planner.getCoordinates(false, KILOMETERS);
		List<Coordinates> coordinatesCanonical = planner.getCoordinates(true, KILOMETERS);

		validateCoordinates(coordinatesNative, Arrays.asList(
				new Coordinates(1.0, 2.0, 3.0),
				new Coordinates(9.0, 7.0, 5.0),
				new Coordinates(-1.0, -3.0, -5.0),
				new Coordinates(-1.0, -5.0, -9.0),
				new Coordinates(4.0, 6.0, 2.0)));
		validateCoordinates(coordinatesCanonical, Arrays.asList(
				new Coordinates(1.0, 2.0, 3.0),
				new Coordinates(9.0, 7.0, 5.0),
				new Coordinates(-1.0, -3.0, -5.0),
				new Coordinates(-1.0, -5.0, -9.0),
				new Coordinates(4.0, 6.0, 2.0)));

		List<Double> distancesNative = planner.calculateDistances(FIRST_SECOND, false, KILOMETERS);
		List<Double> distancesCanonical = planner.calculateDistances(FIRST_SECOND, true, KILOMETERS);

		validateDistances(distancesNative, Arrays.asList(9.433981132056603, 14.142135623730951, 2.0, 12.083045973594572));
		validateDistances(distancesCanonical, Arrays.asList(9.433981132056603, 14.142135623730951, 2.0, 12.083045973594572));

		double distanceNative = planner.calculateDistance(FIRST_SECOND, false, KILOMETERS);
		double distanceCanonical = planner.calculateDistance(FIRST_SECOND, true, KILOMETERS);

		validateDistance(distanceNative, 37.659162729382125);
		validateDistance(distanceCanonical, 37.659162729382125);

		/*
		expected results

		coordinatesNative = [(1.0 2.0 3.0), (9.0 7.0 5.0), (-1.0 -3.0 -5.0), (-1.0 -5.0 -9.0), (4.0 6.0 2.0)]
		coordinatesCanonical = [(1.0 2.0 3.0), (9.0 7.0 5.0), (-1.0 -3.0 -5.0), (-1.0 -5.0 -9.0), (4.0 6.0 2.0)]

		distancesNative= [9.433981132056603, 5.385164807134504, 7.280109889280518, 5.0]
		distancesCanonical = [9.433981132056603, 5.385164807134504, 7.280109889280518, 5.0]

		distanceNative = 27.099255828471623
		distanceCanonical = 27.099255828471623
		*/

		_instream.close();
	}

	/**
	 * test given in pdf
	 * test 2
	 * may need to adjust double comparison (precision issues)
	 */
	@Test
	void test_pdf2() throws IOException {

		String input = "1 ,2,3 \n 9,7,5 \n -1,-3, -5\n -1,-5,-9 \n 4, 6,2";
		InputStream _instream = new ByteArrayInputStream(input.getBytes());
		WaypointPlanner planner = new WaypointPlanner(Z_MINUS, X_PLUS, Y_PLUS, KILOMETERS, _instream);

		List<Coordinates> coordinatesNative = planner.getCoordinates(false, KILOMETERS);
		List<Coordinates> coordinatesCanonical = planner.getCoordinates(true, KILOMETERS);

		validateCoordinates(coordinatesNative, Arrays.asList(
				new Coordinates(1.0, 2.0, 3.0),
				new Coordinates(9.0, 7.0, 5.0),
				new Coordinates(-1.0, -3.0, -5.0),
				new Coordinates(-1.0, -5.0, -9.0),
				new Coordinates(4.0, 6.0, 2.0)));

		validateCoordinates(coordinatesCanonical, Arrays.asList(
				new Coordinates(-3.0, 1.0, 2.0),
				new Coordinates(-5.0, 9.0, 7.0),
				new Coordinates(5.0, -1.0, -3.0),
				new Coordinates(9.0, -1.0, -5.0),
				new Coordinates(-2.0, 4.0, 6.0)));

		List<Double> distancesNative = planner.calculateDistances(FIRST_SECOND, false, KILOMETERS);
		List<Double> distancesCanonical = planner.calculateDistances(FIRST_SECOND, true, KILOMETERS);

		validateDistances(distancesNative, Arrays.asList(9.433981132056603, 14.142135623730951, 2.0, 12.083045973594572));
		validateDistances(distancesCanonical, Arrays.asList(8.246211251235321, 14.142135623730951, 4.0, 12.083045973594572));

		double distanceNative = planner.calculateDistance(FIRST_SECOND, false, KILOMETERS);
		double distanceCanonical = planner.calculateDistance(FIRST_SECOND, true, KILOMETERS);

		validateDistance(distanceNative, 37.659162729382125);
		validateDistance(distanceCanonical, 38.471392848560846);

		_instream.close();
	}

	/**
	 * test given in pdf
	 * test 3
	 * may need to adjust double comparison (precision issues)
	 */
	@Test
	void test_pdf3() throws IOException {

		String _input = "1 ,2,3 \n 9,7,5 \n -1,-3, -5\n -1,-5,-9 \n 4, 6,2";
		InputStream _instream = new ByteArrayInputStream(_input.getBytes());
		WaypointPlanner planner = new WaypointPlanner(Z_MINUS, X_PLUS, Y_PLUS, KILOMETERS, _instream);

		List<Coordinates> coordinatesNative = planner.getCoordinates(false, METERS);
		List<Coordinates> coordinatesCanonical = planner.getCoordinates(true, FEET);

		validateCoordinates(coordinatesNative,
				Arrays.asList(
						new Coordinates(1000.0, 2000.0, 3000.0),
						new Coordinates(9000.0, 7000.0, 5000.0),
						new Coordinates(-1000.0, -3000.0, -5000.0),
						new Coordinates(-1000.0, -5000.0, -9000.0),
						new Coordinates(4000.0, 6000.0, 2000.0))
		);

		validateCoordinates(coordinatesCanonical,
				Arrays.asList(
						new Coordinates(-9842.52, 3280.84, 6561.68),
						new Coordinates(-16404.2, 29527.56, 22965.88),
						new Coordinates(16404.2, -3280.84, -9842.52),
						new Coordinates(29527.56, -3280.84, -16404.2),
						new Coordinates(-6561.68, 13123.36, 19685.04))
		);

		List<Double> distancesNative = planner.calculateDistances(FIRST_SECOND, false, FEET);
		List<Double> distancesCanonical = planner.calculateDistances(FIRST_SECOND, true, MILES);

		validateDistances(distancesNative, Arrays.asList(30951.382657296588, 46398.084239761454, 6561.68, 39642.540552008024));
		validateDistances(distancesCanonical, Arrays.asList(5.123956531391342, 8.787512954653325, 2.485484, 7.508054359658432));

		double distanceNative = planner.calculateDistance(FIRST_SECOND, false, MILES);
		double distanceCanonical = planner.calculateDistance(FIRST_SECOND, true, METERS);

		validateDistance(distanceNative, 23.4003116043189);
		validateDistance(distanceCanonical, 38471.39284856084);

		_instream.close();

		/*
		expected results

		coordinatesNative = [(1000.0 2000.0 3000.0), (9000.0 7000.0 5000.0), (-1000.0 -3000.0 -5000.0), (-1000.0 -5000.0 -9000.0), (4000.0 6000.0 2000.0)]
		coordinatesCanonical = [(-9842.52 3280.84 6561.68), (-16404.2 29527.56 22965.88), (16404.2 -3280.84 -9842.52), (29527.56 -3280.84 -16404.2), (-6561.68 13123.36 19685.04)]

		distancesNative= [30951.382657296588, 17667.864105839166, 23884.875729147097, 16404.2]
		distancesCanonical = [5.123956531391342, 5.123956531391342, 7.559304472427871, 1.9649476319764863]

		distanceNative = 16.838691693393244
		distanceCanonical = 31820.225223235462
		*/
	}

	/**
	 * test given in pdf
	 * test 4
	 * may need to adjust double comparison (precision issues)
	 */
	@Test
	void test_pdf4() throws IOException {

		String _input = "1 ,2,3 \n 9,7,5 \n -1,-3, -5\n -1,-5,-9 \n 4, 6,2";
		InputStream _instream = new ByteArrayInputStream(_input.getBytes());
		WaypointPlanner planner = new WaypointPlanner(Z_MINUS, X_PLUS, Y_MINUS, FEET, _instream);

		List<Coordinates> coordinatesNative = planner.getCoordinates(false, METERS);
		List<Coordinates> coordinatesCanonical = planner.getCoordinates(true, FEET);

		validateCoordinates(coordinatesNative,
				Arrays.asList(
						new Coordinates(0.3048, 0.6096, 0.9144000000000001),
						new Coordinates(2.7432000000000003, 2.1336, 1.524),
						new Coordinates(-0.3048, -0.9144000000000001, -1.524),
						new Coordinates(-0.3048, -1.524, -2.7432000000000003),
						new Coordinates(1.2192, 1.8288000000000002, 0.6096))
		);

		validateCoordinates(coordinatesCanonical,
				Arrays.asList(
						new Coordinates(-3.0000000960000004, 1.000000032, -2.000000064),
						new Coordinates(-5.00000016, 9.000000288, -7.000000224),
						new Coordinates(5.00000016, -1.000000032, 3.0000000960000004),
						new Coordinates(9.000000288, -1.000000032, 5.00000016),
						new Coordinates(-2.000000064, 4.000000128, -6.000000192000001))
		);

		List<Double> distancesNative = planner.calculateDistances(FIRST_SECOND, false, FEET);
		List<Double> distancesCanonical = planner.calculateDistances(FIRST_SECOND, true, MILES);

		validateDistances(distancesNative, Arrays.asList(9.433981433944, 14.142136076279291, 2.0000000639999995, 12.083046360252045));
		validateDistances(distancesCanonical, Arrays.asList(0.0015617819507680816, 0.0026784339485783335, 7.575755232000003E-4, 0.0022884549688238906));

		double distanceNative = planner.calculateDistance(FIRST_SECOND, false, MILES);
		double distanceCanonical = planner.calculateDistance(FIRST_SECOND, true, METERS);

		validateDistance(distanceNative, 0.007132414976996401);
		validateDistance(distanceCanonical, 11.726080540241345);

		_instream.close();

		/*
		expected results

		coordinatesNative = [(0.3048 0.6096 0.9144000000000001), (2.7432000000000003 2.1336 1.524), (-0.3048 -0.9144000000000001 -1.524), (-0.3048 -1.524 -2.7432000000000003), (1.2192 1.8288000000000002 0.6096)]
		coordinatesCanonical = [(-3.0000000960000004 1.000000032 -2.000000064), (-5.00000016 9.000000288 -7.000000224), (5.00000016 -1.000000032 3.0000000960000004), (9.000000288 -1.000000032 5.00000016), (-2.000000064 4.000000128 -6.000000192000001)]

		distancesNative= [9.433981433944, 5.385164979459779, 7.280110122244035, 5.000000160000001]
		distancesCanonical = [doubleDelta5617819507680816, doubleDelta5617819507680814, 0.0023040760031960155, 5.98916038226433E-4]

		distanceNative = 0.00513243322814626
		distanceCanonical = 9.698804648042168
		*/
	}

	/**
	 * test given in pdf
	 * test 5
	 * may need to adjust double comparison (precision issues)
	 */
	@Test
	void test_pdf5() throws IOException {

		String _input = "1 ,2,3 \n 9,7,5 \n -1,-3, -5\n -1,-5,-9 \n 4, 6,2";
		InputStream _instream = new ByteArrayInputStream(_input.getBytes());
		WaypointPlanner planner = new WaypointPlanner(Z_MINUS, X_PLUS, Y_MINUS, FEET, _instream);

		List<Coordinates> coordinatesNative = planner.getCoordinates(false, METERS);
		List<Coordinates> coordinatesCanonical = planner.getCoordinates(true, FEET);

		validateCoordinates(coordinatesNative,
				Arrays.asList(new Coordinates(0.3048, 0.6096, 0.9144000000000001),
						new Coordinates(2.7432000000000003, 2.1336, 1.524),
						new Coordinates(-0.3048, -0.9144000000000001, -1.524),
						new Coordinates(-0.3048, -1.524, -2.7432000000000003),
						new Coordinates(1.2192, 1.8288000000000002, 0.6096))
		);

		validateCoordinates(coordinatesCanonical,
				Arrays.asList(new Coordinates(-3.0000000960000004, 1.000000032, -2.000000064),
						new Coordinates(-5.00000016, 9.000000288, -7.000000224),
						new Coordinates(5.00000016, -1.000000032, 3.0000000960000004),
						new Coordinates(9.000000288, -1.000000032, 5.00000016),
						new Coordinates(-2.000000064, 4.000000128, -6.000000192000001))
		);

		List<Double> distancesNative = planner.calculateDistances(FIRST, false, FEET);
		List<Double> distancesCanonical = planner.calculateDistances(SECOND, true, MILES);

		validateDistances(distancesNative, Arrays.asList(8.000000256, 10.000000320000002, 0.0, 5.00000016));
		validateDistances(distancesCanonical, Arrays.asList(0.0015151510464000003, 0.0018939388080000002, 0.0, 9.469694040000001E-4));

		double distanceNative = planner.calculateDistance(FIRST_SECOND_THIRD, false, MILES);
		double distanceCanonical = planner.calculateDistance(SECOND_THIRD, true, METERS);

		validateDistance(distanceNative, 0.009048563380524408);
		validateDistance(distanceCanonical, 11.478512799915674);

		_instream.close();
	}

		/*
		expected results

		coordinatesNative = [(0.3048 0.6096 0.9144000000000001), (2.7432000000000003 2.1336 1.524), (-0.3048 -0.9144000000000001 -1.524), (-0.3048 -1.524 -2.7432000000000003), (1.2192 1.8288000000000002 0.6096)]
		coordinatesCanonical = [(-3.0000000960000004 1.000000032 -2.000000064), (-5.00000016 9.000000288 -7.000000224), (5.00000016 -1.000000032 3.0000000960000004), (9.000000288 -1.000000032 5.00000016), (-2.000000064 4.000000128 -6.000000192000001)]

		distancesNative= [8.000000256, 2.000000064, 2.000000064, 3.000000096]
		distancesCanonical = [doubleDelta5151510464000003, 3.7878776160000003E-4, 3.7878776160000003E-4, 5.681816424E-4]

		distanceNative = 0.007276889772037
		distanceCanonical = 8.259853176518153
		*/

	// helper methods to compare output

	/**
	 * checks that coordinate lists match
	 * instead of using List.equals(List), to get more detailed fail message
	 */
	private void validateCoordinates(List<Coordinates> _coordinates, List<Coordinates> _coordinatesExpected) {
		// compare size
		if (_coordinatesExpected.size() != _coordinates.size()) {
			fail(String.format("Size mismatch(expected %d, got %d)", _coordinatesExpected.size(), _coordinates.size()));
		}
		// check elements
		Iterator<Coordinates> _it1 = _coordinatesExpected.iterator(), _it2 = _coordinates.iterator();
		while (_it1.hasNext() && _it2.hasNext()) {
			Coordinates _coord1 = _it1.next(), _coord2 = _it2.next();

			// ensure both equals return the same value
			boolean _eq1 = _coord1.equals(_coord2), _eq2 = _coord2.equals(_coord1);

			if (!_eq1 && !_eq2) {
				fail(String.format("Coordinates do not equal, expected \"%s\" got \"%s\"", _coord1.toString(), _coord2.toString()));
			} else if (!_eq1 || !_eq2) {
				fail(String.format("Equals issue, expected \"%s\" got \"%s\", but equals mismatch (%b, %b)", _coord1.toString(), _coord2.toString(), _eq1, _eq2));
			}

		}

		// check List.equals(List), to ensure
		assertTrue(_coordinatesExpected.equals(_coordinates));
	}

	/**
	 * validates coordinates distances
	 */
	private void validateDistances(List<Double> _distances, List<Double> _distancesExpected) {
		// compare size
		if (_distancesExpected.size() != _distances.size()) {
			fail(String.format("Size mismatch(expected %d, got %d)", _distancesExpected.size(), _distances.size()));
		}
		// check elements
		Iterator<Double> _it1 = _distancesExpected.iterator(), _it2 = _distances.iterator();
		int _index = 0;
		while (_it1.hasNext() && _it2.hasNext()) {
			double _distance1 = _it1.next(), _distance2 = _it2.next();

			// print to console to help find issues
			System.out.printf("index: %d\n", _index);

			assertEquals(_distance1, _distance2, doubleDelta);

			++_index;
		}
	}

	/**
	 * validates total coordinates distance
	 * provides more detailed fail message
	 */
	private void validateDistance(double _distance, double _distanceExpected) {
		assertEquals(_distanceExpected, _distance, doubleDelta);
	}

	/**
	 * helper method for getCoordinates tests
	 *
	 * @param _input                 string for input
	 * @param _axisA                 canonical A axis
	 * @param _axisB                 canonical B axis
	 * @param _axisC                 canonical C axis
	 * @param _unitPlanner           units of planner
	 * @param _isCanonicalElseNative format of coordinates
	 * @param _unitCoordinates       unit to get coordinates as
	 * @param _coordinatesExpected   list of coordinates expected
	 * @throws IOException when closing the stream
	 */
	private void test_getCoordinates_validate(String _input, E_AxisNative _axisA, E_AxisNative _axisB, E_AxisNative _axisC, E_Unit _unitPlanner, boolean _isCanonicalElseNative, E_Unit _unitCoordinates, List<Coordinates> _coordinatesExpected) throws IOException {
		InputStream _stream = new ByteArrayInputStream(_input.getBytes());

		WaypointPlanner _planner = new WaypointPlanner(_axisA, _axisB, _axisC, _unitPlanner, _stream);

		List<Coordinates> _coordinates = _planner.getCoordinates(_isCanonicalElseNative, _unitCoordinates);

		validateCoordinates(_coordinates, _coordinatesExpected);

		_stream.close();
	}

	/**
	 * test getCoordinates()
	 * for excel test 1
	 */
	@Test
	void test_input1_getCoordinates() throws IOException {
		String _input = "0,0,0\n1,1,1\n2,2,2\n3,3,3\n4,4,4";

		// native
		test_getCoordinates_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, false, METERS,
				Arrays.asList(new Coordinates(0, 0, 0),
						new Coordinates(1, 1, 1),
						new Coordinates(2, 2, 2),
						new Coordinates(3, 3, 3),
						new Coordinates(4, 4, 4)));

		// canonical (X,Y,Z)
		test_getCoordinates_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(0, 0, 0),
						new Coordinates(1, 1, 1),
						new Coordinates(2, 2, 2),
						new Coordinates(3, 3, 3),
						new Coordinates(4, 4, 4)));
		// canonical (X,Y,-Z)
		test_getCoordinates_validate(_input, X_PLUS, Y_PLUS, Z_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(0, 0, 0),
						new Coordinates(1, 1, -1),
						new Coordinates(2, 2, -2),
						new Coordinates(3, 3, -3),
						new Coordinates(4, 4, -4)));
		// canonical (X,-Y,Z)
		test_getCoordinates_validate(_input, X_PLUS, Y_MINUS, Z_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(0, 0, 0),
						new Coordinates(1, -1, 1),
						new Coordinates(2, -2, 2),
						new Coordinates(3, -3, 3),
						new Coordinates(4, -4, 4)));
		// canonical (X,-Y,-Z)
		test_getCoordinates_validate(_input, X_PLUS, Y_MINUS, Z_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(0, 0, 0),
						new Coordinates(1, -1, -1),
						new Coordinates(2, -2, -2),
						new Coordinates(3, -3, -3),
						new Coordinates(4, -4, -4)));

		// canonical (-X,Y,Z)
		test_getCoordinates_validate(_input, X_MINUS, Y_PLUS, Z_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-0, 0, 0),
						new Coordinates(-1, 1, 1),
						new Coordinates(-2, 2, 2),
						new Coordinates(-3, 3, 3),
						new Coordinates(-4, 4, 4)));
		// canonical (-X,Y,-Z)
		test_getCoordinates_validate(_input, X_MINUS, Y_PLUS, Z_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-0, 0, -0),
						new Coordinates(-1, 1, -1),
						new Coordinates(-2, 2, -2),
						new Coordinates(-3, 3, -3),
						new Coordinates(-4, 4, -4)));
		// canonical (-X,-Y,Z)
		test_getCoordinates_validate(_input, X_MINUS, Y_MINUS, Z_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-0, -0, 0),
						new Coordinates(-1, -1, 1),
						new Coordinates(-2, -2, 2),
						new Coordinates(-3, -3, 3),
						new Coordinates(-4, -4, 4)));
		// canonical (-X,-Y,-Z)
		test_getCoordinates_validate(_input, X_MINUS, Y_MINUS, Z_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-0, -0, -0),
						new Coordinates(-1, -1, -1),
						new Coordinates(-2, -2, -2),
						new Coordinates(-3, -3, -3),
						new Coordinates(-4, -4, -4)));

		// canonical (X,Z,Y)
		test_getCoordinates_validate(_input, X_PLUS, Z_PLUS, Y_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(0, 0, 0),
						new Coordinates(1, 1, 1),
						new Coordinates(2, 2, 2),
						new Coordinates(3, 3, 3),
						new Coordinates(4, 4, 4)));
		// canonical (X,Z,-Y)
		test_getCoordinates_validate(_input, X_PLUS, Z_PLUS, Y_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(0, 0, -0),
						new Coordinates(1, 1, -1),
						new Coordinates(2, 2, -2),
						new Coordinates(3, 3, -3),
						new Coordinates(4, 4, -4)));
		// canonical (X,-Z,Y)
		test_getCoordinates_validate(_input, X_PLUS, Z_MINUS, Y_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(0, -0, 0),
						new Coordinates(1, -1, 1),
						new Coordinates(2, -2, 2),
						new Coordinates(3, -3, 3),
						new Coordinates(4, -4, 4)));
		// canonical (X,-Z,-Y)
		test_getCoordinates_validate(_input, X_PLUS, Z_MINUS, Y_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(0, -0, -0),
						new Coordinates(1, -1, -1),
						new Coordinates(2, -2, -2),
						new Coordinates(3, -3, -3),
						new Coordinates(4, -4, -4)));
	}

	/**
	 * test getCoordinates()
	 * for excel test 2
	 */
	@Test
	void test_input2_getCoordinates() throws IOException {
		String _input = "0,0,0\n-1,-1,-1\n-2,-2,-2\n-3,-3,-3\n-4,-4,-4";

		// native
		test_getCoordinates_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, false, METERS,
				Arrays.asList(new Coordinates(0, 0, 0),
						new Coordinates(-1, -1, -1),
						new Coordinates(-2, -2, -2),
						new Coordinates(-3, -3, -3),
						new Coordinates(-4, -4, -4)));

		// canonical (-X, Z, Y)
		test_getCoordinates_validate(_input, X_MINUS, Z_PLUS, Y_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-0, 0, 0),
						new Coordinates(1, -1, -1),
						new Coordinates(2, -2, -2),
						new Coordinates(3, -3, -3),
						new Coordinates(4, -4, -4)));
		// canonical (-X, Z, -Y)
		test_getCoordinates_validate(_input, X_MINUS, Z_PLUS, Y_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-0, 0, -0),
						new Coordinates(1, -1, 1),
						new Coordinates(2, -2, 2),
						new Coordinates(3, -3, 3),
						new Coordinates(4, -4, 4)));
		// canonical (-X, -Z, Y)
		test_getCoordinates_validate(_input, X_MINUS, Z_MINUS, Y_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-0, 0, 0),
						new Coordinates(1, 1, -1),
						new Coordinates(2, 2, -2),
						new Coordinates(3, 3, -3),
						new Coordinates(4, 4, -4)));
		// canonical (-X, -Z, -Y)
		test_getCoordinates_validate(_input, X_MINUS, Z_MINUS, Y_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-0, 0, -0),
						new Coordinates(1, 1, 1),
						new Coordinates(2, 2, 2),
						new Coordinates(3, 3, 3),
						new Coordinates(4, 4, 4)));

		// canonical (Y, X, Z)
		test_getCoordinates_validate(_input, Y_PLUS, X_PLUS, Z_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(0, 0, 0),
						new Coordinates(-1, -1, -1),
						new Coordinates(-2, -2, -2),
						new Coordinates(-3, -3, -3),
						new Coordinates(-4, -4, -4)));
		// canonical (Y, X, -Z)
		test_getCoordinates_validate(_input, Y_PLUS, X_PLUS, Z_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(0, 0, 0),
						new Coordinates(-1, -1, 1),
						new Coordinates(-2, -2, 2),
						new Coordinates(-3, -3, 3),
						new Coordinates(-4, -4, 4)));
		// canonical (Y, -X, Z)
		test_getCoordinates_validate(_input, Y_PLUS, X_MINUS, Z_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(0, 0, 0),
						new Coordinates(-1, 1, -1),
						new Coordinates(-2, 2, -2),
						new Coordinates(-3, 3, -3),
						new Coordinates(-4, 4, -4)));
		// canonical (Y, -X, -Z)
		test_getCoordinates_validate(_input, Y_PLUS, X_MINUS, Z_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(0, 0, 0),
						new Coordinates(-1, 1, 1),
						new Coordinates(-2, 2, 2),
						new Coordinates(-3, 3, 3),
						new Coordinates(-4, 4, 4)));

		// canonical (-Y, X, Z)
		test_getCoordinates_validate(_input, Y_MINUS, X_PLUS, Z_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(0, 0, 0),
						new Coordinates(1, -1, -1),
						new Coordinates(2, -2, -2),
						new Coordinates(3, -3, -3),
						new Coordinates(4, -4, -4)));
		// canonical (-Y, X, -Z)
		test_getCoordinates_validate(_input, Y_MINUS, X_PLUS, Z_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(0, 0, 0),
						new Coordinates(1, -1, 1),
						new Coordinates(2, -2, 2),
						new Coordinates(3, -3, 3),
						new Coordinates(4, -4, 4)));
		// canonical (-Y, -X, Z)
		test_getCoordinates_validate(_input, Y_MINUS, X_MINUS, Z_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(0, 0, 0),
						new Coordinates(1, 1, -1),
						new Coordinates(2, 2, -2),
						new Coordinates(3, 3, -3),
						new Coordinates(4, 4, -4)));
		// canonical (-Y, -X, -Z)
		test_getCoordinates_validate(_input, Y_MINUS, X_MINUS, Z_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(0, 0, 0),
						new Coordinates(1, 1, 1),
						new Coordinates(2, 2, 2),
						new Coordinates(3, 3, 3),
						new Coordinates(4, 4, 4)));
	}

	/**
	 * test getCoordinates()
	 * for excel test 3
	 */
	@Test
	void test_input3_getCoordinates() throws IOException {
		String _input = "1.5, 2.7, 7.8\n9.0, 1.1, -3.5\n6.33, 03.33, 0.44\n-1.22, -2, -3.5\n-4.009, 2.11, 0.77";

		// native
		test_getCoordinates_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, false, METERS,
				Arrays.asList(new Coordinates(1.5, 2.7, 7.8),
						new Coordinates(9, 1.1, -3.5),
						new Coordinates(6.33, 3.33, 0.44),
						new Coordinates(-1.22, -2, -3.5),
						new Coordinates(-4.009, 2.11, 0.77)));

		// canonical (Y, Z, X)
		test_getCoordinates_validate(_input, Y_PLUS, Z_PLUS, X_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(2.7, 7.8, 1.5),
						new Coordinates(1.1, -3.5, 9),
						new Coordinates(3.33, 0.44, 6.33),
						new Coordinates(-2, -3.5, -1.22),
						new Coordinates(2.11, 0.77, -4.009)));
		// canonical (Y, Z, -X)
		test_getCoordinates_validate(_input, Y_PLUS, Z_PLUS, X_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(2.7, 7.8, -1.5),
						new Coordinates(1.1, -3.5, -9),
						new Coordinates(3.33, 0.44, -6.33),
						new Coordinates(-2, -3.5, 1.22),
						new Coordinates(2.11, 0.77, 4.009)));
		// canonical (Y, -Z, X)
		test_getCoordinates_validate(_input, Y_PLUS, Z_MINUS, X_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(2.7, -7.8, 1.5),
						new Coordinates(1.1, 3.5, 9),
						new Coordinates(3.33, -0.44, 6.33),
						new Coordinates(-2, 3.5, -1.22),
						new Coordinates(2.11, -0.77, -4.009)));
		// canonical (Y, -Z, -X)
		test_getCoordinates_validate(_input, Y_PLUS, Z_MINUS, X_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(2.7, -7.8, -1.5),
						new Coordinates(1.1, 3.5, -9),
						new Coordinates(3.33, -0.44, -6.33),
						new Coordinates(-2, 3.5, 1.22),
						new Coordinates(2.11, -0.77, 4.009)));

		// canonical (-Y, Z, X)
		test_getCoordinates_validate(_input, Y_MINUS, Z_PLUS, X_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-2.7, 7.8, 1.5),
						new Coordinates(-1.1, -3.5, 9),
						new Coordinates(-3.33, 0.44, 6.33),
						new Coordinates(2, -3.5, -1.22),
						new Coordinates(-2.11, 0.77, -4.009)));
		// canonical (-Y, Z, -X)
		test_getCoordinates_validate(_input, Y_MINUS, Z_PLUS, X_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-2.7, 7.8, -1.5),
						new Coordinates(-1.1, -3.5, -9),
						new Coordinates(-3.33, 0.44, -6.33),
						new Coordinates(2, -3.5, 1.22),
						new Coordinates(-2.11, 0.77, 4.009)));
		// canonical (-Y, -Z, X)
		test_getCoordinates_validate(_input, Y_MINUS, Z_MINUS, X_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-2.7, -7.8, 1.5),
						new Coordinates(-1.1, 3.5, 9),
						new Coordinates(-3.33, -0.44, 6.33),
						new Coordinates(2, 3.5, -1.22),
						new Coordinates(-2.11, -0.77, -4.009)));
		// canonical (-Y, -Z, -X)
		test_getCoordinates_validate(_input, Y_MINUS, Z_MINUS, X_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-2.7, -7.8, -1.5),
						new Coordinates(-1.1, 3.5, -9),
						new Coordinates(-3.33, -0.44, -6.33),
						new Coordinates(2, 3.5, 1.22),
						new Coordinates(-2.11, -0.77, 4.009)));

		// canonical (Z, X, Y)
		test_getCoordinates_validate(_input, Z_PLUS, X_PLUS, Y_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(7.8, 1.5, 2.7),
						new Coordinates(-3.5, 9, 1.1),
						new Coordinates(0.44, 6.33, 3.33),
						new Coordinates(-3.5, -1.22, -2),
						new Coordinates(0.77, -4.009, 2.11)));
		// canonical (Z, X, -Y)
		test_getCoordinates_validate(_input, Z_PLUS, X_PLUS, Y_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(7.8, 1.5, -2.7),
						new Coordinates(-3.5, 9, -1.1),
						new Coordinates(0.44, 6.33, -3.33),
						new Coordinates(-3.5, -1.22, 2),
						new Coordinates(0.77, -4.009, -2.11)));
		// canonical (Z, -X, Y)
		test_getCoordinates_validate(_input, Z_PLUS, X_MINUS, Y_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(7.8, -1.5, 2.7),
						new Coordinates(-3.5, -9, 1.1),
						new Coordinates(0.44, -6.33, 3.33),
						new Coordinates(-3.5, 1.22, -2),
						new Coordinates(0.77, 4.009, 2.11)));
		// canonical (Z, -X, -Y)
		test_getCoordinates_validate(_input, Z_PLUS, X_MINUS, Y_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(7.8, -1.5, -2.7),
						new Coordinates(-3.5, -9, -1.1),
						new Coordinates(0.44, -6.33, -3.33),
						new Coordinates(-3.5, 1.22, 2),
						new Coordinates(0.77, 4.009, -2.11)));
	}

	/**
	 * test getCoordinates()
	 * for excel test 5
	 */
	@Test
	void test_input4_getCoordinates() throws IOException {
		String _input = "0.9, 2.99  , 1.11\n-0.6, 3.7, 3.4\n-8, 5.7, 7.8\n-5, 2.3, 5.6\n1.1, 1.2, 11.1";

		// native
		test_getCoordinates_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, false, METERS,
				Arrays.asList(new Coordinates(0.9, 2.99, 1.11),
						new Coordinates(-0.6, 3.7, 3.4),
						new Coordinates(-8, 5.7, 7.8),
						new Coordinates(-5, 2.3, 5.6),
						new Coordinates(1.1, 1.2, 11.1)));

		// canonical (-Z, X, Y)
		test_getCoordinates_validate(_input, Z_MINUS, X_PLUS, Y_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-1.11, 0.9, 2.99),
						new Coordinates(-3.4, -0.6, 3.7),
						new Coordinates(-7.8, -8, 5.7),
						new Coordinates(-5.6, -5, 2.3),
						new Coordinates(-11.1, 1.1, 1.2)));
		// canonical (-Z, X, -Y)
		test_getCoordinates_validate(_input, Z_MINUS, X_PLUS, Y_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-1.11, 0.9, -2.99),
						new Coordinates(-3.4, -0.6, -3.7),
						new Coordinates(-7.8, -8, -5.7),
						new Coordinates(-5.6, -5, -2.3),
						new Coordinates(-11.1, 1.1, -1.2)));
		// canonical (-Z, -X, Y)
		test_getCoordinates_validate(_input, Z_MINUS, X_MINUS, Y_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-1.11, -0.9, 2.99),
						new Coordinates(-3.4, 0.6, 3.7),
						new Coordinates(-7.8, 8, 5.7),
						new Coordinates(-5.6, 5, 2.3),
						new Coordinates(-11.1, -1.1, 1.2)));
		// canonical (-Z, -X, -Y)
		test_getCoordinates_validate(_input, Z_MINUS, X_MINUS, Y_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-1.11, -0.9, -2.99),
						new Coordinates(-3.4, 0.6, -3.7),
						new Coordinates(-7.8, 8, -5.7),
						new Coordinates(-5.6, 5, -2.3),
						new Coordinates(-11.1, -1.1, -1.2)));

		// canonical (Z, Y, X)
		test_getCoordinates_validate(_input, Z_PLUS, Y_PLUS, X_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(1.11, 2.99, 0.9),
						new Coordinates(3.4, 3.7, -0.6),
						new Coordinates(7.8, 5.7, -8),
						new Coordinates(5.6, 2.3, -5),
						new Coordinates(11.1, 1.2, 1.1)));
		// canonical (Z, Y, -X)
		test_getCoordinates_validate(_input, Z_PLUS, Y_PLUS, X_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(1.11, 2.99, -0.9),
						new Coordinates(3.4, 3.7, 0.6),
						new Coordinates(7.8, 5.7, 8),
						new Coordinates(5.6, 2.3, 5),
						new Coordinates(11.1, 1.2, -1.1)));
		// canonical (Z, -Y, X)
		test_getCoordinates_validate(_input, Z_PLUS, Y_MINUS, X_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(1.11, -2.99, 0.9),
						new Coordinates(3.4, -3.7, -0.6),
						new Coordinates(7.8, -5.7, -8),
						new Coordinates(5.6, -2.3, -5),
						new Coordinates(11.1, -1.2, 1.1)));
		// canonical (Z, -Y, -X)
		test_getCoordinates_validate(_input, Z_PLUS, Y_MINUS, X_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(1.11, -2.99, -0.9),
						new Coordinates(3.4, -3.7, 0.6),
						new Coordinates(7.8, -5.7, 8),
						new Coordinates(5.6, -2.3, 5),
						new Coordinates(11.1, -1.2, -1.1)));

		// canonical (-Z, Y, X)
		test_getCoordinates_validate(_input, Z_MINUS, Y_PLUS, X_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-1.11, 2.99, 0.9),
						new Coordinates(-3.4, 3.7, -0.6),
						new Coordinates(-7.8, 5.7, -8),
						new Coordinates(-5.6, 2.3, -5),
						new Coordinates(-11.1, 1.2, 1.1)));
		// canonical (-Z, Y, -X)
		test_getCoordinates_validate(_input, Z_MINUS, Y_PLUS, X_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-1.11, 2.99, -0.9),
						new Coordinates(-3.4, 3.7, 0.6),
						new Coordinates(-7.8, 5.7, 8),
						new Coordinates(-5.6, 2.3, 5),
						new Coordinates(-11.1, 1.2, -1.1)));
		// canonical (-Z, -Y, X)
		test_getCoordinates_validate(_input, Z_MINUS, Y_MINUS, X_PLUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-1.11, -2.99, 0.9),
						new Coordinates(-3.4, -3.7, -0.6),
						new Coordinates(-7.8, -5.7, -8),
						new Coordinates(-5.6, -2.3, -5),
						new Coordinates(-11.1, -1.2, 1.1)));
		// canonical (-Z, -Y, -X)
		test_getCoordinates_validate(_input, Z_MINUS, Y_MINUS, X_MINUS, METERS, true, METERS,
				Arrays.asList(new Coordinates(-1.11, -2.99, -0.9),
						new Coordinates(-3.4, -3.7, 0.6),
						new Coordinates(-7.8, -5.7, 8),
						new Coordinates(-5.6, -2.3, 5),
						new Coordinates(-11.1, -1.2, -1.1)));

	}

	/**
	 * helper method for calculateDistances tests
	 *
	 * @param _input                 input string
	 * @param _axisA                 canonical A axis
	 * @param _axisB                 canonical B axis
	 * @param _axisC                 canonical C axis
	 * @param _unitPlanner           canonical units
	 * @param _axisCombination       axis combination to use for distances
	 * @param _isCanonicalElseNative use canonical or native for distances
	 * @param _unitDistances         units for distances
	 * @param _distancesExpected     list of expected distances
	 */
	private void test_calculateDistances_validate(String _input, E_AxisNative _axisA, E_AxisNative _axisB, E_AxisNative _axisC, E_Unit _unitPlanner, E_AxisCombinationNeutral _axisCombination, boolean _isCanonicalElseNative, E_Unit _unitDistances, List<Double> _distancesExpected) throws IOException {
		InputStream _stream = new ByteArrayInputStream(_input.getBytes());

		WaypointPlanner _planner = new WaypointPlanner(_axisA, _axisB, _axisC, _unitPlanner, _stream);

		List<Double> _distances = _planner.calculateDistances(_axisCombination, _isCanonicalElseNative, _unitDistances);

		validateDistances(_distances, _distancesExpected);

		_stream.close();
	}

	/**
	 * test calculateDistances()
	 * for excel input 1
	 */
	@Test
	@SuppressWarnings("Duplicates")
	void test_input1_calculateDistances() throws IOException {
		String _input = "0,0,0\n1,1,1\n2,2,2\n3,3,3\n4,4,4";

		// native
		// change across ABC should always be the same
		// brute force all axis combinations
		for (E_AxisNative _axisA : E_AxisNative.values()) {
			for (E_AxisNative _axisB : E_AxisNative.values()) {
				for (E_AxisNative _axisC : E_AxisNative.values()) {
					// check no missing axis
					if (!missingAxis(_axisA, _axisB, _axisC)) {
						test_calculateDistances_validate(_input, _axisA, _axisB, _axisC, METERS, FIRST_SECOND_THIRD, false, METERS,
								Arrays.asList(1.732050808, 1.732050808, 1.732050808, 1.732050808));
					}
				}
			}
		}
		// first
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST, false, METERS,
				Arrays.asList(1.0, 1.0, 1.0, 1.0));
		// second
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND, false, METERS,
				Arrays.asList(1.0, 1.0, 1.0, 1.0));
		// third
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, THIRD, false, METERS,
				Arrays.asList(1.0, 1.0, 1.0, 1.0));
		// first second
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_SECOND, false, METERS,
				Arrays.asList(1.414213562, 1.414213562, 1.414213562, 1.414213562));
		// first third
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_THIRD, false, METERS,
				Arrays.asList(1.414213562, 1.414213562, 1.414213562, 1.414213562));
		// second third
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND_THIRD, false, METERS,
				Arrays.asList(1.414213562, 1.414213562, 1.414213562, 1.414213562));

		// canonical (X,Y,Z)
		// first second third
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_SECOND_THIRD, true, METERS,
				Arrays.asList(1.732050808, 1.732050808, 1.732050808, 1.732050808));
		// first
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST, true, METERS,
				Arrays.asList(1.0, 1.0, 1.0, 1.0));
		// second
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND, true, METERS,
				Arrays.asList(1.0, 1.0, 1.0, 1.0));
		// third
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, THIRD, true, METERS,
				Arrays.asList(1.0, 1.0, 1.0, 1.0));
		// first second
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_SECOND, true, METERS,
				Arrays.asList(1.414213562, 1.414213562, 1.414213562, 1.414213562));
		// first third
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_THIRD, true, METERS,
				Arrays.asList(1.414213562, 1.414213562, 1.414213562, 1.414213562));
		// second third
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND_THIRD, true, METERS,
				Arrays.asList(1.414213562, 1.414213562, 1.414213562, 1.414213562));

	}

	/**
	 * test calculateDistances()
	 * for excel input 2
	 */
	@Test
	@SuppressWarnings("Duplicates")
	void test_input2_calculateDistances() throws IOException {
		String _input = "0,0,0\n-1,-1,-1\n-2,-2,-2\n-3,-3,-3\n-4,-4,-4";

		// native
		// change across ABC should always be the same
		// brute force all axis combinations
		for (E_AxisNative _axisA : E_AxisNative.values()) {
			for (E_AxisNative _axisB : E_AxisNative.values()) {
				for (E_AxisNative _axisC : E_AxisNative.values()) {
					// check no missing axis
					if (!missingAxis(_axisA, _axisB, _axisC)) {
						test_calculateDistances_validate(_input, _axisA, _axisB, _axisC, METERS, FIRST_SECOND_THIRD, false, METERS,
								Arrays.asList(1.732050808, 1.732050808, 1.732050808, 1.732050808));
					}
				}
			}
		}
		// first
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST, false, METERS,
				Arrays.asList(1.0, 1.0, 1.0, 1.0));
		// second
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND, false, METERS,
				Arrays.asList(1.0, 1.0, 1.0, 1.0));
		// third
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, THIRD, false, METERS,
				Arrays.asList(1.0, 1.0, 1.0, 1.0));
		// first second
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_SECOND, false, METERS,
				Arrays.asList(1.414213562, 1.414213562, 1.414213562, 1.414213562));
		// first third
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_THIRD, false, METERS,
				Arrays.asList(1.414213562, 1.414213562, 1.414213562, 1.414213562));
		// second third
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND_THIRD, false, METERS,
				Arrays.asList(1.414213562, 1.414213562, 1.414213562, 1.414213562));

		// canonical (-X,Z,Y)
		// first second third
		test_calculateDistances_validate(_input, X_MINUS, Z_PLUS, Y_PLUS, METERS, FIRST_SECOND_THIRD, true, METERS,
				Arrays.asList(1.732050808, 1.732050808, 1.732050808, 1.732050808));
		// first
		test_calculateDistances_validate(_input, X_MINUS, Z_PLUS, Y_PLUS, METERS, FIRST, true, METERS,
				Arrays.asList(1.0, 1.0, 1.0, 1.0));
		// second
		test_calculateDistances_validate(_input, X_MINUS, Z_PLUS, Y_PLUS, METERS, SECOND, true, METERS,
				Arrays.asList(1.0, 1.0, 1.0, 1.0));
		// third
		test_calculateDistances_validate(_input, X_MINUS, Z_PLUS, Y_PLUS, METERS, THIRD, true, METERS,
				Arrays.asList(1.0, 1.0, 1.0, 1.0));
		// first second
		test_calculateDistances_validate(_input, X_MINUS, Z_PLUS, Y_PLUS, METERS, FIRST_SECOND, true, METERS,
				Arrays.asList(1.414213562, 1.414213562, 1.414213562, 1.414213562));
		// first third
		test_calculateDistances_validate(_input, X_MINUS, Z_PLUS, Y_PLUS, METERS, FIRST_THIRD, true, METERS,
				Arrays.asList(1.414213562, 1.414213562, 1.414213562, 1.414213562));
		// second third
		test_calculateDistances_validate(_input, X_MINUS, Z_PLUS, Y_PLUS, METERS, SECOND_THIRD, true, METERS,
				Arrays.asList(1.414213562, 1.414213562, 1.414213562, 1.414213562));

	}

	/**
	 * test calculateDistances()
	 * for excel input 3
	 */
	@Test
	@SuppressWarnings("Duplicates")
	void test_input3_calculateDistances() throws IOException {
		String _input = "1.5, 2.7, 7.8\n9.0, 1.1, -3.5\n6.33, 03.33, 0.44\n-1.22, -2, -3.5\n-4.009, 2.11, 0.77";

		// native
		// change across ABC should always be the same
		// brute force all axis combinations
		for (E_AxisNative _axisA : E_AxisNative.values()) {
			for (E_AxisNative _axisB : E_AxisNative.values()) {
				for (E_AxisNative _axisC : E_AxisNative.values()) {
					// check no missing axis
					if (!missingAxis(_axisA, _axisB, _axisC)) {
						test_calculateDistances_validate(_input, _axisA, _axisB, _axisC, METERS, FIRST_SECOND_THIRD, false, METERS,
								Arrays.asList(13.65650028, 5.255987062, 10.04664123, 6.550077938));
					}
				}
			}
		}
		// first
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST, false, METERS,
				Arrays.asList(7.5, 2.67, 7.55, 2.789));
		// second
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND, false, METERS,
				Arrays.asList(1.6, 2.23, 5.33, 4.11));
		// third
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, THIRD, false, METERS,
				Arrays.asList(11.3, 3.94, 3.94, 4.27));
		// first second
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_SECOND, false, METERS,
				Arrays.asList(7.668767828, 3.478764148, 9.241828823, 4.966952889));
		// first third
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_THIRD, false, METERS,
				Arrays.asList(13.56244816, 4.759464256, 8.51622569, 5.100139312));
		// second third
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND_THIRD, false, METERS,
				Arrays.asList(11.41271221, 4.527306042, 6.628159624, 5.926634796));

		// canonical (Y,Z,X)
		// first second third
		test_calculateDistances_validate(_input, Y_PLUS, Z_PLUS, X_PLUS, METERS, FIRST_SECOND_THIRD, true, METERS,
				Arrays.asList(13.65650028, 5.255987062, 10.04664123, 6.550077938));
		// first
		test_calculateDistances_validate(_input, Y_PLUS, Z_PLUS, X_PLUS, METERS, FIRST, true, METERS,
				Arrays.asList(1.6, 2.23, 5.33, 4.11));
		// second
		test_calculateDistances_validate(_input, Y_PLUS, Z_PLUS, X_PLUS, METERS, SECOND, true, METERS,
				Arrays.asList(11.3, 3.94, 3.94, 4.27));
		// third
		test_calculateDistances_validate(_input, Y_PLUS, Z_PLUS, X_PLUS, METERS, THIRD, true, METERS,
				Arrays.asList(7.5, 2.67, 7.55, 2.789));
		// first second
		test_calculateDistances_validate(_input, Y_PLUS, Z_PLUS, X_PLUS, METERS, FIRST_SECOND, true, METERS,
				Arrays.asList(11.41271221, 4.527306042, 6.628159624, 5.926634796));
		// first third
		test_calculateDistances_validate(_input, Y_PLUS, Z_PLUS, X_PLUS, METERS, FIRST_THIRD, true, METERS,
				Arrays.asList(7.668767828, 3.478764148, 9.241828823, 4.966952889));
		// second third
		test_calculateDistances_validate(_input, Y_PLUS, Z_PLUS, X_PLUS, METERS, SECOND_THIRD, true, METERS,
				Arrays.asList(13.56244816, 4.759464256, 8.51622569, 5.100139312));

		// canonical (Z,X,Y)
		// first second third
		test_calculateDistances_validate(_input, Z_PLUS, X_PLUS, Y_PLUS, METERS, FIRST_SECOND_THIRD, true, METERS,
				Arrays.asList(13.65650028, 5.255987062, 10.04664123, 6.550077938));
		// first
		test_calculateDistances_validate(_input, Z_PLUS, X_PLUS, Y_PLUS, METERS, FIRST, true, METERS,
				Arrays.asList(11.3, 3.94, 3.94, 4.27));
		// second
		test_calculateDistances_validate(_input, Z_PLUS, X_PLUS, Y_PLUS, METERS, SECOND, true, METERS,
				Arrays.asList(7.5, 2.67, 7.55, 2.789));
		// third
		test_calculateDistances_validate(_input, Z_PLUS, X_PLUS, Y_PLUS, METERS, THIRD, true, METERS,
				Arrays.asList(1.6, 2.23, 5.33, 4.11));
		// first second
		test_calculateDistances_validate(_input, Z_PLUS, X_PLUS, Y_PLUS, METERS, FIRST_SECOND, true, METERS,
				Arrays.asList(13.56244816, 4.759464256, 8.51622569, 5.100139312));
		// first third
		test_calculateDistances_validate(_input, Z_PLUS, X_PLUS, Y_PLUS, METERS, FIRST_THIRD, true, METERS,
				Arrays.asList(11.41271221, 4.527306042, 6.628159624, 5.926634796));
		// second third
		test_calculateDistances_validate(_input, Z_PLUS, X_PLUS, Y_PLUS, METERS, SECOND_THIRD, true, METERS,
				Arrays.asList(7.668767828, 3.478764148, 9.241828823, 4.966952889));

	}

	/**
	 * test calculateDistances()
	 * for excel input 5
	 */
	@Test
	@SuppressWarnings("Duplicates")
	void test_input4_calculateDistances() throws IOException {
		String _input = "0.9, 2.99  , 1.11\n-0.6, 3.7, 3.4\n-8, 5.7, 7.8\n-5, 2.3, 5.6\n1.1, 1.2, 11.1";

		// native
		// change across ABC should always be the same
		// brute force all axis combinations
		for (E_AxisNative _axisA : E_AxisNative.values()) {
			for (E_AxisNative _axisB : E_AxisNative.values()) {
				for (E_AxisNative _axisC : E_AxisNative.values()) {
					// check no missing axis
					if (!missingAxis(_axisA, _axisB, _axisC)) {
						test_calculateDistances_validate(_input, _axisA, _axisB, _axisC, METERS, FIRST_SECOND_THIRD, false, METERS,
								Arrays.asList(2.828108909, 8.838551918, 5.039841267, 8.28673639));
					}
				}
			}
		}
		// first
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST, false, METERS,
				Arrays.asList(1.5, 7.4, 3.0, 6.1));
		// second
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND, false, METERS,
				Arrays.asList(0.71, 2.0, 3.4, 1.1));
		// third
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, THIRD, false, METERS,
				Arrays.asList(2.29, 4.4, 2.2, 5.5));
		// first second
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_SECOND, false, METERS,
				Arrays.asList(1.659548131, 7.665507159, 4.53431362, 6.198386887));
		// first third
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_THIRD, false, METERS,
				Arrays.asList(2.737535388, 8.6092973, 3.720215048, 8.213403679));
		// second third
		test_calculateDistances_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND_THIRD, false, METERS,
				Arrays.asList(2.397540406, 4.833218389, 4.049691346, 5.608921465));

		// canonical (-Z,X,Y)
		// first second third
		test_calculateDistances_validate(_input, Z_MINUS, X_PLUS, Y_PLUS, METERS, FIRST_SECOND_THIRD, true, METERS,
				Arrays.asList(2.828108909, 8.838551918, 5.039841267, 8.28673639));
		// first
		test_calculateDistances_validate(_input, Z_MINUS, X_PLUS, Y_PLUS, METERS, FIRST, true, METERS,
				Arrays.asList(2.29, 4.4, 2.2, 5.5));
		// second
		test_calculateDistances_validate(_input, Z_MINUS, X_PLUS, Y_PLUS, METERS, SECOND, true, METERS,
				Arrays.asList(1.5, 7.4, 3.0, 6.1));
		// third
		test_calculateDistances_validate(_input, Z_MINUS, X_PLUS, Y_PLUS, METERS, THIRD, true, METERS,
				Arrays.asList(0.71, 2.0, 3.4, 1.1));
		// first second
		test_calculateDistances_validate(_input, Z_MINUS, X_PLUS, Y_PLUS, METERS, FIRST_SECOND, true, METERS,
				Arrays.asList(2.737535388, 8.6092973, 3.720215048, 8.213403679));
		// first third
		test_calculateDistances_validate(_input, Z_MINUS, X_PLUS, Y_PLUS, METERS, FIRST_THIRD, true, METERS,
				Arrays.asList(2.397540406, 4.833218389, 4.049691346, 5.608921465));
		// second third
		test_calculateDistances_validate(_input, Z_MINUS, X_PLUS, Y_PLUS, METERS, SECOND_THIRD, true, METERS,
				Arrays.asList(1.659548131, 7.665507159, 4.53431362, 6.198386887));

		// canonical (Z,Y,X)
		// first second third
		test_calculateDistances_validate(_input, Z_PLUS, Y_PLUS, X_PLUS, METERS, FIRST_SECOND_THIRD, true, METERS,
				Arrays.asList(2.828108909, 8.838551918, 5.039841267, 8.28673639));
		// first
		test_calculateDistances_validate(_input, Z_PLUS, Y_PLUS, X_PLUS, METERS, FIRST, true, METERS,
				Arrays.asList(2.29, 4.4, 2.2, 5.5));
		// second
		test_calculateDistances_validate(_input, Z_PLUS, Y_PLUS, X_PLUS, METERS, SECOND, true, METERS,
				Arrays.asList(0.71, 2.0, 3.4, 1.1));
		// third
		test_calculateDistances_validate(_input, Z_PLUS, Y_PLUS, X_PLUS, METERS, THIRD, true, METERS,
				Arrays.asList(1.5, 7.4, 3.0, 6.1));
		// first second
		test_calculateDistances_validate(_input, Z_PLUS, Y_PLUS, X_PLUS, METERS, FIRST_SECOND, true, METERS,
				Arrays.asList(2.397540406, 4.833218389, 4.049691346, 5.608921465));
		// first third
		test_calculateDistances_validate(_input, Z_PLUS, Y_PLUS, X_PLUS, METERS, FIRST_THIRD, true, METERS,
				Arrays.asList(2.737535388, 8.6092973, 3.720215048, 8.213403679));
		// second third
		test_calculateDistances_validate(_input, Z_PLUS, Y_PLUS, X_PLUS, METERS, SECOND_THIRD, true, METERS,
				Arrays.asList(1.659548131, 7.665507159, 4.53431362, 6.198386887));

	}


	/**
	 * helper method for calculateDistance tests
	 *
	 * @param _input                 input string
	 * @param _axisA                 canonical A axis
	 * @param _axisB                 canonical B axis
	 * @param _axisC                 canonical C axis
	 * @param _unitPlanner           canonical units
	 * @param _axisCombination       axis combination to use for distances
	 * @param _isCanonicalElseNative use canonical or native for distances
	 * @param _unitDistances         units for distances
	 * @param _distanceExpected      expected distance
	 */
	private void test_calculateDistance_validate(String _input, E_AxisNative _axisA, E_AxisNative _axisB, E_AxisNative _axisC, E_Unit _unitPlanner, E_AxisCombinationNeutral _axisCombination, boolean _isCanonicalElseNative, E_Unit _unitDistances, double _distanceExpected) throws IOException {
		InputStream _stream = new ByteArrayInputStream(_input.getBytes());

		WaypointPlanner _planner = new WaypointPlanner(_axisA, _axisB, _axisC, _unitPlanner, _stream);

		double _distances = _planner.calculateDistance(_axisCombination, _isCanonicalElseNative, _unitDistances);

		validateDistance(_distances, _distanceExpected);

		_stream.close();
	}

	/**
	 * test calculateDistance()
	 * for excel input 1
	 */
	@Test
	@SuppressWarnings("Duplicates")
	void test_input1_calculateDistance() throws IOException {
		String _input = "0,0,0\n1,1,1\n2,2,2\n3,3,3\n4,4,4";

		// native
		// change across ABC should always be the same
		// brute force all axis combinations
		for (E_AxisNative _axisA : E_AxisNative.values()) {
			for (E_AxisNative _axisB : E_AxisNative.values()) {
				for (E_AxisNative _axisC : E_AxisNative.values()) {
					// check no missing axis
					if (!missingAxis(_axisA, _axisB, _axisC)) {
						test_calculateDistance_validate(_input, _axisA, _axisB, _axisC, METERS, FIRST_SECOND_THIRD, false, METERS,
								6.92820323);
					}
				}
			}
		}
		// first
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST, false, METERS,
				4.0);
		// second
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND, false, METERS,
				4.0);
		// third
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, THIRD, false, METERS,
				4.0);
		// first second
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_SECOND, false, METERS,
				5.656854249);
		// first third
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_THIRD, false, METERS,
				5.656854249);
		// second third
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND_THIRD, false, METERS,
				5.656854249);

		// canonical (X,Y,Z)
		// first second third
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_SECOND_THIRD, true, METERS,
				6.92820323);
		// first
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST, true, METERS,
				4.0);
		// second
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND, true, METERS,
				4.0);
		// third
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, THIRD, true, METERS,
				4.0);
		// first second
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_SECOND, true, METERS,
				5.656854249);
		// first third
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_THIRD, true, METERS,
				5.656854249);
		// second third
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND_THIRD, true, METERS,
				5.656854249);

	}

	/**
	 * test calculateDistance()
	 * for excel input 2
	 */
	@Test
	@SuppressWarnings("Duplicates")
	void test_input2_calculateDistance() throws IOException {
		String _input = "0,0,0\n-1,-1,-1\n-2,-2,-2\n-3,-3,-3\n-4,-4,-4";

		// native
		// change across ABC should always be the same
		// brute force all axis combinations
		for (E_AxisNative _axisA : E_AxisNative.values()) {
			for (E_AxisNative _axisB : E_AxisNative.values()) {
				for (E_AxisNative _axisC : E_AxisNative.values()) {
					// check no missing axis
					if (!missingAxis(_axisA, _axisB, _axisC)) {
						test_calculateDistance_validate(_input, _axisA, _axisB, _axisC, METERS, FIRST_SECOND_THIRD, false, METERS,
								6.92820323);
					}
				}
			}
		}
		// first
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST, false, METERS,
				4.0);
		// second
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND, false, METERS,
				4.0);
		// third
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, THIRD, false, METERS,
				4.0);
		// first second
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_SECOND, false, METERS,
				5.656854249);
		// first third
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_THIRD, false, METERS,
				5.656854249);
		// second third
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND_THIRD, false, METERS,
				5.656854249);

		// canonical (-X,Z,Y)
		// first second third
		test_calculateDistance_validate(_input, X_MINUS, Z_PLUS, Y_PLUS, METERS, FIRST_SECOND_THIRD, true, METERS,
				6.92820323);
		// first
		test_calculateDistance_validate(_input, X_MINUS, Z_PLUS, Y_PLUS, METERS, FIRST, true, METERS,
				4.0);
		// second
		test_calculateDistance_validate(_input, X_MINUS, Z_PLUS, Y_PLUS, METERS, SECOND, true, METERS,
				4.0);
		// third
		test_calculateDistance_validate(_input, X_MINUS, Z_PLUS, Y_PLUS, METERS, THIRD, true, METERS,
				4.0);
		// first second
		test_calculateDistance_validate(_input, X_MINUS, Z_PLUS, Y_PLUS, METERS, FIRST_SECOND, true, METERS,
				5.656854249);
		// first third
		test_calculateDistance_validate(_input, X_MINUS, Z_PLUS, Y_PLUS, METERS, FIRST_THIRD, true, METERS,
				5.656854249);
		// second third
		test_calculateDistance_validate(_input, X_MINUS, Z_PLUS, Y_PLUS, METERS, SECOND_THIRD, true, METERS,
				5.656854249);

	}

	/**
	 * test calculateDistance()
	 * for excel input 3
	 */
	@Test
	@SuppressWarnings("Duplicates")
	void test_input3_calculateDistance() throws IOException {
		String _input = "1.5, 2.7, 7.8\n9.0, 1.1, -3.5\n6.33, 03.33, 0.44\n-1.22, -2, -3.5\n-4.009, 2.11, 0.77";

		// native
		// change across ABC should always be the same
		// brute force all axis combinations
		for (E_AxisNative _axisA : E_AxisNative.values()) {
			for (E_AxisNative _axisB : E_AxisNative.values()) {
				for (E_AxisNative _axisC : E_AxisNative.values()) {
					// check no missing axis
					if (!missingAxis(_axisA, _axisB, _axisC)) {
						test_calculateDistance_validate(_input, _axisA, _axisB, _axisC, METERS, FIRST_SECOND_THIRD, false, METERS,
								35.50920651);
					}
				}
			}
		}
		// first
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST, false, METERS,
				20.509);
		// second
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND, false, METERS,
				13.27);
		// third
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, THIRD, false, METERS,
				23.45);
		// first second
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_SECOND, false, METERS,
				25.35631369);
		// first third
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_THIRD, false, METERS,
				31.93827741);
		// second third
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND_THIRD, false, METERS,
				28.49481267);

		// canonical (Y,Z,X)
		// first second third
		test_calculateDistance_validate(_input, Y_PLUS, Z_PLUS, X_PLUS, METERS, FIRST_SECOND_THIRD, true, METERS,
				35.50920651);
		// first
		test_calculateDistance_validate(_input, Y_PLUS, Z_PLUS, X_PLUS, METERS, FIRST, true, METERS,
				13.27);
		// second
		test_calculateDistance_validate(_input, Y_PLUS, Z_PLUS, X_PLUS, METERS, SECOND, true, METERS,
				23.45);
		// third
		test_calculateDistance_validate(_input, Y_PLUS, Z_PLUS, X_PLUS, METERS, THIRD, true, METERS,
				20.509);
		// first second
		test_calculateDistance_validate(_input, Y_PLUS, Z_PLUS, X_PLUS, METERS, FIRST_SECOND, true, METERS,
				28.49481267);
		// first third
		test_calculateDistance_validate(_input, Y_PLUS, Z_PLUS, X_PLUS, METERS, FIRST_THIRD, true, METERS,

				25.35631369);
		// second third
		test_calculateDistance_validate(_input, Y_PLUS, Z_PLUS, X_PLUS, METERS, SECOND_THIRD, true, METERS,
				31.93827741);

		// canonical (Z,X,Y)
		// first second third
		test_calculateDistance_validate(_input, Z_PLUS, X_PLUS, Y_PLUS, METERS, FIRST_SECOND_THIRD, true, METERS,
				35.50920651);
		// first
		test_calculateDistance_validate(_input, Z_PLUS, X_PLUS, Y_PLUS, METERS, FIRST, true, METERS,
				23.45);
		// second
		test_calculateDistance_validate(_input, Z_PLUS, X_PLUS, Y_PLUS, METERS, SECOND, true, METERS,
				20.509);
		// third
		test_calculateDistance_validate(_input, Z_PLUS, X_PLUS, Y_PLUS, METERS, THIRD, true, METERS,
				13.27);
		// first second
		test_calculateDistance_validate(_input, Z_PLUS, X_PLUS, Y_PLUS, METERS, FIRST_SECOND, true, METERS,
				31.93827741);
		// first third
		test_calculateDistance_validate(_input, Z_PLUS, X_PLUS, Y_PLUS, METERS, FIRST_THIRD, true, METERS,
				28.49481267);
		// second third
		test_calculateDistance_validate(_input, Z_PLUS, X_PLUS, Y_PLUS, METERS, SECOND_THIRD, true, METERS,
				25.35631369);

	}

	/**
	 * test calculateDistance()
	 * for excel input 5
	 */
	@Test
	@SuppressWarnings("Duplicates")
	void test_input4_calculateDistance() throws IOException {
		String _input = "0.9, 2.99  , 1.11\n-0.6, 3.7, 3.4\n-8, 5.7, 7.8\n-5, 2.3, 5.6\n1.1, 1.2, 11.1";

		// native
		// change across ABC should always be the same
		// brute force all axis combinations
		for (E_AxisNative _axisA : E_AxisNative.values()) {
			for (E_AxisNative _axisB : E_AxisNative.values()) {
				for (E_AxisNative _axisC : E_AxisNative.values()) {
					// check no missing axis
					if (!missingAxis(_axisA, _axisB, _axisC)) {
						test_calculateDistance_validate(_input, _axisA, _axisB, _axisC, METERS, FIRST_SECOND_THIRD, false, METERS,
								24.99323848
						);
					}
				}
			}
		}
		// first
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST, false, METERS,
				18.0);
		// second
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND, false, METERS,
				7.21);
		// third
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, THIRD, false, METERS,
				14.39);
		// first second
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_SECOND, false, METERS,
				20.0577558);
		// first third
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, FIRST_THIRD, false, METERS,
				23.28045141);
		// second third
		test_calculateDistance_validate(_input, X_PLUS, Y_PLUS, Z_PLUS, METERS, SECOND_THIRD, false, METERS,
				16.88937161);

		// canonical (-Z,X,Y)
		// first second third
		test_calculateDistance_validate(_input, Z_MINUS, X_PLUS, Y_PLUS, METERS, FIRST_SECOND_THIRD, true, METERS,
				24.99323848);
		// first
		test_calculateDistance_validate(_input, Z_MINUS, X_PLUS, Y_PLUS, METERS, FIRST, true, METERS,
				14.39);
		// second
		test_calculateDistance_validate(_input, Z_MINUS, X_PLUS, Y_PLUS, METERS, SECOND, true, METERS,
				18.0);
		// third
		test_calculateDistance_validate(_input, Z_MINUS, X_PLUS, Y_PLUS, METERS, THIRD, true, METERS,
				7.21);
		// first second
		test_calculateDistance_validate(_input, Z_MINUS, X_PLUS, Y_PLUS, METERS, FIRST_SECOND, true, METERS,
				23.28045141);
		// first third
		test_calculateDistance_validate(_input, Z_MINUS, X_PLUS, Y_PLUS, METERS, FIRST_THIRD, true, METERS,
				16.88937161);
		// second third
		test_calculateDistance_validate(_input, Z_MINUS, X_PLUS, Y_PLUS, METERS, SECOND_THIRD, true, METERS,
				20.0577558);

		// canonical (Z,Y,X)
		// first second third
		test_calculateDistance_validate(_input, Z_PLUS, Y_PLUS, X_PLUS, METERS, FIRST_SECOND_THIRD, true, METERS,
				24.99323848);
		// first
		test_calculateDistance_validate(_input, Z_PLUS, Y_PLUS, X_PLUS, METERS, FIRST, true, METERS,
				14.39);
		// second
		test_calculateDistance_validate(_input, Z_PLUS, Y_PLUS, X_PLUS, METERS, SECOND, true, METERS,
				7.21);
		// third
		test_calculateDistance_validate(_input, Z_PLUS, Y_PLUS, X_PLUS, METERS, THIRD, true, METERS,
				18.0);
		// first second
		test_calculateDistance_validate(_input, Z_PLUS, Y_PLUS, X_PLUS, METERS, FIRST_SECOND, true, METERS,
				16.88937161);
		// first third
		test_calculateDistance_validate(_input, Z_PLUS, Y_PLUS, X_PLUS, METERS, FIRST_THIRD, true, METERS,
				23.28045141);
		// second third
		test_calculateDistance_validate(_input, Z_PLUS, Y_PLUS, X_PLUS, METERS, SECOND_THIRD, true, METERS,
				20.0577558);

	}

	// UNIT CONVERSION TESTS

	/**
	 * Check all waypoints on x, y, and zed axes for correct conversion.
	 *
	 * @param _converted Waypoints as given by internal conversion.
	 * @param _expected  Expected results.
	 */
	private void validateUnitConversion_waypoints(List<Coordinates> _converted, List<Coordinates> _expected) {
		for (int i = 0; i < _converted.size(); i++) {
			assertEquals(_converted.get(i).getFirst(), _expected.get(i).getFirst(), doubleDelta);
			assertEquals(_converted.get(i).getSecond(), _expected.get(i).getSecond(), doubleDelta);
			assertEquals(_converted.get(i).getThird(), _expected.get(i).getThird(), doubleDelta);
		}
	}

	/**
	 * Check all intermediate distances for correct conversion.
	 *
	 * @param _converted Distances as given by internal conversion.
	 * @param _expected  Expected results.
	 */
	private void validateUnitConversion_distances(List<Double> _converted, List<Double> _expected) {
		for (int i = 0; i < _expected.size(); i++) {
			assertEquals(_expected.get(i), _converted.get(i), doubleDelta);
		}
	}

	/**
	 * Check total distance for correct conversion.
	 *
	 * @param _converted Total distance as given by internal conversion.
	 * @param _expected  Expected results.
	 */
	private void validateUnitConversion_totalDistance(double _converted, double _expected) {
		assertEquals(_expected, _converted, doubleDelta);
	}

	/**
	 * Helper method to generate list of expected waypoints to save a lot of copy and pasting.
	 *
	 * @param _input Input string; copied from Excel sheet; structure is: X,Y,Z\nX,Y,Z\n...etc.
	 * @return The list of waypoints.
	 */
	private List<Coordinates> createExpectedWaypointsList(String _input) {
		List<Coordinates> _expectedCoordinates = new ArrayList<>();
		Arrays.stream(_input.split("\n")).forEach(line -> {
			double[] _waypoints = Arrays.stream(line.split(","))
					.mapToDouble(Double::parseDouble)
					.toArray();
			_expectedCoordinates.add(new Coordinates(_waypoints[0], _waypoints[1], _waypoints[2]));
		});
		return _expectedCoordinates;
	}

	/**
	 * Helper method to generate list of expected waypoints to save a lot of copy and pasting.
	 * NOTE: exclude first zero-distance from expected String; not used by internal conversion
	 *
	 * @param _input Input string; copied from Excel sheet; structure is: D\nD\n...etc.
	 * @return The list of distances.
	 */
	private List<Double> createExpectedDistancesList(String _input) {
		List<Double> _expectedDistances = new ArrayList<>();
		Arrays.stream(_input.split("\n"))
				.mapToDouble(Double::parseDouble)
				.forEach(_expectedDistances::add);
		return _expectedDistances;
	}

	/**
	 * Tester driver; takes input string, creates waypoint planner, gets converted coordinates, distances, and total distance; tests them against expected values
	 *
	 * @param _input                 Simulated input via String
	 * @param _unitIn                Starting unit
	 * @param _unitOut               Ending unit
	 * @param _expectedCoordinates   Expected results of conversion of coordinates
	 * @param _expectedDistances     Expected results of conversion of distances
	 * @param _expectedTotalDistance Expected result of total distance
	 * @throws IOException IntelliJ said I needed this - pfft
	 */
	private void test_unitConversion(String _input, E_Unit _unitIn, E_Unit _unitOut, List<Coordinates> _expectedCoordinates, List<Double> _expectedDistances, double _expectedTotalDistance) throws IOException {
		// Stream-in points
		InputStream _stream = new ByteArrayInputStream(_input.getBytes());

		// Create waypoint planner
		WaypointPlanner _planner = new WaypointPlanner(X_PLUS, Y_PLUS, Z_PLUS, _unitIn, _stream);

		// Get list of coordinates and intermediate distances; total distance, too
		List<Coordinates> _convertedCoordinates = _planner.getCoordinates(true, _unitOut);
		List<Double> _convertedDistances = _planner.calculateDistances(FIRST_SECOND_THIRD, true, _unitOut);
		double _convertedTotalDistance = _planner.calculateDistance(FIRST_SECOND_THIRD, true, _unitOut);

		// Test the things
		validateUnitConversion_waypoints(_convertedCoordinates, _expectedCoordinates);
		validateUnitConversion_distances(_convertedDistances, _expectedDistances);
		validateUnitConversion_totalDistance(_convertedTotalDistance, _expectedTotalDistance);

		_stream.close();
	}

	// CASE 1: METERS to KILOMETERS
	@Test
	void test_unitConv1_metersToKilometers() {
		String _input = "1698336.000000,-6147953.000000,91.000000\n" +
				"1927176.000000,-6134094.000000,51610.000000\n" +
				"3525156.000000,-5471952.000000,132371.000000\n" +
				"-4634768.000000,457099.000000,173051.000000\n" +
				"-6287169.000000,161034.000000,157118.000000\n" +
				"-5471278.000000,-411456.000000,101902.000000\n" +
				"-4420170.000000,-2045881.000000,45488.000000\n" +
				"-3497035.000000,-2813811.000000,9163.000000\n" +
				"-3483541.000000,-2806099.000000,0.000000";

		String _expectedWaypointValues = "1698.336000,-6147.953000,0.091000\n" +
				"1927.176000,-6134.094000,51.610000\n" +
				"3525.156000,-5471.952000,132.371000\n" +
				"-4634.768000,457.099000,173.051000\n" +
				"-6287.169000,161.034000,157.118000\n" +
				"-5471.278000,-411.456000,101.902000\n" +
				"-4420.170000,-2045.881000,45.488000\n" +
				"-3497.035000,-2813.811000,9.163000\n" +
				"-3483.541000,-2806.099000,0.000000";

		String _expectedDistanceValues = "234.977000\n" +
				"1731.616000\n" +
				"10086.608000\n" +
				"1678.790000\n" +
				"998.234000\n" +
				"1944.056000\n" +
				"1201.339000\n" +
				"18.042000";

		List<Coordinates> _expectedCoordinates = createExpectedWaypointsList(_expectedWaypointValues);

		List<Double> _expectedDistances = createExpectedDistancesList(_expectedDistanceValues);

		double _expectedTotalDistance = 17893.662796;

		try {
			test_unitConversion(_input, METERS, KILOMETERS, _expectedCoordinates, _expectedDistances, _expectedTotalDistance);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// CASE 2: METERS to FEET
	@Test
	void test_unitConv2_metersToFeet() {
		String _input = "1698336.000000,-6147953.000000,91.000000\n" +
				"1927176.000000,-6134094.000000,51610.000000\n" +
				"3525156.000000,-5471952.000000,132371.000000\n" +
				"-4634768.000000,457099.000000,173051.000000\n" +
				"-6287169.000000,161034.000000,157118.000000\n" +
				"-5471278.000000,-411456.000000,101902.000000\n" +
				"-4420170.000000,-2045881.000000,45488.000000\n" +
				"-3497035.000000,-2813811.000000,9163.000000\n" +
				"-3483541.000000,-2806099.000000,0.000000";

		String _expectedWaypointValues = "5571968.682240,-20170450.120520,298.556440\n" +
				"6322756.107840,-20124980.958960,169324.152400\n" +
				"11565472.811040,-17952598.999680,434288.071640\n" +
				"-15205932.245120,1499668.683160,567752.642840\n" +
				"-20627195.541960,528326.788560,515479.019120\n" +
				"-17950387.713520,-1349921.303040,334324.157680\n" +
				"-14501870.542800,-6712208.220040,149238.849920\n" +
				"-11473212.309400,-9231663.681240,30062.336920\n" +
				"-11428940.654440,-9206361.843160,0.000000";

		String _expectedDistanceValues = "770920.783986\n" +
				"5681155.488588\n" +
				"33092546.887141\n" +
				"5507842.893693\n" +
				"3275047.042077\n" +
				"6378138.304249\n" +
				"3941399.441437\n" +
				"59193.804948";

		List<Coordinates> _expectedCoordinates = createExpectedWaypointsList(_expectedWaypointValues);

		List<Double> _expectedDistances = createExpectedDistancesList(_expectedDistanceValues);

		double _expectedTotalDistance = 58706244.646120;

		try {
			test_unitConversion(_input, METERS, FEET, _expectedCoordinates, _expectedDistances, _expectedTotalDistance);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// CASE 3: METERS to MILES
	@Test
	void test_unitConv3_metersToMiles() {
		String _input = "1698336.000000,-6147953.000000,91.000000\n" +
				"1927176.000000,-6134094.000000,51610.000000\n" +
				"3525156.000000,-5471952.000000,132371.000000\n" +
				"-4634768.000000,457099.000000,173051.000000\n" +
				"-6287169.000000,161034.000000,157118.000000\n" +
				"-5471278.000000,-411456.000000,101902.000000\n" +
				"-4420170.000000,-2045881.000000,45488.000000\n" +
				"-3497035.000000,-2813811.000000,9163.000000\n" +
				"-3483541.000000,-2806099.000000,0.000000";

		String _expectedWaypointValues = "1055.296739,-3820.159704,0.056545\n" +
				"1197.491278,-3811.548123,32.068957\n" +
				"2190.429709,-3400.112286,82.251501\n" +
				"-2879.910427,284.028063,107.528873\n" +
				"-3906.664489,100.061858,97.628569\n" +
				"-3399.693482,-255.666826,63.318948\n" +
				"-2746.565453,-1271.251123,28.264924\n" +
				"-2172.956135,-1748.420555,5.693622\n" +
				"-2164.571355,-1743.628542,0.000000";

		String _expectedDistanceValues = "146.007674\n" +
				"1075.976051\n" +
				"6267.525680\n" +
				"1043.151707\n" +
				"620.273849\n" +
				"1207.980327\n" +
				"746.476912\n" +
				"11.210944";

		List<Coordinates> _expectedCoordinates = createExpectedWaypointsList(_expectedWaypointValues);

		List<Double> _expectedDistances = createExpectedDistancesList(_expectedDistanceValues);

		double _expectedTotalDistance = 11118.603145;

		try {
			test_unitConversion(_input, METERS, MILES, _expectedCoordinates, _expectedDistances, _expectedTotalDistance);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}