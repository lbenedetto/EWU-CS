package s17cs350task1.unittests;

import org.junit.jupiter.api.Test;
import s17cs350task1.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test cases using JUnit
 *
 * @author Mathew McCain
 */
@SuppressWarnings("unused")
class IllegalTest {

	// test illegal Point3D constructor
	@Test
	void test_Ill_Point3D_ctr() {
		// Double.NaN
		try {
			Point3D _p = new Point3D(Double.NaN, 0, 0);
			fail("failed to catch Double.NaN x");
		} catch (TaskException ignored) {
		}

		try {
			Point3D _p = new Point3D(0, Double.NaN, 0);
			fail("failed to catch Double.NaN y ");
		} catch (TaskException ignored) {
		}

		try {
			Point3D _p = new Point3D(0, 0, Double.NaN);
			fail("failed to catch Double.NaN z");
		} catch (TaskException ignored) {
		}
	}

	// test illegal Point3D add
	@Test
	void test_Ill_Point3D_add() {
		try {
			Point3D _a = new Point3D(0, 0, 0);
			Point3D _c = _a.add(null);

			fail("failed to catch null");
		} catch (TaskException ignored) {
		}
	}

	// test illegal Point3D subtract
	@Test
	void test_Ill_Point3D_subtract() {
		try {
			Point3D _a = new Point3D(0, 0, 0);
			Point3D _c = _a.subtract(null);

			fail("failed to catch null");
		} catch (TaskException ignored) {
		}
	}

	// test illegal Dimension3D constructor
	@Test
	void test_Ill_Dimension3D_ctr() {
		// Double.NaN
		if (!(test_Ill_Dimension3D_ctr_Test(Double.NaN, 1, 1)
				&& test_Ill_Dimension3D_ctr_Test(1, Double.NaN, 1)
				&& test_Ill_Dimension3D_ctr_Test(1, 1, Double.NaN)
		)) {
			fail("failed to catch dimension Double.NaN");
		}

		// = 0
		if (!(test_Ill_Dimension3D_ctr_Test(0, 1, 1)
				&& test_Ill_Dimension3D_ctr_Test(1, 0, 1)
				&& test_Ill_Dimension3D_ctr_Test(1, 1, 0)
		)) {
			fail("failed to catch dimension = 0");
		}

		// < 0
		if (!(test_Ill_Dimension3D_ctr_Test(-1, 1, 1)
				&& test_Ill_Dimension3D_ctr_Test(1, -1, 1)
				&& test_Ill_Dimension3D_ctr_Test(1, 1, -1)
		)) {
			fail("failed to catch dimension < 0");
		}
	}

	// helper method for test_Ill_Dimension3D_ctr()
	private static boolean test_Ill_Dimension3D_ctr_Test(double _x, double _y, double _z) {
		try {
			Dimension3D _d = new Dimension3D(_x, _y, _z);
			return false;
		} catch (TaskException _e) {
			return true;
		}
	}

	// test illegal ComponentBox constructor
	@Test
	void test_Ill_CompBox_ctr() {
		try {
			ComponentBox _a = new ComponentBox(null, new Dimension3D(1, 1, 1));
			ComponentBox _b = new ComponentBox("asd", null);
			fail("failed to catch null");
		} catch (TaskException ignored) {
		}

		try {
			ComponentBox _a = new ComponentBox("", new Dimension3D(1, 1, 1));
			fail("failed to catch empty id");
		} catch (TaskException ignored) {
		}

		try {
			ComponentBox _a = new ComponentBox(null, new Dimension3D(1, 1, 1), false);
			ComponentBox _b = new ComponentBox("asd", null, false);
			ComponentBox _c = new ComponentBox(null, new Dimension3D(1, 1, 1), false);
			ComponentBox _d = new ComponentBox("asd", null, false);
			fail("failed to catch null");
		} catch (TaskException ignored) {
		}

		try {
			ComponentBox _a = new ComponentBox("", new Dimension3D(1, 1, 1), true);
			ComponentBox _b = new ComponentBox("", new Dimension3D(1, 1, 1), false);
			fail("failed to catch empty id");
		} catch (TaskException ignored) {
		}
	}

	// test illegal ComponentBox connectChild
	@Test
	void test_Ill_CompBox_connectChild() {
		// null test
		try {
			ComponentBox _a = new ComponentBox("a", new Dimension3D(1, 1, 1));
			_a.connectChild(null);

			fail("failed to catch null");
		} catch (TaskException ignored) {
		}

		// parent already set
		try {
			ComponentBox _a = new ComponentBox("a", new Dimension3D(1, 1, 1));
			ComponentBox _b = new ComponentBox("b", new Dimension3D(1, 1, 1));
			ComponentBox _c = new ComponentBox("c", new Dimension3D(1, 1, 1));

			Connector _cc = new Connector(_c, new Point3D(0, 0, 0));

			_a.connectChild(_cc);
			_b.connectChild(_cc);

			fail("failed to catch parent set");
		} catch (TaskException ignored) {
		}

		// duplicate id
		try {
			ComponentBox _a = new ComponentBox("a", new Dimension3D(1, 1, 1));
			ComponentBox _b = new ComponentBox("a", new Dimension3D(1, 1, 1));

			_a.connectChild(new Connector(_b, new Point3D(0, 0, 0)));

			fail("failed to catch duplicate id");
		} catch (TaskException ignored) {
		}

		// cycle long
		try {
			ComponentBox _a = new ComponentBox("a", new Dimension3D(1, 1, 1));
			ComponentBox _b = new ComponentBox("b", new Dimension3D(1, 1, 1));
			ComponentBox _c = new ComponentBox("c", new Dimension3D(1, 1, 1));

			Connector _ca = new Connector(_a, new Point3D(0, 0, 0));
			Connector _cb = new Connector(_b, new Point3D(0, 0, 0));
			Connector _cc = new Connector(_c, new Point3D(0, 0, 0));

			_a.connectChild(_cb);
			_b.connectChild(_cc);
			_c.connectChild(_ca);

			fail("failed to catch long cycle");
		} catch (TaskException ignored) {
		}

		// cycle short
		try {
			ComponentBox _a = new ComponentBox("a", new Dimension3D(1, 1, 1));
			ComponentBox _b = new ComponentBox("b", new Dimension3D(1, 1, 1));
			ComponentBox _c = new ComponentBox("c", new Dimension3D(1, 1, 1));

			Connector _ca = new Connector(_a, new Point3D(0, 0, 0));

			_a.connectChild(_ca);

			fail("failed to catch short cycle");
		} catch (TaskException ignored) {
		}
	}

	// test illegal ComponentBox getConnectorsToChildren
	@Test
	void test_Ill_CompBox_getConnectorsToChildren() {
		// test that internal list isn't passed back
		ComponentBox _a = new ComponentBox("a", new Dimension3D(1, 1, 1));
		ComponentBox _b = new ComponentBox("b", new Dimension3D(1, 1, 1));
		ComponentBox _c = new ComponentBox("c", new Dimension3D(1, 1, 1));
		_a.connectChild(new Connector(_b, new Point3D(1, 1, 1)));
		_a.connectChild(new Connector(_c, new Point3D(2, 2, 2)));

		List<Connector> _lista = _a.getConnectorsToChildren();
		_lista.clear();

		assertTrue(_a.getConnectorsToChildren().size() == 2);
	}

	// test illegal ComponentBox getConnectorToParent
	@Test
	void test_Ill_CompBox_getConnectorToParent() {
		try {
			ComponentBox _a = new ComponentBox("a", new Dimension3D(1, 1, 1));
			Connector _p = _a.getConnectorToParent();

			fail("failed to throw exception for null connector");
		} catch (TaskException ignored) {
		}
	}

	// test illegal ComponentBox setConnectorToParent
	// no test, isn't publically visible

	// test illegal ComponentBox getAbsoluteCenterPosition
	@Test
	void test_Ill_CompBox_getAbsoluteCenterPosition() {
		// no root
		try {
			ComponentBox _a = new ComponentBox("a", new Dimension3D(1, 1, 1));
			ComponentBox _b = new ComponentBox("b", new Dimension3D(1, 1, 1));
			ComponentBox _c = new ComponentBox("c", new Dimension3D(1, 1, 1));
			_a.connectChild(new Connector(_b, new Point3D(1, 1, 1)));
			_b.connectChild(new Connector(_c, new Point3D(2, 2, 2)));

			Point3D _center = _a.getAbsoluteCenterPosition();

			fail("failed to catch no root");
		} catch (TaskException ignored) {
		}

		// broken parent connector parent component
		try {
			ComponentBox _a = new ComponentBox("a", new Dimension3D(1, 1, 1));
			ComponentBox _b = new ComponentBox("b", new Dimension3D(1, 1, 1));
			ComponentBox _c = new ComponentBox("c", new Dimension3D(1, 1, 1));
			Connector _ca = new Connector(_a, new Point3D(0, 0, 0));
			_a.connectChild(new Connector(_b, new Point3D(1, 1, 1)));
			_b.connectChild(new Connector(_c, new Point3D(2, 2, 2)));

			Point3D _center = _a.getAbsoluteCenterPosition();

			fail("failed to catch broken parent component");
		} catch (TaskException ignored) {
		}
	}

	// test illegal ComponentBox export
	@Test
	void test_Ill_CompBox_export() {
		// null
		try {
			ComponentBox _a = new ComponentBox("a", new Dimension3D(1, 1, 1));
			String _exp = _a.export(null);

			fail("failed to catch null");
		} catch (TaskException ignored) {
		}

		// no root
		try {
			ComponentBox _a = new ComponentBox("a", new Dimension3D(1, 1, 1));
			String _exp = _a.export(new ExporterXML());

			fail("failed to catch no root");
		} catch (TaskException ignored) {
		}
	}

	// test illegal ComponentBox calculateAreaAll
	@Test
	void test_Ill_CompBox_calculateAreaAll() {
		try {
			ComponentBox _a = new ComponentBox("a", new Dimension3D(1, 1, 1));
			double _area = _a.calculateAreaAll(null);

			fail("failed to catch null");
		} catch (TaskException ignored) {
		}
	}

	// test illegal ComponentBox calculateAreaSelf
	@Test
	void test_Ill_CompBox_calculateAreaSelf() {
		try {
			ComponentBox _a = new ComponentBox("a", new Dimension3D(1, 1, 1));
			double _area = _a.calculateAreaSelf(null);

			fail("failed to catch null");
		} catch (TaskException ignored) {
		}
	}

	// test illegal BoundingBox E_Plane
	// nothing to test

	// test illegal BoundingBox constructor 
	@Test
	void test_Ill_BoundingBox_ctr() {
		// null
		try {
			BoundingBox _a = new BoundingBox(null, new Dimension3D(1, 1, 1));

			fail("failed to catch null exception");
		} catch (TaskException ignored) {
		}

		try {
			BoundingBox _a = new BoundingBox(new Point3D(1, 1, 1), null);

			fail("failed to catch null exception");
		} catch (TaskException ignored) {
		}
	}

	// test illegal BoundingBox calculateArea
	@Test
	void test_Ill_BoundingBox_calculateArea() {
		try {
			BoundingBox _a = new BoundingBox(new Point3D(0, 0, 0), new Dimension3D(1, 1, 1));
			double _area = _a.calculateArea(null);

			fail("failed to catch null");
		} catch (TaskException ignored) {
		}
	}

	// test illegal BoundingBox extend
	@Test
	void test_Ill_BoundingBox_extend() {
		try {
			BoundingBox _a = new BoundingBox(new Point3D(0, 0, 0), new Dimension3D(1, 1, 1));
			BoundingBox _b = _a.extend(null);

			fail("failed to catch null");
		} catch (TaskException ignored) {
		}
	}

	// test illegal Connector constructor
	@Test
	void test_Ill_Connector_ctr() {
		// null
		try {
			Connector _c = new Connector(null, new Point3D(1, 1, 1));

			fail("failed to catch null");
		} catch (TaskException ignored) {
		}

		try {
			ComponentBox _a = new ComponentBox("a", new Dimension3D(1, 1, 1));
			Connector _c = new Connector(_a, null);

			fail("failed to catch null");
		} catch (TaskException ignored) {
		}

		// is root
		try {
			ComponentBox _a = new ComponentBox("a", new Dimension3D(1, 1, 1), true);
			Connector _c = new Connector(_a, new Point3D(1, 1, 1));

			fail("failed to catch is root");
		} catch (TaskException ignored) {
		}
	}

	// test illegal Connector getComponentParent
	@Test
	void test_Ill_Connector_getComponentParent() {
		try {
			ComponentBox _a = new ComponentBox("a", new Dimension3D(1, 1, 1));
			Connector _c = new Connector(_a, new Point3D(1, 1, 1));

			A_Component _parent = _c.getComponentParent();

			fail("failed to catch null");
		} catch (TaskException ignored) {
		}
	}

	// test illegal Connector setComponentParent
	// no test, isn't publically visible

	// test illegal ExporterXML closeExport
	@Test
	void test_Ill_ExporterXML_closeExport() {
		// closed
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode("A");
			_exporter.closeComponentNode("A");
			_exporter.closeExport();

			_exporter.closeExport();
			fail("Failed to catch closed already");
		} catch (TaskException ignored) {
		}

		// open component
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode("A");
			_exporter.closeExport();

			fail("Failed to catch open component");
		} catch (TaskException ignored) {
		}

	}

	// test illegal ExporterXML addPoint
	@Test
	void test_Ill_ExporterXML_addPoint() {
		// null
		try {
			ExporterXML _exporter = new ExporterXML();

			_exporter.openComponentNode("A");
			_exporter.addPoint(null, null);

			fail("failed to catch null");
		} catch (TaskException ignored) {
		}

		try {
			ExporterXML _exporter = new ExporterXML();

			_exporter.openComponentNode("A");
			_exporter.addPoint("asd", null);

			fail("failed to catch null");
		} catch (TaskException ignored) {
		}

		try {
			ExporterXML _exporter = new ExporterXML();

			_exporter.openComponentNode("A");
			_exporter.addPoint(null, new Point3D(1, 1, 1));

			fail("failed to catch null");
		} catch (TaskException ignored) {
		}

		// empty id
		try {
			ExporterXML _exporter = new ExporterXML();

			_exporter.openComponentNode("A");
			_exporter.addPoint("  ", new Point3D(1, 1, 1));

			fail("failed to catch empty id");
		} catch (TaskException ignored) {
		}

		try {
			ExporterXML _exporter = new ExporterXML();

			_exporter.openComponentNode("A");
			_exporter.addPoint("", new Point3D(1, 1, 1));

			fail("failed to catch empty id");
		} catch (TaskException ignored) {
		}

		// closed
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.closeExport();

			_exporter.addPoint("A", new Point3D(1, 1, 1));

			fail("failed to catch closed export");
		} catch (TaskException ignored) {
		}

		// id exists
		try {
			ExporterXML _exporter = new ExporterXML();

			_exporter.openComponentNode("A");
			_exporter.addPoint("AA", new Point3D(1, 1, 1));
			_exporter.addPoint("AA", new Point3D(1, 1, 1));

			fail("failed to catch id exists");
		} catch (TaskException ignored) {
		}

		// no open component
		try {
			ExporterXML _exporter = new ExporterXML();

			_exporter.addPoint("AA", new Point3D(1, 1, 1));

			fail("failed to catch no open node");
		} catch (TaskException ignored) {
		}
	}

	// test illegal ExporterXML closeComponent
	@Test
	void test_Ill_ExporterXML_closeComponent() {
		// null
		try {
			ExporterXML _exporter = new ExporterXML();

			_exporter.openComponentNode("A");
			_exporter.closeComponentNode(null);

			fail("failed to catch null");
		} catch (TaskException ignored) {
		}

		// empty id
		try {
			ExporterXML _exporter = new ExporterXML();

			_exporter.openComponentNode("A");
			_exporter.closeComponentNode("");

			fail("failed to catch empty");
		} catch (TaskException ignored) {
		}

		try {
			ExporterXML _exporter = new ExporterXML();

			_exporter.openComponentNode("A");
			_exporter.closeComponentNode("  ");

			fail("failed to catch empty");
		} catch (TaskException ignored) {
		}

		// no component open
		try {
			ExporterXML _exporter = new ExporterXML();

			_exporter.closeComponentNode("A");

			fail("failed to catch no open component");
		} catch (TaskException ignored) {
		}

		// open component id does not match
		try {
			ExporterXML _exporter = new ExporterXML();

			_exporter.openComponentNode("A");
			_exporter.closeComponentNode("B");

			fail("failed to catch does not match open component");
		} catch (TaskException ignored) {
		}

		// closed
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode("A");
			_exporter.closeComponentNode("A");
			_exporter.closeExport();

			_exporter.closeComponentNode("B");

			fail("failed to catch closed");
		} catch (TaskException ignored) {
		}
	}

	// test illegal ExporterXML openComponent
	@Test
	void test_Ill_ExporterXML_openComponent() {
		// null
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode(null);
			fail("failed to catch null");
		} catch (TaskException ignored) {
		}
		// empty
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode("");
			fail("failed to catch empty");
		} catch (TaskException ignored) {
		}
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode("  ");
			fail("failed to catch empty");
		} catch (TaskException ignored) {
		}
		// node already open
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode("A");
			_exporter.openComponentNode("B");
			fail("failed to catch open node");
		} catch (TaskException ignored) {
		}
		// node w/ ID exists
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode("A");
			_exporter.closeComponentNode("A");
			_exporter.openComponentNode("A");

			fail("failed to catch node with ID exists");
		} catch (TaskException ignored) {
		}
		// closed
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode("A");
			_exporter.closeComponentNode("A");
			_exporter.closeExport();
			_exporter.openComponentNode("B");
			fail("failed to catch closed");
		} catch (TaskException ignored) {
		}
	}

	// test illegal ExporterXML closeComponent extended
	@Test
	void test_Ill_ExporterXML_openComponent_ext() {
		// null
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode(null, null);
			fail("failed to catch null");
		} catch (TaskException ignored) {
		}
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode("A", null);
			fail("failed to catch null");
		} catch (TaskException ignored) {
		}
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode(null, "A");
			fail("failed to catch null");
		} catch (TaskException ignored) {
		}

		// empty
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode("", "A");
			fail("failed to catch empty");
		} catch (TaskException ignored) {
		}
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode("  ", "A");
			fail("failed to catch empty");
		} catch (TaskException ignored) {
		}
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode("A", "");
			fail("failed to catch empty");
		} catch (TaskException ignored) {
		}
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode("A", "   ");
			fail("failed to catch empty");
		} catch (TaskException ignored) {
		}
		// node already open
		try {
			ExporterXML _exporter = new ExporterXML();

			_exporter.openComponentNode("A");
			_exporter.openComponentNode("B", "A");

			fail("failed to catch open node");
		} catch (TaskException ignored) {
		}

		// node w/ ID exists
		try {
			ExporterXML _exporter = new ExporterXML();

			_exporter.openComponentNode("A");
			_exporter.closeComponentNode("A");
			_exporter.openComponentNode("B", "A");
			_exporter.closeComponentNode("B");

			_exporter.openComponentNode("B", "A");

			fail("failed to catch node with ID exists");
		} catch (TaskException ignored) {
		}

		// closed
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode("A");
			_exporter.closeComponentNode("A");
			_exporter.closeExport();

			_exporter.openComponentNode("B", "A");

			fail("failed to catch closed");
		} catch (TaskException ignored) {
		}

		// IDs match
		try {
			ExporterXML _exporter = new ExporterXML();
			_exporter.openComponentNode("A");
			_exporter.closeComponentNode("A");
			_exporter.openComponentNode("B", "B");

			fail("failed to catch id matches");
		} catch (TaskException ignored) {
		}
	}

	// test Point3D constructor
	// no point, other tests will validate

	// test Point3D clone
	@Test
	void test_Point3D_clone() throws CloneNotSupportedException {
		Point3D _a = new Point3D(1, 2, 3);
		Point3D _b = _a.clone();

		assertFalse(_a == _b);
		assertTrue(_a.equals(_b));
	}

	// test Point3D add
	@Test
	void test_Point3D_add() {
		Point3D _a = new Point3D(-1, 0, 1);
		Point3D _b = new Point3D(0.5, 1.5, -0.75);
		Point3D _c = new Point3D(-5, 10, 1);

		Point3D _ab = _a.add(_b);
		Point3D _ba = _b.add(_a);

		Point3D _ac = _a.add(_c);
		Point3D _ca = _c.add(_a);

		Point3D _bc = _b.add(_c);
		Point3D _cb = _c.add(_b);

		if (!_ab.equals(_ba)) {
			fail("add (a+b) does not match");
		}

		if (!_ac.equals(_ca)) {
			fail("add (a+c) does not match");
		}

		if (!_bc.equals(_cb)) {
			fail("add (c+b) does not match");
		}
	}

	// test Point3D subtract
	@Test
	void test_Point3D_subtract() {
		Point3D _a = new Point3D(-1, 0, 1);
		Point3D _b = new Point3D(0.5, 1.5, -0.75);
		Point3D _c = new Point3D(-5, 10, 1);

		Point3D _ab = _a.subtract(_b);
		Point3D _ba = _b.subtract(_a);

		Point3D _ac = _a.subtract(_c);
		Point3D _ca = _c.subtract(_a);

		Point3D _bc = _b.subtract(_c);
		Point3D _cb = _c.subtract(_b);

		if (!test_Point3D_match(_ab, -1.5, -1.5, 1.75)) {
			fail("subtract (a-b) does not match");
		}
		if (!test_Point3D_match(_ba, 1.5, 1.5, -1.75)) {
			fail("subtract (b-a) does not match");
		}

		if (!test_Point3D_match(_ac, 4, -10, 0)) {
			fail("subtract (a-c) does not match");
		}
		if (!test_Point3D_match(_ca, -4, 10, 0)) {
			fail("subtract (c-a) does not match");
		}

		if (!test_Point3D_match(_bc, 5.5, -8.5, -1.75)) {
			fail("subtract (b-c) does not match");
		}
		if (!test_Point3D_match(_cb, -5.5, 8.5, 1.75)) {
			fail("subtract (c-b) does not match");
		}

	}

	// test Point3D tostring
	@Test
	void test_Point3D_tostring() {
		System.out.println("Point3D.toString()");

		Point3D _a = new Point3D(-1, 0, 1);
		Point3D _b = new Point3D(0.5, 1.5, -0.75);
		Point3D _c = new Point3D(-5, 10, 1);

		System.out.printf("A: \"%s\"\n", _a.toString());
		System.out.printf("B: \"%s\"\n", _b.toString());
		System.out.printf("C: \"%s\"\n", _c.toString());
	}

	// helper method for points
	// checks if points values match
	private boolean test_Point3D_match(Point3D _point, double _x, double _y, double _z) {
		return _point.equals(new Point3D(_x, _y, _z));
	}

	// test Dimension3D constructor
	// no point, other tests will validate

	// test Dimension3D equals
	@Test
	void test_Dimension3D_equals() {
		Dimension3D _a = new Dimension3D(1, 1, 2);
		Dimension3D _b = new Dimension3D(1, 1, 2);
		Dimension3D _c = new Dimension3D(2, 1, 2);
		Dimension3D _d = new Dimension3D(1, 3, 4);

		assertTrue(_a.equals(_b));
		assertTrue(_b.equals(_a));
		assertFalse(_a.equals(_c));
		assertFalse(_a.equals(_d));
		assertFalse(_b.equals(_c));
		assertFalse(_b.equals(_d));
	}

	// test Dimension3D clone
	@Test
	void test_Dimension3D_clone() throws CloneNotSupportedException {
		Dimension3D _a = new Dimension3D(1, 2, 3);
		Dimension3D _b = _a.clone();

		assertFalse(_a == _b);
		assertTrue(_a.equals(_b));
	}

	// test Dimension3D toString
	@Test
	void test_Dimension3D_tostring() {
		System.out.println("Dimension3D.toString()");

		Dimension3D _a = new Dimension3D(1, 1, 2);
		Dimension3D _b = new Dimension3D(2, 1, 2);
		Dimension3D _c = new Dimension3D(1, 3, 4);

		System.out.printf("A: \"%s\"\n", _a.toString());
		System.out.printf("B: \"%s\"\n", _b.toString());
		System.out.printf("C: \"%s\"\n", _c.toString());
	}

	// test BoundingBox ctr
	// no need, other tests handle

	// test BoundingBox calculateArea
	@Test
	void test_BoundingBox_calculateArea() {
		BoundingBox _a = new BoundingBox(new Point3D(1, 1, 1), new Dimension3D(1, 1, 1));
		assertTrue(_a.calculateArea(BoundingBox.E_Plane.XY) == 2 * (1.0));
		assertTrue(_a.calculateArea(BoundingBox.E_Plane.XZ) == 2 * (1.0));
		assertTrue(_a.calculateArea(BoundingBox.E_Plane.YZ) == 2 * (1.0));

		BoundingBox _b = new BoundingBox(new Point3D(0.5, 0.4, 0.6), new Dimension3D(1.5, 1.4, 1.3));
		assertTrue(_b.calculateArea(BoundingBox.E_Plane.XY) == 2 * (1.5 * 1.4));
		assertTrue(_b.calculateArea(BoundingBox.E_Plane.XZ) == 2 * (1.5 * 1.3));
		assertTrue(_b.calculateArea(BoundingBox.E_Plane.YZ) == 2 * (1.3 * 1.4));

		BoundingBox _c = new BoundingBox(new Point3D(1.5, 1.6, 1.4), new Dimension3D(0.1, 0.2, 0.3));
		assertTrue(_c.calculateArea(BoundingBox.E_Plane.XY) == 2 * (0.1 * 0.2));
		assertTrue(_c.calculateArea(BoundingBox.E_Plane.XZ) == 2 * (0.1 * 0.3));
		assertTrue(_c.calculateArea(BoundingBox.E_Plane.YZ) == 2 * (0.2 * 0.3));
	}

	// test BoundingBox calculateVolume
	@Test
	void test_BoundingBox_calculateVolume() {
		BoundingBox _a = new BoundingBox(new Point3D(1, 1, 1), new Dimension3D(1, 1, 1));
		BoundingBox _b = new BoundingBox(new Point3D(0.5, 0.4, 0.6), new Dimension3D(1.5, 1.4, 1.3));
		BoundingBox _c = new BoundingBox(new Point3D(1.5, 1.6, 1.4), new Dimension3D(0.1, 0.2, 0.3));

		assertTrue(_a.calculateVolume() == (1));
		assertTrue(_b.calculateVolume() == (1.5 * 1.4 * 1.3));
		assertTrue(_c.calculateVolume() == (0.3 * 0.2 * 0.1));
	}

	// test BoundingBox extend
	@Test
	void test_BoundingBox_extend() {
		BoundingBox _a = new BoundingBox(new Point3D(1, 1, 1), new Dimension3D(1, 2, 3));
		BoundingBox _b = new BoundingBox(new Point3D(3, 3, 3), new Dimension3D(3, 2, 1));
		BoundingBox _c = new BoundingBox(new Point3D(0, 5, 0), new Dimension3D(1, 1, 1));
		BoundingBox _d = new BoundingBox(new Point3D(2, 4, 0), new Dimension3D(1, 1, 1));
		BoundingBox _e = new BoundingBox(new Point3D(0, 0, 0), new Dimension3D(3, 3, 3));
		BoundingBox _f = new BoundingBox(new Point3D(0, 0, 0), new Dimension3D(1, 1, 1));

		BoundingBox _ab = _a.extend(_b);
		BoundingBox _cd = _c.extend(_d);
		BoundingBox _ef = _e.extend(_f);

		assertTrue(_ab.getCenter().equals(new Point3D(2.5, 2, 1.5)) && _ab.getSize().equals(new Dimension3D(4, 4, 4)));
		assertTrue(_cd.getCenter().equals(new Point3D(1, 4.5, 0)) && _cd.getSize().equals(new Dimension3D(3, 2, 1)));
		assertTrue(_ef.getCenter().equals(new Point3D(0, 0, 0)) && _ef.getSize().equals(new Dimension3D(3, 3, 3)));
	}

	// test BoundingBox generateCorners
	@Test
	void test_BoundingBox_generateCorners() {
		BoundingBox _a = new BoundingBox(new Point3D(0.5, 0.4, 0.6), new Dimension3D(1.5, 1.4, 1.3));

		// expected
		List<Point3D> _ecorners = new ArrayList<Point3D>();
		_ecorners.add(new Point3D(-0.25, -0.3, -0.05));
		_ecorners.add(new Point3D(1.25, -0.3, -0.05));
		_ecorners.add(new Point3D(1.25, -0.3, 1.25));
		_ecorners.add(new Point3D(-0.25, -0.3, 1.25));
		_ecorners.add(new Point3D(-0.25, 1.1, -0.05));
		_ecorners.add(new Point3D(1.25, 1.1, -0.05));
		_ecorners.add(new Point3D(1.25, 1.1, 1.25));
		_ecorners.add(new Point3D(-0.25, 1.1, 1.25));

		// actual corners
		List<Point3D> _acorners = _a.generateCorners();

		// compare lists
		Iterator<Point3D> _eit = _ecorners.iterator(), _ait = _acorners.iterator();

		while (_eit.hasNext() && _ait.hasNext()) {
			Point3D _ec = _eit.next(), _ac = _ait.next();

			// compare coords
			double _difX = _ec.getX() - _ac.getX();
			double _difY = _ec.getY() - _ac.getY();
			double _difZ = _ec.getZ() - _ac.getZ();

			// check differences
			if ((_difX != 0) && (_difY != 0) && (_difZ != 0)) {
				fail(String.format("corner mismatch actual(%s) - expected(%s), difference(%f,%f,%f)\n", _ac.toString(), _ec.toString(), _difX, _difY, _difZ));
			}
		}

		if (_eit.hasNext() || _ait.hasNext()) {
			fail("one iterator still has corners");
		}
	}

	// test BoundingBox clone
	@Test
	void test_BoundingBox_clone() throws CloneNotSupportedException {
		BoundingBox _a = new BoundingBox(new Point3D(1, 1, 1), new Dimension3D(1, 2, 3));
		BoundingBox _b = _a.clone();

		assertFalse(_a == _b);
		assertTrue(_a.equals(_b));
	}

	// test BoundingBox tostring
	@Test
	void test_BoundingBox_tostring() {
		System.out.println("BoundingBox.toString()");

		BoundingBox _a = new BoundingBox(new Point3D(1, 1, 1), new Dimension3D(1, 1, 1));
		BoundingBox _b = new BoundingBox(new Point3D(0.5, 0.4, 0.6), new Dimension3D(1.5, 1.4, 1.3));
		BoundingBox _c = new BoundingBox(new Point3D(1.5, 1.6, 1.4), new Dimension3D(0.1, 0.2, 0.3));

		System.out.printf("A: \"%s\"\n", _a.toString());
		System.out.printf("B: \"%s\"\n", _b.toString());
		System.out.printf("C: \"%s\"\n", _c.toString());
	}

	// test ComponentBox getAbsoluteCenterPosition
	@Test
	void test_ComponentBox_getAbsoluteCenterPosition() {
		ComponentBox _a = new ComponentBox("box1", new Dimension3D(3, 5, 2), true);
		ComponentBox _b = new ComponentBox("box2", new Dimension3D(4, 6, 4));
		ComponentBox _c = new ComponentBox("box3", new Dimension3D(8, 7, 3));
		ComponentBox _d = new ComponentBox("box4", new Dimension3D(2, 5, 6));

		_a.connectChild(new Connector(_b, new Point3D(10, 12, 5)));
		_b.connectChild(new Connector(_c, new Point3D(-2, -15, -6)));
		_b.connectChild(new Connector(_d, new Point3D(7, -8, 2)));

		assertTrue(_a.getAbsoluteCenterPosition().equals(new Point3D(0, 0, 0)));
		assertTrue(_b.getAbsoluteCenterPosition().equals(new Point3D(10, 12, 5)));
		assertTrue(_c.getAbsoluteCenterPosition().equals(new Point3D(8, -3, -1)));
		assertTrue(_d.getAbsoluteCenterPosition().equals(new Point3D(17, 4, 7)));
	}

	// test ComponentBox getChildren
	@Test
	void test_ComponentBox_getChildren() {
		ComponentBox _a = new ComponentBox("box1", new Dimension3D(3, 5, 2), true);
		ComponentBox _b = new ComponentBox("box2", new Dimension3D(4, 6, 4));
		ComponentBox _c = new ComponentBox("box3", new Dimension3D(8, 7, 3));
		ComponentBox _d = new ComponentBox("box4", new Dimension3D(2, 5, 6));
		ComponentBox _e = new ComponentBox("box5", new Dimension3D(4, 2, 7));
		_a.connectChild(new Connector(_b, new Point3D(10, 12, 5)));
		_b.connectChild(new Connector(_e, new Point3D(-2, -15, -6)));
		_b.connectChild(new Connector(_c, new Point3D(7, -8, 2)));
		_b.connectChild(new Connector(_d, new Point3D(-7, 4, 2.5)));

		// general test
		assertTrue(_a.getChildren().size() == 1);
		assertTrue(_b.getChildren().size() == 3);
		assertTrue(_c.getChildren().size() == 0);
		assertTrue(_d.getChildren().size() == 0);

		// test sort
		List<A_Component> _children = _b.getChildren();

		A_Component _p = null;
		for (A_Component _comp : _children) {
			if (_p != null) {
				int _cmp = _p.getID().compareTo(_comp.getID());
				assertTrue(_cmp <= 0);
			}
			_p = _comp;
		}
	}

	// test ComponentBox getChildCount
	@Test
	void test_ComponentBox_getChildCount() {
		ComponentBox _a = new ComponentBox("box1", new Dimension3D(3, 5, 2), true);
		ComponentBox _b = new ComponentBox("box2", new Dimension3D(4, 6, 4));
		ComponentBox _c = new ComponentBox("box3", new Dimension3D(8, 7, 3));
		ComponentBox _d = new ComponentBox("box4", new Dimension3D(2, 5, 6));
		ComponentBox _e = new ComponentBox("box5", new Dimension3D(4, 2, 7));
		_a.connectChild(new Connector(_b, new Point3D(10, 12, 5)));
		_b.connectChild(new Connector(_e, new Point3D(-2, -15, -6)));
		_b.connectChild(new Connector(_c, new Point3D(7, -8, 2)));
		_b.connectChild(new Connector(_d, new Point3D(-7, 4, 2.5)));

		assertTrue(_a.getChildCount() == 1);
		assertTrue(_b.getChildCount() == 3);
		assertTrue(_c.getChildCount() == 0);
		assertTrue(_d.getChildCount() == 0);
		assertTrue(_e.getChildCount() == 0);
	}

	// test ComponentBox getDescendants
	@Test
	void test_ComponentBox_getDescendants() {
		ComponentBox _a = new ComponentBox("box1", new Dimension3D(3, 5, 2), true);
		ComponentBox _b = new ComponentBox("box2", new Dimension3D(4, 6, 4));
		ComponentBox _c = new ComponentBox("box3", new Dimension3D(8, 7, 3));
		ComponentBox _d = new ComponentBox("box4", new Dimension3D(2, 5, 6));
		ComponentBox _e = new ComponentBox("box5", new Dimension3D(4, 2, 7));
		_a.connectChild(new Connector(_b, new Point3D(10, 12, 5)));
		_b.connectChild(new Connector(_e, new Point3D(-2, -15, -6)));
		_b.connectChild(new Connector(_c, new Point3D(7, -8, 2)));
		_b.connectChild(new Connector(_d, new Point3D(-7, 4, 2.5)));

		// general test
		assertTrue(_a.getDescendants().size() == 4);
		assertTrue(_b.getDescendants().size() == 3);
		assertTrue(_c.getDescendants().size() == 0);
		assertTrue(_d.getDescendants().size() == 0);

		// test sort
		List<A_Component> _descendants = _b.getDescendants();

		A_Component _p = null;
		for (A_Component _comp : _descendants) {
			if (_p != null) {
				int _cmp = _p.getID().compareTo(_comp.getID());
				assertTrue(_cmp <= 0);
			}
			_p = _comp;
		}
	}

	// test ComponentBox getDescendantCount
	@Test
	void test_ComponentBox_getDescendantCount() {
		ComponentBox _a = new ComponentBox("box1", new Dimension3D(3, 5, 2), true);
		ComponentBox _b = new ComponentBox("box2", new Dimension3D(4, 6, 4));
		ComponentBox _c = new ComponentBox("box3", new Dimension3D(8, 7, 3));
		ComponentBox _d = new ComponentBox("box4", new Dimension3D(2, 5, 6));
		ComponentBox _e = new ComponentBox("box5", new Dimension3D(4, 2, 7));
		_a.connectChild(new Connector(_b, new Point3D(10, 12, 5)));
		_b.connectChild(new Connector(_e, new Point3D(-2, -15, -6)));
		_b.connectChild(new Connector(_c, new Point3D(7, -8, 2)));
		_b.connectChild(new Connector(_d, new Point3D(-7, 4, 2.5)));

		// general test
		assertTrue(_a.getDescendantCount() == 4);
		assertTrue(_b.getDescendantCount() == 3);
		assertTrue(_c.getDescendantCount() == 0);
		assertTrue(_d.getDescendantCount() == 0);
	}

	// test ComponentBox hashCode
	@Test
	void test_ComponentBox_hashCode() {
		String _sa = "weac", _sb = "asdwe", _sc = "otekme";

		ComponentBox _a = new ComponentBox(_sa, new Dimension3D(1, 1, 1));
		ComponentBox _b = new ComponentBox(_sb, new Dimension3D(1, 1, 1));
		ComponentBox _c = new ComponentBox(_sc, new Dimension3D(1, 1, 1));

		assertTrue(_a.hashCode() == _sa.toUpperCase().hashCode());
		assertTrue(_b.hashCode() == _sb.toUpperCase().hashCode());
		assertTrue(_c.hashCode() == _sc.toUpperCase().hashCode());
	}

	// test ComponentBox generateBoundingBoxAll
	@Test
	void test_ComponentBox_generateBoundingBoxAll() {
		ComponentBox _a = new ComponentBox("a", new Dimension3D(3, 4, 5), true);
		ComponentBox _b = new ComponentBox("b", new Dimension3D(2, 4, 8));
		ComponentBox _c = new ComponentBox("c", new Dimension3D(4, 6, 1));

		_a.connectChild(new Connector(_b, new Point3D(1, 1, 1)));
		_a.connectChild(new Connector(_c, new Point3D(-1, -1, -1)));

		BoundingBox _bounding = _a.generateBoundingBoxAll();

		assertTrue(_bounding.equals(new BoundingBox(new Point3D(-0.5, -0.5, 1), new Dimension3D(5, 7, 8))));
	}

	// test ComponentBox generateBoundingBoxSelf
	@Test
	void test_ComponentBox_generateBoundingBoxSelf() {
		ComponentBox _a = new ComponentBox("a", new Dimension3D(3, 4, 5), true);
		BoundingBox _bounding = _a.generateBoundingBoxSelf();

		assertTrue(_bounding.equals(new BoundingBox(new Point3D(0, 0, 0), new Dimension3D(3, 4, 5))));
	}

	// test ComponentBox calculateCenterOfMassAll
	@Test
	void test_ComponentBox_calculateCenterOfMassAll() {
		ComponentBox _a = new ComponentBox("a", new Dimension3D(3, 4, 5), true);
		ComponentBox _b = new ComponentBox("b", new Dimension3D(2, 4, 8));
		ComponentBox _c = new ComponentBox("c", new Dimension3D(4, 6, 1));

		_a.connectChild(new Connector(_b, new Point3D(1, 1, 1)));
		_a.connectChild(new Connector(_c, new Point3D(-1, -1, -1)));

		Point3D _center = _a.calculateCenterOfMassAll();

		assertTrue(_center.equals(new Point3D(-0.5, -0.5, 1)));
	}

	// test ComponentBox calculateCenterOfMassSelf
	@Test
	void test_ComponentBox_calculateCenterOfMassSelf() {
		ComponentBox _a = new ComponentBox("a", new Dimension3D(3, 4, 5), true);

		Point3D _center = _a.calculateCenterOfMassSelf();

		assertTrue(_center.equals(new Point3D(0, 0, 0)));
	}

	// test ComponentBox generateFramesAll
	@Test
	void test_ComponentBox_generateFramesAll() {
		ComponentBox _a = new ComponentBox("a", new Dimension3D(3, 4, 5), true);
		ComponentBox _b = new ComponentBox("b", new Dimension3D(2, 4, 8));
		ComponentBox _c = new ComponentBox("c", new Dimension3D(4, 6, 1));

		_a.connectChild(new Connector(_b, new Point3D(1, 1, 1)));
		_a.connectChild(new Connector(_c, new Point3D(-1, -1, -1)));

		List<List<Point3D>> _frames = _a.generateFramesAll();

		assertTrue(_frames.get(0).equals(_a.generateFrameSelf()));
		assertTrue(_frames.get(1).equals(_b.generateFrameSelf()));
		assertTrue(_frames.get(2).equals(_c.generateFrameSelf()));
	}

	// test ComponentBox generateFramesSelf
	@Test
	void test_ComponentBox_generateFramesSelf() {
		ComponentBox _a = new ComponentBox("a", new Dimension3D(3, 4, 5), true);
		List<Point3D> _frame = _a.generateFrameSelf();

		assertTrue(_frame.get(0).equals(new Point3D(0, 0, 0)));
		assertTrue(_frame.get(1).equals(new Point3D(-1.5, -2, -2.5)));
		assertTrue(_frame.get(2).equals(new Point3D(1.5, -2, -2.5)));
		assertTrue(_frame.get(3).equals(new Point3D(1.5, -2, 2.5)));
		assertTrue(_frame.get(4).equals(new Point3D(-1.5, -2, 2.5)));
		assertTrue(_frame.get(5).equals(new Point3D(-1.5, 2, -2.5)));
		assertTrue(_frame.get(6).equals(new Point3D(1.5, 2, -2.5)));
		assertTrue(_frame.get(7).equals(new Point3D(1.5, 2, 2.5)));
		assertTrue(_frame.get(8).equals(new Point3D(-1.5, 2, 2.5)));
	}

	// test ComponentBox calculateAreaAll
	@Test
	void test_ComponentBox_calculateAreaAll() {
		ComponentBox _a = new ComponentBox("a", new Dimension3D(3, 4, 5), true);
		ComponentBox _b = new ComponentBox("b", new Dimension3D(1, 1, 1));
		ComponentBox _c = new ComponentBox("c", new Dimension3D(0.5, 0.5, 0.5));

		_a.connectChild(new Connector(_b, new Point3D(1, 1, 1)));
		_a.connectChild(new Connector(_c, new Point3D(1, 1, 1)));

		assertTrue(_a.calculateAreaAll(BoundingBox.E_Plane.XY) == 2 * (3 * 4 + 1 * 1 + 0.5 * 0.5));
		assertTrue(_a.calculateAreaAll(BoundingBox.E_Plane.XZ) == 2 * (3 * 5 + 1 * 1 + 0.5 * 0.5));
		assertTrue(_a.calculateAreaAll(BoundingBox.E_Plane.YZ) == 2 * (5 * 4 + 1 * 1 + 0.5 * 0.5));
	}

	// test ComponentBox calculateAreaSelf
	@Test
	void test_ComponentBox_calculateAreaSelf() {
		ComponentBox _a = new ComponentBox("a", new Dimension3D(3, 4, 5), true);

		assertTrue(_a.calculateAreaAll(BoundingBox.E_Plane.XY) == 2 * (3 * 4));
		assertTrue(_a.calculateAreaAll(BoundingBox.E_Plane.XZ) == 2 * (3 * 5));
		assertTrue(_a.calculateAreaAll(BoundingBox.E_Plane.YZ) == 2 * (5 * 4));
	}

	// test ComponentBox calculateVolumeAll
	@Test
	void test_ComponentBox_calculateVolumeAll() {
		ComponentBox _a = new ComponentBox("a", new Dimension3D(3, 4, 5), true);
		ComponentBox _b = new ComponentBox("b", new Dimension3D(1, 1, 1));
		ComponentBox _c = new ComponentBox("c", new Dimension3D(0.5, 0.5, 0.5));

		_a.connectChild(new Connector(_b, new Point3D(1, 1, 1)));
		_a.connectChild(new Connector(_c, new Point3D(1, 1, 1)));

		assertTrue(_a.calculateVolumeAll() == (3 * 4 * 5 + 1 * 1 * 1 + 0.5 * 0.5 * 0.5));
	}

	// test ComponentBox calculateVolumeSelf
	@Test
	void test_ComponentBox_calculateVolumeSelf() {
		ComponentBox _a = new ComponentBox("a", new Dimension3D(3, 4, 5), true);

		assertTrue(_a.calculateVolumeSelf() == (3 * 4 * 5));
	}

	// test ComponentBox toString
	@Test
	void test_ComponentBox_toString() {
		System.out.println("ComponentBox.toString()");

		ComponentBox _a = new ComponentBox("a", new Dimension3D(3, 5, 2), true);
		ComponentBox _b = new ComponentBox("b", new Dimension3D(4, 6, 4));
		ComponentBox _c = new ComponentBox("c", new Dimension3D(8, 7, 3));
		ComponentBox _d = new ComponentBox("d", new Dimension3D(2, 5, 6));
		_a.connectChild(new Connector(_b, new Point3D(10, 12, 5)));
		_b.connectChild(new Connector(_c, new Point3D(-2, -15, -6)));
		_b.connectChild(new Connector(_d, new Point3D(7, -8, 2)));

		System.out.printf("A: \"%s\"\n", _a.toString());
		System.out.printf("B: \"%s\"\n", _b.toString());
		System.out.printf("C: \"%s\"\n", _c.toString());
		System.out.printf("D: \"%s\"\n", _d.toString());
	}

	// test ComponentBox clone
	@Test
	void test_ComponentBox_clone() throws TaskException, CloneNotSupportedException {
		// create boxes
		ComponentBox box1 = new ComponentBox("box1", new Dimension3D(3, 5, 2), true);
		ComponentBox box2 = new ComponentBox("box2", new Dimension3D(4, 6, 4));
		ComponentBox box3 = new ComponentBox("box3", new Dimension3D(8, 7, 3));
		ComponentBox box4 = new ComponentBox("box4", new Dimension3D(2, 5, 6));
		// build tree
		box1.connectChild(new Connector(box2, new Point3D(10, 12, 5)));
		box2.connectChild(new Connector(box3, new Point3D(-2, -15, -6)));
		box2.connectChild(new Connector(box4, new Point3D(7, -8, 2)));
		// extend tree
		ComponentBox box5 = new ComponentBox("box5", new Dimension3D(1, 2, 1));
		ComponentBox box6 = new ComponentBox("box6", new Dimension3D(5, 5, 5));
		ComponentBox box7 = new ComponentBox("box7", new Dimension3D(2, 1, 2));
		box1.connectChild(new Connector(box7, new Point3D(2, 7, 4)));
		box1.connectChild(new Connector(box5, new Point3D(-4, 6, 0)));
		box1.connectChild(new Connector(box6, new Point3D(8, 0, -8)));

		ComponentBox _clone = box1.clone();

		test_ComponentBox_clone_validate(box1, _clone);
	}

	// helper method to validate ComponentBox clone
	private boolean test_ComponentBox_clone_validate(A_Component _comp, A_Component _clone) {
		// references
		assertFalse(_comp == null);
		assertFalse(_clone == null);
		assertFalse(_comp == _clone);

		// id
		assertFalse(_comp.getID() == _clone.getID());
		assertTrue(_comp.getID().equals(_clone.getID()));

		// is root
		boolean _r1 = _comp.isRoot(), _r2 = _clone.isRoot();
		assertTrue(((_r1 && _r2) || (!_r1 && !_r2)));

		// check if box
		boolean _b1 = _comp instanceof ComponentBox, _b2 = _clone instanceof ComponentBox;
		assertTrue(((_r1 && _r2) || (!_r1 && !_r2)));
		// size
		if (_b1) {
			ComponentBox _box = (ComponentBox) _comp, _cloneBox = (ComponentBox) _clone;

			assertFalse(_box.getSize() == _cloneBox.getSize());
			assertTrue(_box.getSize().equals(_cloneBox.getSize()));
		}

		// parent connector
		boolean _hpc1 = _comp.hasConnectorToParent(), _hpc2 = _clone.hasConnectorToParent();
		assertTrue(((_hpc1 && _hpc2) || (!_hpc1 && !_hpc2)));

		// don't check parent connector details

		// children connectors
		List<Connector> _children1 = _comp.getConnectorsToChildren(), _children2 = _clone.getConnectorsToChildren();
		// sort
		_children1.sort(new ConnectorComparator());
		_children2.sort(new ConnectorComparator());

		assertTrue(_children1.size() == _children2.size());

		Iterator<Connector> _it1 = _children1.iterator(), _it2 = _children2.iterator();

		while ((_it1.hasNext()) && (_it2.hasNext())) {
			Connector _c1 = _it1.next(), _c2 = _it2.next();
			assertFalse(_c1 == _c2);

			// offset
			assertFalse(_c1.getOffsetFromParent() == _c2.getOffsetFromParent());
			assertTrue(_c1.getOffsetFromParent().equals(_c2.getOffsetFromParent()));

			// children
			A_Component _comp1 = _c1.getComponentChild(), _comp2 = _c2.getComponentChild();
			assertTrue(test_ComponentBox_clone_validate(_comp1, _comp2));
		}

		assertFalse(_it1.hasNext() || _it2.hasNext());

		return true;
	}

	// test comparator for list of connectors
	public static class ConnectorComparator implements Comparator<Connector> {

		/**
		 * just compare by child components ID
		 */
		public int compare(Connector _c1, Connector _c2) throws TaskException {
			if ((_c1 == null) || (_c2 == null)) {
				throw new TaskException("null argument");
			}

			// get string identifiers
			String _id1 = _c1.getComponentChild().getID(), _id2 = _c2.getComponentChild().getID();
			// compare string identifiers
			return (_id1.compareTo(_id2));
		}

	}

	// test that a stray connector works out
	@Test
	void test_stray_connector() {
		ComponentBox box1 = new ComponentBox("box1", new Dimension3D(3, 5, 2));
		ComponentBox box2 = new ComponentBox("box2", new Dimension3D(4, 6, 4));
		ComponentBox box3 = new ComponentBox("box3", new Dimension3D(8, 7, 3));
		ComponentBox box4 = new ComponentBox("box4", new Dimension3D(2, 5, 6));
		ComponentBox box5 = new ComponentBox("box5", new Dimension3D(1, 2, 1));
		ComponentBox box6 = new ComponentBox("box6", new Dimension3D(5, 5, 5));
		ComponentBox box7 = new ComponentBox("box7", new Dimension3D(2, 1, 2));

		Connector _c1 = new Connector(box1, new Point3D(1, 1, 1));
		box2.connectChild(new Connector(box3, new Point3D(-2, -15, -6)));
		box2.connectChild(new Connector(box4, new Point3D(7, -8, 2)));

		box1.connectChild(new Connector(box2, new Point3D(10, 12, 5)));
		box1.connectChild(new Connector(box7, new Point3D(2, 7, 4)));
		box1.connectChild(new Connector(box5, new Point3D(-4, 6, 0)));
		box1.connectChild(new Connector(box6, new Point3D(8, 0, -8)));
	}

	// test general ExporterXML
	@Test
	void test_exporterxml() {
		ComponentBox boxA = new ComponentBox("A", new Dimension3D(1, 2, 3), true);
		ComponentBox boxB = new ComponentBox("B", new Dimension3D(4, 6, 8), false);
		ComponentBox boxC = new ComponentBox("C", new Dimension3D(10, 20, 30), false);
		ComponentBox boxD = new ComponentBox("D", new Dimension3D(5, 15, 25), false);
		ComponentBox boxE = new ComponentBox("E", new Dimension3D(7, 14, 21), false);

		boxA.connectChild(new Connector(boxE, new Point3D(4, 5, 6)));
		boxA.connectChild(new Connector(boxB, new Point3D(3, 2, 1)));
		boxB.connectChild(new Connector(boxC, new Point3D(9, 8, 7)));
		boxB.connectChild(new Connector(boxD, new Point3D(10, 12, 14)));

		ExporterXML exporter = new ExporterXML();
		System.out.println(boxA.export(exporter));
	}
}
