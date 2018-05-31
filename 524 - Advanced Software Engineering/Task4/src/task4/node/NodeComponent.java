package task4.node;

import java.util.ArrayList;
import java.util.List;

public class NodeComponent {
	NodeTriple volume;
	NodeTriple socket;
	String identifier;
	List<NodeSubcomponentMount> subcomponentMountList;

	public NodeComponent(String identifier, NodeTriple volume, NodeTriple socket, List<NodeSubcomponentMount> subcomponentMountList) {
		this.volume = volume;
		this.socket = socket;
		this.identifier = identifier;
		this.subcomponentMountList = subcomponentMountList;
		if (subcomponentMountList == null) this.subcomponentMountList = new ArrayList<>();
	}

	public String getIdentifier() {
		return identifier;
	}

	public String exportToGnuplot() {
		return "";
	}

	public String printXML() {
		StringBuilder s = new StringBuilder();
		s.append(String.format("<component identifier=%s>\n", identifier));
		s.append("<size>\n");
		s.append(volume.printXML());
		s.append("</size>\n");
		s.append("<socket>\n");
		s.append(socket.printXML());
		s.append("</socket>\n");
		if (subcomponentMountList != null && !subcomponentMountList.isEmpty()) {
			s.append("<connections>\n");
			subcomponentMountList.forEach(node -> s.append(node.printXML()));
			s.append("</connections>\n");
		}
		s.append("</component>\n");
		return s.toString();
	}
}