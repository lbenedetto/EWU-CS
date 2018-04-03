package s17cs350task1;

public class Dimension3D implements Cloneable {
	private double width;
	private double height;
	private double depth;

	public Dimension3D(double width, double height, double depth) {
		if (width <= 0 || height <= 0 || depth <= 0) throw new TaskException("Invalid box size");
		if (Double.isNaN(width + height + depth)) throw new TaskException("NaN size");
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

	@Override
	public String toString() {
		return String.format("Dimension3D [w = %s, h = %s, d = %s]", width, height, depth);
	}

	@Override
	public boolean equals(Object o) {
		Dimension3D that = (Dimension3D) o;
		return (that != null && this.height == that.height && this.width == that.width && this.depth == that.depth);
	}
}
