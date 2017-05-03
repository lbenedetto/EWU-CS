package s17cs350task1.unittests;

import javafx.geometry.Point3D;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import s17cs350task1.BoundingBox;
import s17cs350task1.Box;
import s17cs350task1.Dimension3D;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BoundingBoxTest {
	private static Box b1, b2, b3, b4, b5;
	private static BoundingBox bb1, bb2;

	@BeforeClass
	public static void setUpClass() {
		//Runs once
		bb1 = new BoundingBox(new Point3D(0, 0, 0), new Dimension3D(1, 1, 1));
		bb2 = new BoundingBox(new Point3D(10, 10, 10.0001), new Dimension3D(1.1, 1.01, 1.001));
	}

	@Before
	public void setUp() {
	}

	@Test
	public void test_clone() {
		try {
			BoundingBox clone1 = bb1.clone();
			assertNotEquals(bb1, clone1);
			assertEquals(bb1.toString(), clone1.toString());
			BoundingBox clone2 = bb2.clone();
			assertNotEquals(bb2, clone2);
			assertEquals(bb2.toString(), clone2.toString());
		} catch (CloneNotSupportedException e) {

		}
	}

	@Test
	public void test_calculateArea() {
		assertEquals(2, bb1.calculateArea(BoundingBox.E_Plane.XY), 0);
		assertEquals(2, bb1.calculateArea(BoundingBox.E_Plane.XZ), 0);
		assertEquals(2, bb1.calculateArea(BoundingBox.E_Plane.YZ), 0);
		assertEquals(2.2220000000000004, bb2.calculateArea(BoundingBox.E_Plane.XY), 0);
		assertEquals(2.2022, bb2.calculateArea(BoundingBox.E_Plane.XZ), 0);
		assertEquals(2.02202, bb2.calculateArea(BoundingBox.E_Plane.YZ), 0);
	}

	@Test
	public void test_calculateVolume() {
		double actual;
		actual = bb1.calculateVolume();
		assertEquals(1, actual, 0);
		actual = bb2.calculateVolume();
		assertEquals(1.112111, actual, 0);
	}

	@Test
	public void test_extend() {

	}

	@Test
	public void test_generateCorners() {
		List<Point3D> actual = bb1.generateCorners();
		assertEquals(8, actual.size());
		List<Point3D> expected = new ArrayList<>();
		double d1 = -.5;
		double d2 = 0.5;
		double d3 = -.5;
		double d4 = 0.5;
		double d5 = -.5;
		double d6 = 0.5;
		expected.add(new Point3D(d1, d3, d5));//left bottom far
		expected.add(new Point3D(d2, d3, d5));//right bottom far
		expected.add(new Point3D(d2, d3, d6));//right bottom near
		expected.add(new Point3D(d1, d3, d6));//left bottom near
		expected.add(new Point3D(d1, d4, d5));//left top far
		expected.add(new Point3D(d2, d4, d5));//right top far
		expected.add(new Point3D(d2, d4, d6));//right top near
		expected.add(new Point3D(d1, d4, d6));//left top near
		for (int i = 0; i < 8; i++) {
			assertEquals(expected.get(i), actual.get(i));
		}
		actual = bb2.generateCorners();
		assertEquals(8, actual.size());
		expected = new ArrayList<>();
		d1 = 9.45;
		d2 = 10.55;
		d3 = 9.495;
		d4 = 10.505;
		d5 = 9.4996;
		d6 = 10.5006;
		expected.add(new Point3D(d1, d3, d5));//left bottom far
		expected.add(new Point3D(d2, d3, d5));//right bottom far
		expected.add(new Point3D(d2, d3, d6));//right bottom near
		expected.add(new Point3D(d1, d3, d6));//left bottom near
		expected.add(new Point3D(d1, d4, d5));//left top far
		expected.add(new Point3D(d2, d4, d5));//right top far
		expected.add(new Point3D(d2, d4, d6));//right top near
		expected.add(new Point3D(d1, d4, d6));//left top near
		for (int i = 0; i < 8; i++) {
			assertEquals(expected.get(i), actual.get(i));
		}
	}

	@Test
	public void test_toString() {
		String actual, expected;
		actual = bb1.toString();
		expected = "[Point3D [x = 0.0, y = 0.0, z = 0.0], Dimension3D [w = 1.0, h = 1.0, d = 1.0]]";
		assertEquals(expected, actual);
		actual = bb2.toString();
		expected = "[Point3D [x = 10.0, y = 10.0, z = 10.0001], Dimension3D [w = 1.1, h = 1.01, d = 1.001]]";
		assertEquals(expected, actual);
	}

}