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

	public String printXML() {
		return String.format("<triple x=\"%f\" y=\"%f\" z=\"%f\"/>\n", x, y, z);
	}
}
