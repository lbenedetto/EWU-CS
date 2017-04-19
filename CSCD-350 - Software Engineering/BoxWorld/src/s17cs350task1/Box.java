package s17cs350task1;

import javafx.geometry.Point3D;

import java.util.*;

public class Box implements Cloneable {
	private String id;
	private Dimension3D size;
	private boolean isRoot;
	private ArrayList<Connector> children;
	private Connector parent;

	public Box(String id, Dimension3D size) {
		if (id == null || id.equals("")) throw new TaskException("id passed to box was null or empty");
		if (size == null) throw new TaskException("size passed to box was null");
		this.id = id;
		this.size = size;
		children = new ArrayList<>();
	}

	public Box(String id, Dimension3D size, boolean isRoot) {
		this(id, size);
		this.isRoot = isRoot;
		children = new ArrayList<>();
	}

	public Box clone() throws CloneNotSupportedException {
		Box clone = (Box) super.clone();
		clone.parent = null;
		clone.size = size.clone();
		clone.children = new ArrayList<>();
		clone.id = new String(id + "");
		for (Connector c : this.children) {
			Connector c1 = c.clone();
			clone.connectChild(c1);
			c1.getChildBox().setConnectorToParent(c1);
		}
		return clone;
	}

	public void connectChild(Connector connector) {
		if (connector == null) throw new TaskException("connector passed to connectChild was null");
		if (connector.getChildBox().isRoot) throw new TaskException("cannot combine two trees with roots");
		ArrayList<String> parentTreeIDs = getTreeIDs();
		ArrayList<String> childTreeIDS = connector.getChildBox().getTreeIDs();
		if (!Collections.disjoint(parentTreeIDs, childTreeIDS))
			throw new TaskException("Cannot connect box/tree to tree that contains duplicate box id");
		connector.setParentBox(this);
		children.add(connector);
	}

	private ArrayList<String> getTreeIDs() {
		Box b = this;
		try {
			while (b.hasConnectorToParent())
				b = b.getConnectorToParent().getParentBox();
		} catch (TaskException e) {
			//Shhhh
		}
		List<Box> boxes = b.getDescendantBoxes();
		boxes.add(b);
		ArrayList<String> ids = new ArrayList<>();
		for (Box box : boxes) {
			ids.add(box.id);
		}
		return ids;
	}

	public Point3D getAbsoluteCenterPosition() {
		Point3D p = new Point3D(0, 0, 0);
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
		boxes.sort(new BoxComparator());
		return boxes;
	}

	public List<Connector> getConnectorsToChildren() {
		return children;
	}

	public Connector getConnectorToParent() {
		if (hasConnectorToParent()) return parent;
		throw new TaskException("Cannot get connector to parent: Box does not have connector to parent");
	}

	public void setConnectorToParent(Connector connector) {
		if (connector == null) throw new TaskException("connector was null");
		if (hasConnectorToParent()) throw new TaskException("Box already has parent");
		this.parent = connector;
	}

	public int getDescendantBoxCount() {
		return getDescendantBoxes().size();
	}

	public List<Box> getDescendantBoxes() {
		ArrayList<Box> boxes = new ArrayList<>();
		for (Box child : getChildBoxes()) {
			boxes.add(child);
			boxes.addAll(child.getDescendantBoxes());
		}
		boxes.sort(new BoxComparator());
		return boxes;
	}

	public String getID() {
		return id;
	}

	public Dimension3D getSize() {
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
		return "ComponentBox[id=\'" + id + "\' isRoot=" + isRoot + " size=" + size;
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

	public static class BoxComparator implements Comparator<Box> {
		@Override
		public int compare(Box o1, Box o2) {
			return o1.id.compareTo(o2.id);
		}
	}
}
