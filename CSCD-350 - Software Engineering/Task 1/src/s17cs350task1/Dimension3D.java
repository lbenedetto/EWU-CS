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

	public double getHeight() {
		return height;
	}

	public double getDepth() {
		return depth;
	}

	@Override
	public Dimension3D clone() throws CloneNotSupportedException {
		return (Dimension3D) super.clone();
	}
}
