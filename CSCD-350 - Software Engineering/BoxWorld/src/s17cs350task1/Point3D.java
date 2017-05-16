package s17cs350task1;

public class Point3D {
	private double x, y, z;

	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3D add(Point3D p) {
		return new Point3D(x + p.x, y + p.y, z + p.z);
	}

	public Point3D subtract(Point3D p) {
		return new Point3D(x - p.x, y - p.y, z - p.z);

	}

	@Override
	public Point3D clone() throws CloneNotSupportedException {
		Point3D clone = (Point3D) super.clone();
		clone.x = x;
		clone.y = y;
		clone.z = z;
		return clone;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public String toString() {
		return String.format("Point3D [x = %s, y = %s, z = %s]", x, y, z);
	}
}
