package s17cs350task1;

public class Dimension3D implements Cloneable {
	private double width;
	private double height;
	private double depth;

	public Dimension3D(double width, double height, double depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
	}

	public double getWidth() {
		return width;
	}

	public Dimension3D setWidth(double width) {
		this.width = width;
		return this;
	}

	public double getHeight() {
		return height;
	}

	public Dimension3D setHeight(double height) {
		this.height = height;
		return this;
	}

	public double getDepth() {
		return depth;
	}

	public Dimension3D setDepth(double depth) {
		this.depth = depth;
		return this;
	}

	@Override
	public Dimension3D clone() throws CloneNotSupportedException {
		return (Dimension3D) super.clone();
	}

	@Override
	public String toString() {
		return "[" + width + " " + height + " " + depth + "]";
	}
}
