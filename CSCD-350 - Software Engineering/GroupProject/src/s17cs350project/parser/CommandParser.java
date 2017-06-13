package s17cs350project.parser;

import s17cs350project.command.A_Command;
import s17cs350project.command.CommandController;
import s17cs350project.support.ProjectException;

public class CommandParser {
	private CommandController controller;
	private String commands;

	public CommandParser(CommandController controller, String commands) {
		if(controller==null || commands == null) throw new RuntimeException("controller or commands were null");
		this.controller = controller;
		this.commands = commands;
	}

	public A_Command parse() {
		CommandPattern matchedPattern;
		String[] commandsAr = commands.split("\n");
		if(commandsAr.length == 0) throw new ProjectException(String.format("No commands: \"%s\"", commands));
		for (String command : commandsAr) {
			matchedPattern = null;
			// find pattern
			for (CommandPattern pattern : CommandPattern.values()) {
				if (pattern.interpret(command, controller)) {
					matchedPattern = pattern;
					break;
				}
			}
			// check if matched
			if (matchedPattern == null) {
				throw new ProjectException(String.format("Unrecognized command: \"%s\"", command));
			}
		}
		return null;
	}
}
