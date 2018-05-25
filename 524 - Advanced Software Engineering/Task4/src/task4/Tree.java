package task4;

import task4.node.NodeComponent;

import java.util.HashMap;

public class Tree {
	private static HashMap<String, NodeComponent> components = new HashMap<>();

	public void addComponent(NodeComponent nodeComponent) {
		components.put(nodeComponent.getIdentifier(), nodeComponent);
	}
}
