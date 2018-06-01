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

	public NodeTriple(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String printXML() {
		return String.format("<triple x=\"%f\" y=\"%f\" z=\"%f\"/>\n", x, y, z);
	}

	public NodeTriple add(NodeTriple other) {
		return new NodeTriple(
				x + other.x,
				y + other.y,
				z + other.z
		);
	}

	public NodeTriple subtract(NodeTriple other) {
		return new NodeTriple(
				x - other.x,
				y - other.y,
				z - other.z
		);
	}

	public NodeTriple divide(double d) {
		return new NodeTriple(
				x / d,
				y / d,
				z / d
		);
	}
}
