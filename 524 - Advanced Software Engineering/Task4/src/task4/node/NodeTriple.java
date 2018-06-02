package task4.node;

public class NodeTriple {
	public double x;
	public double y;
	public double z;

	public NodeTriple(String x, String y, String z) {
		this.x = Double.parseDouble(x);
		this.y = Double.parseDouble(y);
		this.z = Double.parseDouble(z);
	}

	private NodeTriple(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String printXML() {
		return String.format("<triple x=\"%f\" y=\"%f\" z=\"%f\"/>\n", x, y, z);
	}

	NodeTriple add(NodeTriple other) {
		return new NodeTriple(
				x + other.x,
				y + other.y,
				z + other.z
		);
	}

	NodeTriple subtract(NodeTriple other) {
		return new NodeTriple(
				x - other.x,
				y - other.y,
				z - other.z
		);
	}

	NodeTriple half() {
		return new NodeTriple(
				x / 2.0,
				y / 2.0,
				z / 2.0
		);
	}
}
