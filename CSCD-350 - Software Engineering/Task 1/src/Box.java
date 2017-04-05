import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Box implements Cloneable {
	private String id;
	private Dimension2D size;
	private boolean isRoot;
	private ArrayList<Connector> children;
	private Connector parent;

	public Box(String id, Dimension2D size) {
		this.id = id;
		this.size = size;
		children = new ArrayList<>();
	}

	public Box(String id, Dimension2D size, boolean isRoot) {
		this(id, size);
		this.isRoot = isRoot;
	}

	public Box clone() throws CloneNotSupportedException {
		Box box = (Box) super.clone();
		box.id = id;
		box.size = new Dimension2D(size.getWidth(), size.getHeight());
		box.isRoot = isRoot;
		box.parent = parent.clone();
		ArrayList<Connector> children = new ArrayList<>();
		for (Connector c : this.children)
			children.add(c.clone());
		box.children = children;
		return box;
	}

	public void connectChild(Connector connector) {
		connector.setParentBox(this);
		children.add(connector);
	}

	public Point2D getAbsoluteCenterPosition() {
		Point2D p = new Point2D(0, 0);
		if (hasConnectorToParent())
			p.add(parent.getOffsetFromParentBox());
		return p;
	}

	public int getChildBoxCount() {
		return getChildBoxes().size();
	}

	public List<Box> getChildBoxes() {
		ArrayList<Box> boxes = new ArrayList<>();
		children.forEach(x -> boxes.add(x.getChildBox()));
		return boxes;
	}

	public List<Connector> getConnectorsToChildren() {
		return children;
	}

	public Connector getConnectorToParent() {
		if (hasConnectorToParent()) return parent;
		return null;
	}

	public void setConnectorToParent(Connector connector) {
		this.parent = connector;
	}

	public int getDescendantBoxCount() {
		return getDescendantBoxes().size();
	}

	public List<Box> getDescendantBoxes() {
		ArrayList<Box> boxes = new ArrayList<>();
		for (Connector c : children) {
			Box child = c.getChildBox();
			boxes.add(child);
			boxes.addAll(child.getChildBoxes());
		}
		return boxes;
	}

	public String getId() {
		return id;
	}

	public Dimension2D getSize() {
		return size;
	}

	public boolean hasConnectorToParent() {
		return parent != null;
	}

	public boolean isRoot() {
		return isRoot;
	}
}
