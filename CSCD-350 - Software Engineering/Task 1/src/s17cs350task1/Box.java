package s17cs350task1;

import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
		if(!isRoot) throw new CloneNotSupportedException("Must clone tree starting from root");
		Box box = (Box) super.clone();
		box.size = size.clone();
		box.children = new ArrayList<>();
		box.id = new String(id + "");
		for (Connector c : this.children)
			box.connectChild(c.clone());
		return box;
	}

	public void connectChild(Connector connector) {
		if (connector == null) throw new TaskException("connector passed to connectChild was null");
		connector.setParentBox(this);
		children.add(connector);
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
		return null;
	}

	public void setConnectorToParent(Connector connector) {
		if (connector == null) throw new TaskException("connector was null");
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
