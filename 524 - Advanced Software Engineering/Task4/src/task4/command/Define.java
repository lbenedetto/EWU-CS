package task4.command;

import task4.node.NodeComponent;

public class Define implements Command{
	String variable;
	NodeComponent nodeComponent;

	public Define(String variable, NodeComponent nodeComponent) {
		this.variable = variable;
		this.nodeComponent = nodeComponent;
	}

	@Override
	public void execute() {

	}
}
