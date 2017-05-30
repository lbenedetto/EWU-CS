package s17cs350task1;


import java.util.ArrayList;
import java.util.List;

public class ComponentBox extends A_Component {
	private Dimension3D size;

	public ComponentBox(String id, Dimension3D size) {
		super(id, false);
		if (size == null) throw new TaskException("size passed to box was null");
		this.size = size;
	}

	public ComponentBox(String id, Dimension3D size, boolean isRoot) {
		super(id, isRoot);
		this.size = size;
	}


	public double calculateAreaAll(BoundingBox.E_Plane plane) {
		if (plane == null)
			throw new TaskException("Plane was null");
		double d = calculateAreaSelf(plane);
		for (A_Component b : getDescendants()) {
			d += b.calculateAreaSelf(plane);
		}
		return d;
	}

	public double calculateAreaSelf(BoundingBox.E_Plane plane) {
		if (plane == null)
			throw new TaskException("Plane was null");
		return generateBoundingBoxSelf().calculateArea(plane);
	}

	public Point3D calculateCenterOfMassAll() {
		return generateBoundingBoxAll().getCenter();
	}

	public Point3D calculateCenterOfMassSelf() {
		return getAbsoluteCenterPosition();
	}

	public BoundingBox generateBoundingBoxAll() {
		BoundingBox bb = generateBoundingBoxSelf();
		for (A_Component b : getDescendants()) {
			bb = bb.extend(b.generateBoundingBoxSelf());
		}
		return bb;
	}

	public BoundingBox generateBoundingBoxSelf() {
		return new BoundingBox(getAbsoluteCenterPosition(), size);
	}

	public double calculateVolumeAll() {
		double d = calculateVolumeSelf();
		for (A_Component b : getDescendants()) {
			d += b.calculateVolumeSelf();
		}
		return d;
	}

	public double calculateVolumeSelf() {
		return generateBoundingBoxSelf().calculateVolume();
	}

	@Override
	public ComponentBox clone() throws CloneNotSupportedException {
		ComponentBox clone = (ComponentBox) super.clone();
		clone.size = size.clone();
		return clone;
	}

	@Override
	public String export(A_Exporter exporter) {
		if (exporter == null) throw new TaskException("Exporter was null");
		if (isRoot()) exporter.openComponentNode(getID());
		else exporter.openComponentNode(getID(), getConnectorToParent().getComponentParent().getID());
		Point3D[] points = generateFrameSelf().toArray(new Point3D[9]);
		int i = 0;
		exporter.addPoint("center", points[i++]);
		exporter.addPoint("left-bottom-far", points[i++]);
		exporter.addPoint("right-bottom-far", points[i++]);
		exporter.addPoint("right-bottom-near", points[i++]);
		exporter.addPoint("left-bottom-near", points[i++]);
		exporter.addPoint("left-top-far", points[i++]);
		exporter.addPoint("right-top-far", points[i++]);
		exporter.addPoint("right-top-near", points[i++]);
		exporter.addPoint("left-top-near", points[i]);
		exporter.closeComponentNode(getID());
		getChildren().forEach(b -> b.export(exporter));
		if (isRoot()) exporter.closeExport();
		return exporter.export();
	}

	public List<List<Point3D>> generateFramesAll() {
		List<List<Point3D>> points = new ArrayList<>();
		points.add(generateFrameSelf());
		for (A_Component b : getDescendants()) {
			points.add(b.generateFrameSelf());
		}
		return points;
	}

	public List<Point3D> generateFrameSelf() {
		List<Point3D> points = new ArrayList<>();
		points.add(getAbsoluteCenterPosition());
		points.addAll(generateBoundingBoxSelf().generateCorners());
		return points;
	}

	public Dimension3D getSize() {
		return new Dimension3D(size.getWidth(), size.getHeight(), size.getDepth());
	}

	@Override
	public String toString() {
		StringBuilder out = new StringBuilder(String.format("ComponentBox[id='%s', isRoot=%s, size=%s]", getID(), isRoot(), size));
		for (Connector c : getConnectorsToChildren())
			out.append("\n").append(c.toString());
		return out.toString().replaceAll("\n", "\n\t");
	}
}
