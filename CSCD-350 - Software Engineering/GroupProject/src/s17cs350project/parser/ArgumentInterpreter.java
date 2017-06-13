package s17cs350project.parser;

import s17cs350project.command.*;
import s17cs350project.command.CommandGeneratePath.E_Axis;
import s17cs350project.component.thruster.A_Thruster.E_Orientation;
import s17cs350project.component.thruster.A_Thruster.E_Surface;
import s17cs350project.datatype.*;
import s17cs350project.datatype.Attitude.E_Mask;
import s17cs350project.support.ProjectException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static s17cs350project.command.CommandGeneratePath.E_Axis.*;

public abstract class ArgumentInterpreter {
	private static A_Command lastExecutedCommand;
	private static Object[] a;

	// patterns for validating arguments
	private static final Pattern patternID = Pattern.compile("^[a-zA-Z0-9]+$");
	private static final Pattern patternNumber = Pattern.compile("^([+-])?[0-9]+(\\.[0-9]+)?$");
	private static final Pattern patternPoints = Pattern.compile("^(\\s*\\[ [0-9.+-]+ [0-9.+-]+ [0-9.+-]+ ]\\s*){2,}$");


	static void executeCommand(A_Command command, CommandController controller) {
		lastExecutedCommand = command;
		controller.schedule(command);
	}

	/**
	 * Interprets the args and executes the command using the controller
	 *
	 * @param args       arguments to be interpreted
	 * @param controller controller to bind command to
	 */
	abstract public void interpret(String[] args, CommandController controller);

	public A_Command getLastExecutedCommand() {
		return lastExecutedCommand;
	}

	public Object[] getLastUsedArgs() {
		return a;
	}

	// checks if string is a valid id field
	private static String validID(String id) {
		Matcher matcher = patternID.matcher(id);
		if (!matcher.matches()) {
			throw new ProjectException(String.format("Invalid ID \"%s\"", id));
		}
		return id;
	}

	// check if string is a valid number field
	private static String validNumber(String number, boolean allowWildcard) {
		if (allowWildcard && number.equals("_")) return number;
		Matcher matcher = patternNumber.matcher(number);
		if (!matcher.matches()) {
			throw new ProjectException(String.format("Invalid number field \"%s\"", number));
		}

		return number;
	}

	// check if string is a valid list of points
	private static String validPoints(String points) {
		Matcher matcher = patternPoints.matcher(points);
		if (!matcher.matches()) {
			throw new ProjectException(String.format("Invalid points list \"%s\"", points));
		}

		return points;
	}

	private static Vector Vector(String[] args, int startIX, boolean allowWildcard) {
		// parse values
		Double[] attitudeValues = parseNDoubles(args, startIX, 3, allowWildcard);
		Double magnitude = Double(validNumber(args[startIX+3], false));
		// determine mask
		Attitude.E_Mask attiudeMask = Attitude.generateMask(!Double.isNaN(attitudeValues[0]), !Double.isNaN(attitudeValues[1]), !Double.isNaN(attitudeValues[2]));

		// determine magnitude
		Attitude attitude = new Attitude(attitudeValues[0], attitudeValues[1], attitudeValues[2], attiudeMask);

		return new Vector(attitude, magnitude);
	}

	private static Attitude Attitude(String[] args, int startIX) {
		double[] att = new double[3];
		boolean[] has = new boolean[3];
		for (int i = 0; i < 3; i++) {
			if (args[startIX + i].equals("")) has[i] = true;
			else att[i] = Double.parseDouble(args[startIX + i]);
		}
		E_Mask mask = Attitude.generateMask(!has[0], !has[1], !has[2]);
		return new Attitude(att[0], att[1], att[2], mask);
	}

	// Dimension3D from string args
	private static Dimension3D Dimension3D(String[] args, int startIX) {
		Double[] d = parseNDoubles(args, startIX, 3, false);
		return new Dimension3D(d[0], d[1], d[2]);
	}

	// Point3D from string args
	private static Point3D Point3D(int startIX, boolean allowWildcard, String... args) {
		Double[] d = parseNDoubles(args, startIX, 3, allowWildcard);
		// determine mask
		Point3D.E_Mask mask = Point3D.generateMask(!Double.isNaN(d[0]), !Double.isNaN(d[1]), !Double.isNaN(d[2]));
		return new Point3D(d[0], d[1], d[2], mask);
	}

	// list of Point3D from string args
	private static List<Point3D> Point3Ds(String s) {
		List<Point3D> points = new ArrayList<>();
		String[] ps = s.split(" ");
		for (int i = 1; i < ps.length;) {
			points.add(Point3D(i, false, ps));
			i += 5;
		}

		return points;
	}

	private static void fillE_Axis(String[] args) {
		if (!args[0].equals("")) {
			a[0] = E_Axis(args[0], args[1]);
			a[1] = E_Axis(args[2], args[3]);
			a[2] = E_Axis(args[4], args[5]);
		}
	}

	private static E_Axis E_Axis(String di, String ax) {
		ax += "_";
		if (di.equals("+"))
			return E_Axis.valueOf(ax + "PLUS");
		return E_Axis.valueOf(ax + "MINUS");
	}

	private static E_Orientation E_Orientation(String s) {
		return E_Orientation.valueOf(s.toUpperCase());
	}

	private static E_Surface E_Surface(String s) {
		return E_Surface.valueOf(s.toUpperCase());
	}

	private static Thrust Thrust(String s) {
		return new Thrust(Double.parseDouble(validNumber(s, false)));
	}

	private static Percent Percent(String s) {
		return new Percent(Double.parseDouble(validNumber(s, false)));
	}

	private static Time Time(String s) {
		return new Time(Double.parseDouble(validNumber(s, false)));
	}

	private static void allow(String[] args, int inputIX, Object[] a, int outputIX) {
		a[outputIX] = args[inputIX].equalsIgnoreCase("DISCONNECTION");
		a[outputIX + 1] = args[inputIX + 1].equalsIgnoreCase("RECONNECTION");
	}

	private static Double[] parseNDoubles(String[] args, int startIX, int n, boolean allowWildcard) {
		ArrayList<Double> temp = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			if (allowWildcard && args[startIX + i].equals("_"))
				temp.add(Double.NaN);
			else
				temp.add(Double(validNumber(args[startIX + i], allowWildcard)));
		}
		return temp.toArray(new Double[n]);
	}

	private static double Double(String s) {
		return Double.parseDouble(s);
	}

	//TODO: Write more argument Interpreters.
	// Make sure you've used all possible constructors for the command,
	// I might have missed some when I stubbed this out.

	// create component
	public static class CreateComponentArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			a = new Object[]{validID(args[1]), Dimension3D(args, 2), false};
			a[2] = args[0].equalsIgnoreCase("ROOT");
			executeCommand(new CommandCreateComponent((String) a[0], (Dimension3D) a[1], (boolean) a[2]), controller);
		}
	}

	// create main thruster (fixed)
	public static class CreateMainThrusterV1ArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(String id, Point3D position, E_Surface surface, E_Orientation orientation, Percent rate, Thrust thrust)
			//CREATE MAIN THRUSTER id AT OFFSET ( x y z ) ON SURFACE surface WITH ORIENTATION orientation USING FIXED THRUST thrust RATE percent
			a = new Object[]{validID(args[0]), Point3D(1, false, args), E_Surface(args[4]), E_Orientation(args[5]), Percent(args[7]), Thrust(args[6])};
			executeCommand(new CommandCreateMainThruster((String) a[0], (Point3D) a[1], (E_Surface) a[2], (E_Orientation) a[3], (Percent) a[4], (Thrust) a[5]), controller);
		}
	}

	// create main thruster (variable)
	public static class CreateMainThrusterV2ArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//String id, Point3D position, E_Surface surface, E_Orientation orientation, Percent rate, Thrust thrustMin, Thrust thrustMax)
			//CREATE MAIN THRUSTER id AT OFFSET ( x y z ) ON SURFACE surface WITH ORIENTATION orientation USING VARIABLE THRUST MIN t1 MAX t2 RATE percent1
			a = new Object[]{validID(args[0]), Point3D(1, false, args), E_Surface(args[4]), E_Orientation(args[5]), Percent(args[8]), Thrust(args[6]), Thrust(args[7])};
			executeCommand(new CommandCreateMainThruster((String) a[0], (Point3D) a[1], (E_Surface) a[2], (E_Orientation) a[3], (Percent) a[4], (Thrust) a[5], (Thrust) a[6]), controller);
		}
	}

	// create vernier thruster
	public static class CreateVernierThrusterArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(String id, Point3D position, E_Surface surface, E_Orientation orientation, Percent rate, Thrust thrust)
			//CREATE VERNIER THRUSTER id AT OFFSET ( x y z ) ON SURFACE surface WITH ORIENTATION orientation USING THRUST thrust RATE percent
			a = new Object[]{validID(args[0]), Point3D(1, false, args), E_Surface(args[4]), E_Orientation(args[5]), Percent(args[7]), Thrust(args[6])};
			executeCommand(new CommandCreateVernierThruster((String) a[0], (Point3D) a[1], (E_Surface) a[2], (E_Orientation) a[3], (Percent) a[4], (Thrust) a[5]), controller);
		}
	}

	//Structural Commands

	// create static connector
	public static class CreateStaticConnectorArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(String id, String idChild, String idParent, Point3D offset, boolean isDisconnectable, boolean isReconnectable)
			//CREATE STATIC CONNECTOR id1 FROM id2 TO id3 WITH OFFSET ( x y z )
			//CREATE STATIC CONNECTOR id1 FROM id2 TO id3 WITH OFFSET ( x y z ) ALLOW disconnection
			//CREATE STATIC CONNECTOR id1 FROM id2 TO id3 WITH OFFSET ( x y z ) ALLOW reconnection
			//CREATE STATIC CONNECTOR id1 FROM id2 TO id3 WITH OFFSET ( x y z ) ALLOW disconnection reconnection
			a = new Object[]{validID(args[0]), validID(args[1]), validID(args[2]), Point3D(3, false, args), false, false};
			allow(args, 6, a, 4);
			executeCommand(new CommandCreateStaticConnector((String) a[0], (String) a[1], (String) a[2], (Point3D) a[3], (Boolean) a[4], (Boolean) a[5]), controller);
		}
	}

	// create dynamic connector
	public static class CreateDynamicConnectorArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(String id, String idChild, String idParent, Point3D offsetAlpha, Point3D offsetBeta, Percent extentInitial, Percent extentSpeed, boolean isDisconnectable, boolean isReconnectable)
			//CREATE DYNAMIC CONNECTOR id1 FROM id2 TO id3 WITH OFFSET ALPHA ( x y z ) BETA ( x y z ) EXTENT INITIAL percent1 SPEED percent2
			//CREATE DYNAMIC CONNECTOR id1 FROM id2 TO id3 WITH OFFSET ALPHA ( x y z ) BETA ( x y z ) EXTENT INITIAL percent1 SPEED percent2 ALLOW reconnection
			//CREATE DYNAMIC CONNECTOR id1 FROM id2 TO id3 WITH OFFSET ALPHA ( x y z ) BETA ( x y z ) EXTENT INITIAL percent1 SPEED percent2 ALLOW disconnection reconnection
			a = new Object[]{validID(args[0]), validID(args[1]), validID(args[2]), Point3D(3, false, args), Point3D(6, false, args), Percent(args[9]), Percent(args[10]), false, false};
			allow(args, 11, a, 7);
			executeCommand(new CommandCreateDynamicConnector((String) a[0], (String) a[1], (String) a[2], (Point3D) a[3], (Point3D) a[4], (Percent) a[5], (Percent) a[6], (Boolean) a[7], (Boolean) a[8]), controller);
		}
	}

	// create main thruster group
	public static class BuildMainThrusterGroupArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(String groupID, List<String> thrusterIDs)
			a = new Object[]{validID(args[0]), Arrays.asList(args[1].split(" "))};

			// validate list of thruster IDs
			for (String tID : (List<String>) a[1]) {
				validID(tID);
			}

			executeCommand(new CommandBuildMainThrusterGroup((String) a[0], (List<String>) a[1]), controller);
		}
	}

	// create vernier thruster group
	public static class BuildVernierThrusterGroupArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(String groupID, List<String> thrusterIDs)
			a = new Object[]{validID(args[0]), Arrays.asList(args[1].split(" "))};

			// validate list of thruster IDs
			for (String tID : (List<String>) a[1]) {
				validID(tID);
			}

			executeCommand(new CommandBuildVernierThrusterGroup((String) a[0], (List<String>) a[1]), controller);
		}
	}

	// add thruster group
	public static class AddThrusterGroupsArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(List<String> groupIDs, String componentID)
			a = new Object[]{Arrays.asList(args[0].split(" ")), validID(args[1])};

			// validate list of group IDs
			for (String gID : (List<String>) a[0]) {
				validID(gID);
			}

			executeCommand(new CommandAddThrusterGroups((List<String>) a[0], (String) a[1]), controller);
		}
	}

	//Behavioral Commands

	// fire thruster
	public static class FireThrusterArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(String id, Time time)
			//(String id, Time time, Thrust thrust)
			if (args[2].equals("")) {
				a = new Object[]{validID(args[0]), Time(args[1])};
				executeCommand(new CommandFireThruster((String) a[0], (Time) a[1]), controller);
			} else {
				a = new Object[]{validID(args[0]), Time(args[1]), Thrust(args[2])};
				executeCommand(new CommandFireThruster((String) a[0], (Time) a[1], (Thrust) a[2]), controller);
			}
		}
	}

	// extend strut
	public static class ExtendStrutArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(String id)
			a = new Object[]{validID(args[0])};
			executeCommand(new CommandExtendStrut((String) a[0]), controller);
		}
	}

	// retract strut
	public static class RetractStrutArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(String id)
			a = new Object[]{validID(args[0])};
			executeCommand(new CommandRetractStrut(args[0]), controller);
		}
	}

	// disconnect strut
	public static class DisconnectStrutArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(String id)
			a = new Object[]{validID(args[0])};
			executeCommand(new CommandDisconnectStrut(args[0]), controller);
		}
	}

	// reconnect strut
	public static class ReconnectStrutArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(String idStrut, String idParent)
			a = new Object[]{validID(args[0]), validID(args[1])};
			executeCommand(new CommandReconnectStrut((String) a[0], (String) a[1]), controller);
		}
	}

	// generate path points
	public static class GeneratePathV1ArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(E_Axis axis1, E_Axis axis2, E_Axis axis3, List<Point3D> waypoints, double attitudeRate, double motionRate)
			//^GENERATE FLIGHT PATH USING \[ ([+-]) ([XYZ]) ([+-]) ([XYZ]) ([+-]) ([XYZ]) ] FROM \{(.+)} WITH ATTITUDE RATE (\S+) MOTION RATE (\S+)$
			//GENERATE FLIGHT PATH USING [ + X + Y + Z] FROM { [ 1 1 1] [ 1 2 3] } WITH ATTITUDE RATE 2 MOTION RATE 2
			a = new Object[]{X_PLUS, Y_PLUS, Z_PLUS, Point3Ds(validPoints(args[6])), Double(args[7]), Double(args[8])};
			fillE_Axis(args);
			if (((List<Point3D>)a[3]).size() < 2) {
				throw new ProjectException("Not enough points provided");
			}

			executeCommand(new CommandGeneratePath((E_Axis) a[0], (E_Axis) a[1], (E_Axis) a[2], (List<Point3D>) a[3], (Double) a[4], (Double) a[5]), controller);
		}
	}

	// generate path filename
	public static class GeneratePathV2ArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(E_Axis axis1, E_Axis axis2, E_Axis axis3, String filename, double attitudeRate, double motionRate)
			a = new Object[]{X_PLUS, Y_PLUS, Z_PLUS, args[6], Double(args[7]), Double(args[8])};
			fillE_Axis(args);
			executeCommand(new CommandGeneratePath((E_Axis) a[0], (E_Axis) a[1], (E_Axis) a[2], (String) a[3], (Double) a[4], (Double) a[5]), controller);
		}
	}
	//Metacommands

	// force attitude
	public static class ForceAttitudeArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(String id, Attitude attitude)
			a = new Object[]{validID(args[0]), Attitude(args, 1)};
			executeCommand(new CommandMetaForceAttitude((String) a[0], (Attitude) a[1]), controller);
		}
	}

	// force attitude rate
	public static class ForceAttitudeRateArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(String id, Attitude attitudeRate)
			a = new Object[]{validID(args[0]), Attitude(args, 1)};
			executeCommand(new CommandMetaForceAttitudeRate((String) a[0], (Attitude) a[1]), controller);
		}
	}

	// force position
	public static class ForcePositionArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(String id, Point3D position)
			a = new Object[]{validID(args[0]), Point3D(1, true, args)};
			executeCommand(new CommandMetaForcePosition((String) a[0], (Point3D) a[1]), controller);
		}
	}

	// force motion
	public static class ForceMotionVectorArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(String id, Vector vector)
			a = new Object[]{validID(args[0]), Vector(args, 1, true)};
			executeCommand(new CommandMetaForceMotionVector((String) a[0], (Vector) a[1]), controller);
		}
	}

	// config clock
	public static class ConfigClockArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(Time rate, Time step)
			a = new Object[]{Time(args[0]), Time(args[1])};
			executeCommand(new CommandMetaConfigClock((Time) a[0], (Time) a[1]), controller);
		}
	}

	// wait
	public static class WaitArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(Time deltaTime)
			a = new Object[]{Time(args[0])};
			executeCommand(new CommandMetaWait((Time) a[0]), controller);
		}
	}

	// schedule (not needed)
	public static class ScheduleArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(Time absoluteTime, A_Command command)
			throw new UnsupportedOperationException("@schedule not supported");
		}
	}

	// load argument
	public static class LoadArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(File commandFile)
			a = new Object[]{new File(args[0])};
			executeCommand(new CommandMetaLoad(controller, (File) a[0]), controller);
		}
	}

	// commit
	public static class CommitArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			executeCommand(new CommandMetaCommit(), controller);
		}
	}

	// exit
	public static class ExitArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			executeCommand(new CommandMetaExit(), controller);
		}
	}

	// pause
	public static class PauseArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			executeCommand(new CommandMetaPause(), controller);
		}
	}

	// resume
	public static class ResumeArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			executeCommand(new CommandMetaResume(), controller);
		}
	}

	// dump
	public static class DumpComponentArgumentInterpreter extends ArgumentInterpreter {
		@Override
		public void interpret(String[] args, CommandController controller) {
			//(String id)
			a = new Object[]{validID(args[0]), args[1]};
			A_Command command;
			if (a[1].equals(""))
				command = new CommandMetaDumpComponent((String) a[0]);
			else
				command = new CommandMetaDumpComponent((String) a[0], (String) a[1]);
			executeCommand(command, controller);
		}
	}
}

