package s17cs350project.parser;

import s17cs350project.command.A_Command;
import s17cs350project.command.CommandController;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static s17cs350project.parser.ArgumentInterpreter.*;

public enum CommandPattern {
	//Creational Commands
	CreateComponent("^CREATE( ROOT)? COMPONENT (\\S+) SIZE WIDTH (\\S+) HEIGHT (\\S+) DEPTH (\\S+)$", new CreateComponentArgumentInterpreter()),
	CreateMainThrusterV1("^CREATE MAIN THRUSTER (\\S+) AT OFFSET \\( (\\S+) (\\S+) (\\S+) \\) ON SURFACE (\\S+) WITH ORIENTATION (\\S+) USING FIXED THRUST (\\S+) RATE (\\S+)$", new CreateMainThrusterV1ArgumentInterpreter()),
	CreateMainThrusterV2("^CREATE MAIN THRUSTER (\\S+) AT OFFSET \\( (\\S+) (\\S+) (\\S+) \\) ON SURFACE (\\S+) WITH ORIENTATION (\\S+) USING VARIABLE THRUST MIN (\\S+) MAX (\\S+) RATE (\\S+)$", new CreateMainThrusterV2ArgumentInterpreter()),
	CreateVernierThruster("^CREATE VERNIER THRUSTER (\\S+) AT OFFSET \\( (\\S+) (\\S+) (\\S+) \\) ON SURFACE (\\S+) WITH ORIENTATION (\\S+) USING THRUST (\\S+) RATE (\\S+)$", new CreateVernierThrusterArgumentInterpreter()),
	//Structural Commands
	CreateStaticConnector("^CREATE STATIC CONNECTOR (\\S+) FROM (\\S+) TO (\\S+) WITH OFFSET \\( (\\S+) (\\S+) (\\S+) \\)(?: ALLOW (DISCONNECTION)? ?(RECONNECTION)?)?$", new CreateStaticConnectorArgumentInterpreter()),
	CreateDynamicConnector("^CREATE DYNAMIC CONNECTOR (\\S+) FROM (\\S+) TO (\\S+) WITH OFFSET ALPHA \\( (\\S+) (\\S+) (\\S+) \\) BETA \\( (\\S+) (\\S+) (\\S+) \\) EXTENT INITIAL (\\S+) SPEED (\\S+)(?: ALLOW (DISCONNECTION)? ?(RECONNECTION)?)?$", new CreateDynamicConnectorArgumentInterpreter()),
	BuildMainThrusterGroup("^BUILD MAIN THRUSTER GROUP (\\S+) WITH THRUSTERS? (.+)$", new BuildMainThrusterGroupArgumentInterpreter()),
	BuildVernierThrusterGroup("^BUILD VERNIER THRUSTER GROUP (\\S+) WITH THRUSTERS? (.+)$", new BuildVernierThrusterGroupArgumentInterpreter()),
	AddThrusterGroups("^ADD THRUSTER GROUPS? (.+) TO (\\S+)$", new AddThrusterGroupsArgumentInterpreter()),
	//Behavioral Commands
	FireThruster("^FIRE THRUSTER GROUP (\\S+) FOR (\\S+) SECONDS?(?: AT THRUST (\\S+))?$", new FireThrusterArgumentInterpreter()),
	ExtendStrut("^EXTEND STRUT (\\S+)$", new ExtendStrutArgumentInterpreter()),
	RetractStrut("^RETRACT STRUT (\\S+)$", new RetractStrutArgumentInterpreter()),
	DisconnectStrut("^DISCONNECT STRUT (\\S+)$", new DisconnectStrutArgumentInterpreter()),
	ReconnectStrut("^RECONNECT STRUT (\\S+) TO (\\S+)$", new ReconnectStrutArgumentInterpreter()),
	GeneratePathV1("^GENERATE FLIGHT PATH (?:USING \\[ ([+-]) ([XYZ]) ([+-]) ([XYZ]) ([+-]) ([XYZ]) ] )?FROM \\{(.+)} WITH ATTITUDE RATE (\\S+) MOTION RATE (\\S+)$", new GeneratePathV1ArgumentInterpreter()),
	GeneratePathV2("^GENERATE FLIGHT PATH (?:USING \\[ ([+-]) ([XYZ]) ([+-]) ([XYZ]) ([+-]) ([XYZ]) ] )?FROM '(.+)' WITH ATTITUDE RATE (\\S+) MOTION RATE (\\S+)$", new GeneratePathV2ArgumentInterpreter()),
	//Metacommands
	ForceAttitude("^@FORCE ATTITUDE ON (\\S+) TO(?: YAW (\\S+))?(?: PITCH (\\S+))?(?: ROLL (\\S+))?$", new ForceAttitudeArgumentInterpreter()),
	ForceAttitudeRate("^@FORCE ATTITUDE RATE ON (\\S+) TO(?: YAW (\\S+))?(?: PITCH (\\S+))?(?: ROLL (\\S+))?$", new ForceAttitudeRateArgumentInterpreter()),
	ForcePosition("^@FORCE POSITION ON (\\S+) TO \\( (\\S+) (\\S+) (\\S+) \\)$", new ForcePositionArgumentInterpreter()),
	ForceMotionVector("^@FORCE MOTION VECTOR ON (\\S+) TO \\[ (\\S+) (\\S+) (\\S+) (\\S+) ]$", new ForceMotionVectorArgumentInterpreter()),
	ConfigClock("^@CONFIG CLOCK (\\S+) (\\S+)$", new ConfigClockArgumentInterpreter()),
	Wait("^@WAIT (\\S+)$", new WaitArgumentInterpreter()),
	Schedule("^@SCHEDULE (\\S+) < .* >$", new ScheduleArgumentInterpreter()),
	Load("^@LOAD '(.+)'$", new LoadArgumentInterpreter()),
	Commit("^@COMMIT$", new CommitArgumentInterpreter()),
	Exit("^@EXIT$", new ExitArgumentInterpreter()),
	Pause("^@PAUSE$", new PauseArgumentInterpreter()),
	Resume("^@RESUME$", new ResumeArgumentInterpreter()),
	DumpComponent("^@DUMP COMPONENT (\\S+) ?(?:'(.*)')?$", new DumpComponentArgumentInterpreter());

	private Pattern pattern;
	private Matcher matcher;
	private ArgumentInterpreter interpreter;
	private static A_Command lastExecutedCommand;
	private static ArrayList<String[]> lastUsedArgs = new ArrayList<>();
	private static final boolean TESTMODE = true;

	/**
	 * @param regex       Regex Pattern to use
	 * @param interpreter ArgumentInterpreter for mapping the arguments and executing the command
	 */
	CommandPattern(String regex, ArgumentInterpreter interpreter) {
		pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		this.interpreter = interpreter;
	}

	public static A_Command getLastExecutedCommand() {
		return lastExecutedCommand;
	}

	public static String[][] getLastUsedArgs() {
		return lastUsedArgs.toArray(new String[lastUsedArgs.size()][]);
	}

	/**
	 * Interprets the command and if possible executes it using the controller
	 *
	 * @param command    command to be interpreted
	 * @param controller controller used to execute command
	 * @return if command was executed
	 */
	public boolean interpret(String command, CommandController controller) {
		if (matches(command)) {
			interpreter.interpret(getArgs(), controller);
			if (TESTMODE) lastExecutedCommand = interpreter.getLastExecutedCommand();
			return true;
		}
		return false;
	}

	/**
	 * Checks if this instances pattern matches the command
	 *
	 * @param command the command to be checked
	 * @return true if whole command matches pattern exactly
	 */
	private boolean matches(String command) {
		matcher = pattern.matcher(command);
		return matcher.matches();
	}

	/**
	 * Adds all the capture groups from the regex statement to a string array
	 *
	 * @return array of args as strings
	 */
	private String[] getArgs() {
		String[] args = new String[matcher.groupCount()];
		for (int i = 1; i <= args.length; i++) {
			String grp = matcher.group(i);
			if (grp != null) {
				grp = grp.trim();
			}else{
				grp = "";
			}
			args[i - 1] = grp;
		}
		if (TESTMODE) lastUsedArgs.add(args);
		return args;
	}
}
