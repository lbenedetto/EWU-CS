package s17cs350task1;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Comparator;
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
		children = new ArrayList<>();
	}

	public Box clone() throws CloneNotSupportedException {
		Box box = (Box) super.clone();
		box.size = new Dimension2D(size.getWidth(), size.getHeight());
		box.children = new ArrayList<>();
		for (Connector c : this.children)
			box.connectChild(c.clone());
		return box;
	}

	public void connectChild(Connector connector) {
		connector.setParentBox(this);
		children.add(connector);
	}

	public Point2D getAbsoluteCenterPosition() {
		Point2D p = new Point2D(0, 0);
		if (hasConnectorToParent())
			p = p.add(parent.getOffsetFromParentBox()).add(parent.getParentBox().getAbsoluteCenterPosition());
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

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("");
		s.append(getId());
		for (Connector c : children) {
			s.append(c.toString());
		}
		return s.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Box box = (Box) o;
		return isRoot == box.isRoot &&
				id.equals(box.id) &&
				size.equals(box.size) &&
				children.equals(box.children);
	}

	private class BoxComparator implements Comparator<Box> {
		@Override
		public int compare(Box o1, Box o2) {
			return o1.id.compareTo(o2.id);
		}
	}
}
