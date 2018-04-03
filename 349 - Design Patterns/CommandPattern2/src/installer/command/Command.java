package installer.command;

public interface Command {
	void execute();
	int estimateTime();
}
