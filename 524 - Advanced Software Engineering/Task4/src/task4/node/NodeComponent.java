package task4.node;

import java.util.ArrayList;
import java.util.List;

public class NodeComponent {
	private NodeTriple volume;
	private NodeTriple socket;
	private String identifier;
	private List<NodeSubcomponentMount> subcomponentMountList;

	public NodeComponent(String identifier, NodeTriple volume, NodeTriple socket, List<NodeSubcomponentMount> subcomponentMountList) {
		this.volume = volume;
		this.socket = socket;
		this.identifier = identifier.substring(1, identifier.length() - 1);
		this.subcomponentMountList = subcomponentMountList;
		if (subcomponentMountList == null) this.subcomponentMountList = new ArrayList<>();
	}

	public String getIdentifier() {
		return identifier;
	}

	public String exportToGnuplot(NodeTriple origin) {
		NodeTriple half = volume.half();
		NodeTriple shifted = origin.subtract(socket);
		double uX = shifted.x + half.x;
		double dX = shifted.x - half.x;
		double uY = shifted.y + half.y;
		double dY = shifted.y - half.y;
		double uZ = shifted.z + half.z;
		double dZ = shifted.z - half.z;

		StringBuilder s = new StringBuilder();
		s.append(String.format("# component [%s] {\n# top\n", identifier));
		s.append(String.format("%f %f %f\n", dX, dY, uZ));
		s.append(String.format("%f %f %f\n", uX, dY, uZ));
		s.append(String.format("%f %f %f\n", uX, uY, uZ));
		s.append(String.format("%f %f %f\n", dX, uY, uZ));
		s.append(String.format("%f %f %f\n\n", dX, dY, uZ));
		s.append("# bottom\n");
		s.append(String.format("%f %f %f\n", dX, dY, dZ));
		s.append(String.format("%f %f %f\n", uX, dY, dZ));
		s.append(String.format("%f %f %f\n", uX, uY, dZ));
		s.append(String.format("%f %f %f\n", dX, uY, dZ));
		s.append(String.format("%f %f %f\n\n\n", dX, dY, dZ));

		if (subcomponentMountList != null && !subcomponentMountList.isEmpty()) {
			s.append("# subcomponents {\n");
			subcomponentMountList.forEach(node -> s.append(node.exportToGnuplot(shifted)));
			s.append("# } subcomponents\n");
		}
		s.append(String.format("# } component [%s]\n", identifier));
		return s.toString();
	}

	public String printXML() {
		StringBuilder s = new StringBuilder();
		s.append(String.format("<component identifier=\"%s\">\n", identifier));
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