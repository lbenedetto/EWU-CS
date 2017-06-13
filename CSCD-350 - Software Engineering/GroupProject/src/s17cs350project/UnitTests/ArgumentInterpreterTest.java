package s17cs350project.UnitTests;

import org.junit.jupiter.api.Test;
import s17cs350project.command.CommandCreateComponent;
import s17cs350project.command.CommandCreateMainThruster;
import s17cs350project.command.CommandCreateVernierThruster;
import s17cs350project.component.thruster.A_Thruster;
import s17cs350project.datatype.Dimension3D;
import s17cs350project.datatype.Percent;
import s17cs350project.datatype.Point3D;
import s17cs350project.datatype.Thrust;
import s17cs350project.parser.ArgumentInterpreter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;


class ArgumentInterpreterTest {
	//TODO: Pass dummy controller instead of null
	/**
	 * Argument Interpreter tests
	 */

	// Faux equals() method to compare Dimension3D objects using reflection to get the private methods
	private boolean dimension3DEquals(Dimension3D expected, Dimension3D target) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Method _getWidth = Dimension3D.class.getDeclaredMethod("getWidth");
		_getWidth.setAccessible(true);
		Method _getHeight = Dimension3D.class.getDeclaredMethod("getHeight");
		_getHeight.setAccessible(true);
		Method _getDepth = Dimension3D.class.getDeclaredMethod("getDepth");
		_getHeight.setAccessible(true);

		double _targetWidth = (Double) _getWidth.invoke(target);
		double _targetHeight = (Double) _getHeight.invoke(target);
		double _targetDepth = (Double) _getDepth.invoke(target);

		double _expectedWidth = (Double) _getWidth.invoke(expected);
		double _expectedHeight = (Double) _getHeight.invoke(expected);
		double _expectedDepth = (Double) _getDepth.invoke(expected);

		return _targetWidth == _expectedWidth && _targetHeight == _expectedHeight && _targetDepth == _expectedDepth;
	}

	// Faux equals() method to compare Point3D objects using reflection to get the private methods
	private boolean point3DEquals(Point3D expected, Point3D target) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		Method _getX = Point3D.class.getDeclaredMethod("getX");
		_getX.setAccessible(true);
		Method _getY = Point3D.class.getDeclaredMethod("getY");
		_getY.setAccessible(true);
		Method _getZ = Point3D.class.getDeclaredMethod("getZ");
		_getY.setAccessible(true);

		double _targetX = (Double) _getX.invoke(target);
		double _targetY = (Double) _getY.invoke(target);
		double _targetZ = (Double) _getZ.invoke(target);

		double _expectedX = (Double) _getX.invoke(expected);
		double _expectedY = (Double) _getY.invoke(expected);
		double _expectedZ = (Double) _getZ.invoke(expected);

		return _targetX == _expectedX && _targetY == _expectedY && _targetZ == _expectedZ;
	}


	@Test
	void test_CreateComponentArgumentInterpreterV1() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		// With root
		ArgumentInterpreter ai = new ArgumentInterpreter.CreateComponentArgumentInterpreter();
		ai.interpret(new String[]{"ROOT", "idOne", "1.2", "2.3", "3.4"}, null);
		assertTrue(ai.getLastExecutedCommand() instanceof CommandCreateComponent);
		Object[] args = ai.getLastUsedArgs();
		assertEquals("idOne", args[0]);
		assertTrue(args[1] instanceof Dimension3D);
		assertTrue(dimension3DEquals(new Dimension3D(1.2, 2.3, 3.4), (Dimension3D) args[1]));
		assertTrue(args[2] instanceof Boolean);
		assertTrue((Boolean) args[2]);

		// Without root
		ai.interpret(new String[]{"", "idOne", "1.2", "2.3", "3.4"}, null);
		assertTrue(ai.getLastExecutedCommand() instanceof CommandCreateComponent);
		args = ai.getLastUsedArgs();
		assertEquals("idOne", args[0]);
		assertTrue(args[1] instanceof Dimension3D);
		assertTrue(dimension3DEquals(new Dimension3D(1.2, 2.3, 3.4), (Dimension3D) args[1]));
		assertTrue(args[2] instanceof Boolean);
		assertFalse((Boolean) args[2]);
	}

	@Test
	void test_CreateMainThrusterV1ArgumentInterpreter() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		ArgumentInterpreter ai = new ArgumentInterpreter.CreateMainThrusterV1ArgumentInterpreter();
		ai.interpret(new String[]{"Jim", "3.2", "4.8", "5.4", "BACK", "DOWNWARD", "50.8", "100.0"}, null);
		assertTrue(ai.getLastExecutedCommand() instanceof CommandCreateMainThruster);
		Object[] args = ai.getLastUsedArgs();
		// Validate data types
		assertTrue(args[0] instanceof String);
		assertTrue(args[1] instanceof Point3D);
		assertTrue(args[2] instanceof A_Thruster.E_Surface);
		assertTrue(args[3] instanceof A_Thruster.E_Orientation);
		assertTrue(args[4] instanceof Percent);
		assertTrue(args[5] instanceof Thrust);
		// Validate values
		assertEquals("Jim", args[0]);
		assertTrue(point3DEquals(new Point3D(3.2, 4.8, 5.4), (Point3D) args[1]));
		assertTrue(args[2] == A_Thruster.E_Surface.BACK);
		assertTrue(args[3] == A_Thruster.E_Orientation.DOWNWARD);
		assertTrue(new Percent(100.0).compareTo((Percent) args[4]) == 0);
		assertTrue(new Thrust(50.8).compareTo((Thrust) args[5]) == 0);
	}

	@Test
	void test_CreateMainThrusterV2ArgumentInterpreter() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		ArgumentInterpreter ai = new ArgumentInterpreter.CreateMainThrusterV2ArgumentInterpreter();
		ai.interpret(new String[]{"Dan", "3.2", "4.8", "5.4", "BOTTOM", "DOWNWARD", "25.3", "100.0", "77.0"}, null);
		Object[] args = ai.getLastUsedArgs();
		// Validate data types
		assertTrue(ai.getLastExecutedCommand() instanceof CommandCreateMainThruster);
		assertTrue(args[0] instanceof String);
		assertTrue(args[1] instanceof Point3D);
		assertTrue(args[2] instanceof A_Thruster.E_Surface);
		assertTrue(args[3] instanceof A_Thruster.E_Orientation);
		assertTrue(args[4] instanceof Percent);
		assertTrue(args[5] instanceof Thrust);
		// Validate values
		assertEquals("Dan", args[0]);
		assertTrue(point3DEquals(new Point3D(3.2, 4.8, 5.4), (Point3D) args[1]));
		assertTrue(args[2] == A_Thruster.E_Surface.BOTTOM);
		assertTrue(args[3] == A_Thruster.E_Orientation.DOWNWARD);
		assertTrue(new Percent(77.0).compareTo((Percent) args[4]) == 0);
		assertTrue(new Thrust(25.3).compareTo((Thrust) args[5]) == 0);
		assertTrue(new Thrust(100.0).compareTo((Thrust) args[6]) == 0);
	}

	@Test
	void test_CreateVernierThrusterArgumentInterpreter() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		ArgumentInterpreter ai = new ArgumentInterpreter.CreateVernierThrusterArgumentInterpreter();
		ai.interpret(new String[]{"Vern", "0.0", "-3.0", "5.0", "FRONT", "UPWARD", "100.0", "77.0"}, null);
		Object[] args = ai.getLastUsedArgs();
		// Validate data types
		assertTrue(ai.getLastExecutedCommand() instanceof CommandCreateVernierThruster);
		assertTrue(args[0] instanceof String);
		assertTrue(args[1] instanceof Point3D);
		assertTrue(args[2] instanceof A_Thruster.E_Surface);
		assertTrue(args[3] instanceof A_Thruster.E_Orientation);
		assertTrue(args[4] instanceof Percent);
		assertTrue(args[5] instanceof Thrust);
		// Validate values
		assertEquals("Vern", args[0]);
		assertTrue(point3DEquals(new Point3D(0.0, -3.0, 5.0), (Point3D) args[1]));
		assertTrue(args[2] == A_Thruster.E_Surface.FRONT);
		assertTrue(args[3] == A_Thruster.E_Orientation.UPWARD);
		assertTrue(new Percent(77.0).compareTo((Percent) args[4]) == 0);
		assertTrue(new Thrust(100.0).compareTo((Thrust) args[5]) == 0);
	}

	@Test
	void test_CreateStaticConnectorArgumentInterpreter() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		ArgumentInterpreter ai = new ArgumentInterpreter.CreateStaticConnectorArgumentInterpreter();

		// No disconnection or reconnection
		ai.interpret(new String[]{"Connie", "ChildPart", "ParentPart", "8.5", "-9.4", "-1.0", "null", "null"}, null);
		Object[] args = ai.getLastUsedArgs();
		assertTrue(args[0] instanceof String);
		assertTrue(args[1] instanceof String);
		assertTrue(args[2] instanceof String);
		assertTrue(args[3] instanceof Point3D);
		assertTrue(args[4] instanceof Boolean);
		assertTrue(args[5] instanceof Boolean);
		assertEquals("Connie", args[0]);
		assertEquals("ChildPart", args[1]);
		assertEquals("ParentPart", args[2]);
		assertTrue(point3DEquals(new Point3D(8.5, -9.4, -1.0), (Point3D) args[3]));
		assertFalse((boolean) args[4]);
		assertFalse((boolean) args[5]);

		// Disconnection only
		ai.interpret(new String[]{"Connie", "ChildPart", "ParentPart", "8.5", "-9.4", "-1.0", "disconnection", "null"}, null);
		args = ai.getLastUsedArgs();
		assertTrue((boolean) args[4]);
		assertFalse((boolean) args[5]);

		// Reconnection only
		ai.interpret(new String[]{"Connie", "ChildPart", "ParentPart", "8.5", "-9.4", "-1.0", "null", "reconnection"}, null);
		args = ai.getLastUsedArgs();
		assertFalse((boolean) args[4]);
		assertTrue((boolean) args[5]);

		// Disconnection and Reconnection
		ai.interpret(new String[]{"Connie", "ChildPart", "ParentPart", "8.5", "-9.4", "-1.0", "disconnection", "reconnection"}, null);
		args = ai.getLastUsedArgs();
		assertTrue((boolean) args[4]);
		assertTrue((boolean) args[5]);
	}

	@Test
	void test_CreateDynamicConnectorArgumentInterpreter() {

	}

	@Test
	void test_BuildMainThrusterGroupArgumentInterpreter() {

	}

	@Test
	void test_BuildVernierThrusterGroupArgumentInterpreter() {

	}

	@Test
	void test_AddThrusterGroupsArgumentInterpreter() {

	}

	@Test
	void test_FireThrusterArgumentInterpreter() {

	}

	@Test
	void test_ExtendStrutArgumentInterpreter() {

	}

	@Test
	void test_RetractStrutArgumentInterpreter() {

	}

	@Test
	void test_DisconnectStrutArgumentInterpreter() {

	}

	@Test
	void test_ReconnectStrutArgumentInterpreter() {

	}

	@Test
	void test_GeneratePathV1ArgumentInterpreter() {

	}

	@Test
	void test_GeneratePathV2ArgumentInterpreter() {

	}

	@Test
	void test_ForceAttitudeArgumentInterpreter() {

	}

	@Test
	void test_ForceAttitudeRateArgumentInterpreter() {

	}

	@Test
	void test_ForcePositionArgumentInterpreter() {

	}

	@Test
	void test_ForceMotionVectorArgumentInterpreter() {

	}

	@Test
	void test_ConfigClockArgumentInterpreter() {

	}

	@Test
	void test_WaitArgumentInterpreter() {

	}

	@Test
	void test_ScheduleArgumentInterpreter() {

	}

	@Test
	void test_LoadArgumentInterpreter() {

	}

	@Test
	void test_CommitArgumentInterpreter() {

	}

	@Test
	void test_ExitArgumentInterpreter() {

	}

	@Test
	void test_PauseArgumentInterpreter() {

	}

	@Test
	void test_ResumeArgumentInterpreter() {

	}

	@Test
	void test_DumpComponentArgumentInterpreter() {

	}
}
