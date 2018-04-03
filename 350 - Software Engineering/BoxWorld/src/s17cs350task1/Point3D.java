package s17cs350task1;

public class Point3D implements Cloneable{
	private double x, y, z;

	public Point3D(double x, double y, double z) {
		if (Double.isNaN(x + y + z)) throw new TaskException("x y or z was NaN");
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3D add(Point3D p) {
		if (p == null) throw new TaskException("p was null");
		return new Point3D(x + p.x, y + p.y, z + p.z);
	}

	public Point3D subtract(Point3D p) {
		if (p == null) throw new TaskException("p was null");
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Point3D point3D = (Point3D) o;
		return Double.compare(point3D.x, x) == 0 && Double.compare(point3D.y, y) == 0 && Double.compare(point3D.z, z) == 0;
	}
}
