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
		return 0;//TODO: Throw exception instead?
	}

	public double calculateVolume() {
		return size.getDepth() * size.getHeight() * size.getWidth();
	}

	public BoundingBox extend(BoundingBox boundingBox) {
		List<Point3D> b1Corners = this.generateCorners();
		List<Point3D> b2Corners = boundingBox.generateCorners();
		//TODO:Figure out how to do this
		return new BoundingBox(this.center, this.size);
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
		return center;
	}

	public Dimension3D getSize() {
		return size;
	}

	public String toString() {
		return "[" + center.toString() + "," + size.toString() + "]";
	}

	public enum E_Plane implements Serializable, Comparable<E_Plane> {
		XY, YZ, XZ
	}
}
