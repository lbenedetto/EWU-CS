package s17cs350project.UnitTests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import s17cs350project.planner.WaypointPlanner;
import s17cs350project.planner.WaypointPlanner.*;

class WaypointPlannerTest {

	/**
	 * Illegal tests for WaypointPlanner()
	 */
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

		// closed stream
		assertThrows(IllegalArgumentException.class, () -> new WaypointPlanner(E_AxisNative.X_PLUS, E_AxisNative.Y_PLUS, E_AxisNative.Z_PLUS, E_Unit.METERS, _stream));

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
		test_WaypointPlanner_illegal_stream("1,1,1\n1,2,3\n1,2,3");
		test_WaypointPlanner_illegal_stream("1,1,1\n1,2,3\n1,2,3");
		test_WaypointPlanner_illegal_stream("1,1,1\n1,2.23,3\n1,2,3");
		test_WaypointPlanner_illegal_stream("1,b,1\n1,a,3\n.01,2,3");
		test_WaypointPlanner_illegal_stream("1,1,1\n1,1.1,2,3\n1,.2,.3");
		test_WaypointPlanner_illegal_stream("1,1,1\n1,2,3\n1,1,2,3");
		test_WaypointPlanner_illegal_stream("a,b,c\nd,e,f");
	}

	/**
	 * helper method for test_WaypointPlanner_illegal()
	 * uses given string to Waypoint constructor (as input stream)
	 * <p>
	 * may have to return a bool for the assertion
	 */
	private void test_WaypointPlanner_illegal_stream(String _str) throws RuntimeException, IOException {
		InputStream _stream = new ByteArrayInputStream(_str.getBytes());

		assertThrows(IllegalArgumentException.class, () -> new WaypointPlanner(E_AxisNative.X_PLUS, E_AxisNative.Y_PLUS, E_AxisNative.Z_PLUS, E_Unit.METERS, _stream));

		_stream.close();
	}

	/**
	 * Illegal tests for calculateDistance()
	 */
	@Test
	void test_calculateDistance_illegal() throws IOException {
		InputStream _stream = new ByteArrayInputStream("1,2,3\n3,4,5\n6,7,8".getBytes());
		WaypointPlanner _planner = new WaypointPlanner(E_AxisNative.X_PLUS, E_AxisNative.Y_PLUS, E_AxisNative.Z_PLUS, E_Unit.METERS, _stream);

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
		WaypointPlanner _planner = new WaypointPlanner(E_AxisNative.X_PLUS, E_AxisNative.Y_PLUS, E_AxisNative.Z_PLUS, E_Unit.METERS, _stream);

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
		WaypointPlanner _planner = new WaypointPlanner(E_AxisNative.X_PLUS, E_AxisNative.Y_PLUS, E_AxisNative.Z_PLUS, E_Unit.METERS, _stream);
		_stream.close();

		assertThrows(IllegalArgumentException.class, () -> _planner.getCoordinates(false, null));

		// some kind of internal list is not returned
		List<Coordinates> _coordinates = _planner.getCoordinates(false, E_Unit.METERS);
		_coordinates.clear();
		_coordinates = _planner.getCoordinates(false, E_Unit.METERS);

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
		WaypointPlanner _planner1 = new WaypointPlanner(E_AxisNative.X_PLUS, E_AxisNative.Y_PLUS, E_AxisNative.Z_PLUS, E_Unit.METERS, _stream);
		WaypointPlanner _planner2 = new WaypointPlanner(E_AxisNative.X_PLUS, E_AxisNative.Y_PLUS, E_AxisNative.Z_PLUS, E_Unit.METERS, _stream);

		_stream.close();

		List<Coordinates> _cordinates1 = _planner1.getCoordinates(true, E_Unit.METERS);
		List<Coordinates> _cordinates2 = _planner2.getCoordinates(true, E_Unit.METERS);

		// TODO: add an equals() method to WaypointPlanner.Coordinates, and verify List.equals(List), will use elements equals()

		assertTrue(_cordinates1.equals(_cordinates2));
	}

	/**
	 * helper method
	 * checks if any axis (X,Y,Z) is missing
	 */
	private boolean missingAxis(E_AxisNative _axisA, E_AxisNative _axisB, E_AxisNative _axisC) {
		// X-axis check
		if (!(isAxisX(_axisA) || isAxisX(_axisB) || isAxisX(_axisC))) {
			return false;
		}
		// Y-axis check
		if (!(isAxisY(_axisA) || isAxisY(_axisB) || isAxisY(_axisC))) {
			return false;
		}
		// Z-axis check
		return isAxisZ(_axisA) || isAxisZ(_axisB) || isAxisZ(_axisC);
	}

	/**
	 * helper methods in extension
	 * check if an axis matches a type
	 */
	private boolean isAxisX(E_AxisNative _axis) {
		return (_axis == E_AxisNative.X_MINUS || _axis == E_AxisNative.X_PLUS);
	}

	private boolean isAxisY(E_AxisNative _axis) {
		return (_axis == E_AxisNative.Y_MINUS || _axis == E_AxisNative.Y_PLUS);
	}

	private boolean isAxisZ(E_AxisNative _axis) {
		return (_axis == E_AxisNative.Z_MINUS || _axis == E_AxisNative.Z_PLUS);
	}

	/**
	 * test getCoordinates()
	 */
	@Test
	void test_getCoordinates() {
		// TODO: transfer input strings w/ excel calculations, try all combinations
	}

	/**
	 * test calculateDistance()
	 */
	@Test
	void test_calculateDistance() {
		// TODO: transfer input strings w/ excel calculations, try all combinations
	}

	/**
	 * test calculateDistances()
	 */
	@Test
	void test_calculateDistances() {
		// TODO: transfer input strings w/ excel calculations, try all combinations
	}

	/**
	 * test given in pdf
	 * test 1
	 * may need to adjust double comparison (precision issues)
	 */
	@Test
	void test_pdf1() throws IOException {
		E_AxisNative _axisA = E_AxisNative.X_PLUS;
		E_AxisNative _axisB = E_AxisNative.Y_PLUS;
		E_AxisNative _axisC = E_AxisNative.Z_PLUS;

		String input = "1 ,2,3 \n 9,7,5 \n -1,-3, -5\n -1,-5,-9 \n 4, 6,2";
		InputStream _instream = new ByteArrayInputStream(input.getBytes());
		WaypointPlanner planner = new WaypointPlanner(_axisA, _axisB, _axisC, E_Unit.KILOMETERS, _instream);

		List<Coordinates> coordinatesNative = planner.getCoordinates(false, E_Unit.KILOMETERS);
		List<Coordinates> coordinatesCanonical = planner.getCoordinates(true, E_Unit.KILOMETERS);

		assertTrue(coordinatesNative.equals(
				Arrays.asList(new Coordinates(1.0, 2.0, 3.0),
						new Coordinates(9.0, 7.0, 5.0),
						new Coordinates(-1.0, -3.0, -5.0),
						new Coordinates(-1.0, -5.0, -9.0),
						new Coordinates(4.0, 6.0, 2.0))
		));

		assertTrue(coordinatesCanonical.equals(
				Arrays.asList(new Coordinates(1.0, 2.0, 3.0),
						new Coordinates(9.0, 7.0, 5.0),
						new Coordinates(-1.0, -3.0, -5.0),
						new Coordinates(-1.0, -5.0, -9.0),
						new Coordinates(4.0, 6.0, 2.0))
		));

		List<Double> distancesNative = planner.calculateDistances(E_AxisCombinationNeutral.FIRST_SECOND, false, E_Unit.KILOMETERS);
		List<Double> distancesCanonical = planner.calculateDistances(E_AxisCombinationNeutral.FIRST_SECOND, true, E_Unit.KILOMETERS);

		assertTrue(distancesNative.equals(Arrays.asList(9.433981132056603, 5.385164807134504, 7.280109889280518, 5.0)));
		assertTrue(distancesCanonical.equals(Arrays.asList(9.433981132056603, 5.385164807134504, 7.280109889280518, 5.0)));

		double distanceNative = planner.calculateDistance(E_AxisCombinationNeutral.FIRST_SECOND, false, E_Unit.KILOMETERS);
		double distanceCanonical = planner.calculateDistance(E_AxisCombinationNeutral.FIRST_SECOND, true, E_Unit.KILOMETERS);

		assertTrue(distanceNative == 27.099255828471623);
		assertTrue(distanceCanonical == 27.099255828471623);

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
		E_AxisNative _axisA = E_AxisNative.Z_MINUS;
		E_AxisNative _axisB = E_AxisNative.X_PLUS;
		E_AxisNative _axisC = E_AxisNative.Y_PLUS;

		String input = "1 ,2,3 \n 9,7,5 \n -1,-3, -5\n -1,-5,-9 \n 4, 6,2";
		InputStream _instream = new ByteArrayInputStream(input.getBytes());
		WaypointPlanner planner = new WaypointPlanner(_axisA, _axisB, _axisC, E_Unit.KILOMETERS, _instream);

		List<Coordinates> coordinatesNative = planner.getCoordinates(false, E_Unit.KILOMETERS);
		List<Coordinates> coordinatesCanonical = planner.getCoordinates(true, E_Unit.KILOMETERS);

		assertTrue(coordinatesNative.equals(
				Arrays.asList(new Coordinates(1.0, 2.0, 3.0),
						new Coordinates(9.0, 7.0, 5.0),
						new Coordinates(-1.0, -3.0, -5.0),
						new Coordinates(-1.0, -5.0, -9.0),
						new Coordinates(4.0, 6.0, 2.0))
		));

		assertTrue(coordinatesCanonical.equals(
				Arrays.asList(new Coordinates(-3.0, 1.0, 2.0),
						new Coordinates(-5.0, 9.0, 7.0),
						new Coordinates(5.0, -1.0, -3.0),
						new Coordinates(9.0, -1.0, -5.0),
						new Coordinates(-2.0, 4.0, 6.0))
		));

		List<Double> distancesNative = planner.calculateDistances(E_AxisCombinationNeutral.FIRST_SECOND, false, E_Unit.KILOMETERS);
		List<Double> distancesCanonical = planner.calculateDistances(E_AxisCombinationNeutral.FIRST_SECOND, true, E_Unit.KILOMETERS);

		assertTrue(distancesNative.equals(Arrays.asList(9.433981132056603, 5.385164807134504, 7.280109889280518, 5.0)));
		assertTrue(distancesCanonical.equals(Arrays.asList(8.246211251235321, 8.246211251235321, 12.165525060596439, 3.1622776601683795)));

		double distanceNative = planner.calculateDistance(E_AxisCombinationNeutral.FIRST_SECOND, false, E_Unit.KILOMETERS);
		double distanceCanonical = planner.calculateDistance(E_AxisCombinationNeutral.FIRST_SECOND, true, E_Unit.KILOMETERS);

		assertTrue(distanceNative == 27.099255828471623);
		assertTrue(distanceCanonical == 31.82022522323546);

		_instream.close();

		/*
		expected results

		coordinatesNative = [(1.0 2.0 3.0), (9.0 7.0 5.0), (-1.0 -3.0 -5.0), (-1.0 -5.0 -9.0), (4.0 6.0 2.0)]
		coordinatesCanonical = [(-3.0 1.0 2.0), (-5.0 9.0 7.0), (5.0 -1.0 -3.0), (9.0 -1.0 -5.0), (-2.0 4.0 6.0)]

		distancesNative= [9.433981132056603, 5.385164807134504, 7.280109889280518, 5.0]
		distancesCanonical = [8.246211251235321, 8.246211251235321, 12.165525060596439, 3.1622776601683795]

		distanceNative = 27.099255828471623
		distanceCanonical = 31.82022522323546
		 */
	}

	/**
	 * test given in pdf
	 * test 3
	 * may need to adjust double comparison (precision issues)
	 */
	@Test
	void test_pdf3() throws IOException {
		E_AxisNative _axisA = E_AxisNative.Z_MINUS;
		E_AxisNative _axisB = E_AxisNative.X_PLUS;
		E_AxisNative _axisC = E_AxisNative.Y_PLUS;

		String _input = "1 ,2,3 \n 9,7,5 \n -1,-3, -5\n -1,-5,-9 \n 4, 6,2";
		InputStream _instream = new ByteArrayInputStream(_input.getBytes());
		WaypointPlanner planner = new WaypointPlanner(_axisA, _axisB, _axisC, E_Unit.KILOMETERS, _instream);

		List<Coordinates> coordinatesNative = planner.getCoordinates(false, E_Unit.METERS);
		List<Coordinates> coordinatesCanonical = planner.getCoordinates(true, E_Unit.FEET);

		assertTrue(coordinatesNative.equals(
				Arrays.asList(new Coordinates(1000.0, 2000.0, 3000.0),
						new Coordinates(9000.0, 7000.0, 5000.0),
						new Coordinates(-1000.0, -3000.0, -5000.0),
						new Coordinates(-1000.0, -5000.0, -9000.0),
						new Coordinates(4000.0, 6000.0, 2000.0))
		));

		assertTrue(coordinatesCanonical.equals(
				Arrays.asList(new Coordinates(-9842.52, 3280.84, 6561.68),
						new Coordinates(-16404.2, 29527.56, 22965.88),
						new Coordinates(16404.2, -3280.84, -9842.52),
						new Coordinates(29527.56, -3280.84, -16404.2),
						new Coordinates(-6561.68, 13123.36, 19685.04))
		));

		List<Double> distancesNative = planner.calculateDistances(E_AxisCombinationNeutral.FIRST_SECOND, false, E_Unit.FEET);
		List<Double> distancesCanonical = planner.calculateDistances(E_AxisCombinationNeutral.FIRST_SECOND, true, E_Unit.MILES);

		assertTrue(distancesNative.equals(Arrays.asList(30951.382657296588, 17667.864105839166, 23884.875729147097, 16404.2)));
		assertTrue(distancesCanonical.equals(Arrays.asList(5.123956531391342, 5.123956531391342, 7.559304472427871, 1.9649476319764863)));

		double distanceNative = planner.calculateDistance(E_AxisCombinationNeutral.FIRST_SECOND, false, E_Unit.MILES);
		double distanceCanonical = planner.calculateDistance(E_AxisCombinationNeutral.FIRST_SECOND, true, E_Unit.METERS);

		assertTrue(distanceNative == 16.838691693393244);
		assertTrue(distanceCanonical == 31820.225223235462);

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
		E_AxisNative _axisA = E_AxisNative.Z_MINUS;
		E_AxisNative _axisB = E_AxisNative.X_PLUS;
		E_AxisNative _axisC = E_AxisNative.Y_MINUS;

		String _input = "1 ,2,3 \n 9,7,5 \n -1,-3, -5\n -1,-5,-9 \n 4, 6,2";
		InputStream _instream = new ByteArrayInputStream(_input.getBytes());
		WaypointPlanner planner = new WaypointPlanner(_axisA, _axisB, _axisC, E_Unit.FEET, _instream);

		List<Coordinates> coordinatesNative = planner.getCoordinates(false, E_Unit.METERS);
		List<Coordinates> coordinatesCanonical = planner.getCoordinates(true, E_Unit.FEET);

		assertTrue(coordinatesNative.equals(
				Arrays.asList(new Coordinates(0.3048, 0.6096, 0.9144000000000001),
						new Coordinates(2.7432000000000003, 2.1336, 1.524),
						new Coordinates(-0.3048, -0.9144000000000001, -1.524),
						new Coordinates(-0.3048, -1.524, -2.7432000000000003),
						new Coordinates(1.2192, 1.8288000000000002, 0.6096))
		));

		assertTrue(coordinatesCanonical.equals(
				Arrays.asList(new Coordinates(-3.0000000960000004, 1.000000032, -2.000000064),
						new Coordinates(-5.00000016, 9.000000288, -7.000000224),
						new Coordinates(5.00000016, -1.000000032, 3.0000000960000004),
						new Coordinates(9.000000288, -1.000000032, 5.00000016),
						new Coordinates(-2.000000064, 4.000000128, -6.000000192000001))
		));

		List<Double> distancesNative = planner.calculateDistances(E_AxisCombinationNeutral.FIRST_SECOND, false, E_Unit.FEET);
		List<Double> distancesCanonical = planner.calculateDistances(E_AxisCombinationNeutral.FIRST_SECOND, true, E_Unit.MILES);

		assertTrue(distancesNative.equals(Arrays.asList(9.433981433944, 5.385164979459779, 7.280110122244035, 5.000000160000001)));
		assertTrue(distancesCanonical.equals(Arrays.asList(0.0015617819507680816, 0.0015617819507680814, 0.0023040760031960155, 5.98916038226433E-4)));

		double distanceNative = planner.calculateDistance(E_AxisCombinationNeutral.FIRST_SECOND, false, E_Unit.MILES);
		double distanceCanonical = planner.calculateDistance(E_AxisCombinationNeutral.FIRST_SECOND, true, E_Unit.METERS);

		assertTrue(distanceNative == 0.00513243322814626);
		assertTrue(distanceCanonical == 9.698804648042168);

		_instream.close();

		/*
		expected results

		coordinatesNative = [(0.3048 0.6096 0.9144000000000001), (2.7432000000000003 2.1336 1.524), (-0.3048 -0.9144000000000001 -1.524), (-0.3048 -1.524 -2.7432000000000003), (1.2192 1.8288000000000002 0.6096)]
		coordinatesCanonical = [(-3.0000000960000004 1.000000032 -2.000000064), (-5.00000016 9.000000288 -7.000000224), (5.00000016 -1.000000032 3.0000000960000004), (9.000000288 -1.000000032 5.00000016), (-2.000000064 4.000000128 -6.000000192000001)]

		distancesNative= [9.433981433944, 5.385164979459779, 7.280110122244035, 5.000000160000001]
		distancesCanonical = [0.0015617819507680816, 0.0015617819507680814, 0.0023040760031960155, 5.98916038226433E-4]

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
		E_AxisNative _axisA = E_AxisNative.Z_MINUS;
		E_AxisNative _axisB = E_AxisNative.X_PLUS;
		E_AxisNative _axisC = E_AxisNative.Y_MINUS;

		String _input = "1 ,2,3 \n 9,7,5 \n -1,-3, -5\n -1,-5,-9 \n 4, 6,2";
		InputStream _instream = new ByteArrayInputStream(_input.getBytes());
		WaypointPlanner planner = new WaypointPlanner(_axisA, _axisB, _axisC, E_Unit.FEET, _instream);

		List<Coordinates> coordinatesNative = planner.getCoordinates(false, E_Unit.METERS);
		List<Coordinates> coordinatesCanonical = planner.getCoordinates(true, E_Unit.FEET);

		assertTrue(coordinatesNative.equals(
				Arrays.asList(new Coordinates(0.3048, 0.6096, 0.9144000000000001),
						new Coordinates(2.7432000000000003, 2.1336, 1.524),
						new Coordinates(-0.3048, -0.9144000000000001, -1.524),
						new Coordinates(-0.3048, -1.524, -2.7432000000000003),
						new Coordinates(1.2192, 1.8288000000000002, 0.6096))
		));

		assertTrue(coordinatesCanonical.equals(
				Arrays.asList(new Coordinates(-3.0000000960000004, 1.000000032, -2.000000064),
						new Coordinates(-5.00000016, 9.000000288, -7.000000224),
						new Coordinates(5.00000016, -1.000000032, 3.0000000960000004),
						new Coordinates(9.000000288, -1.000000032, 5.00000016),
						new Coordinates(-2.000000064, 4.000000128, -6.000000192000001))
		));

		List<Double> distancesNative = planner.calculateDistances(E_AxisCombinationNeutral.FIRST, false, E_Unit.FEET);
		List<Double> distancesCanonical = planner.calculateDistances(E_AxisCombinationNeutral.SECOND, true, E_Unit.MILES);

		assertTrue(distancesNative.equals(Arrays.asList(8.000000256, 2.000000064, 2.000000064, 3.000000096)));
		assertTrue(distancesCanonical.equals(Arrays.asList(0.0015151510464000003, 3.7878776160000003E-4, 3.7878776160000003E-4, 5.681816424E-4)));

		double distanceNative = planner.calculateDistance(E_AxisCombinationNeutral.FIRST_SECOND_THIRD, false, E_Unit.MILES);
		double distanceCanonical = planner.calculateDistance(E_AxisCombinationNeutral.SECOND_THIRD, true, E_Unit.METERS);

		assertTrue(distanceNative == 0.007276889772037);
		assertTrue(distanceCanonical == 8.259853176518153);

		_instream.close();

		/*
		expected results

		coordinatesNative = [(0.3048 0.6096 0.9144000000000001), (2.7432000000000003 2.1336 1.524), (-0.3048 -0.9144000000000001 -1.524), (-0.3048 -1.524 -2.7432000000000003), (1.2192 1.8288000000000002 0.6096)]
		coordinatesCanonical = [(-3.0000000960000004 1.000000032 -2.000000064), (-5.00000016 9.000000288 -7.000000224), (5.00000016 -1.000000032 3.0000000960000004), (9.000000288 -1.000000032 5.00000016), (-2.000000064 4.000000128 -6.000000192000001)]

		distancesNative= [8.000000256, 2.000000064, 2.000000064, 3.000000096]
		distancesCanonical = [0.0015151510464000003, 3.7878776160000003E-4, 3.7878776160000003E-4, 5.681816424E-4]

		distanceNative = 0.007276889772037
		distanceCanonical = 8.259853176518153
		*/
	}
}