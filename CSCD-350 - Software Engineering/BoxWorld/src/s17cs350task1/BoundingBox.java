package s17cs350task1;

import javafx.geometry.Point3D;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoundingBox implements Cloneable {
	private Point3D center;
	private Dimension3D size;

	public BoundingBox(Point3D center, Dimension3D size) {
		this.center = center;
		this.size = size;
	}

	@Override
	public BoundingBox clone() throws CloneNotSupportedException {
		BoundingBox clone = (BoundingBox) super.clone();
		clone.center = new Point3D(center.getX(), center.getY(), center.getZ());
		clone.size = size.clone();
		return clone;
	}

	public double calculateArea(E_Plane plane) {
		switch (plane) {
			case XY:
				return (size.getWidth() * size.getHeight()) * 2;
			case XZ:
				return (size.getWidth() * size.getDepth()) * 2;
			case YZ:
				return (size.getDepth() * size.getHeight()) * 2;
		}
		throw new TaskException("The universe is broken");
	}

	public double calculateVolume() {
		return size.getDepth() * size.getHeight() * size.getWidth();
	}

	public BoundingBox extend(BoundingBox boundingBox) {
		List<Point3D> corners = this.generateCorners();
		corners.addAll(boundingBox.generateCorners());
		double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE, maxZ = Double.MIN_VALUE;
		double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
		for (int i = 0; i < 16; i++) {
			Point3D p = corners.get(i);
			double x = p.getX();
			maxX = Math.max(maxX, x);
			minX = Math.min(minX, x);
			double y = p.getY();
			maxY = Math.max(maxX, y);
			minY = Math.min(minX, y);
			double z = p.getZ();
			maxZ = Math.max(maxZ, z);
			minZ = Math.min(minZ, z);
		}
		double w, h, d;
		w = maxX - minX;
		h = maxY - minY;
		d = maxZ - minZ;
		return new BoundingBox(new Point3D(maxX - (w / 2), maxY - (h / 2), maxZ - (d / 2)),
				new Dimension3D(w, h, d));
	}

	public List<Point3D> generateCorners() {
		List<Point3D> corners = new ArrayList<>();
		double w, h, d, x, y, z;
		w = size.getWidth() / 2;
		h = size.getHeight() / 2;
		d = size.getDepth() / 2;
		x = center.getX();
		y = center.getY();
		z = center.getZ();
		corners.add(new Point3D(x - w, y - h, z - d));//left bottom far
		corners.add(new Point3D(x + w, y - h, z - d));//right bottom far
		corners.add(new Point3D(x + w, y - h, z + d));//right bottom near
		corners.add(new Point3D(x - w, y - h, z + d));//left bottom near
		corners.add(new Point3D(x - w, y + h, z - d));//left top far
		corners.add(new Point3D(x + w, y + h, z - d));//right top far
		corners.add(new Point3D(x + w, y + h, z + d));//right top near
		corners.add(new Point3D(x - w, y + h, z + d));//left top near
		return corners;
	}

	public Point3D getCenter() {
		return new Point3D(center.getX(), center.getY(), center.getZ());
	}

	public Dimension3D getSize() {
		return size;
	}

	public String toString() {
		return String.format("[%s, %s]", center.toString(), size.toString());
	}

	public enum E_Plane implements Serializable, Comparable<E_Plane> {
		XY, YZ, XZ
	}
}
