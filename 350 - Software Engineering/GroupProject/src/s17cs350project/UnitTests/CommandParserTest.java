package s17cs350project.UnitTests;

import org.junit.jupiter.api.Test;
import s17cs350project.command.*;
import s17cs350project.datatype.*;
import s17cs350project.parser.CommandParser;
import s17cs350project.tests.DumbyCommandController;
import s17cs350project.tests.DumbyProjectEngine;
import s17cs350project.tests.DumbyProjectServer;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static s17cs350project.command.CommandGeneratePath.E_Axis.*;
import static s17cs350project.component.thruster.A_Thruster.E_Orientation.*;
import static s17cs350project.component.thruster.A_Thruster.E_Surface.*;
import static s17cs350project.datatype.Attitude.E_Mask.*;
import static s17cs350project.datatype.Point3D.E_Mask.*;

// test dumby controller
class CommandParserTest {
	// value for allowed double delta
	private final double validDelta = 0.0000001;

	// test for illegal arguments to parser
	@Test
	void testIllegalCtr() {
		// null
		assertThrows(RuntimeException.class, () -> new CommandParser(null, null));
		assertThrows(RuntimeException.class, () -> new CommandParser(getTestController(), null));
		assertThrows(RuntimeException.class, () -> new CommandParser(null, "@LOAD 'hello.txt'\n@EXIT"));
	}

	// test that empty commands throw issues
	// TODO: validate if this is necessary
	@Test
	void testIllegalEmpty() {
		validateParseThrows(RuntimeException.class, "");
		validateParseThrows(RuntimeException.class, "\n\n\n\n\n");
		validateParseThrows(RuntimeException.class, "\n     \n   \n\n");
		validateParseThrows(RuntimeException.class, "\n@PAUSE\n\n");
		validateParseThrows(RuntimeException.class, "@PAUSE\n \n");
		validateParseThrows(RuntimeException.class, "\n \n@PAUSE");
	}

	// Test dumby controller and helper methods for testing
	@Test
	void TestController() {
		// build list of commands
		String commands = "";
		commands += "CREATE ROOT COMPONENT a SIZE WIDTH 1 HEIGHT 1 DEPTH 1\n";
		commands += "CREATE COMPONENT b SIZE WIDTH 2 HEIGHT 2 DEPTH 2\n";
		commands += "CREATE MAIN THRUSTER at AT OFFSET ( 0 0 1 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING FIXED THRUST 1 RATE 5\n";
		commands += "CREATE MAIN THRUSTER bt AT OFFSET ( 0 0 -2 ) ON SURFACE BOTTOM WITH ORIENTATION DOWNWARD USING VARIABLE THRUST MIN 1 MAX 100 RATE 100\n";
		// build dumby controller and parser
		DumbyCommandController controller = getTestController();
		CommandParser parser = new CommandParser(controller, commands);
		parser.parse();

		// validate list of commands from parser
		validateCommandsList(controller.getCommands(), Arrays.asList(
				new CommandCreateComponent("a", new Dimension3D(1, 1, 1), true),
				new CommandCreateComponent("b", new Dimension3D(2, 2, 2), false),
				new CommandCreateMainThruster("at", new Point3D(0, 0, 1), TOP, UPWARD, new Percent(5), new Thrust(1)),
				new CommandCreateMainThruster("bt", new Point3D(0, 0, -2), BOTTOM, DOWNWARD, new Percent(100), new Thrust(1), new Thrust(100))
		));

	}

	//  create component
	@Test
	void testCreateComponent() {
		// root
		validateParseCommand("CREATE ROOT COMPONENT abc SIZE WIDTH 1 HEIGHT 2 DEPTH 3", new CommandCreateComponent("abc", new Dimension3D(1, 2, 3), true));
		validateParseCommand("CREATE RooT COMPONENT aasdwasdwaebc SIZE WIDTH 1.5 HEIGHT 2.5 DEPTH 99.9", new CommandCreateComponent("aasdwasdwaebc", new Dimension3D(1.5, 2.5, 99.9), true));
		validateParseCommand("CREATE rOOt COMPONENT WEASD SIZE WIDTH 0.99 HEIGHT 0.1 DEPTH 100", new CommandCreateComponent("WEASD", new Dimension3D(0.99, 0.1, 100), true));

		// non root
		validateParseCommand("CREATE COMPONENT ROOT SIZE WIDTH 1 HEIGHT 2 DEPTH 3", new CommandCreateComponent("ROOT", new Dimension3D(1, 2, 3), false));
		validateParseCommand("CREATE COMPONENT ROOTab SIZE WIDTH 1.5 HEIGHT 2.5 DEPTH 99.9", new CommandCreateComponent("ROOTab", new Dimension3D(1.5, 2.5, 99.9), false));
		validateParseCommand("CREATE COMPONENT WaZROOT SIZE WIDTH 0.99 HEIGHT 1 DEPTH 100", new CommandCreateComponent("WaZROOT", new Dimension3D(0.99, 1, 100), false));

		// test number values
		validateParseCommand("CREATE COMPONENT BASD SIZE WIDTH 1 HEIGHT 1232 DEPTH 123.321", new CommandCreateComponent("BASD", new Dimension3D(1, 1232, 123.321), false));
		validateParseCommand("CREATE COMPONENT BASD SIZE WIDTH 999999 HEIGHT 0.99999 DEPTH 478542.32547", new CommandCreateComponent("BASD", new Dimension3D(999999, 0.99999, 478542.32547), false));
		// test '+' allowed
		validateParseCommand("CREATE COMPONENT A1 SIZE WIDTH 2 HEIGHT 4 DEPTH 8", new CommandCreateComponent("A1", new Dimension3D(2, 4, 8), false));
		validateParseCommand("CREATE COMPONENT A1 SIZE WIDTH +2 HEIGHT 4 DEPTH 8", new CommandCreateComponent("A1", new Dimension3D(2, 4, 8), false));
		validateParseCommand("CREATE COMPONENT A1 SIZE WIDTH 2 HEIGHT +4 DEPTH 8", new CommandCreateComponent("A1", new Dimension3D(2, 4, 8), false));
		validateParseCommand("CREATE COMPONENT A1 SIZE WIDTH 2 HEIGHT 4 DEPTH +8", new CommandCreateComponent("A1", new Dimension3D(2, 4, 8), false));
		validateParseCommand("CREATE COMPONENT A1 SIZE WIDTH +2 HEIGHT +4 DEPTH +8", new CommandCreateComponent("A1", new Dimension3D(2, 4, 8), false));
		validateParseCommand("CREATE COMPONENT A1 SIZE WIDTH +2 HEIGHT +4 DEPTH 8", new CommandCreateComponent("A1", new Dimension3D(2, 4, 8), false));
		validateParseCommand("CREATE COMPONENT A1 SIZE WIDTH +2 HEIGHT 4 DEPTH +8", new CommandCreateComponent("A1", new Dimension3D(2, 4, 8), false));
		validateParseCommand("CREATE COMPONENT A1 SIZE WIDTH 2 HEIGHT +4 DEPTH +8", new CommandCreateComponent("A1", new Dimension3D(2, 4, 8), false));
		validateParseCommand("CREATE COMPONENT A1 SIZE WIDTH +2 HEIGHT 4 DEPTH +8", new CommandCreateComponent("A1", new Dimension3D(2, 4, 8), false));
	}

	// illegal create component
	@Test
	void testIllegalCreateComponent() {
		// close
		validateParseThrows(RuntimeException.class, "CREATE BASD COMPONENT SIZE WIDTH 1 HEIGHT 1232 DEPTH 123.321");
		validateParseThrows(RuntimeException.class, "CREATE ROOT COMPONENT BASD SIZE 1 HEIGHT 1232 DEPTH 123.321");
		validateParseThrows(RuntimeException.class, "CREATE ROOT COMPONENT BASD SIZE WIDTH 1 1232 DEPTH 123.321");
		validateParseThrows(RuntimeException.class, "CREATE ROOT COMPONENT BASD SIZE WIDTH 1 HEIGHT 1232 123.321");
		validateParseThrows(RuntimeException.class, "CREATE ROOT COMPONENT BASD WIDTH 1 HEIGHT 2 DEPTH 3");
		validateParseThrows(RuntimeException.class, "ROOT COMPONENT BASD SIZE WIDTH 1 HEIGHT 2 DEPTH 3");
		validateParseThrows(RuntimeException.class, "CREATE ROOT BASD SIZE WIDTH 1 HEIGHT 2 DEPTH 3");

		// bad id
		validateParseThrows(RuntimeException.class, "CREATE ROOT COMPONENT awda awd SIZE WIDTH 1 HEIGHT 2 DEPTH 3");
		validateParseThrows(RuntimeException.class, "CREATE ROOT COMPONENT asdSIZE WIDTH 1 HEIGHT 2 DEPTH 3");
		validateParseThrows(RuntimeException.class, "CREATE ROOT COMPONENTROOT SIZE WIDTH 1 HEIGHT 2 DEPTH 3");
		validateParseThrows(RuntimeException.class, "CREATE ROOT COMPONENT RO[T SIZE WIDTH 1 HEIGHT 2 DEPTH 3");
		validateParseThrows(RuntimeException.class, "CREATE ROOT COMPONENT RO|T SIZE WIDTH 1 HEIGHT 2 DEPTH 3");

		// bad number fields
		validateParseThrows(RuntimeException.class, "CREATE COMPONENT A SIZE WIDTH 0 HEIGHT 2 DEPTH 3");
		validateParseThrows(RuntimeException.class, "CREATE COMPONENT A SIZE WIDTH 1 HEIGHT 0 DEPTH 3");
		validateParseThrows(RuntimeException.class, "CREATE COMPONENT A SIZE WIDTH 1 HEIGHT 2 DEPTH 0");
		validateParseThrows(RuntimeException.class, "CREATE COMPONENT A SIZE WIDTH -1 HEIGHT 2 DEPTH 3");
		validateParseThrows(RuntimeException.class, "CREATE COMPONENT A SIZE WIDTH -1 HEIGHT -2 DEPTH 3");
		validateParseThrows(RuntimeException.class, "CREATE COMPONENT A SIZE WIDTH -1 HEIGHT -2 DEPTH -3");
		validateParseThrows(RuntimeException.class, "CREATE COMPONENT A SIZE WIDTH -1 HEIGHT 2 DEPTH -3");
		validateParseThrows(RuntimeException.class, "CREATE COMPONENT A SIZE WIDTH 1 HEIGHT 2 DEPTH -3");
		validateParseThrows(RuntimeException.class, "CREATE COMPONENT A SIZE WIDTH 1 HEIGHT -2 DEPTH -3");
		validateParseThrows(RuntimeException.class, "CREATE COMPONENT A SIZE WIDTH 1 HEIGHT -2 DEPTH 3");

	}

	// 2. Create main thruster (fixed thrust)
	@Test
	void testCreateMainThrusterFixed() {
		// general
		validateParseCommand("CREATE MAIN THRUSTER A AT OFFSET ( 1 2 3 ) ON SURFACE FRONT WITH ORIENTATION FORWARD USING FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("A", new Point3D(1, 2, 3), FRONT, FORWARD, new Percent(1), new Thrust(1)));
		validateParseCommand("CREATE main THRUSTER a AT OFFSET ( 15.3 2587.8 3 ) ON SURFACE LEFT WITH ORIENTATION UPWARD USING FIXED THRUST 75.99 RATE 0.99",
				new CommandCreateMainThruster("a", new Point3D(15.3, 2587.8, 3), LEFT, UPWARD, new Percent(0.99), new Thrust(75.99)));
		validateParseCommand("CREATE MAIN THRUstER bcAWD AT OffSET ( 1 2 3.366 ) ON surface BACK WITH ORIeNTaTION LEFTWARD UsinG FIXED THrUST 95.9 RATE 19.85",
				new CommandCreateMainThruster("bcAWD", new Point3D(1, 2, 3.366), BACK, LEFTWARD, new Percent(19.85), new Thrust(95.9)));

		// id
		validateParseCommand("CREATE MAIN THRUSTER AbCED AT OFFSET ( 1 2 3 ) ON SURFACE FRONT WITH ORIENTATION FORWARD USING FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("AbCED", new Point3D(1, 2, 3), FRONT, FORWARD, new Percent(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THRUSTER AT AT OFFSET ( 1 2 3 ) ON SURFACE FRONT WITH ORIENTATION FORWARD USING FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("AT", new Point3D(1, 2, 3), FRONT, FORWARD, new Percent(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THRUSTER THRUSTER AT OFFSET ( 1 2 3 ) ON SURFACE FRONT WITH ORIENTATION FORWARD USING FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("THRUSTER", new Point3D(1, 2, 3), FRONT, FORWARD, new Percent(1), new Thrust(1)));

		// offset numbers
		validateParseCommand("CREATE MAIN THRUSTER ab AT OFFSET ( 9 6 3 ) ON SURFACE FRONT WITH ORIENTATION FORWARD USING FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("ab", new Point3D(9, 6, 3), FRONT, FORWARD, new Percent(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THRUSTER ab AT OFFSET ( 0 0 0 ) ON SURFACE FRONT WITH ORIENTATION FORWARD USING FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("ab", new Point3D(0, 0, 0), FRONT, FORWARD, new Percent(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THRUSTER ab AT OFFSET ( +0 -0 -0 ) ON SURFACE FRONT WITH ORIENTATION FORWARD USING FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("ab", new Point3D(0, 0, 0), FRONT, FORWARD, new Percent(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THRUSTER ab AT OFFSET ( -58.9 +25 +253.954 ) ON SURFACE FRONT WITH ORIENTATION FORWARD USING FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("ab", new Point3D(-58.9, 25, 253.954), FRONT, FORWARD, new Percent(1), new Thrust(1)));

		// surface
		validateParseCommand("CREATE main THRUSTER asd AT offset ( 0 1 2 ) ON SURFACE FrONT WITH ORIENTATION FORWARD USING FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), FRONT, FORWARD, new Percent(1), new Thrust(1)));
		validateParseCommand("create MAIN THRUSTER asd AT OFFSET ( 0 1 2 ) ON SURFACE LeFt WITH ORIENTATION FORWARD USING FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), LEFT, FORWARD, new Percent(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN thruSTER asd at OFFSET ( 0 1 2 ) ON SURFACE BAck WITH ORIENTATION FORWARD USING FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), BACK, FORWARD, new Percent(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THRUSTER asd AT OFFSET ( 0 1 2 ) ON SURFACE BOtTOM WITH ORIENtaTION FORWARD USING FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), BOTTOM, FORWARD, new Percent(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THRUSTER asd AT OFFSET ( 0 1 2 ) ON SURFACE RiGHT WITH ORIENTATION FORWARD USING FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), RIGHT, FORWARD, new Percent(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THRUSTER asd AT OFFSET ( 0 1 2 ) ON SuRFaCE tOP WITH ORIENTATION FORWARD USING FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), TOP, FORWARD, new Percent(1), new Thrust(1)));

		// orientation
		validateParseCommand("CREATE MAIN THRUSTER asd AT OFFSET ( 0 1 2 ) ON SURFACE FRONT WITH ORIENTATION FORWARD using FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), FRONT, FORWARD, new Percent(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THruSTER asd AT OFFSET ( 0 1 2 ) oN surface FRONT WITH ORIENTATION BACKWARD using FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), FRONT, BACKWARD, new Percent(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THruSTER asd AT OFFSET ( 0 1 2 ) oN surface FRONT WITH ORIENTATION upWARD using FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THruSTER asd AT OFFSET ( 0 1 2 ) oN surface FRONT WITH ORIENTATION DOWNward using FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), FRONT, DOWNWARD, new Percent(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THruSTER asd AT OFFSET ( 0 1 2 ) oN surface FRONT WITH orientation LeFTwARD using FIXED THRUST 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), FRONT, LEFTWARD, new Percent(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THruSTER asd AT OFFSET ( 0 1 2 ) oN surface front with orientation rightWard using fixed THRUST 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), FRONT, RIGHTWARD, new Percent(1), new Thrust(1)));

		// thrust
		validateParseCommand("create main thruster 2a31wd at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 0",
				new CommandCreateMainThruster("2a31wd", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("create main thruster 2a31wd at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 100",
				new CommandCreateMainThruster("2a31wd", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(100), new Thrust(0)));
		validateParseCommand("create main thruster 2a31wd at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 0.9985",
				new CommandCreateMainThruster("2a31wd", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(0.9985), new Thrust(0)));
		validateParseCommand("create main thruster 2a31wd at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 50.213",
				new CommandCreateMainThruster("2a31wd", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(50.213), new Thrust(0)));
		validateParseCommand("create main thruster 2a31wd at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 99.99",
				new CommandCreateMainThruster("2a31wd", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(99.99), new Thrust(0)));

		// rate
		validateParseCommand("create main thruster 12345 at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 1",
				new CommandCreateMainThruster("12345", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(0)));
		validateParseCommand("create main thruster 12345 at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 100 RATE 1",
				new CommandCreateMainThruster("12345", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(100)));
		validateParseCommand("create main thruster 12345 at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 1.85 RATE 1",
				new CommandCreateMainThruster("12345", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(1.85)));
		validateParseCommand("create main thruster 12345 at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 99.6 RATE 1",
				new CommandCreateMainThruster("12345", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(99.6)));
		validateParseCommand("create main thruster 12345 at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 50.2874 RATE 1",
				new CommandCreateMainThruster("12345", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(50.2874)));
	}

	// illegal create main thruster (fixed)
	@Test
	void testIllegalCreateMainThrusterFixed() {
		// syntax
		validateParseThrows(RuntimeException.class, "create main thruster poawdaz at offset ( 0 1 2) on surface front with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruste poawdaz at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create mai thruster poawdaz at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create thruster poawdaz at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster awdploim at offset ( 0 1 2 ) on surface front with orientation using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster awdploim at offset ( 0 1 2 ) on surface front with orientation upward fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster awdploim at offset ( 0 1 2 ) on surface front orientation upward using THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster awdploim at offset ( 0 1 2 ) on front with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster awdploim at offset ( 0 1 2 ) surface front with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster awdploim offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster awdploim at ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 0");

		// id
		validateParseThrows(RuntimeException.class, "create main thruster ads klwm at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster a|t offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster alt=b at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster adw-weq at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster poq_qwe at offset ( 0 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 0");

		// offset numbers
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset 0 1 2 on surface front with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0e2 1 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1e4 2 ) on surface front with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2e4 ) on surface front with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( .0 1. .2 ) on surface front with orientation upward using fixed THRUST 0 RATE 0");

		// surface
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface surface with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface ont with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface fron with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front1 with orientation upward using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface frontwith orientation upward using fixed THRUST 0 RATE 0");

		// orientation
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation awdkl using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation w13jn3 using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation orientation using fixed THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation ward using fixed THRUST 0 RATE 0");

		// thrust
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation upward using fixed thrust +00500 rate 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation upward using fixed thrust -1 rate 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation upward using fixed thrust 101 rate 0");

		// rate
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation upward using fixed thrust 0 rate -1");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation upward using fixed thrust 0 rate 101");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation upward using fixed thrust 0 rate -101");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation upward using fixed thrust 0 rate 005000");

	}

	// 3. Create main thruster (variable thrust)
	@Test
	void testCreateMainThrusterVariable() {
		// general
		validateParseCommand("CREATE MAIN THRUSTER A AT OFFSET ( 1 2 3 ) ON SURFACE FRONT WITH ORIENTATION FORWARD USING VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("A", new Point3D(1, 2, 3), FRONT, FORWARD, new Percent(1), new Thrust(1), new Thrust(1)));
		validateParseCommand("CREATE main THRUSTER a AT OFFSET ( 15.3 2587.8 3 ) ON SURFACE LEFT WITH ORIENTATION UPWARD USING VARIABLE THRUST MIN 75.99 MAX 92.6 RATE 0.99",
				new CommandCreateMainThruster("a", new Point3D(15.3, 2587.8, 3), LEFT, UPWARD, new Percent(0.99), new Thrust(75.99), new Thrust(92.6)));
		validateParseCommand("CREATE MAIN THRUstER bcAWD AT OffSET ( 1 2 3.366 ) ON surface BACK WITH ORIeNTaTION LEFTWARD UsinG VARIABLE THrUST MIN 95.9 MAX 99.2 RATE 19.85",
				new CommandCreateMainThruster("bcAWD", new Point3D(1, 2, 3.366), BACK, LEFTWARD, new Percent(19.85), new Thrust(95.9), new Thrust(99.2)));

		// id
		validateParseCommand("CREATE MAIN THRUSTER AbCED AT OFFSET ( 1 2 3 ) ON SURFACE FRONT WITH ORIENTATION FORWARD USING VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("AbCED", new Point3D(1, 2, 3), FRONT, FORWARD, new Percent(1), new Thrust(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THRUSTER AT AT OFFSET ( 1 2 3 ) ON SURFACE FRONT WITH ORIENTATION FORWARD USING VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("AT", new Point3D(1, 2, 3), FRONT, FORWARD, new Percent(1), new Thrust(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THRUSTER THRUSTER AT OFFSET ( 1 2 3 ) ON SURFACE FRONT WITH ORIENTATION FORWARD USING VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("THRUSTER", new Point3D(1, 2, 3), FRONT, FORWARD, new Percent(1), new Thrust(1), new Thrust(1)));

		// offset numbers
		validateParseCommand("CREATE MAIN THRUSTER ab AT OFFSET ( 9 6 3 ) ON SURFACE FRONT WITH ORIENTATION FORWARD USING VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("ab", new Point3D(9, 6, 3), FRONT, FORWARD, new Percent(1), new Thrust(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THRUSTER ab AT OFFSET ( 0 0 0 ) ON SURFACE FRONT WITH ORIENTATION FORWARD USING VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("ab", new Point3D(0, 0, 0), FRONT, FORWARD, new Percent(1), new Thrust(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THRUSTER ab AT OFFSET ( +0 -0 -0 ) ON SURFACE FRONT WITH ORIENTATION FORWARD USING VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("ab", new Point3D(0, 0, 0), FRONT, FORWARD, new Percent(1), new Thrust(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THRUSTER ab AT OFFSET ( -58.9 +25 +253.954 ) ON SURFACE FRONT WITH ORIENTATION FORWARD USING VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("ab", new Point3D(-58.9, 25, 253.954), FRONT, FORWARD, new Percent(1), new Thrust(1), new Thrust(1)));

		// surface
		validateParseCommand("CREATE main THRUSTER asd AT offset ( 0 1 2 ) ON SURFACE FrONT WITH ORIENTATION FORWARD USING VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), FRONT, FORWARD, new Percent(1), new Thrust(1), new Thrust(1)));
		validateParseCommand("create MAIN THRUSTER asd AT OFFSET ( 0 1 2 ) ON SURFACE LeFt WITH ORIENTATION FORWARD USING VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), LEFT, FORWARD, new Percent(1), new Thrust(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN thruSTER asd at OFFSET ( 0 1 2 ) ON SURFACE BAck WITH ORIENTATION FORWARD USING VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), BACK, FORWARD, new Percent(1), new Thrust(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THRUSTER asd AT OFFSET ( 0 1 2 ) ON SURFACE BOtTOM WITH ORIENtaTION FORWARD USING VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), BOTTOM, FORWARD, new Percent(1), new Thrust(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THRUSTER asd AT OFFSET ( 0 1 2 ) ON SURFACE RiGHT WITH ORIENTATION FORWARD USING VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), RIGHT, FORWARD, new Percent(1), new Thrust(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THRUSTER asd AT OFFSET ( 0 1 2 ) ON SuRFaCE tOP WITH ORIENTATION FORWARD USING VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), TOP, FORWARD, new Percent(1), new Thrust(1), new Thrust(1)));

		// orientation
		validateParseCommand("CREATE MAIN THRUSTER asd AT OFFSET ( 0 1 2 ) ON SURFACE FRONT WITH ORIENTATION FORWARD using VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), FRONT, FORWARD, new Percent(1), new Thrust(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THruSTER asd AT OFFSET ( 0 1 2 ) oN surface FRONT WITH ORIENTATION BACKWARD using VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), FRONT, BACKWARD, new Percent(1), new Thrust(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THruSTER asd AT OFFSET ( 0 1 2 ) oN surface FRONT WITH ORIENTATION upWARD using VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THruSTER asd AT OFFSET ( 0 1 2 ) oN surface FRONT WITH ORIENTATION DOWNward using VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), FRONT, DOWNWARD, new Percent(1), new Thrust(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THruSTER asd AT OFFSET ( 0 1 2 ) oN surface FRONT WITH orientation LeFTwARD using VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), FRONT, LEFTWARD, new Percent(1), new Thrust(1), new Thrust(1)));
		validateParseCommand("CREATE MAIN THruSTER asd AT OFFSET ( 0 1 2 ) oN surface front with orientation rightWard using VARIABLE THRUST MIN 1 MAX 1 RATE 1",
				new CommandCreateMainThruster("asd", new Point3D(0, 1, 2), FRONT, RIGHTWARD, new Percent(1), new Thrust(1), new Thrust(1)));

		// rate
		validateParseCommand("create main thruster 2a31wd at offset ( 0 1 2 ) on surface front with orientation upward using vAriable THRUST MIN 0 MAX 1 RATE 0",
				new CommandCreateMainThruster("2a31wd", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(0), new Thrust(0), new Thrust(1)));
		validateParseCommand("create main thruster 2a31wd at offset ( 0 1 2 ) on surface front with orientation upward using variAble THRUST MIN 0 MAX 1 RATE 100",
				new CommandCreateMainThruster("2a31wd", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(100), new Thrust(0), new Thrust(1)));
		validateParseCommand("create main thruster 2a31wd at offset ( 0 1 2 ) on surface front with orientation upward using variable THRUST MIN 0 MAX 1 RATE 0.9985",
				new CommandCreateMainThruster("2a31wd", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(0.9985), new Thrust(0), new Thrust(1)));
		validateParseCommand("create main thruster 2a31wd at offset ( 0 1 2 ) on surface front with orientation upward using variable THRUST MIN 0 MAX 1 RATE 50.213",
				new CommandCreateMainThruster("2a31wd", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(50.213), new Thrust(0), new Thrust(1)));
		validateParseCommand("create main thruster 2a31wd at offset ( 0 1 2 ) on surface front with orientation upward using variable THRUST MIN 0 MAX 1 RATE 99.99",
				new CommandCreateMainThruster("2a31wd", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(99.99), new Thrust(0), new Thrust(1)));

		// min thrust
		validateParseCommand("create main thruster 12345 at offset ( 0 1 2 ) on surface front with orientation upward using variable THRUST MIN 0 max 100 RATE 1",
				new CommandCreateMainThruster("12345", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(0), new Thrust(100)));
		validateParseCommand("create main thruster 12345 at offset ( 0 1 2 ) on surface front with orientation upward using variable THRUST min 100 MAX 100 RATE 1",
				new CommandCreateMainThruster("12345", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(100), new Thrust(100)));
		validateParseCommand("create main thruster 12345 at offset ( 0 1 2 ) on surface front with orientation upward using variable THRUST MIN 1.74 MAX 100 RATE 1",
				new CommandCreateMainThruster("12345", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(1.74), new Thrust(100)));
		validateParseCommand("create main thruster 12345 at offset ( 0 1 2 ) on surface front with orientation upward using variable THRUST MIN 99.6 MAX 100 RATE 1",
				new CommandCreateMainThruster("12345", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(99.6), new Thrust(100)));
		validateParseCommand("create main thruster 12345 at offset ( 0 1 2 ) on surface front with orientation upward using variable THRUST MIN 50.2874 MAX 100 RATE 1",
				new CommandCreateMainThruster("12345", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(50.2874), new Thrust(100)));

		// max thrust
		validateParseCommand("create main thruster 12345 at offset ( 0 1 2 ) on surface front with orientation upward using variable THRUST MIN 0 max 0 RATE 1",
				new CommandCreateMainThruster("12345", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(0), new Thrust(0)));
		validateParseCommand("create main thruster 12345 at offset ( 0 1 2 ) on surface front with orientation upward using variable THRUST min 1 MAX 100 RATE 1",
				new CommandCreateMainThruster("12345", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(1), new Thrust(100)));
		validateParseCommand("create main thruster 12345 at offset ( 0 1 2 ) on surface front with orientation upward using variable THRUST MIN 1 MAX 1.85 RATE 1",
				new CommandCreateMainThruster("12345", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(1), new Thrust(1.85)));
		validateParseCommand("create main thruster 12345 at offset ( 0 1 2 ) on surface front with orientation upward using variable THRUST MIN 1 MAX 99.6 RATE 1",
				new CommandCreateMainThruster("12345", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(1), new Thrust(99.6)));
		validateParseCommand("create main thruster 12345 at offset ( 0 1 2 ) on surface front with orientation upward using variable THRUST MIN 1 MAX 50.2874 RATE 1",
				new CommandCreateMainThruster("12345", new Point3D(0, 1, 2), FRONT, UPWARD, new Percent(1), new Thrust(1), new Thrust(50.2874)));

	}

	// illegal create main thruster (variable)
	@Test
	void testIllegalCreateMainThrusterVariable() {
		// syntax
		validateParseThrows(RuntimeException.class, "create thruster poawdaz at offset ( 0 1 2 ) on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruste poawdaz at offset ( 0 1 2 ) on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create mai thruster poawdaz at offset ( 0 1 2 ) on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create thruster poawdaz at offset ( 0 1 2 ) on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster at offset ( 0 1 2 ) on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster awdploim at offset ( 0 1 2 ) on surface front with orientation using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster awdploim at offset ( 0 1 2 ) on surface front with orientation upward variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster awdploim at offset ( 0 1 2 ) on surface front orientation upward using thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster awdploim at offset ( 0 1 2 ) on front with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster awdploim at offset ( 0 1 2 ) surface front with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster awdploim offset ( 0 1 2 ) on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster awdploim at ( 0 1 2 ) on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");

		// id
		validateParseThrows(RuntimeException.class, "create main thruster ads klwm at offset ( 0 1 2 ) on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster a|t offset ( 0 1 2 ) on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster alt=b at offset ( 0 1 2 ) on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster adw-weq at offset ( 0 1 2) on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster poq_qwe at offset ( 0 1 2) on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");

		// offset numbers
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset 0 1 2 on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0e2 1 2 ) on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1e4 2 ) on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2e4 ) on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( .0 1. .2 ) on surface front with orientation upward using variable thrust min 0 max 0 RATE 0");

		// surface
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface surface with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface ont with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface fron with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front1 with orientation upward using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface frontwith orientation upward using variable thrust min 0 max 0 RATE 0");

		// orientation
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation awdkl using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation w13jn3 using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation orientation using variable thrust min 0 max 0 RATE 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation ward using variable thrust min 0 max 0 RATE 0");

		// rate
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation upward using variable thrust min 0 max 0 rate -1");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation upward using variable thrust min 0 max 0 rate 101");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation upward using variable thrust min 0 max 0 rate -101");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation upward using variable thrust min 0 max 0 rate 005000");

		// min thrust
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation upward using variable thrust min +00500 max 100 rate 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation upward using variable thrust min -1 max 100 rate 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2 ) on surface front with orientation upward using variable thrust min 101 max 100 rate 0");

		// max thrust
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2) on surface front with orientation upward using variable thrust min 0 max +0859 rate 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2) on surface front with orientation upward using variable thrust min 0 max -1 rate 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2) on surface front with orientation upward using variable thrust min 0 max 101 rate 0");
		validateParseThrows(RuntimeException.class, "create main thruster ads at offset ( 0 1 2) on surface front with orientation upward using variable thrust min 0 max 1.1e3 rate 0");
	}


	// 4. Create vernier thruster
	@Test
	void testCreateVernierThruster() {
		// general
		validateParseCommand("CREATe VERNIER THRUSTER qwe AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH orientation UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("qwe", new Point3D(1, 2, 3), TOP, UPWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("create verNIEr thruster asd2 AT offset ( 3 4 3 ) ON surface LEFT WITH ORIENTATION FORWARD using THRUST 87.65 RaTe 0.5",
				new CommandCreateVernierThruster("asd2", new Point3D(3, 4, 3), LEFT, FORWARD, new Percent(0.5), new Thrust(87.65)));
		validateParseCommand("crEAte vernier THRUSTER 2312 at OFFSET ( 1.5 2.4 -3 ) ON SURFACE BOTTOM with ORIENTATION LEFTWARD USING thruST 63.25 RATE 12.1452",
				new CommandCreateVernierThruster("2312", new Point3D(1.5, 2.4, -3), BOTTOM, LEFTWARD, new Percent(12.1452), new Thrust(63.25)));

		// id
		validateParseCommand("CREATE VERNIER THRUSTER asd321 AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("asd321", new Point3D(1, 2, 3), TOP, UPWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER 21kl3m AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("21kl3m", new Point3D(1, 2, 3), TOP, UPWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER WAD123DWWE AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("WAD123DWWE", new Point3D(1, 2, 3), TOP, UPWARD, new Percent(0), new Thrust(0)));

		// offset numbers
		validateParseCommand("CREATE VERNIER THRUSTER a2kdnao82eh AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("a2kdnao82eh", new Point3D(0, 0, 0), TOP, UPWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER a2kdnao82eh AT OFFSET ( -11423 +587413 12475.145 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("a2kdnao82eh", new Point3D(-11423, 587413, 12475.145), TOP, UPWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER a2kdnao82eh AT OFFSET ( +36.2761 -524.1247 -12.147 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("a2kdnao82eh", new Point3D(36.2761, -524.1247, -12.147), TOP, UPWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER a2kdnao82eh AT OFFSET ( 95214.1247 65.528 +3147.2 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("a2kdnao82eh", new Point3D(95214.1247, 65.528, 3147.2), TOP, UPWARD, new Percent(0), new Thrust(0)));

		// surface
		validateParseCommand("CREATE VERNIER THRUSTER akAKem12309AKDM23ka AT OFFSET ( 1 2 3 ) ON SURFACE toP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("akAKem12309AKDM23ka", new Point3D(1, 2, 3), TOP, UPWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER akAKem12309AKDM23ka AT OFFSET ( 1 2 3 ) ON SURFACE bottom WITH ORIENTATION UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("akAKem12309AKDM23ka", new Point3D(1, 2, 3), BOTTOM, UPWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER akAKem12309AKDM23ka AT OFFSET ( 1 2 3 ) ON SURFACE LEFT WITH ORIENTATION UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("akAKem12309AKDM23ka", new Point3D(1, 2, 3), LEFT, UPWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER akAKem12309AKDM23ka AT OFFSET ( 1 2 3 ) ON SURFACE RIght WITH ORIENTATION UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("akAKem12309AKDM23ka", new Point3D(1, 2, 3), RIGHT, UPWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER akAKem12309AKDM23ka AT OFFSET ( 1 2 3 ) ON SURFACE fROnt WITH ORIENTATION UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("akAKem12309AKDM23ka", new Point3D(1, 2, 3), FRONT, UPWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER akAKem12309AKDM23ka AT OFFSET ( 1 2 3 ) ON SURFACE back WITH ORIENTATION UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("akAKem12309AKDM23ka", new Point3D(1, 2, 3), BACK, UPWARD, new Percent(0), new Thrust(0)));

		// orientation
		validateParseCommand("CREATE VERNIER THRUSTER wkaemAWDNAJUWwaekmaAKEaeawMEkam AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("wkaemAWDNAJUWwaekmaAKEaeawMEkam", new Point3D(1, 2, 3), TOP, UPWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER wkaemAWDNAJUWwaekmaAKEaeawMEkam AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION DOWNWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("wkaemAWDNAJUWwaekmaAKEaeawMEkam", new Point3D(1, 2, 3), TOP, DOWNWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER wkaemAWDNAJUWwaekmaAKEaeawMEkam AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION leftWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("wkaemAWDNAJUWwaekmaAKEaeawMEkam", new Point3D(1, 2, 3), TOP, LEFTWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER wkaemAWDNAJUWwaekmaAKEaeawMEkam AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION RIGHTward USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("wkaemAWDNAJUWwaekmaAKEaeawMEkam", new Point3D(1, 2, 3), TOP, RIGHTWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER wkaemAWDNAJUWwaekmaAKEaeawMEkam AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION foRWard USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("wkaemAWDNAJUWwaekmaAKEaeawMEkam", new Point3D(1, 2, 3), TOP, FORWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER wkaemAWDNAJUWwaekmaAKEaeawMEkam AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION BACkwaRD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("wkaemAWDNAJUWwaekmaAKEaeawMEkam", new Point3D(1, 2, 3), TOP, BACKWARD, new Percent(0), new Thrust(0)));

		// thrust
		validateParseCommand("CREATE VERNIER THRUSTER WAD123DWWE AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("WAD123DWWE", new Point3D(1, 2, 3), TOP, UPWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER WAD123DWWE AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 100 RATE 0",
				new CommandCreateVernierThruster("WAD123DWWE", new Point3D(1, 2, 3), TOP, UPWARD, new Percent(0), new Thrust(100)));
		validateParseCommand("CREATE VERNIER THRUSTER WAD123DWWE AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 1.54 RATE 0",
				new CommandCreateVernierThruster("WAD123DWWE", new Point3D(1, 2, 3), TOP, UPWARD, new Percent(0), new Thrust(1.54)));
		validateParseCommand("CREATE VERNIER THRUSTER WAD123DWWE AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 58.9 RATE 0",
				new CommandCreateVernierThruster("WAD123DWWE", new Point3D(1, 2, 3), TOP, UPWARD, new Percent(0), new Thrust(58.9)));

		// rate
		validateParseCommand("CREATE VERNIER THRUSTER lweaOM3 AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0",
				new CommandCreateVernierThruster("lweaOM3", new Point3D(1, 2, 3), TOP, UPWARD, new Percent(0), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER lweaOM3 AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 100",
				new CommandCreateVernierThruster("lweaOM3", new Point3D(1, 2, 3), TOP, UPWARD, new Percent(100), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER lweaOM3 AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 95.624",
				new CommandCreateVernierThruster("lweaOM3", new Point3D(1, 2, 3), TOP, UPWARD, new Percent(95.624), new Thrust(0)));
		validateParseCommand("CREATE VERNIER THRUSTER lweaOM3 AT OFFSET ( 1 2 3 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 12.36",
				new CommandCreateVernierThruster("lweaOM3", new Point3D(1, 2, 3), TOP, UPWARD, new Percent(12.36), new Thrust(0)));

	}

	// illegal create vernier thruster
	@Test
	void testIllegalCreateVernierThruster() {
		// syntax
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIeNTATION USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER AT OFFSET ( 0 0 0 ) ON surf TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREAT VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD usin THRUST 0 RATE 0");

		// id
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER i d AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER i-d AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER i?d AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id ? AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id a AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER a id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id_id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");

		// offset numbers
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 1 .2 4 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 5 6 .4 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( .7 2 4 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 4e1 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 1e0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 4.5e2 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");

		// surface
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE T0P WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE asd WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TqweOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE WITH WITH ORIENTATION UPWARD USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE BOTTOM ORIENTATION UPWARD USING THRUST 0 RATE 0");

		// orientation
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION USING USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION TOP USING THRUST 0 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWAR USING THRUST 0 RATE 0");

		// thrust
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 00199 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST -1 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 9e2 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 101 RATE 0");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 101.11 RATE 0");

		// rate
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE -5");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 104");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 102.364");
		validateParseThrows(RuntimeException.class, "CREATE VERNIER THRUSTER id AT OFFSET ( 0 0 0 ) ON SURFACE TOP WITH ORIENTATION UPWARD USING THRUST 0 RATE 5e1");

	}

	// 5. Create static connector
	@Test
	void testCreateStaticConnector() {
		// general
		validateParseCommand("CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 1 2 3 )",
				new CommandCreateStaticConnector("a", "b", "c", new Point3D(1, 2, 3), false, false));
		validateParseCommand("CREATE STATIC CONNECTOR alkpo2jdj92 FROM 13a8ndb TO AWKDMAWKMD WITH OFFSET ( 0 -4.4 -9.56 ) ALLOW RECONNECTION",
				new CommandCreateStaticConnector("alkpo2jdj92", "13a8ndb", "AWKDMAWKMD", new Point3D(0, -4.4, -9.56), false, true));
		validateParseCommand("CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 2.51 32.124 005 ) ALLOW DISCONNECTION",
				new CommandCreateStaticConnector("a", "b", "c", new Point3D(2.51, 32.124, 5), true, false));

		// id1
		validateParseCommand("CREATE STATIC CONNECTOR abAWK3m1 FROM b TO c WITH OFFSET ( 1 2 3 )",
				new CommandCreateStaticConnector("abAWK3m1", "b", "c", new Point3D(1, 2, 3), false, false));
		validateParseCommand("CREATE STATIC CONNECTOR awkl39amWMK FROM b TO c WITH OFFSET ( 1 2 3 )",
				new CommandCreateStaticConnector("awkl39amWMK", "b", "c", new Point3D(1, 2, 3), false, false));
		validateParseCommand("CREATE STATIC CONNECTOR w3amd029amM FROM b TO c WITH OFFSET ( 1 2 3 )",
				new CommandCreateStaticConnector("w3amd029amM", "b", "c", new Point3D(1, 2, 3), false, false));

		// id2
		validateParseCommand("CREATE STATIC CONNECTOR a FROM lwoamnei12912 TO c WITH OFFSET ( 1 2 3 )",
				new CommandCreateStaticConnector("a", "lwoamnei12912", "c", new Point3D(1, 2, 3), false, false));
		validateParseCommand("CREATE STATIC CONNECTOR a FROM peizmmmr2 TO c WITH OFFSET ( 1 2 3 )",
				new CommandCreateStaticConnector("a", "peizmmmr2", "c", new Point3D(1, 2, 3), false, false));
		validateParseCommand("CREATE STATIC CONNECTOR a FROM awdM3naK3393Mlm3 TO c WITH OFFSET ( 1 2 3 )",
				new CommandCreateStaticConnector("a", "awdM3naK3393Mlm3", "c", new Point3D(1, 2, 3), false, false));

		// id3
		validateParseCommand("CREATE STATIC CONNECTOR a FROM b TO lwm3ma30ka WITH OFFSET ( 1 2 3 )",
				new CommandCreateStaticConnector("a", "b", "lwm3ma30ka", new Point3D(1, 2, 3), false, false));
		validateParseCommand("CREATE STATIC CONNECTOR a FROM b TO AWKLDMAWDK WITH OFFSET ( 1 2 3 )",
				new CommandCreateStaticConnector("a", "b", "AWKLDMAWDK", new Point3D(1, 2, 3), false, false));
		validateParseCommand("CREATE STATIC CONNECTOR a FROM b TO 12331208 WITH OFFSET ( 1 2 3 )",
				new CommandCreateStaticConnector("a", "b", "12331208", new Point3D(1, 2, 3), false, false));

		// offset numbers
		validateParseCommand("CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 12.14 25.36 14.21 )",
				new CommandCreateStaticConnector("a", "b", "c", new Point3D(12.14, 25.36, 14.21), false, false));
		validateParseCommand("CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( -25.14 +6589.314 -62.247 )",
				new CommandCreateStaticConnector("a", "b", "c", new Point3D(-25.14, 6589.314, -62.247), false, false));
		validateParseCommand("CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( +25.21 79.214 +0.9999 )",
				new CommandCreateStaticConnector("a", "b", "c", new Point3D(25.21, 79.214, 0.9999), false, false));
		validateParseCommand("CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( +14222 -369 95 )",
				new CommandCreateStaticConnector("a", "b", "c", new Point3D(14222, -369, 95), false, false));

		// optional allow
		validateParseCommand("CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 1 2 3 ) ALLOW DISCONNECTION RECONNECTION",
				new CommandCreateStaticConnector("a", "b", "c", new Point3D(1, 2, 3), true, true));
		validateParseCommand("CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 1 2 3 ) ALLOW DISCONNECTION",
				new CommandCreateStaticConnector("a", "b", "c", new Point3D(1, 2, 3), true, false));
		validateParseCommand("CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 1 2 3 ) ALLOW RECONNECTION",
				new CommandCreateStaticConnector("a", "b", "c", new Point3D(1, 2, 3), false, true));
	}

	// illegal create static connector
	@Test
	void testIllegalCreateStaticConnector() {
		// syntax
		validateParseThrows(RuntimeException.class, "CREATE CONNECTOR a FROM b TO c WITH OFFSET ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC a FROM b TO c WITH OFFSET ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a TO c WITH OFFSET ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO c OFFSET ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO c WITH ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 1 2 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET  1 2 3  ");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 1 2 3 ) ALLOW");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 1 2 3 ) ALLOW RECONECT");

		// id1
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR FROM b TO c WITH OFFSET ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a b FROM b TO c WITH OFFSET ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a2_3a FROM b TO c WITH OFFSET ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR __ FROM b TO c WITH OFFSET ( 1 2 3 )");

		// id2
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM TO c WITH OFFSET ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM awd awd TO c WITH OFFSET ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM TO TO TO c WITH OFFSET ( 1 2 3 )");

		// id3
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b WITH OFFSET ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO WITH OFFSET ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO awd mkw WITH OFFSET ( 1 2 3 )");

		// offset numbers
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 1 2 3e4 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 1 2e2 3 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 1e2 2 3 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( ab c 2 )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 1 2 a )");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 1 b 3 )");

		// optional allow
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 1 2 3 ) ALLOW");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 1 2 3 ) ALLOW DISCONNECTION NOTHING");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 1 2 3 ) ALLOW RECONNECTION NOTHING");
		validateParseThrows(RuntimeException.class, "CREATE STATIC CONNECTOR a FROM b TO c WITH OFFSET ( 1 2 3 ) ALLOW RECONNECTION DISCONNECTION");
	}

	// 6. Create dynamic connector
	@Test
	void testCreateDynamicConnector() {
		// general
		validateParseCommand("CREATE dynamic CONNECTOR a from b TO c WITH offset ALPHA ( 1 2 3 ) beta ( 4 5 6 ) extent INITIAL 7 speed 8",
				new CommandCreateDynamicConnector("a", "b", "c", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(7), new Percent(8), false, false));
		validateParseCommand("CREATE dynamic CONNECTOR a from b to c WITH OFFSET alpha ( 45.12 -632.12 99 ) BETA ( 4.658 255 -6215 ) EXTENT initial 100 SPEED 100 allow RECONNECTION",
				new CommandCreateDynamicConnector("a", "b", "c", new Point3D(45.12, -632.12, 99), new Point3D(4.658, 255, -6215), new Percent(100), new Percent(100), false, true));
		validateParseCommand("CREATE dynamic CONNECTOR 123ma FROM bbbcd TO al3a93 WiTH offSET ALPHA ( 9 8 7 ) beta ( 11.11 12.21 36.85 ) EXTENT INITIAL 7.58 SPEED 99.333 allow disconnectiON",
				new CommandCreateDynamicConnector("123ma", "bbbcd", "al3a93", new Point3D(9, 8, 7), new Point3D(11.11, 12.21, 36.85), new Percent(7.58), new Percent(99.333), true, false));

		// id1
		validateParseCommand("CREATE DYNAMIC CONNECTOR aklawdlkm293 FROM b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8",
				new CommandCreateDynamicConnector("aklawdlkm293", "b", "c", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(7), new Percent(8), false, false));
		validateParseCommand("CREATE DYNAMIC CONNECTOR akm393ma FROM b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8",
				new CommandCreateDynamicConnector("akm393ma", "b", "c", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(7), new Percent(8), false, false));
		validateParseCommand("CREATE DYNAMIC CONNECTOR AWDKMAW13 FROM b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8",
				new CommandCreateDynamicConnector("AWDKMAW13", "b", "c", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(7), new Percent(8), false, false));

		// id2
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM klawdklm390 TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8",
				new CommandCreateDynamicConnector("a", "klawdklm390", "c", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(7), new Percent(8), false, false));
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM akm3am3adbb TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8",
				new CommandCreateDynamicConnector("a", "akm3am3adbb", "c", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(7), new Percent(8), false, false));
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM mkakmaklwliel TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8",
				new CommandCreateDynamicConnector("a", "mkakmaklwliel", "c", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(7), new Percent(8), false, false));

		// id3
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM b TO 3928 WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8",
				new CommandCreateDynamicConnector("a", "b", "3928", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(7), new Percent(8), false, false));
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM b TO mdjmejkeAWKKM WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8",
				new CommandCreateDynamicConnector("a", "b", "mdjmejkeAWKKM", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(7), new Percent(8), false, false));
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM b TO aKMDmeke93AKMWbz WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8",
				new CommandCreateDynamicConnector("a", "b", "aKMDmeke93AKMWbz", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(7), new Percent(8), false, false));

		// alpha offset numbers
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM b TO c WITH OFFSET ALPHA ( +1 +8.8 +16 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8",
				new CommandCreateDynamicConnector("a", "b", "c", new Point3D(1, 8.8, 16), new Point3D(4, 5, 6), new Percent(7), new Percent(8), false, false));
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM b TO c WITH OFFSET ALPHA ( -5.32 -05.1 -123 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8",
				new CommandCreateDynamicConnector("a", "b", "c", new Point3D(-5.32, -5.1, -123), new Point3D(4, 5, 6), new Percent(7), new Percent(8), false, false));

		// beta offset numbers
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( +8.5 +965 +12.14 ) EXTENT INITIAL 7 SPEED 8",
				new CommandCreateDynamicConnector("a", "b", "c", new Point3D(1, 2, 3), new Point3D(8.5, 965, 12.14), new Percent(7), new Percent(8), false, false));
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( -9.33 -258.7 -3.9444 ) EXTENT INITIAL 7 SPEED 8",
				new CommandCreateDynamicConnector("a", "b", "c", new Point3D(1, 2, 3), new Point3D(-9.33, -258.7, -3.9444), new Percent(7), new Percent(8), false, false));

		// initial extent percent
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 0 SPEED 8",
				new CommandCreateDynamicConnector("a", "b", "c", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(0), new Percent(8), false, false));
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 100 SPEED 8",
				new CommandCreateDynamicConnector("a", "b", "c", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(100), new Percent(8), false, false));
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 50.33 SPEED 8",
				new CommandCreateDynamicConnector("a", "b", "c", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(50.33), new Percent(8), false, false));

		// initial speed percent
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 92.66",
				new CommandCreateDynamicConnector("a", "b", "c", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(7), new Percent(92.66), false, false));
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 0",
				new CommandCreateDynamicConnector("a", "b", "c", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(7), new Percent(0), false, false));
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 100",
				new CommandCreateDynamicConnector("a", "b", "c", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(7), new Percent(100), false, false));

		// optional allow
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8 aLLow DISCONNECTION RECONNECTION",
				new CommandCreateDynamicConnector("a", "b", "c", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(7), new Percent(8), true, true));
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8 ALLOW disconnectiON",
				new CommandCreateDynamicConnector("a", "b", "c", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(7), new Percent(8), true, false));
		validateParseCommand("CREATE DYNAMIC CONNECTOR a FROM b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8 ALLOW ReCOnNeCTION",
				new CommandCreateDynamicConnector("a", "b", "c", new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Percent(7), new Percent(8), false, true));

	}

	// illegal create dynamic connector
	@Test
	void testIllegalCreateDynamicConnector() {
		// syntax
		validateParseThrows(RuntimeException.class, "CREATE CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR from b TO c WITH ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8 DISCONNECTION RECONNECTION");
		validateParseThrows(RuntimeException.class, "dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8 ALLOW DISCONNECTION RECONNECTION");
		validateParseThrows(RuntimeException.class, "CREATE static CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8 ALLOW DISCONNECTION RECONNECTION");

		// id1
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR _ from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a b from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");

		// id2
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from _ TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b a TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");

		// id3
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c_c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c d WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");

		// alpha offset numbers
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA 1 2 3 BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 a 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( b 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 c ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( .1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 .2 .3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8");

		// beta offset numbers
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA 1 2 3 EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 1 3 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 1 a 3 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( b 2 3 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 1 2 c ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( .1 2 3 ) EXTENT INITIAL 7 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 1 .2 .3 ) EXTENT INITIAL 7 SPEED 8");

		// initial extent percent
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL a SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL -1 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 101 SPEED 8");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 00700 SPEED 8");

		// initial speed percent
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED d");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED -5");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED -0.5");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 0590");

		// optional allow
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8 ALLOW");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8 ALLOW RECONNECTION DISCONNECTION");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8 ALLOW nothing DISCONNECTION");
		validateParseThrows(RuntimeException.class, "CREATE dynamic CONNECTOR a from b TO c WITH OFFSET ALPHA ( 1 2 3 ) BETA ( 4 5 6 ) EXTENT INITIAL 7 SPEED 8 ALLOW RECONNECTION invalid");
	}

	// 7. Build main thruster group
	@Test
	void testBuildMainThrusterGroup() {
		// general
		validateParseCommand("BUILD MaIN THRUSTER GRouP a WiTh THRUSTERS b c d", new CommandBuildMainThrusterGroup("a", Arrays.asList("b", "c", "d")));
		validateParseCommand("build MAIN thruster GROUP akm3 WITH THRUSTER awk3 k2k3al ASD", new CommandBuildMainThrusterGroup("akm3", Arrays.asList("awk3", "k2k3al", "ASD")));
		validateParseCommand("BUILD MAIN THRUSTER GROUP 0000 WITH ThRUstER efi239", new CommandBuildMainThrusterGroup("0000", Arrays.asList("efi239")));
		validateParseCommand("BUILD MAIN THRUSTER GROUP aWE WITH ThRUstERs 39 232 02", new CommandBuildMainThrusterGroup("aWE", Arrays.asList("39", "232", "02")));

		// group id
		validateParseCommand("BUILD MAIN THRUSTER GROUP asdqwe123 WITH THRUSTER a", new CommandBuildMainThrusterGroup("asdqwe123", Arrays.asList("a")));
		validateParseCommand("BUILD MAIN THRUSTER GROUP WMEK203 WITH THRUSTER a", new CommandBuildMainThrusterGroup("WMEK203", Arrays.asList("a")));
		validateParseCommand("BUILD MAIN THRUSTER GROUP 130aDMe03k WITH THRUSTER a", new CommandBuildMainThrusterGroup("130aDMe03k", Arrays.asList("a")));

		// id list
		validateParseCommand("BUILD MAIN THRUSTER GROUP abc WITH THRUSTER a23 2 4", new CommandBuildMainThrusterGroup("abc", Arrays.asList("a23", "2", "4")));
		validateParseCommand("BUILD MAIN THRUSTER GROUP abc WITH THRUSTER kwela AMKM3 013m3", new CommandBuildMainThrusterGroup("abc", Arrays.asList("kwela", "AMKM3", "013m3")));
		validateParseCommand("BUILD MAIN THRUSTER GROUP abc WITH THRUSTER 0239 923 adm2mN", new CommandBuildMainThrusterGroup("abc", Arrays.asList("0239", "923", "adm2mN")));
	}

	// illegal build main thruster group
	@Test
	void testIllegalBuildMainThrusterGroup() {
		// syntax
		validateParseThrows(RuntimeException.class, "MAIN THRUSTER GROUP a WITH THRUSTER b");
		validateParseThrows(RuntimeException.class, "BUILD MAIN GROUP a WITH THRUSTER b");
		validateParseThrows(RuntimeException.class, "BUILD MAIN THRUSTER GROUP a WITH b");
		validateParseThrows(RuntimeException.class, "BUILD MAIN THRUSTER GROUP a THRUSTER b");
		validateParseThrows(RuntimeException.class, "BUILD MAIN THRUSTER a WITH THRUSTERS b");
		validateParseThrows(RuntimeException.class, "BUILD THRUSTER GROUP a WITH THRUSTERS b");

		// group id
		validateParseThrows(RuntimeException.class, "BUILD MAIN THRUSTER GROUP WITH THRUSTER b");
		validateParseThrows(RuntimeException.class, "BUILD MAIN THRUSTER GROUP _ WITH THRUSTER b");
		validateParseThrows(RuntimeException.class, "BUILD MAIN THRUSTER GROUP a b WITH THRUSTER b");
		validateParseThrows(RuntimeException.class, "BUILD MAIN THRUSTER GROUP WITH THRUSTERS b");
		validateParseThrows(RuntimeException.class, "BUILD MAIN THRUSTER GROUP b WITH THRUSTERS _");
		validateParseThrows(RuntimeException.class, "BUILD MAIN THRUSTER GROUP c d WITH THRUSTERS b");

		// id list
		validateParseThrows(RuntimeException.class, "BUILD MAIN THRUSTER GROUP a WITH THRUSTER");
		validateParseThrows(RuntimeException.class, "BUILD MAIN THRUSTER GROUP a WITH THRUSTER b _");
		validateParseThrows(RuntimeException.class, "BUILD MAIN THRUSTER GROUP a WITH THRUSTER _ a b");
		validateParseThrows(RuntimeException.class, "BUILD MAIN THRUSTER GROUP a WITH THRUSTERS");
		validateParseThrows(RuntimeException.class, "BUILD MAIN THRUSTER GROUP a WITH THRUSTERS _ a");
		validateParseThrows(RuntimeException.class, "BUILD MAIN THRUSTER GROUP a WITH THRUSTERS b a _ c");
	}

	// 8. Build vernier thruster group
	@Test
	void testBuildVernierThrusterGroup() {
		// general
		validateParseCommand("BUILD VeRNIER THRUSTER GRouP a WiTh THRUSTERS b c d", new CommandBuildVernierThrusterGroup("a", Arrays.asList("b", "c", "d")));
		validateParseCommand("build VErNIER thruster GROUP akm3 WITH THRUSTER awk3 k2k3al ASD", new CommandBuildVernierThrusterGroup("akm3", Arrays.asList("awk3", "k2k3al", "ASD")));
		validateParseCommand("BUILD VERNIeR THRUSTER GROUP 0000 WITH ThRUstER efi239", new CommandBuildVernierThrusterGroup("0000", Arrays.asList("efi239")));
		validateParseCommand("BUILD VERnIER THRUSTER GROUP aWE WITH ThRUstERs 39 232 02", new CommandBuildVernierThrusterGroup("aWE", Arrays.asList("39", "232", "02")));

		// group id
		validateParseCommand("BUILD VERNIER THRUSTER GROUP asdqwe123 WITH THRUSTER a", new CommandBuildVernierThrusterGroup("asdqwe123", Arrays.asList("a")));
		validateParseCommand("BUILD VERNIER THRUSTER GROUP WMEK203 WITH THRUSTER a", new CommandBuildVernierThrusterGroup("WMEK203", Arrays.asList("a")));
		validateParseCommand("BUILD VERNIER THRUSTER GROUP 130aDMe03k WITH THRUSTER a", new CommandBuildVernierThrusterGroup("130aDMe03k", Arrays.asList("a")));

		// id list
		validateParseCommand("BUILD VERNIER THRUSTER GROUP abc WITH THRUSTER a23 2 4", new CommandBuildVernierThrusterGroup("abc", Arrays.asList("a23", "2", "4")));
		validateParseCommand("BUILD VERNIER THRUSTER GROUP abc WITH THRUSTER kwela AMKM3 013m3", new CommandBuildVernierThrusterGroup("abc", Arrays.asList("kwela", "AMKM3", "013m3")));
		validateParseCommand("BUILD VERNIER THRUSTER GROUP abc WITH THRUSTER 0239 923 adm2mN", new CommandBuildVernierThrusterGroup("abc", Arrays.asList("0239", "923", "adm2mN")));
	}

	// illegal build vernier thruster group
	@Test
	void testIllegalBuildVernierThrusterGroup() {
		// syntax
		validateParseThrows(RuntimeException.class, "VERNIER THRUSTER GROUP a WITH THRUSTER b");
		validateParseThrows(RuntimeException.class, "BUILD VERNIER GROUP a WITH THRUSTER b");
		validateParseThrows(RuntimeException.class, "BUILD VERNIER THRUSTER GROUP a WITH b");
		validateParseThrows(RuntimeException.class, "BUILD VERNIER THRUSTER GROUP a THRUSTER b");
		validateParseThrows(RuntimeException.class, "BUILD VERNIER THRUSTER a WITH THRUSTERS b");
		validateParseThrows(RuntimeException.class, "BUILD THRUSTER GROUP a WITH THRUSTERS b");

		// group id
		validateParseThrows(RuntimeException.class, "BUILD VERNIER THRUSTER GROUP WITH THRUSTER b");
		validateParseThrows(RuntimeException.class, "BUILD VERNIER THRUSTER GROUP _ WITH THRUSTER b");
		validateParseThrows(RuntimeException.class, "BUILD VERNIER THRUSTER GROUP a b WITH THRUSTER b");
		validateParseThrows(RuntimeException.class, "BUILD VERNIER THRUSTER GROUP WITH THRUSTERS b");
		validateParseThrows(RuntimeException.class, "BUILD VERNIER THRUSTER GROUP b WITH THRUSTERS _");
		validateParseThrows(RuntimeException.class, "BUILD VERNIER THRUSTER GROUP c d WITH THRUSTERS b");

		// id list
		validateParseThrows(RuntimeException.class, "BUILD VERNIER THRUSTER GROUP a WITH THRUSTER");
		validateParseThrows(RuntimeException.class, "BUILD VERNIER THRUSTER GROUP a WITH THRUSTER b _");
		validateParseThrows(RuntimeException.class, "BUILD VERNIER THRUSTER GROUP a WITH THRUSTER _ a b");
		validateParseThrows(RuntimeException.class, "BUILD VERNIER THRUSTER GROUP a WITH THRUSTERS");
		validateParseThrows(RuntimeException.class, "BUILD VERNIER THRUSTER GROUP a WITH THRUSTERS _ a");
		validateParseThrows(RuntimeException.class, "BUILD VERNIER THRUSTER GROUP a WITH THRUSTERS b a _ c");
	}

	// 9. Add thruster group
	@Test
	void testAddThrusterGroup() {
		// ADD THRUSTER ( GROUP | GROUPS ) idn+ TO id1
		// general
		validateParseCommand("ADD thruster GROUPS b c d TO a", new CommandAddThrusterGroups(Arrays.asList("b", "c", "d"), "a"));
		validateParseCommand("add THRUSTER GROUPS awd TO a", new CommandAddThrusterGroups(Arrays.asList("awd"), "a"));
		validateParseCommand("ADD THRUSTER groups 213a c d TO a", new CommandAddThrusterGroups(Arrays.asList("213a", "c", "d"), "a"));
		validateParseCommand("ADD THRUSTER group e f g TO a", new CommandAddThrusterGroups(Arrays.asList("e", "f", "g"), "a"));
		validateParseCommand("ADD THRUSTER GROUP c to a", new CommandAddThrusterGroups(Arrays.asList("c"), "a"));
		validateParseCommand("ADD THRUStEr GROUP cde efd TO a", new CommandAddThrusterGroups(Arrays.asList("cde", "efd"), "a"));

		// id list
		validateParseCommand("ADD THRUSTER GROUPS b 12kmM3 TO a", new CommandAddThrusterGroups(Arrays.asList("b", "12kmM3"), "a"));
		validateParseCommand("ADD THRUSTER GROUPS b TO TO a", new CommandAddThrusterGroups(Arrays.asList("b", "TO"), "a"));
		validateParseCommand("ADD THRUSTER GROUPS 1 2 3 TO a", new CommandAddThrusterGroups(Arrays.asList("1", "2", "3"), "a"));

		// group id
		validateParseCommand("ADD THRUSTER GROUPS b c d TO aakwm", new CommandAddThrusterGroups(Arrays.asList("b", "c", "d"), "aakwm"));
		validateParseCommand("ADD THRUSTER GROUPS b c d TO 123", new CommandAddThrusterGroups(Arrays.asList("b", "c", "d"), "123"));
		validateParseCommand("ADD THRUSTER GROUPS b c d TO AWNMe213aMKa", new CommandAddThrusterGroups(Arrays.asList("b", "c", "d"), "AWNMe213aMKa"));
	}

	// illegal add thruster group
	@Test
	void testIllegalAddThrusterGroup() {
		// syntax
		validateParseThrows(RuntimeException.class, "ADD GROUPS b c d TO a");
		validateParseThrows(RuntimeException.class, "THRUSTER GROUPS b c d TO a");
		validateParseThrows(RuntimeException.class, "ADD THRUSTER b c d TO a");
		validateParseThrows(RuntimeException.class, "THRUSTER GROUP b c d TO a");
		validateParseThrows(RuntimeException.class, "ADD THRUSTER b c d TO a");
		validateParseThrows(RuntimeException.class, "ADD GROUP b c d TO a");
		validateParseThrows(RuntimeException.class, "ADD THRUSTER GROUP b c d a");

		// id list
		validateParseThrows(RuntimeException.class, "ADD THRUSTER GROUPS TO a");
		validateParseThrows(RuntimeException.class, "ADD THRUSTER GROUPS b  d TO a");
		validateParseThrows(RuntimeException.class, "ADD THRUSTER GROUPS b _ d TO a");

		// group id
		validateParseThrows(RuntimeException.class, "ADD THRUSTER GROUPS b c d TO");
		validateParseThrows(RuntimeException.class, "ADD THRUSTER GROUPS b c d TO ");
		validateParseThrows(RuntimeException.class, "ADD THRUSTER GROUPS b c d TO _");
		validateParseThrows(RuntimeException.class, "ADD THRUSTER GROUPS b c d TO a b");
	}

	// 10. Fire thruster group
	@Test
	void testFireThrusterGroup() {
		// general
		validateParseCommand("FIRE thrustER GROUP a FOR 1 seconds", new CommandFireThruster("a", new Time(1)));
		validateParseCommand("fIRe THRUSTER GROUP 2ad2 for 5.5 SecOND", new CommandFireThruster("2ad2", new Time(5.5)));
		validateParseCommand("FIRE THRUSTER GRouP 392 FOR 78888.2 sECOnDs", new CommandFireThruster("392", new Time(78888.2)));

		// group id
		validateParseCommand("FIRE THRUSTER GROUP a FOR 1 SECOND", new CommandFireThruster("a", new Time(1)));
		validateParseCommand("FIRE THRUSTER GROUP 123 FOR 1 SECOND", new CommandFireThruster("123", new Time(1)));
		validateParseCommand("FIRE THRUSTER GROUP FOR FOR 1 SECOND", new CommandFireThruster("FOR", new Time(1)));

		// number seconds
		validateParseCommand("FIRE THRUSTER GROUP a FOR 0 SECOND", new CommandFireThruster("a", new Time(0)));
		validateParseCommand("FIRE THRUSTER GROUP a FOR 1.99 SECOND", new CommandFireThruster("a", new Time(1.99)));
		validateParseCommand("FIRE THRUSTER GROUP a FOR +123458.99 SECOND", new CommandFireThruster("a", new Time(123458.99)));
		validateParseCommand("FIRE THRUSTER GROUP a FOR 0 SECONDS", new CommandFireThruster("a", new Time(0)));
		validateParseCommand("FIRE THRUSTER GROUP a FOR 1.99 SECONDS", new CommandFireThruster("a", new Time(1.99)));
		validateParseCommand("FIRE THRUSTER GROUP a FOR +123458.99 SECONDS", new CommandFireThruster("a", new Time(123458.99)));

		// optional thrust
		validateParseCommand("FIRE THRUSTER GROUP a FOR 1 SECOND AT thrust 0", new CommandFireThruster("a", new Time(1), new Thrust(0)));
		validateParseCommand("FIRE THRUSTER GROUP a FOR 1 SECOND AT THRUst 100", new CommandFireThruster("a", new Time(1), new Thrust(100)));
		validateParseCommand("FIRE THRUSTER GROUP a FOR 1 SECOND AT THRUST 95.6", new CommandFireThruster("a", new Time(1), new Thrust(95.6)));
		validateParseCommand("FIRE THRUSTER GROUP a FOR 1 SECONDS AT THRuST 0", new CommandFireThruster("a", new Time(1), new Thrust(0)));
		validateParseCommand("FIRE THRUSTER GROUP a FOR 1 SECONDS AT THRUST 100", new CommandFireThruster("a", new Time(1), new Thrust(100)));
		validateParseCommand("FIRE THRUSTER GROUP a FOR 1 SECONDS AT THRUST 95.6", new CommandFireThruster("a", new Time(1), new Thrust(95.6)));
	}

	// illegal fire thruster group
	@Test
	void testIllegalFireThrusterGroup() {
		// syntax
		validateParseThrows(RuntimeException.class, "THRUSTER GROUP a FOR 1 SECOND");
		validateParseThrows(RuntimeException.class, "FIRE THRUSTER GROUP a FOR 1 SECONDA");
		validateParseThrows(RuntimeException.class, "THRUSTER GROUP a FOR 1 SECOND");
		validateParseThrows(RuntimeException.class, "FIRE GROUP a FOR 1 SECONDS");
		validateParseThrows(RuntimeException.class, "THRUSTER GROUP a FOR 1 SECONDS");
		validateParseThrows(RuntimeException.class, "FIRE THRUSTER a FOR 1 SECONDS");
		validateParseThrows(RuntimeException.class, "FIRE THRUSTER a FOR 1 SECOND THRUST thrust 0");
		validateParseThrows(RuntimeException.class, "FIRE THRUSTER GROUP a FOR 1 SECOND AT 0");
		validateParseThrows(RuntimeException.class, "FIRE THRUSTER a FOR 1 SECOND AT THRUST thrust 0");
		validateParseThrows(RuntimeException.class, "FIRE THRUSTER GROUP a FOR 1 SECONDS AT THRUST");
		validateParseThrows(RuntimeException.class, "FIRE THRUSTER GROUP a FOR 1 SECONDS THRUST 0");

		// group id
		validateParseThrows(RuntimeException.class, "FIRE THRUSTER GROUP FOR 1 SECOND");
		validateParseThrows(RuntimeException.class, "FIRE THRUSTER GROUP a b FOR 1 SECOND");
		validateParseThrows(RuntimeException.class, "FIRE THRUSTER GROUP a cd FOR 1 SECOND");

		// number seconds
		validateParseThrows(RuntimeException.class, "FIRE THRUSTER GROUP a FOR -1 SECOND");
		validateParseThrows(RuntimeException.class, "FIRE THRUSTER GROUP a FOR SECOND");
		validateParseThrows(RuntimeException.class, "FIRE THRUSTER GROUP a FOR -0.99 SECOND");

		// optional thrust
		validateParseThrows(RuntimeException.class, "FIRE THRUSTER GROUP a FOR 1 SECOND AT THUST 0");
		validateParseThrows(RuntimeException.class, "FIRE THRUSTER GROUP a FOR 1 SECONDS AT THRUT 0");

	}

	// 11. Extend strut
	@Test
	void testExtendStrut() {
		// general
		validateParseCommand("EXTEND STRUT a", new CommandExtendStrut("a"));
		validateParseCommand("EXTEND strut a", new CommandExtendStrut("a"));
		validateParseCommand("extend STRUT a", new CommandExtendStrut("a"));

		// id
		validateParseCommand("EXTEND STRUT ab", new CommandExtendStrut("ab"));
		validateParseCommand("EXTEND STRUT STRUT", new CommandExtendStrut("STRUT"));
		validateParseCommand("EXTEND STRUT 123adDS", new CommandExtendStrut("123adDS"));
	}

	// illegal extend strut
	@Test
	void testIllegalExtendStrut() {
		// syntax
		validateParseThrows(RuntimeException.class, "EXTENDSTRUT A");
		validateParseThrows(RuntimeException.class, "EXTEND STRUTA");
		validateParseThrows(RuntimeException.class, "STRUT A");
		validateParseThrows(RuntimeException.class, "EXTEND A");

		// id
		validateParseThrows(RuntimeException.class, "EXTEND STRUT");
		validateParseThrows(RuntimeException.class, "EXTEND STRUT ");
		validateParseThrows(RuntimeException.class, "EXTEND STRUT A B");
		validateParseThrows(RuntimeException.class, "EXTEND STRUT _");
		validateParseThrows(RuntimeException.class, "EXTEND STRUT _ B");
		validateParseThrows(RuntimeException.class, "EXTEND STRUT adw-wd");
	}

	// 12. Retract strut
	@Test
	void testRetractStrut() {
		// general
		validateParseCommand("RETRACT STRUT id", new CommandRetractStrut("id"));
		validateParseCommand("RETRACT strut id", new CommandRetractStrut("id"));
		validateParseCommand("retract STRUT id", new CommandRetractStrut("id"));

		// id
		validateParseCommand("RETRACT STRUT 231", new CommandRetractStrut("231"));
		validateParseCommand("RETRACT STRUT STRUT", new CommandRetractStrut("STRUT"));
		validateParseCommand("RETRACT STRUT 293maM", new CommandRetractStrut("293maM"));
	}

	// illegal retract strut
	@Test
	void testIllegalRetractStrut() {
		// syntax
		validateParseThrows(RuntimeException.class, "STRUT id");
		validateParseThrows(RuntimeException.class, "RETRACT id");
		validateParseThrows(RuntimeException.class, "@RETRACT STRUT id");

		// id
		validateParseThrows(RuntimeException.class, "RETRACT STRUT");
		validateParseThrows(RuntimeException.class, "RETRACT STRUT id a");
		validateParseThrows(RuntimeException.class, "RETRACT STRUT id_b");
		validateParseThrows(RuntimeException.class, "RETRACT STRUT iddw mwa a_b");
	}

	// 13. Disconnect strut
	@Test
	void testDisconnectStrut() {
		// general
		validateParseCommand("DISCONNECT STRUT id", new CommandDisconnectStrut("id"));
		validateParseCommand("DISCONNECT strut id", new CommandDisconnectStrut("id"));
		validateParseCommand("disconnect STRUT id", new CommandDisconnectStrut("id"));

		// id
		validateParseCommand("DISCONNECT STRUT 330", new CommandDisconnectStrut("330"));
		validateParseCommand("DISCONNECT STRUT iawdklm31d", new CommandDisconnectStrut("iawdklm31d"));
		validateParseCommand("DISCONNECT STRUT STRUT", new CommandDisconnectStrut("STRUT"));
	}

	// illegal disconnect strut
	@Test
	void testIllegalDisconnectStrut() {
		// syntax
//		validateParseThrows(RuntimeException.class, "DISCONNECT STRUT id");
//		validateParseThrows(RuntimeException.class, "DISCONNECT STRUT id");
//		validateParseThrows(RuntimeException.class, "DISCONNECT STRUT id");

		// id
		validateParseThrows(RuntimeException.class, "DISCONNECT STRUT");
		validateParseThrows(RuntimeException.class, "DISCONNECT STRUT ");
		validateParseThrows(RuntimeException.class, "DISCONNECT STRUT _");
		validateParseThrows(RuntimeException.class, "DISCONNECT STRUT id asd");
		validateParseThrows(RuntimeException.class, "DISCONNECT STRUT _ asdwm");
		validateParseThrows(RuntimeException.class, "DISCONNECT STRUT id wklam klawd klwe");
	}

	// 14. Reconnect strut
	@Test
	void testReconnectStrut() {
		// general
		validateParseCommand("reconNECT STRUT id1 TO id2", new CommandReconnectStrut("id1", "id2"));
		validateParseCommand("RECONNECT STruT id1 TO id2", new CommandReconnectStrut("id1", "id2"));
		validateParseCommand("RECONNECT STRUT id1 to id2", new CommandReconnectStrut("id1", "id2"));
		validateParseCommand("RECONNEct STRUT id1 TO id2", new CommandReconnectStrut("id1", "id2"));

		// id1
		validateParseCommand("RECONNECT STRUT STRUT TO id2", new CommandReconnectStrut("STRUT", "id2"));
		validateParseCommand("RECONNECT STRUT iawd123d1 TO id2", new CommandReconnectStrut("iawd123d1", "id2"));
		validateParseCommand("RECONNECT STRUT TO TO id2", new CommandReconnectStrut("TO", "id2"));

		// id2
		validateParseCommand("RECONNECT STRUT id1 TO 32ad2", new CommandReconnectStrut("id1", "32ad2"));
		validateParseCommand("RECONNECT STRUT id1 TO 3mAKMAAWAKMPQm3", new CommandReconnectStrut("id1", "3mAKMAAWAKMPQm3"));
		validateParseCommand("RECONNECT STRUT id1 TO ndndndndndnwjklnqe", new CommandReconnectStrut("id1", "ndndndndndnwjklnqe"));
	}

	// illegal reconnect strut
	@Test
	void testIllegalReconnectStrut() {
		// syntax
		validateParseThrows(RuntimeException.class, "RECONNECT STRUT id1 id2");
		validateParseThrows(RuntimeException.class, "RECONNECT id1 TO id2");
		validateParseThrows(RuntimeException.class, "STRUT id1 TO id2");

		// id1
		validateParseThrows(RuntimeException.class, "RECONNECT STRUT TO id2");
		validateParseThrows(RuntimeException.class, "RECONNECT STRUT id1 TO TO id2");
		validateParseThrows(RuntimeException.class, "RECONNECT STRUT id_1 TO id2");
		validateParseThrows(RuntimeException.class, "RECONNECT STRUT _ TO id2");

		// id2
		validateParseThrows(RuntimeException.class, "RECONNECT STRUT id1 TO ");
		validateParseThrows(RuntimeException.class, "RECONNECT STRUT id1 TO _");
		validateParseThrows(RuntimeException.class, "RECONNECT STRUT id1 TO");
		validateParseThrows(RuntimeException.class, "RECONNECT STRUT id1 TO A TO");
		validateParseThrows(RuntimeException.class, "RECONNECT STRUT id1 TO A_TO");
	}

	// 15. Generate flight path using points
	@Test
	void testGenerateFlightPathPoints() {
		// general
		validateParseCommand("generate FLIGHT PATH from { [ 1 2 3 ] [ 4 5 6 ] } with ATTITUDE rate 7 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6)), 7, 9));
		validateParseCommand("GENERATE flight PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH attitude RATE 7 MOTION rate 9", new CommandGeneratePath(Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6)), 7, 9));
		validateParseCommand("GENERATE FLIGHT pAtH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH atTITude raTE 7 motion RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6)), 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH USING [ + X - Y + Z ] FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH attitude RATE 7 MOTION RATE 9", new CommandGeneratePath(X_PLUS, Y_MINUS, Z_PLUS, Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6)), 7, 9));
		validateParseCommand("GENERATE flight PATH using [ - X - Y - Z ] FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(X_MINUS, Y_MINUS, Z_MINUS, Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6)), 7, 9));
		validateParseCommand("generATE FLIGHT PATH usINg [ + X + Y + Z ] from { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(X_PLUS, Y_PLUS, Z_PLUS, Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6)), 7, 9));

		// optional using
		validateParseCommand("GENERATE FLIGHT PATH USING [ + X - Y - Z ] FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(X_PLUS, Y_MINUS, Z_MINUS, Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6)), 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH USING [ - X - Y + Z ] FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(X_MINUS, Y_MINUS, Z_PLUS, Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6)), 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH USING [ - X + Y - Z ] FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(X_MINUS, Y_PLUS, Z_MINUS, Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6)), 7, 9));

		// multiple waypoints
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] [ 8 9 10 ] [ 11 12 13 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Point3D(8, 9, 10), new Point3D(11, 12, 13)), 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] [ 1 2 3 ] [ 4 5.55 123 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Point3D(1, 2, 3), new Point3D(4, 5.55, 123)), 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] [ 9 8 2 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6), new Point3D(9, 8, 2)), 7, 9));

		// waypoint numbers
		// origin
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 0 -1.6 -0.556 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(0, -1.6, -0.556), new Point3D(4, 5, 6)), 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ +11.1 +22 +3.5 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(11.1, 22, 3.5), new Point3D(4, 5, 6)), 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ +18 +21.2 +3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(18, 21.2, 3), new Point3D(4, 5, 6)), 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 17.1 -6 3.56 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(17.1, -6, 3.56), new Point3D(4, 5, 6)), 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ -8 8 -3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(-8, 8, -3), new Point3D(4, 5, 6)), 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ -25.3 8.79 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(-25.3, 8.79, 3), new Point3D(4, 5, 6)), 7, 9));
		// point
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 4 5 6 ] [ 0 -1.6 -0.556 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(4, 5, 6), new Point3D(0, -1.6, -0.556)), 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 4 5 6 ] [ +11.1 +22 +3.5 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(4, 5, 6), new Point3D(11.1, 22, 3.5)), 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 4 5 6 ] [ +18 +21.2 +3 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(4, 5, 6), new Point3D(18, 21.2, 3)), 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 4 5 6 ] [ 17.1 -6 3.56 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(4, 5, 6), new Point3D(17.1, -6, 3.56)), 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 4 5 6 ] [ -8 8 -3 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(4, 5, 6), new Point3D(-8, 8, -3)), 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 4 5 6 ] [ -25.3 8.79 3 ] } WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(4, 5, 6), new Point3D(-25.3, 8.79, 3)), 7, 9));

		// attitude rate number
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE +25 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6)), 25, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE +36.77 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6)), 36.77, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 99.99 MOTION RATE 9", new CommandGeneratePath(Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6)), 99.99, 9));

		// motion rate number
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 1.5", new CommandGeneratePath(Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6)), 7, 1.5));
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE +123", new CommandGeneratePath(Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6)), 7, 123));
		validateParseCommand("GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE +1231.25", new CommandGeneratePath(Arrays.asList(new Point3D(1, 2, 3), new Point3D(4, 5, 6)), 7, 1231.25));
	}

	// illegal generate flight path (points)
	@Test
	void testIllegalGenerateFlightPathPoints() {
		// syntax
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM [ 1 2 3 ] [ 4 5 6 ] WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM   [ 1 2 3 ] [ 4 5 6 ]   WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ]   WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM   [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 56 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATEFLIGHT PATHFROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATEFLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHTPATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDERATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITHATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHTPATHFROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATHFROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHTPATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTIONRATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING[ + X + Y + Z ] FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATHUSING [ + X + Y + Z ] FROM { [ 1 2 3 ] [ 4 5 6 ] }WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING [ + X + Y + Z ] FROM{ [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");

		// optional using
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING [ X +Y +Z ] FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING [ + X + Y Z ] FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING [ + X + Y _ Z ] FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING [ + X + Y Z Z ] FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING [  X   Y   Z ] FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING   + X + Y + Z   FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING  + X + Y + Z  FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		//validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING [ + Z + Y + Z ] FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");

		// multiple waypoints
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] [] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ ] [ 1 2 2 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] [ 1 2 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] [ 1 2 3] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] [1 2 3]} WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] [1 2 3 ]} WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] [ 1 2 3]} WITH ATTITUDE RATE 7 MOTION RATE 9");

		// waypoint numbers
		// origin
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ a 0 0 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 0 c 0 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 0   0 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 0 0   ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [   0 0 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [0 0 0] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		// points
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 0 a 0 ] [ 0 0 0 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 0 0 0a ] [ 0 0 0 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 0e4 0 0 ] [ 0 0 0 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 0 0 0 ] [ 0we 02 0 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 0 0 0 ] [ 0 0 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 0 0 0 ] [ 0 0w 0 ] } WITH ATTITUDE RATE 7 MOTION RATE 9");

		// attitude rate number
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE -1 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 0 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE a MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE -0.001 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE   MOTION RATE 9");

		// motion rate number
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 0");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE -1");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE -0.0001");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE c");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE ");
	}

	// 16. Generate flight path using filename
	@Test
	void testGenerateFlightPathFilename() {
		// general
		validateParseCommand("generate FLIGHT PATH from 'filename' with ATTITUDE rate 7 MOTION RATE 9", new CommandGeneratePath("filename", 7, 9));
		validateParseCommand("GENERATE flight PATH FROM 'filename' WITH attitude RATE 7 MOTION rate 9", new CommandGeneratePath("filename", 7, 9));
		validateParseCommand("GENERATE FLIGHT pAtH FROM 'filename' WITH atTITude raTE 7 motion RATE 9", new CommandGeneratePath("filename", 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH USING [ + X - Y + Z ] FROM 'filename' WITH attitude RATE 7 MOTION RATE 9", new CommandGeneratePath(X_PLUS, Y_MINUS, Z_PLUS, "filename", 7, 9));
		validateParseCommand("GENERATE flight PATH using [ - X - Y - Z ] FROM 'filename' WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(X_MINUS, Y_MINUS, Z_MINUS, "filename", 7, 9));
		validateParseCommand("generATE FLIGHT PATH usINg [ + X + Y + Z ] from 'filename' WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(X_PLUS, Y_PLUS, Z_PLUS, "filename", 7, 9));

		// optional using
		validateParseCommand("GENERATE FLIGHT PATH USING [ + X - Y - Z ] FROM 'filename' WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(X_PLUS, Y_MINUS, Z_MINUS, "filename", 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH USING [ - X - Y + Z ] FROM 'filename' WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(X_MINUS, Y_MINUS, Z_PLUS, "filename", 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH USING [ - X + Y - Z ] FROM 'filename' WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath(X_MINUS, Y_PLUS, Z_MINUS, "filename", 7, 9));

		// filename
		validateParseCommand("GENERATE FLIGHT PATH FROM 'C:\\root.txt' WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath("C:\\root.txt", 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM '..\\filename' WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath("..\\filename", 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM 'filename.dir.ext.exxt' WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath("filename.dir.ext.exxt", 7, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM './sub dir/file.txt' WITH ATTITUDE RATE 7 MOTION RATE 9", new CommandGeneratePath("./sub dir/file.txt", 7, 9));

		// attitude rate number
		validateParseCommand("GENERATE FLIGHT PATH FROM 'filename' WITH ATTITUDE RATE +25 MOTION RATE 9", new CommandGeneratePath("filename", 25, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM 'filename' WITH ATTITUDE RATE +36.77 MOTION RATE 9", new CommandGeneratePath("filename", 36.77, 9));
		validateParseCommand("GENERATE FLIGHT PATH FROM 'filename' WITH ATTITUDE RATE 99.99 MOTION RATE 9", new CommandGeneratePath("filename", 99.99, 9));

		// motion rate number
		validateParseCommand("GENERATE FLIGHT PATH FROM 'filename' WITH ATTITUDE RATE 7 MOTION RATE 1.5", new CommandGeneratePath("filename", 7, 1.5));
		validateParseCommand("GENERATE FLIGHT PATH FROM 'filename' WITH ATTITUDE RATE 7 MOTION RATE +123", new CommandGeneratePath("filename", 7, 123));
		validateParseCommand("GENERATE FLIGHT PATH FROM 'filename' WITH ATTITUDE RATE 7 MOTION RATE +1231.25", new CommandGeneratePath("filename", 7, 1231.25));
	}

	// illegal generate flight path (filename)
	@Test
	void testIllegalGenerateFlightPathFilename() {
		// syntax
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM 'FILENAME'  WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM 'FILENAME'  WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM 'FILENAME'  WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "FLIGHT PATH FROM 'FILENAME'  WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE PATH FROM 'FILENAME'  WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT FROM 'FILENAME'  WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH 'FILENAME'  WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM 'FILENAME' ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM 'FILENAME' WITH RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM 'FILENAME' WITH ATTITUDE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM 'FILENAME' WITH ATTITUDE RATE 7 RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM 'FILENAME' WITH RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM 'FILENAME' WITH ATTUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM 'FILENAME' WITH RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATEFLIGHT PATHFROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATEFLIGHT PATH FROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHTPATH FROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM 'FILENAME' WITH ATTITUDERATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM 'FILENAME' WITHATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHTPATHFROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATHFROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHTPATH FROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM 'FILENAME' WITH ATTITUDE RATE 7 MOTIONRATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING[ + X + Y + Z ] FROM 'FILENAME'  WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATHUSING [ + X + Y + Z ] FROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING [ + X + Y + Z ] FROM'FILENAME'  WITH ATTITUDE RATE 7 MOTION RATE 9");

		// optional using
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING [ X +Y +Z ] FROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING [ + X + Y Z ] FROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING [ + X + Y _ Z ] FROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING [ + X + Y Z Z ] FROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING [  X   Y   Z ] FROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING   + X + Y + Z   FROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING  + X + Y + Z  FROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");
		//validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING [ + Z + Y + Z ] FROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH USING FROM 'FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");

		// filename
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM FILENAME WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM 'FILENAME WITH ATTITUDE RATE 7 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM FILENAME' WITH ATTITUDE RATE 7 MOTION RATE 9");

		// attitude rate number
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE -1 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 0 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE a MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE -0.001 MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE MOTION RATE 9");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE   MOTION RATE 9");

		// motion rate number
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE 0");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE -1");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE -0.0001");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE c");
		validateParseThrows(RuntimeException.class, "GENERATE FLIGHT PATH FROM { [ 1 2 3 ] [ 4 5 6 ] } WITH ATTITUDE RATE 7 MOTION RATE ");
	}

	// 17. @FORCE Attitude
	@Test
	void testForceAttitude() {
		// general
		validateParseCommand("@FORCE ATTITUDE ON id TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitude("id", new Attitude(0, 1, 2)));
		validateParseCommand("@force ATTITUDE ON id TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitude("id", new Attitude(0, 1, 2)));
		validateParseCommand("@FORCE ATtitUDE ON id TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitude("id", new Attitude(0, 1, 2)));
		validateParseCommand("@FORCE ATTITUDE oN id TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitude("id", new Attitude(0, 1, 2)));
		validateParseCommand("@FORCE ATTITUDE ON id To YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitude("id", new Attitude(0, 1, 2)));
		validateParseCommand("@FORCE ATTITUDE ON id TO yaw 0 pitch 1 roll 2", new CommandMetaForceAttitude("id", new Attitude(0, 1, 2)));

		// id
		validateParseCommand("@FORCE ATTITUDE ON id TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitude("id", new Attitude(0, 1, 2)));
		validateParseCommand("@FORCE ATTITUDE ON 123 TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitude("123", new Attitude(0, 1, 2)));
		validateParseCommand("@FORCE ATTITUDE ON ON TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitude("ON", new Attitude(0, 1, 2)));
		validateParseCommand("@FORCE ATTITUDE ON TO TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitude("TO", new Attitude(0, 1, 2)));

		// yaw/pitch/roll combinations
		validateParseCommand("@FORCE ATTITUDE ON id TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitude("id", new Attitude(0, 1, 2, YAW_PITCH_ROLL)));
		validateParseCommand("@FORCE ATTITUDE ON id TO YAW 0 PITCH 1", new CommandMetaForceAttitude("id", new Attitude(0, 1, 2, YAW_PITCH)));
		validateParseCommand("@FORCE ATTITUDE ON id TO YAW 0 ROLL 2", new CommandMetaForceAttitude("id", new Attitude(0, 1, 2, YAW_ROLL)));
		validateParseCommand("@FORCE ATTITUDE ON id TO PITCH 1", new CommandMetaForceAttitude("id", new Attitude(0, 1, 2, PITCH)));
		// PITCH_ROLL in Attitude.generateMask() unreachable
		//validateParseCommand("@FORCE ATTITUDE ON id TO PITCH 1 ROLL 2", new CommandMetaForceAttitude("id", new Attitude(0, 1, 2, PITCH_ROLL)));
		validateParseCommand("@FORCE ATTITUDE ON id TO PITCH 1 ROLL 2", new CommandMetaForceAttitude("id", new Attitude(0, 1, 2, YAW_PITCH_ROLL)));
		validateParseCommand("@FORCE ATTITUDE ON id TO ROLL 2", new CommandMetaForceAttitude("id", new Attitude(0, 1, 2, ROLL)));

		// attitude values
		validateParseCommand("@FORCE ATTITUDE ON id TO YAW -359.99 PITCH 5.5 ROLL 59", new CommandMetaForceAttitude("id", new Attitude(-359.99, 5.5, 59)));
		validateParseCommand("@FORCE ATTITUDE ON id TO YAW 359.99 PITCH -5.4 ROLL -2", new CommandMetaForceAttitude("id", new Attitude(359.99, -5.4, -2)));
		validateParseCommand("@FORCE ATTITUDE ON id TO YAW -5 PITCH -350 ROLL 98.5", new CommandMetaForceAttitude("id", new Attitude(-5, -350, 98.5)));
		validateParseCommand("@FORCE ATTITUDE ON id TO YAW 5 PITCH 350.0 ROLL -65.754", new CommandMetaForceAttitude("id", new Attitude(5, 350.0, -65.754)));
		validateParseCommand("@FORCE ATTITUDE ON id TO YAW 320.0 PITCH 330 ROLL -340.0", new CommandMetaForceAttitude("id", new Attitude(320.0, 330, -340.0)));
		validateParseCommand("@FORCE ATTITUDE ON id TO YAW -320.0 PITCH -5 ROLL 340.0", new CommandMetaForceAttitude("id", new Attitude(-320.0, -5, 340.0)));
		validateParseCommand("@FORCE ATTITUDE ON id TO YAW 8.1 PITCH 5 ROLL -315", new CommandMetaForceAttitude("id", new Attitude(8.1, 5, -315)));
		validateParseCommand("@FORCE ATTITUDE ON id TO YAW -9.3 PITCH -358.0 ROLL 359", new CommandMetaForceAttitude("id", new Attitude(-9.3, -358.0, 359)));
	}

	// illegal force attitude
	@Test
	void testIllegalForceAttitude() {
		// syntax
		validateParseThrows(RuntimeException.class, "@FORCEATTITUDE ON id TO YAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDEON id TO YAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ONid TO YAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON idTO YAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TOYAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO YAW 0PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO YAW 0 PITCH1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO YAW0 PITCH 1ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO YAW0PITCH1ROLL2");
		validateParseThrows(RuntimeException.class, "FORCE ATTITUDE ON id TO YAW 0 PITCH 1 ROLL 2");

		// id
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON TO YAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON i d TO YAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON _ TO YAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON  TO YAW 0 PITCH 1 ROLL 2");

		// attitude combinations
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO PITCH 0 PITCH 1 PITCH 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO YAW 0 YAW 1 YAW 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO ROLL 0 ROLL 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO PITCH 0 YAW 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO ROLL 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO PITCH 0 PITCH 1 YAW 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO PITCH 0 YAW 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO ROLL 0 PITCH 1 YAW 2");

		// attitude values
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO YAW a PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO YAW 0 PITCH b ROLL c");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO YAW 0 PITCH 1 ROLL .");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO YAW 00500 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO YAW 0 PITCH 01000 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO YAW 0 PITCH 1 ROLL 00490");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO YAW -366 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE ON id TO YAW 0 PITCH -373.2 ROLL -360.1");
	}

	// 18. @FORCE Attitude rate
	@Test
	void testForceAttitudeRate() {
		// general
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitudeRate("id", new Attitude(0, 1, 2)));
		validateParseCommand("@force atTITude RATE ON id TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitudeRate("id", new Attitude(0, 1, 2)));
		validateParseCommand("@FORCE ATTITUDE rate ON id TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitudeRate("id", new Attitude(0, 1, 2)));
		validateParseCommand("@FORCE ATTITUDE raTE oN id TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitudeRate("id", new Attitude(0, 1, 2)));
		validateParseCommand("@FORCE ATTITUDE RATE ON id To YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitudeRate("id", new Attitude(0, 1, 2)));
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO yaw 0 pitch 1 roll 2", new CommandMetaForceAttitudeRate("id", new Attitude(0, 1, 2)));

		// id
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitudeRate("id", new Attitude(0, 1, 2)));
		validateParseCommand("@FORCE ATTITUDE RATE ON 123 TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitudeRate("123", new Attitude(0, 1, 2)));
		validateParseCommand("@FORCE ATTITUDE RATE ON ON TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitudeRate("ON", new Attitude(0, 1, 2)));
		validateParseCommand("@FORCE ATTITUDE RATE ON TO TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitudeRate("TO", new Attitude(0, 1, 2)));

		// yaw/pitch/roll combinations
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO YAW 0 PITCH 1 ROLL 2", new CommandMetaForceAttitudeRate("id", new Attitude(0, 1, 2, YAW_PITCH_ROLL)));
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO YAW 0 PITCH 1", new CommandMetaForceAttitudeRate("id", new Attitude(0, 1, 2, YAW_PITCH)));
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO YAW 0 ROLL 2", new CommandMetaForceAttitudeRate("id", new Attitude(0, 1, 2, YAW_ROLL)));
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO PITCH 1", new CommandMetaForceAttitudeRate("id", new Attitude(0, 1, 2, PITCH)));
		// PITCH_ROLL unreachable in Attitude.generateMask()
		//validateParseCommand("@FORCE ATTITUDE RATE ON id TO PITCH 1 ROLL 2", new CommandMetaForceAttitudeRate("id", new Attitude(0, 1, 2, PITCH_ROLL)));
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO PITCH 1 ROLL 2", new CommandMetaForceAttitudeRate("id", new Attitude(0, 1, 2, YAW_PITCH_ROLL)));
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO ROLL 2", new CommandMetaForceAttitudeRate("id", new Attitude(0, 1, 2, ROLL)));

		// attitude values
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO YAW -359 PITCH 5.5 ROLL 59", new CommandMetaForceAttitudeRate("id", new Attitude(-359, 5.5, 59)));
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO YAW 359 PITCH -5.4 ROLL -2", new CommandMetaForceAttitudeRate("id", new Attitude(359, -5.4, -2)));
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO YAW -5 PITCH -357 ROLL 98.5", new CommandMetaForceAttitudeRate("id", new Attitude(-5, -357, 98.5)));
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO YAW 5 PITCH 357.3 ROLL -65.754", new CommandMetaForceAttitudeRate("id", new Attitude(5, 357.3, -65.754)));
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO YAW 359.8 PITCH 357 ROLL -355.4", new CommandMetaForceAttitudeRate("id", new Attitude(359.8, 357, -355.4)));
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO YAW -359.8 PITCH -5 ROLL 355.7", new CommandMetaForceAttitudeRate("id", new Attitude(-359.8, -5, 355.7)));
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO YAW 8.1 PITCH 5 ROLL -357", new CommandMetaForceAttitudeRate("id", new Attitude(8.1, 5, -357)));
		validateParseCommand("@FORCE ATTITUDE RATE ON id TO YAW -9.3 PITCH -357.2 ROLL 355", new CommandMetaForceAttitudeRate("id", new Attitude(-9.3, -357.2, 355)));
	}

	// illegal force attitude rate
	@Test
	void testIllegalForceAttitudeRate() {
		// syntax
		validateParseThrows(RuntimeException.class, "@FORCEATTITUDE RATE ON id TO YAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATEON id TO YAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ONid TO YAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON idTO YAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TOYAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO YAW 0PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO YAW 0 PITCH1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO YAW0 PITCH 1ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO YAW0PITCH1ROLL2");
		validateParseThrows(RuntimeException.class, "FORCE ATTITUDE RATE ON id TO YAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDERATE ON id TO YAW 0 PITCH 1 ROLL 2");

		// id
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON TO YAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON i d TO YAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON _ TO YAW 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON  TO YAW 0 PITCH 1 ROLL 2");

		// attitude combinations
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO PITCH 0 PITCH 1 PITCH 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO YAW 0 YAW 1 YAW 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO ROLL 0 ROLL 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO PITCH 0 YAW 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO ROLL 0 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO PITCH 0 PITCH 1 YAW 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO PITCH 0 YAW 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO ROLL 0 PITCH 1 YAW 2");

		// attitude values
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO YAW a PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO YAW 0 PITCH b ROLL c");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO YAW 0 PITCH 1 ROLL d");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO YAW 00500 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO YAW 0 PITCH 01000 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO YAW 0 PITCH 1 ROLL 00490");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO YAW -366 PITCH 1 ROLL 2");
		validateParseThrows(RuntimeException.class, "@FORCE ATTITUDE RATE ON id TO YAW 0 PITCH -373.2 ROLL -360.1");
	}

	// 19. @FORCE position
	@Test
	void testForcePosition() {
		// general
		validateParseCommand("@FORCE POSITION ON id TO ( 1 2 3 )", new CommandMetaForcePosition("id", new Point3D(1, 2, 3)));
		// YZ unreachable in Point3D.generateMask()
		//validateParseCommand("@force POSITION ON id to ( _ 2 3 )", new CommandMetaForcePosition("id", new Point3D(1, 2, 3, YZ)));
		validateParseCommand("@force POSITION ON id to ( _ 2 3 )", new CommandMetaForcePosition("id", new Point3D(Double.NaN, 2, 3, XYZ)));
		validateParseCommand("@FORCE POSITION on id TO ( 1 _ 3 )", new CommandMetaForcePosition("id", new Point3D(1, 2, 3, XZ)));
		validateParseCommand("@FORCE posITIon ON id TO ( 1 2 _ )", new CommandMetaForcePosition("id", new Point3D(1, 2, 3, XY)));

		// id
		validateParseCommand("@FORCE POSITION ON 123 TO ( 1 2 3 )", new CommandMetaForcePosition("123", new Point3D(1, 2, 3)));
		validateParseCommand("@FORCE POSITION ON ON TO ( 1 2 3 )", new CommandMetaForcePosition("ON", new Point3D(1, 2, 3)));
		validateParseCommand("@FORCE POSITION ON TO TO ( 1 2 3 )", new CommandMetaForcePosition("TO", new Point3D(1, 2, 3)));
		validateParseCommand("@FORCE POSITION ON asdm3k1ADM TO ( 1 2 3 )", new CommandMetaForcePosition("asdm3k1ADM", new Point3D(1, 2, 3)));

		// point mask
		validateParseCommand("@FORCE POSITION ON id TO ( 1 2 3 )", new CommandMetaForcePosition("id", new Point3D(1, 2, 3, XYZ)));
		// YZ unreachable in Point3D.generateMask()
		//validateParseCommand("@FORCE POSITION ON id TO ( _ 2 3 )", new CommandMetaForcePosition("id", new Point3D(1, 2, 3, YZ)));
		validateParseCommand("@FORCE POSITION ON id TO ( _ 2 3 )", new CommandMetaForcePosition("id", new Point3D(Double.NaN, 2, 3, XYZ)));
		validateParseCommand("@FORCE POSITION ON id TO ( 1 _ 3 )", new CommandMetaForcePosition("id", new Point3D(1, 2, 3, XZ)));
		validateParseCommand("@FORCE POSITION ON id TO ( 1 2 _ )", new CommandMetaForcePosition("id", new Point3D(1, 2, 3, XY)));
		validateParseCommand("@FORCE POSITION ON id TO ( _ _ 3 )", new CommandMetaForcePosition("id", new Point3D(1, 2, 3, Z)));
		validateParseCommand("@FORCE POSITION ON id TO ( 1 _ _ )", new CommandMetaForcePosition("id", new Point3D(1, 2, 3, X)));
		validateParseCommand("@FORCE POSITION ON id TO ( _ 2 _ )", new CommandMetaForcePosition("id", new Point3D(1, 2, 3, Y)));

		// point numbers
		validateParseCommand("@FORCE POSITION ON id TO ( 1 -2 -3.4 )", new CommandMetaForcePosition("id", new Point3D(1, -2, -3.4)));
		validateParseCommand("@FORCE POSITION ON id TO ( 0 -9.5 365.5 )", new CommandMetaForcePosition("id", new Point3D(0, -9.5, 365.5)));
		validateParseCommand("@FORCE POSITION ON id TO ( -1.2 2 38 )", new CommandMetaForcePosition("id", new Point3D(-1.2, 2, 38)));
		validateParseCommand("@FORCE POSITION ON id TO ( -1 2.45 +12 )", new CommandMetaForcePosition("id", new Point3D(-1, 2.45, 12)));
		validateParseCommand("@FORCE POSITION ON id TO ( +1 +88 +9.8 )", new CommandMetaForcePosition("id", new Point3D(1, 88, 9.8)));
		validateParseCommand("@FORCE POSITION ON id TO ( +8.92 +71.14 -4 )", new CommandMetaForcePosition("id", new Point3D(8.92, 71.14, -4)));
		validateParseCommand("@FORCE POSITION ON id TO ( 1.89 0 0 )", new CommandMetaForcePosition("id", new Point3D(1.89, 0, 0)));

	}

	// illegal force position
	@Test
	void testIllegalForcePosition() {
		// general
		validateParseThrows(RuntimeException.class, "FORCE POSITION ON id TO ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "@FORCEPOSITION ON id TO ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "@FORCE POSITIONON id TO ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ONid TO ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON idTO ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ONidTO ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON id TO   1 2 3  ");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON id TO  1 2 3 )");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON id TO ( 1 2 3 ");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON id TO  1 2 3 ");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON id TO");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON id TO ");

		// id
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON  TO ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON   TO ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ONTO TO TO ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON _ TO ( 1 2 3 )");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON .. TO ( 1 2 3 )");

		// point mask
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON id TO ( _ _ _ )");

		// point numbers
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON id TO ( a b c )");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON id TO ( 2 3 )");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON id TO ( 1   4 )");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON id TO ( a 4 5 )");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON id TO ( 3 d 6 )");
		validateParseThrows(RuntimeException.class, "@FORCE POSITION ON id TO ( 9 5 k )");
	}

	// 20. @FORCE motion
	@Test
	void testForceMotion() {
		// general
		validateParseCommand("@force MOTION VECTOR ON id TO [ 1 2 3 4 ]", new CommandMetaForceMotionVector("id", new Vector(1, 2, 3, 4)));
		validateParseCommand("@FORCE motion vector ON id TO [ 1 2 3 4 ]", new CommandMetaForceMotionVector("id", new Vector(1, 2, 3, 4)));
		validateParseCommand("@FORCE MOTION VECTOR On id tO [ 1 2 3 4 ]", new CommandMetaForceMotionVector("id", new Vector(1, 2, 3, 4)));

		// id
		validateParseCommand("@FORCE MOTION VECTOR ON 1233 TO [ 1 2 3 4 ]", new CommandMetaForceMotionVector("1233", new Vector(1, 2, 3, 4)));
		validateParseCommand("@FORCE MOTION VECTOR ON ON TO [ 1 2 3 4 ]", new CommandMetaForceMotionVector("ON", new Vector(1, 2, 3, 4)));
		validateParseCommand("@FORCE MOTION VECTOR ON TO TO [ 1 2 3 4 ]", new CommandMetaForceMotionVector("TO", new Vector(1, 2, 3, 4)));
		validateParseCommand("@FORCE MOTION VECTOR ON AWEM3mM TO [ 1 2 3 4 ]", new CommandMetaForceMotionVector("AWEM3mM", new Vector(1, 2, 3, 4)));

		// vector attitudes masks
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ 1 2 3 4 ]", new CommandMetaForceMotionVector("id", new Vector(new Attitude(1, 2, 3, YAW_PITCH_ROLL), 4)));
		// PITCH_ROLL unreachable in Attitude.generateMask()
		//validateParseCommand("@FORCE MOTION VECTOR ON id TO [ _ 2 3 4 ]", new CommandMetaForceMotionVector("id", new Vector(new Attitude(1, 2, 3, PITCH_ROLL), 4)));
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ _ 2 3 4 ]", new CommandMetaForceMotionVector("id", new Vector(new Attitude(1, 2, 3, YAW_PITCH_ROLL), 4)));
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ 1 _ 3 4 ]", new CommandMetaForceMotionVector("id", new Vector(new Attitude(1, 2, 3, YAW_ROLL), 4)));
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ 1 2 _ 4 ]", new CommandMetaForceMotionVector("id", new Vector(new Attitude(1, 2, 3, YAW_PITCH), 4)));
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ 1 _ _ 4 ]", new CommandMetaForceMotionVector("id", new Vector(new Attitude(1, 2, 3, YAW), 4)));
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ _ _ 3 4 ]", new CommandMetaForceMotionVector("id", new Vector(new Attitude(1, 2, 3, ROLL), 4)));
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ _ 2 _ 4 ]", new CommandMetaForceMotionVector("id", new Vector(new Attitude(1, 2, 3, PITCH), 4)));

		// vector attitude numbers
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ -1 -2 -3 4 ]", new CommandMetaForceMotionVector("id", new Vector(-1, -2, -3, 4)));
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ -2.5 +2.4 +5 4 ]", new CommandMetaForceMotionVector("id", new Vector(-2.5, 2.4, 5, 4)));
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ 9.4 +2 +5.9 4 ]", new CommandMetaForceMotionVector("id", new Vector(9.4, 2, 5.9, 4)));
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ +5 -2.5 -9.8 4 ]", new CommandMetaForceMotionVector("id", new Vector(5, -2.5, -9.8, 4)));
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ +3.6 56.5 5.55 4 ]", new CommandMetaForceMotionVector("id", new Vector(3.6, 56.5, 5.55, 4)));

		// vector magnitude
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ 1 2 3 0 ]", new CommandMetaForceMotionVector("id", new Vector(1, 2, 3, 0)));
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ 1 2 3 1.55 ]", new CommandMetaForceMotionVector("id", new Vector(1, 2, 3, 1.55)));
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ 1 2 3 0.99 ]", new CommandMetaForceMotionVector("id", new Vector(1, 2, 3, 0.99)));
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ 1 2 3 +9 ]", new CommandMetaForceMotionVector("id", new Vector(1, 2, 3, 9)));
		validateParseCommand("@FORCE MOTION VECTOR ON id TO [ 1 2 3 +9.5 ]", new CommandMetaForceMotionVector("id", new Vector(1, 2, 3, 9.5)));

	}

	// illegal force motion
	@Test
	void testIllegalForceMotion() {
		// syntax
		validateParseThrows(RuntimeException.class, "FORCE MOTION VECTOR ON a TO [ 1 2 3 4 ]");
		validateParseThrows(RuntimeException.class, "@FORCEMOTION VECTOR ON a TO [ 1 2 3 4 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTIONVECTOR ON a TO [ 1 2 3 4 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTORON a TO [ 1 2 3 4 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO[ 1 2 3 4 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON aTO [ 1 2 3 4 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO  1 2 3 4 ");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO  1 2 3 4 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO [ 1 2 3 4 ");

		// id
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON TO [ 1 2 3 4 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON  TO [ 1 2 3 4 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON _ TO [ 1 2 3 4 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a b TO [ 1 2 3 4 ]");

		// vector attitude masks
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO [ _ _ _ 4 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO [ 1 _ 4 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO [ 1 2 3 _ ]");

		// vector numbers
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO [ 1 2 3 e ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO [ a 2 3 4 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO [ 1 b c 4 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO [ 1 2 d 4 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO [ 1 2 3 ]");

		// vector magnitude
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO [ 1 2 3 -1 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO [ 1 2 3 -0.5 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO [ 1 2 3 b ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO [ 1 2 3 ]");
		validateParseThrows(RuntimeException.class, "@FORCE MOTION VECTOR ON a TO [ 1 2 3   ]");

	}

	// 21. @CONFIG
	@Test
	void testConfig() {
		// general
		validateParseCommand("@CONFIG CLOCK 1 2", new CommandMetaConfigClock(new Time(1), new Time(2)));
		validateParseCommand("@COnFIG CLOCK 1 2", new CommandMetaConfigClock(new Time(1), new Time(2)));
		validateParseCommand("@CONFIG CloCK 1 2", new CommandMetaConfigClock(new Time(1), new Time(2)));

		// time number1
		validateParseCommand("@CONFIG CLOCK +1 2", new CommandMetaConfigClock(new Time(1), new Time(2)));
		validateParseCommand("@CONFIG CLOCK +8.5 2", new CommandMetaConfigClock(new Time(8.5), new Time(2)));
		validateParseCommand("@CONFIG CLOCK 0.99 2", new CommandMetaConfigClock(new Time(0.99), new Time(2)));
		validateParseCommand("@CONFIG CLOCK 0 2", new CommandMetaConfigClock(new Time(0), new Time(2)));

		// time number2
		validateParseCommand("@CONFIG CLOCK 4 +1", new CommandMetaConfigClock(new Time(4), new Time(1)));
		validateParseCommand("@CONFIG CLOCK 5 +8.5", new CommandMetaConfigClock(new Time(5), new Time(8.5)));
		validateParseCommand("@CONFIG CLOCK 6 0.99", new CommandMetaConfigClock(new Time(6), new Time(0.99)));
	}

	// illegal config
	@Test
	void testIllegalConfig() {
		// syntax
		validateParseThrows(RuntimeException.class, "CONFIG CLOCK 1 2");
		validateParseThrows(RuntimeException.class, "@ CLOCK 1 2");
		validateParseThrows(RuntimeException.class, "@CONFIG 1 2");
		validateParseThrows(RuntimeException.class, "CLOCK 1 2");
		validateParseThrows(RuntimeException.class, "@CONFIG CLOCK 2");
		validateParseThrows(RuntimeException.class, "@CONFIG CLOCK   2");
		validateParseThrows(RuntimeException.class, "@CONFIG CLOCK 1  ");

		// time number1
		validateParseThrows(RuntimeException.class, "@CONFIG CLOCK -1 2");
		validateParseThrows(RuntimeException.class, "@CONFIG CLOCK -5.7 2");
		validateParseThrows(RuntimeException.class, "@CONFIG CLOCK -0.1 2");
		validateParseThrows(RuntimeException.class, "@CONFIG CLOCK a 2");

		// time number2
		validateParseThrows(RuntimeException.class, "@CONFIG CLOCK 1 -5");
		validateParseThrows(RuntimeException.class, "@CONFIG CLOCK 1 -0.6");
		validateParseThrows(RuntimeException.class, "@CONFIG CLOCK 1 -6.9");
		validateParseThrows(RuntimeException.class, "@CONFIG CLOCK 1 b");
	}

	// 22. @WAIT
	@Test
	void testWait() {
		// general
		validateParseCommand("@WAIT 1", new CommandMetaWait(new Time(1)));
		validateParseCommand("@wait 1", new CommandMetaWait(new Time(1)));

		// number
		validateParseCommand("@WAIT +1", new CommandMetaWait(new Time(1)));
		validateParseCommand("@WAIT +25.63", new CommandMetaWait(new Time(25.63)));
		validateParseCommand("@WAIT 14.25", new CommandMetaWait(new Time(14.25)));
	}

	// illegal wait
	@Test
	void testIllegalWait() {
		// syntax
		validateParseThrows(RuntimeException.class, "WAIT 1");
		validateParseThrows(RuntimeException.class, "@ 1");
		validateParseThrows(RuntimeException.class, "@WAIT1");

		// number
		validateParseThrows(RuntimeException.class, "@WAIT a");
		validateParseThrows(RuntimeException.class, "@WAIT");
		validateParseThrows(RuntimeException.class, "@WAIT  ");
		validateParseThrows(RuntimeException.class, "@WAIT -0.001");
	}

	// 24. @LOAD
	@Test
	void testLoad() {
		// general
		validateParseCommand("@LOAD 'filename'", new CommandMetaLoad(getTestController(), new File("filename")));
		validateParseCommand("@load 'filename'", new CommandMetaLoad(getTestController(), new File("filename")));

		// string
		validateParseCommand("@LOAD 'C:\\root.txt'", new CommandMetaLoad(getTestController(), new File("C:\\root.txt")));
		validateParseCommand("@LOAD '..\\filename'", new CommandMetaLoad(getTestController(), new File("..\\filename")));
		validateParseCommand("@LOAD 'filename.dir.ext.exxt'", new CommandMetaLoad(getTestController(), new File("filename.dir.ext.exxt")));
		validateParseCommand("@LOAD './sub dir/file.txt'", new CommandMetaLoad(getTestController(), new File("./sub dir/file.txt")));
	}

	// illegal load
	@Test
	void testIllegalLoad() {
		// syntax
		validateParseThrows(RuntimeException.class, "LOAD 'filename'");
		validateParseThrows(RuntimeException.class, "@ 'filename'");
		validateParseThrows(RuntimeException.class, "@'filename'");
		validateParseThrows(RuntimeException.class, "@LOAD'filename'");

		// string
		validateParseThrows(RuntimeException.class, "@LOAD filename'");
		validateParseThrows(RuntimeException.class, "@LOAD filename");
		validateParseThrows(RuntimeException.class, "@LOAD 'filename");
	}

	// 25. @COMMIT
	@Test
	void testCommit() {
		// general
		validateParseCommand("@COMMIT", new CommandMetaCommit());
		validateParseCommand("@commIT", new CommandMetaCommit());
	}

	// illegal commit
	@Test
	void testIllegalCommit() {
		// syntax
		validateParseThrows(RuntimeException.class, "COMMIT");
		validateParseThrows(RuntimeException.class, "@");
		validateParseThrows(RuntimeException.class, "@COMMIT id");
	}

	// 26. @EXIT
	@Test
	void testExit() {
		// general
		validateParseCommand("@EXIT", new CommandMetaExit());
		validateParseCommand("@exit", new CommandMetaExit());
	}

	// illegal exit
	@Test
	void testIllegalExit() {
		// syntax
		validateParseThrows(RuntimeException.class, "EXIT");
		validateParseThrows(RuntimeException.class, "@");
		validateParseThrows(RuntimeException.class, "@EXIT ID");
	}

	// 27. @PAUSE
	@Test
	void testPause() {
		// general
		validateParseCommand("@PAUSE", new CommandMetaPause());
		validateParseCommand("@pause", new CommandMetaPause());
	}

	// illegal pause
	@Test
	void testIllegalPause() {
		// syntax
		validateParseThrows(RuntimeException.class, "PAUSE");
		validateParseThrows(RuntimeException.class, "@");
		validateParseThrows(RuntimeException.class, "@PAUSE ID");
	}

	// 28. @RESUME
	@Test
	void testResume() {
		// general
		validateParseCommand("@RESUME", new CommandMetaResume());
		validateParseCommand("@resUME", new CommandMetaResume());
	}

	// illegal resume
	@Test
	void testIllegalResume() {
		// syntax
		validateParseThrows(RuntimeException.class, "RESUME");
		validateParseThrows(RuntimeException.class, "@");
		validateParseThrows(RuntimeException.class, "@RESUME ID");
	}

	// 29. @DUMP
	@Test
	void testDump() {
		// general
		validateParseCommand("@DUMP COMPONENT id", new CommandMetaDumpComponent("id"));
		validateParseCommand("@dump componENT id", new CommandMetaDumpComponent("id"));
		validateParseCommand("@DUMP COMPONENT id 'hi'", new CommandMetaDumpComponent("id", "hi"));
		// id
		validateParseCommand("@DUMP COMPONENT COMPONENT", new CommandMetaDumpComponent("COMPONENT"));
		validateParseCommand("@DUMP COMPONENT 123", new CommandMetaDumpComponent("123"));
		validateParseCommand("@DUMP COMPONENT MEkMKAwm3", new CommandMetaDumpComponent("MEkMKAwm3"));

		// string
		validateParseCommand("@DUMP COMPONENT id ''", new CommandMetaDumpComponent("id"));
		validateParseCommand("@DUMP COMPONENT id 'asdwae'", new CommandMetaDumpComponent("id", "asdwae"));
		validateParseCommand("@DUMP COMPONENT id '[-+}[]()123 231a31zx,.></?;:+=_-*&*^%#$@!@!#'", new CommandMetaDumpComponent("id", "[-+}[]()123 231a31zx,.></?;:+=_-*&*^%#$@!@!#"));
	}

	// illegal dump
	@Test
	void testIllegalDump() {
		// syntax
		validateParseThrows(RuntimeException.class, "@DUMPCOMPONENT id");
		validateParseThrows(RuntimeException.class, "@DUMPCOMPONENT id 'hi'");
		validateParseThrows(RuntimeException.class, "@DUMP COMPONENT id 'asd' awdm");

		// id
		validateParseThrows(RuntimeException.class, "@DUMP COMPONENT ");
		validateParseThrows(RuntimeException.class, "@DUMP COMPONENT");
		validateParseThrows(RuntimeException.class, "@DUMP COMPONENT  ");

		// string
		validateParseThrows(RuntimeException.class, "@DUMP COMPONENT id 'asd");
		validateParseThrows(RuntimeException.class, "@DUMP COMPONENT id asd'");
		validateParseThrows(RuntimeException.class, "@DUMP COMPONENT id asd");
	}

	// helper method, checks that parse will throw given exception
	private void validateParseThrows(Class thrown, String input) {
		// build dumby controller and parser
		DumbyCommandController controller = getTestController();
		CommandParser parser = new CommandParser(controller, input);

		assertThrows(thrown, parser::parse);
	}

	// helper method to only take in string of commands and expected commands
	// use for testing a sequence of commands
	private void validateParseCommands(String input, List<A_Command> expectedCommands) {
		// build dumby controller and parser
		DumbyCommandController controller = getTestController();
		CommandParser parser = new CommandParser(controller, input);
		parser.parse();

		// validate list of commands from parser
		validateCommandsList(controller.getCommands(), expectedCommands);
	}

	// helper method to only take one string command and one expected command
	private void validateParseCommand(String input, A_Command expectedCommand) {
		validateParseCommands(input, Arrays.asList(expectedCommand));
	}

	// helper method to validate lists of commands from dumby controller
	private void validateCommandsList(List<A_Command> actualCommands, List<A_Command> expectedCommands) {
		// compare values
		Iterator<A_Command> itActual = actualCommands.iterator(), itExpected = expectedCommands.iterator();
		int i = 0;

		while (itActual.hasNext() && itExpected.hasNext()) {
			A_Command actual = itActual.next(), expected = itExpected.next();
			try {
				validateCommands(expected, actual, String.format("Index(%d) mismatch", i));
			} catch (IllegalAccessException e) {
				fail(String.format("Correct reflector access issue"), e);
			}
			++i;
		}

		// validate both empty
		if (itActual.hasNext() || itExpected.hasNext()) {
			fail(String.format("size mismatch, expected %d got %d", expectedCommands.size(), actualCommands.size()));
		}

	}

	// helper method
	// validates both commands are equal
	private void validateCommands(A_Command expected, A_Command actual, String msg) throws IllegalAccessException {
		// compare types
		assertEquals(expected.getClass(), actual.getClass(), String.format("%s, type mismatch", msg));

		// compare fields
		Field[] expectedFields = expected.getClass().getDeclaredFields();
		Field[] actualFields = actual.getClass().getDeclaredFields();

		// sizes should be the same, just in case
		assertEquals(expectedFields.length, actualFields.length, String.format("%s, fields length mismatch", msg));

		// only compare values
		for (int i = 0; i < expectedFields.length; ++i) {
			Field expectedField = expectedFields[i], actualField = actualFields[i];

			// field names should match, just in case
			assertEquals(expectedField.getName(), actualField.getName(), String.format("%s, field name mismatch", msg));

			String cmsg = String.format("%s, field \"%s\"", msg, expectedField.getName());

			expectedField.setAccessible(true);
			actualField.setAccessible(true);

			Object expectedValue = expectedField.get(expected), actualValue = actualField.get(actual);

			// null check
			if ((expectedValue != null && actualValue == null) || (expectedValue == null && actualValue != null)) {
				fail(String.format("%s, null mismatch, expected null(%b), got null(%b)", cmsg, expectedValue == null, actualValue == null));
			} else if (expectedValue == null) {
				continue;
			}

			// check if type is a datatype lacking an equals
			if (expectedValue instanceof Dimension3D) {
				equalsDimension3D((Dimension3D) expectedValue, (Dimension3D) actualValue, cmsg);
			} else if (expectedValue instanceof Point3D) {
				equalsPoint3D((Point3D) expectedValue, (Point3D) actualValue, cmsg);
			} else if (expectedValue instanceof Percent) {
				equalsPercent((Percent) expectedValue, (Percent) actualValue, cmsg);
			} else if (expectedValue instanceof Thrust) {
				equalsThrust((Thrust) expectedValue, (Thrust) actualValue, cmsg);
			} else if (expectedValue instanceof Time) {
				equalsTime((Time) expectedValue, (Time) actualValue, cmsg);
			} else if (expectedValue instanceof Attitude) {
				equalsAttitude((Attitude) expectedValue, (Attitude) actualValue, cmsg);
			} else if (expectedValue instanceof Vector) {
				equalsVector((Vector) expectedValue, (Vector) actualValue, cmsg);
			} else if (expectedValue instanceof File) {
				equalsFile((File) expectedValue, (File) actualValue, cmsg);
			} else if (expectedValue instanceof List) {
				equalsList((List) expectedValue, (List) actualValue, cmsg);
			} else {
				assertEquals(expectedValue, actualValue, String.format("%s, value mismatch, type: %s", cmsg, expectedField.getType()));
			}
		}
	}

	// helper method for lists
	// determines subtype to check
	private void equalsList(List expectedList, List actualList, String msg) {
		// check size
		assertEquals(expectedList.size(), actualList.size(), String.format("%s, list size mismatch", msg));

		// determine subtype
		Object listValue = expectedList.get(0);
		if (listValue instanceof Point3D) {
			equalsListPoint3D(expectedList, actualList, msg);
		} else {
			// just use base comparison
			assertEquals(expectedList, actualList, String.format("%s, list mismatch, item type: %s", msg, listValue.getClass()));
		}
	}

	// helper methods, equals for List of Point3D
	private void equalsListPoint3D(List<Point3D> expectedList, List<Point3D> actualList, String msg) {
		// iterators
		Iterator<Point3D> expectedIt = expectedList.iterator(), actualIt = actualList.iterator();

		int i = 0;
		while (expectedIt.hasNext() && actualIt.hasNext()) {
			Point3D expected = expectedIt.next(), actual = actualIt.next();

			equalsPoint3D(expected, actual, String.format("%s, index(%d) mismatch", msg, i));
			++i;
		}

		// check if any remaining
		if (expectedIt.hasNext() || actualIt.hasNext()) {
			fail(String.format("%s, size mismatch expected(%d), got(%d)", msg, expectedList.size(), actualList.size()));
		}

	}

	// helper equals method for Dimension3D
	private void equalsDimension3D(Dimension3D expected, Dimension3D actual, String msg) {
		assertEquals(expected.getDepth(), actual.getDepth(), String.format("%s, depth mismatch", msg));
		assertEquals(expected.getHeight(), actual.getHeight(), String.format("%s, height mismatch", msg));
		assertEquals(expected.getWidth(), actual.getWidth(), String.format("%s, width mismatch", msg));
	}

	// helper equals method for Point3D
	private void equalsPoint3D(Point3D expected, Point3D actual, String msg) {
		// compare mask
		assertEquals(expected.getMask(), actual.getMask(), String.format("%s, mask mismatch", msg));

		// compare values, based on mask
		Point3D.E_Mask mask = expected.getMask();
		// x check
		if (mask == X || mask == XY || mask == XZ || mask == XYZ) {
			assertEquals(expected.getX(), actual.getX(), validDelta, String.format("%s, X mismatch", msg));
		}
		// y check
		if (mask == Y || mask == XY || mask == YZ || mask == XYZ) {
			assertEquals(expected.getY(), actual.getY(), validDelta, String.format("%s, Y mismatch", msg));
		}
		// z check
		if (mask == Z || mask == XZ || mask == YZ || mask == XYZ) {
			assertEquals(expected.getZ(), actual.getZ(), validDelta, String.format("%s, Z mismatch", msg));
		}
	}

	// helper method for Percent
	private void equalsPercent(Percent expected, Percent actual, String msg) {
		assertEquals(expected.getValue(), actual.getValue(), validDelta, String.format("%s, percent mismatch", msg));
	}

	// helper method for Thrust
	private void equalsThrust(Thrust expected, Thrust actual, String msg) {
		equalsPercent(expected.getThrust(), actual.getThrust(), String.format("%s, thrust mistmatch", msg));
	}

	// helper method for Time
	private void equalsTime(Time expected, Time actual, String msg) {
		assertEquals(expected.getValue(), actual.getValue(), validDelta, String.format("%s, time mismatch", msg));
	}

	// helper method for Attitude
	private void equalsAttitude(Attitude expected, Attitude actual, String msg) {
		// compare masks
		assertEquals(expected.getMask(), actual.getMask(), String.format("%s, mask mismatch", msg));

		// compare based on masks
		Attitude.E_Mask mask = expected.getMask();
		// yaw check
		if (mask == YAW || mask == YAW_PITCH || mask == YAW_ROLL || mask == YAW_PITCH_ROLL) {
			equalsAttitudeAxis(expected.getYaw(), expected.getYaw(), String.format("%s, yaw mismatch", msg));
		}
		// pitch check
		if (mask == YAW_PITCH || mask == PITCH || mask == PITCH_ROLL || mask == YAW_PITCH_ROLL) {
			equalsAttitudeAxis(expected.getPitch(), expected.getPitch(), String.format("%s, pitch mismatch", msg));
		}
		// roll check
		if (mask == YAW_ROLL || mask == PITCH_ROLL || mask == ROLL || mask == YAW_PITCH_ROLL) {
			equalsAttitudeAxis(expected.getRoll(), expected.getRoll(), String.format("%s, roll mismatch", msg));
		}
	}

	// compare sub attitude
	private void equalsAttitudeAxis(A_Attitude expected, A_Attitude actual, String msg) {
		assertEquals(expected.getValue(), actual.getValue(), validDelta, msg);
	}

	// helper method for Vector
	private void equalsVector(Vector expected, Vector actual, String msg) {
		// vector
		equalsAttitude(expected.getAttitude(), actual.getAttitude(), String.format("%s, vector mismatch", msg));
		// magnitude
		assertEquals(expected.getMagnitude(), actual.getMagnitude(), validDelta, String.format("%s, vector magnitude mismatch", msg));
	}

	// helper method for File
	private void equalsFile(File expected, File actual, String msg) {
		assertEquals(expected.getName(), actual.getName(), String.format("%s, filename mismatch", msg));
	}

	// dumby classes for tests
	private DumbyProjectServer dumbyServer;
	private DumbyProjectEngine dumbyEngine;
	private DumbyCommandController dumbyController;
	private boolean setDumby = false;

	// gets and resets the controller for this test
	// to avoid instantiating new ones for each test
	private DumbyCommandController getTestController() {
		if (!setDumby) {
			dumbyServer = new DumbyProjectServer();
			try {
				dumbyServer.stop();
			} catch (Exception e) {
				fail("Handle exception", e);
			}
			dumbyEngine = new DumbyProjectEngine(dumbyServer);
			dumbyController = new DumbyCommandController(dumbyEngine);

			setDumby = true;
		}
		dumbyController.WipeCommands();
		return dumbyController;
	}
}
