package task4;

import task4.node.NodeComponent;

import java.util.HashMap;

public class Tree {
	private static HashMap<String, NodeComponent> components = new HashMap<>();
	private static HashMap<String, NodeComponent> variables = new HashMap<>();

	public static void addVariable(String variableName, NodeComponent nodeComponent) {
		variables.put(variableName, nodeComponent);
	}

	public static void addComponent(NodeComponent nodeComponent) {
		components.put(nodeComponent.getIdentifier(), nodeComponent);
	}
}
