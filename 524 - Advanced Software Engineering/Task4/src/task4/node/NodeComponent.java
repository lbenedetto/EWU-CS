package task4.node;

import java.util.ArrayList;
import java.util.List;

public class NodeComponent {
	NodeTriple volume;
	NodeSocket socket;
	String identifier;
	List<NodeSubcomponentMount> subcomponentMountList;

	public NodeComponent(String identifier, NodeTriple volume, NodeSocket socket, List<NodeSubcomponentMount> subcomponentMountList) {
		this.volume = volume;
		this.socket = socket;
		this.identifier = identifier;
		this.subcomponentMountList = subcomponentMountList;
		if (subcomponentMountList == null) this.subcomponentMountList = new ArrayList<>();
	}

	public String getIdentifier() {
		return identifier;
	}

	public void exportToGnuplot(){

	}

	public void printXML(){

	}
}
