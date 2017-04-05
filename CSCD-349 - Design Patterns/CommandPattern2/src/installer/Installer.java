package installer;

import installer.command.*;

class Installer {
	private Command[] commands;

	//This is the Invoker
	Installer(Command[] commands) {
		this.commands = commands;
	}

	void install() {
		System.out.println("Installing OS...");
		double totalTimeEstimate = 0;
		double timeCompleted = 0;
		double percentCompleted;
		for (Command c : commands) {
			totalTimeEstimate += c.estimateTime();
		}
		for (Command c : commands) {
			c.execute();
			timeCompleted += c.estimateTime();
			percentCompleted = (timeCompleted / totalTimeEstimate) * 100;
			System.out.print((int)percentCompleted + "% : [");
			for (int i = 0; i < 100; i++) {
				if (i < percentCompleted)
					System.out.print("=");
				else
					System.out.print(" ");
			}
			System.out.println("]");
		}
	}
}
