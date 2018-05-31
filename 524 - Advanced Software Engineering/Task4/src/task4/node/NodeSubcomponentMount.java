package task4.node;

public class NodeSubcomponentMount {
	private NodeComponent nodeComponent;
	private NodeTriple nodeTriple;

	public NodeSubcomponentMount(NodeComponent nodeComponent, NodeTriple nodeTriple) {
		this.nodeComponent = nodeComponent;
		this.nodeTriple = nodeTriple;
	}

	public String exportToGnuplot() {
		return "";
	}

	public String printXML() {
		return "<mount>\n" +
				nodeComponent.printXML() +
				"<ball>\n" +
				nodeTriple.printXML() +
				"</ball>\n" +
				"</mount>\n";
	}
}
