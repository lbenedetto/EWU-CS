package task4.command;

import task4.node.NodeTriple;

public class ExportToGnuplot implements Command {
	private String variable;
	private NodeTriple triple;

	public ExportToGnuplot(String variable, NodeTriple triple){
		this.variable = variable;
		this.triple = triple;
	}
	@Override
	public void execute() {

	}
}
