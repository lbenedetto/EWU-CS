package s17cs350task1.unittests;

import javafx.geometry.Point3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import s17cs350task1.Box;
import s17cs350task1.Connector;
import s17cs350task1.Dimension3D;
import s17cs350task1.TaskException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoxTest {
	private static Box b1, b2, b3, b4, b5, b6, b7;

	/**
	 * Use this code for code coverage testing.
	 * This is will not tell if you if the tests passed, only if they ran
	 */
	public static void testAll() throws CloneNotSupportedException {
		BoxTest bt = new BoxTest();
		bt.setUp();
		bt.test_clone();
		bt.setUp();
		bt.test_connectChild();
		bt.setUp();
		bt.test_getAbsoluteCenterPosition();
		bt.setUp();
		bt.test_getChildBoxCount();
		bt.setUp();
		bt.test_getChildBoxes();
		bt.setUp();
		bt.test_getConnectorsToChildren();
		bt.setUp();
		bt.test_getConnectorToParent();
		bt.setUp();
		bt.test_setConnectorToParent();
		bt.setUp();
		bt.test_getDescendantBoxCount();
		bt.setUp();
		bt.test_getDescendantBoxes();
		bt.setUp();
		bt.test_getID();
		bt.setUp();
		bt.test_getSize();
		bt.setUp();
		bt.test_hasConnectorToParent();
		bt.setUp();
		bt.test_isRoot();
		bt.setUp();
		bt.test_toString();
		bt.setUp();
		bt.test_equals();
		bt.setUp();
		bt.test_NEW_calculateAreaAll();
		bt.setUp();
		bt.test_NEW_calculateAreaSelf();
		bt.setUp();
		bt.test_NEW_calculateCenterOfMassAll();
		bt.setUp();
		bt.test_NEW_calculateCenterOfMassSelf();
		bt.setUp();
		bt.test_NEW_generateBoundingBoxAll();
		bt.setUp();
		bt.test_NEW_generateBoundingBoxSelf();
		bt.setUp();
		bt.test_NEW_calculateVolumeAll();
		bt.setUp();
		bt.test_NEW_calculateVolumeSelf();
		bt.setUp();
		bt.test_NEW_generateFramesAll();
		bt.setUp();
		bt.test_NEW_generateFramesSelf();
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
		connectBoxes(b1, b2);
		connectBoxes(b2, b3);
		connectBoxes(b2, b4);
	}

	@Test
	void test_clone() throws CloneNotSupportedException {
		Box c1 = b1.clone();
		assertEquals(b1, c1);
		assertEquals(false, b1 == c1);
		Box c2 = b2.clone();
		assertEquals(b2, c2);
		assertEquals(false, b2 == c2);
	}

	@Test
	void test_connectChild() {
		//Fails
		assertThrows(TaskException.class, () -> b1.connectChild(new Connector(b6, new Point3D(5, 5, 5))));//same id
		assertThrows(TaskException.class, () -> b1.connectChild(new Connector(b5, new Point3D(5, 5, 5))));//same id (case sensitivity)
		assertThrows(TaskException.class, () -> b1.connectChild(new Connector(b7, new Point3D(5, 5, 5))));//both root
		assertThrows(TaskException.class, () -> b1.connectChild(null));//null connector
		//Successes

	}

	@Test
	void test_getAbsoluteCenterPosition() {
	}

	@Test
	void test_getChildBoxCount() {
	}

	@Test
	void test_getChildBoxes() {
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
	void test_getDescendantBoxCount() {
	}

	@Test
	void test_getDescendantBoxes() {
	}

	@Test
	void test_getID() {
	}

	@Test
	void test_getSize() {
	}

	@Test
	void test_hasConnectorToParent() {
	}

	@Test
	void test_isRoot() {
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
	}

	@Test
	void test_NEW_calculateAreaSelf() {
	}

	@Test
	void test_NEW_calculateCenterOfMassAll() {
	}

	@Test
	void test_NEW_calculateCenterOfMassSelf() {
	}

	@Test
	void test_NEW_generateBoundingBoxAll() {
	}

	@Test
	void test_NEW_generateBoundingBoxSelf() {
	}

	@Test
	void test_NEW_calculateVolumeAll() {
	}

	@Test
	void test_NEW_calculateVolumeSelf() {
	}

	@Test
	void test_NEW_generateFramesAll() {
	}

	@Test
	void test_NEW_generateFramesSelf() {
	}

}