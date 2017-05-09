package s17cs350task1.unittests;

import javafx.geometry.Point3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import s17cs350task1.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static s17cs350task1.BoundingBox.E_Plane.*;

public class BoxTest {
	private static Box b1, b2, b3, b4, b5, b6, b7, b8;

	/**
	 * Use this code for code coverage testing.
	 * This is will not tell if you if the tests passed, only if they ran
	 */
	public static void testAll() throws Exception {
		BoxTest bt = new BoxTest();
		bt.setUp();
		bt.test_connectChild();
		bt.setUp();
		bt.test_setConnectorToParent();
		bt.setUp();
		bt.test_NEW_calculateAreaAll();
		bt.setUp();
		bt.test_NEW_calculateAreaSelf();
		bt.setUp();
		bt.test_clone();
		bt.test_getAbsoluteCenterPosition();
		bt.test_getChildBoxCount();
		bt.test_getChildBoxes();
		bt.test_getConnectorsToChildren();
		bt.test_getConnectorToParent();
		bt.test_getDescendantBoxCount();
		bt.test_getDescendantBoxes();
		bt.test_getSize();
		bt.test_hasConnectorToParent();
		bt.test_toString();
		bt.test_equals();
		bt.test_NEW_calculateCenterOfMassAll();
		bt.test_NEW_calculateCenterOfMassSelf();
		bt.test_NEW_generateBoundingBoxAll();
		bt.test_NEW_generateBoundingBoxSelf();
		bt.test_NEW_calculateVolumeAll();
		bt.test_NEW_calculateVolumeSelf();
	}

	private static void connectBoxes(Box b1, Box b2) {
		Connector c1 = new Connector(b2, new Point3D(5, 5, 5));
		b1.connectChild(c1);
	}

	@BeforeEach
	public void setUp() {
		//Runs before each test
		b1 = new Box("Box1", new Dimension3D(1, 1, 1), true);
		b2 = new Box("Box2", new Dimension3D(2, 2, 2));
		b3 = new Box("Box3", new Dimension3D(3, 3, 3));
		b4 = new Box("Box4", new Dimension3D(4, 4, 4));
		b5 = new Box("box1", new Dimension3D(5, 5, 5));
		b6 = new Box("Box1", new Dimension3D(6, 6, 6));
		b7 = new Box("Box7", new Dimension3D(7, 7, 7), true);
		b8 = new Box("Box8", new Dimension3D(2, 3, 4));
		connectBoxes(b1, b2);
		connectBoxes(b2, b3);
		connectBoxes(b2, b4);
	}

	@Test
	void test_clone() throws CloneNotSupportedException {
		Box c1 = b1.clone();
		assertEquals(b1, c1);
		assertFalse(b1 == c1);
		Box c2 = b2.clone();
		assertEquals(b2, c2);
		assertFalse(b2 == c2);
	}

	@Test
	void test_connectChild() {
		//Fail
		assertThrows(TaskException.class, () -> b1.connectChild(new Connector(b6, new Point3D(5, 5, 5))));//same id
		assertThrows(TaskException.class, () -> b1.connectChild(new Connector(b5, new Point3D(5, 5, 5))));//same id (case sensitivity)
		assertThrows(TaskException.class, () -> b1.connectChild(new Connector(b7, new Point3D(5, 5, 5))));//both root
		assertThrows(TaskException.class, () -> b1.connectChild(null));//null connector
		//Succeed
		b1.connectChild(new Connector(b8, new Point3D(5, 5, 5)));
		String expected = "Box[id='Box1', isRoot=true, size=Dimension3D [w = 1.0, h = 1.0, d = 1.0]]" +
				"\n\tConnector[GoodParent=true, offset=Point3D [x = 5.0, y = 5.0, z = 5.0]] -> Box[id='Box2', isRoot=false, size=Dimension3D [w = 2.0, h = 2.0, d = 2.0]]" +
				"\n\t\tConnector[GoodParent=true, offset=Point3D [x = 5.0, y = 5.0, z = 5.0]] -> Box[id='Box3', isRoot=false, size=Dimension3D [w = 3.0, h = 3.0, d = 3.0]]" +
				"\n\t\tConnector[GoodParent=true, offset=Point3D [x = 5.0, y = 5.0, z = 5.0]] -> Box[id='Box4', isRoot=false, size=Dimension3D [w = 4.0, h = 4.0, d = 4.0]]" +
				"\n\tConnector[GoodParent=true, offset=Point3D [x = 5.0, y = 5.0, z = 5.0]] -> Box[id='Box8', isRoot=false, size=Dimension3D [w = 2.0, h = 3.0, d = 4.0]]";
		assertEquals(expected, b1.toString());
	}

	@Test
	void test_getAbsoluteCenterPosition() {
		assertEquals(new Point3D(0, 0, 0), b1.getAbsoluteCenterPosition());//is root
		assertEquals(new Point3D(10, 10, 10), b4.getAbsoluteCenterPosition());//child, has root
		assertThrows(TaskException.class, () -> b5.getAbsoluteCenterPosition());//orphan, no root
	}

	@Test
	void test_getChildBoxCount() {
		assertEquals(1, b1.getChildBoxCount());
		assertEquals(2, b2.getChildBoxCount());
		assertEquals(0, b3.getChildBoxCount());
	}

	@Test
	void test_getChildBoxes() {
		List<Box> children = b1.getChildBoxes();
		assertFalse(children == b1.getChildBoxes());//Make sure not returning a reference
		assertEquals(children, b1.getChildBoxes());
		assertTrue(children.contains(b2));
		children = b2.getChildBoxes();
		assertTrue(children.containsAll(Arrays.asList(b3, b4)));
	}

	@Test
	void test_getConnectorsToChildren() {
	}

	@Test
	void test_getConnectorToParent() {
	}

	@Test
	void test_setConnectorToParent() {
	}

	@Test
	void test_getDescendantBoxCount() throws Exception {
		assertEquals(3, b1.getDescendantBoxCount());
		assertEquals(2, b2.getDescendantBoxCount());
		assertEquals(0, b3.getDescendantBoxCount());
	}

	@Test
	void test_getDescendantBoxes() {
	}

	@Test
	void test_getSize() {
		assertEquals(new Dimension3D(2, 3, 4), b8.getSize());
		assertFalse(b8.getSize() == b8.getSize());
	}

	@Test
	void test_hasConnectorToParent() {
	}

	@Test
	void test_toString() {
		String expected = "Box[id='Box1', isRoot=true, size=Dimension3D [w = 1.0, h = 1.0, d = 1.0]]" +
				"\n\tConnector[GoodParent=true, offset=Point3D [x = 5.0, y = 5.0, z = 5.0]] -> Box[id='Box2', isRoot=false, size=Dimension3D [w = 2.0, h = 2.0, d = 2.0]]" +
				"\n\t\tConnector[GoodParent=true, offset=Point3D [x = 5.0, y = 5.0, z = 5.0]] -> Box[id='Box3', isRoot=false, size=Dimension3D [w = 3.0, h = 3.0, d = 3.0]]" +
				"\n\t\tConnector[GoodParent=true, offset=Point3D [x = 5.0, y = 5.0, z = 5.0]] -> Box[id='Box4', isRoot=false, size=Dimension3D [w = 4.0, h = 4.0, d = 4.0]]";
		assertEquals(expected, b1.toString());
		System.out.println(b1.toString());
		expected = "Box[id='Box2', isRoot=false, size=Dimension3D [w = 2.0, h = 2.0, d = 2.0]]" +
				"\n\tConnector[GoodParent=true, offset=Point3D [x = 5.0, y = 5.0, z = 5.0]] -> Box[id='Box3', isRoot=false, size=Dimension3D [w = 3.0, h = 3.0, d = 3.0]]" +
				"\n\tConnector[GoodParent=true, offset=Point3D [x = 5.0, y = 5.0, z = 5.0]] -> Box[id='Box4', isRoot=false, size=Dimension3D [w = 4.0, h = 4.0, d = 4.0]]";
		assertEquals(expected, b2.toString());
	}

	@Test
	void test_equals() {
		Box b72 = new Box("Box7", new Dimension3D(7, 7, 7), true);
		assertEquals(b72, b7);
		assertEquals(b1, b1);
	}

	@Test
	void test_NEW_calculateAreaAll() {
		assertEquals(60, b1.NEW_calculateAreaAll(XY));
		assertEquals(60, b1.NEW_calculateAreaAll(XZ));
		assertEquals(60, b1.NEW_calculateAreaAll(YZ));
		b1.connectChild(new Connector(b8, new Point3D(5, 5, 5)));
		assertEquals(72, b1.NEW_calculateAreaAll(XY));
		assertEquals(76, b1.NEW_calculateAreaAll(XZ));
		assertEquals(84, b1.NEW_calculateAreaAll(YZ));
		assertEquals(58, b2.NEW_calculateAreaAll(XY));
		assertEquals(58, b2.NEW_calculateAreaAll(XZ));
		assertEquals(58, b2.NEW_calculateAreaAll(YZ));
		assertThrows(TaskException.class, () -> b1.NEW_calculateAreaAll(null));
	}

	@Test
	void test_NEW_calculateAreaSelf() {
		assertEquals(2, b1.NEW_calculateAreaSelf(XY));
		assertEquals(8, b2.NEW_calculateAreaSelf(XY));
		assertEquals(8, b2.NEW_calculateAreaSelf(XZ));
		assertEquals(8, b2.NEW_calculateAreaSelf(YZ));
		//The program will crash if you don't connect it. But should it?
		b1.connectChild(new Connector(b8, new Point3D(5, 5, 5)));
		assertEquals(12, b8.NEW_calculateAreaSelf(XY));
		assertEquals(16, b8.NEW_calculateAreaSelf(XZ));
		assertEquals(24, b8.NEW_calculateAreaSelf(YZ));
		assertThrows(TaskException.class, () -> b1.NEW_calculateAreaSelf(null));
	}

	@Test
	void test_NEW_calculateCenterOfMassAll() {
		b4.NEW_generateBoundingBoxSelf().generateCorners();
		assertEquals(new Point3D(5.75, 5.75, 5.75), b1.NEW_calculateCenterOfMassAll());
		assertThrows(TaskException.class, () -> b8.NEW_calculateCenterOfMassAll());//Needs root to find center of mass
	}

	@Test
	void test_NEW_calculateCenterOfMassSelf() {
		assertEquals(new Point3D(0, 0, 0), b1.NEW_calculateCenterOfMassSelf());
		assertThrows(TaskException.class, () -> b8.NEW_calculateCenterOfMassSelf());//Needs root to find center of mass
		assertEquals(new Point3D(5, 5, 5), b2.NEW_calculateCenterOfMassSelf());
	}

	@Test
	void test_NEW_generateBoundingBoxAll() {
		BoundingBox bb = b1.NEW_generateBoundingBoxAll();
		assertEquals(new Point3D(5.75, 5.75, 5.75), bb.getCenter());
		assertEquals(new Dimension3D(12.5, 12.5, 12.5), bb.getSize());
	}

	@Test
	void test_NEW_generateBoundingBoxSelf() {
		BoundingBox bb = b4.NEW_generateBoundingBoxSelf();
		assertEquals(new Point3D(10, 10, 10), bb.getCenter());
		assertEquals(new Dimension3D(4, 4, 4), bb.getSize());
	}

	@Test
	void test_NEW_calculateVolumeAll() {
		assertEquals(100, b1.NEW_calculateVolumeAll());
		assertEquals(99, b2.NEW_calculateVolumeAll());
		assertThrows(TaskException.class, () -> b8.NEW_calculateVolumeAll());
	}

	@Test
	void test_NEW_calculateVolumeSelf() {
		assertThrows(TaskException.class, () -> b8.NEW_calculateVolumeSelf());
		b1.connectChild(new Connector(b8, new Point3D(5, 5, 5)));
		assertEquals(2 * 3 * 4, b8.NEW_calculateVolumeSelf());
	}
}