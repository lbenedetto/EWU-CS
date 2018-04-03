package s17cs350project.tests;

import s17cs350project.command.A_Command;
import s17cs350project.command.CommandController;
import s17cs350project.command.CommandGeneratePath;
import s17cs350project.datatype.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import s17cs350project.component.thruster.A_Thruster.*;
import s17cs350project.datatype.Vector;

/**
 * Dumby controller for testing
 * DO NOT UPLOAD FOR ASSIGNMENT
 */
public class DumbyCommandController extends CommandController {

	// internal list for commands received
	private List<A_Command> commands = new ArrayList<>();

	// constructor
	public DumbyCommandController(DumbyProjectEngine engine) {
		super(engine);
	}

	// get internal list
	public List<A_Command> getCommands() {
		return Collections.unmodifiableList(commands);
	}

	// wipe internal list
	public void WipeCommands() {
		commands.clear();
	}

	// overrided schedule to check against
	@Override
	public void schedule(A_Command command) {
		commands.add(command);
	}

	// for CommandMetaLoad
	@Override
	public void doMetaLoad(File commandFile) {
		// do nothing
	}

}
