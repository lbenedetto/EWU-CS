package s17cs350task1;

import java.util.*;

public abstract class A_Component implements Cloneable {
	A_Component(String id, boolean isRoot) {
		this.id = id;
		this.isRoot = isRoot;
		children = new ArrayList<>();
	}

	private String id;
	private boolean isRoot;
	private ArrayList<Connector> children;
	private Connector parent;


	abstract double calculateAreaAll(BoundingBox.E_Plane plane);

	abstract public double calculateAreaSelf(BoundingBox.E_Plane plane);

	abstract public Point3D calculateCenterOfMassAll();

	abstract public Point3D calculateCenterOfMassSelf();

	abstract public double calculateVolumeAll();

	abstract public double calculateVolumeSelf();

	public A_Component clone() throws CloneNotSupportedException {
		A_Component clone = (A_Component) super.clone();
		clone.parent = null;
		clone.children = new ArrayList<>();
		clone.id = new String(id + "");
		for (Connector c : this.children) {
			Connector c1 = c.clone();
			clone.connectChild(c1);
			c1.getComponentChild().setConnectorToParent(c1);
		}
		return clone;
	}

	public void connectChild(Connector connector) {
		if (connector == null) throw new TaskException("connector passed to connectChild was null");
		if (connector.getComponentChild().isRoot) throw new TaskException("cannot combine two trees with roots");
		List<String> parentTreeIDs = getTreeIDs();
		List<String> childTreeIDS = connector.getComponentChild().getTreeIDs();
		if (!Collections.disjoint(parentTreeIDs, childTreeIDS))
			throw new TaskException("Cannot connect box/tree to tree that contains duplicate box id");
		connector.setComponentParent(this);
		children.add(connector);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		A_Component comp = (A_Component) o;
		return isRoot == comp.isRoot &&
				id.equals(comp.id) &&
				children.equals(comp.children);
	}

	abstract String export(A_Exporter exporter);


	abstract public BoundingBox generateBoundingBoxAll();

	abstract public BoundingBox generateBoundingBoxSelf();

	abstract public List<List<Point3D>> generateFramesAll();

	abstract public List<Point3D> generateFramesSelf();

	public Point3D getAbsoluteCenterPosition() {
		Point3D p = new Point3D(0, 0, 0);
		if (hasConnectorToParent()) {
			p = p.add(parent.getOffsetFromParent()).add(parent.getComponentParent().getAbsoluteCenterPosition());
		} else if (!isRoot) {
			throw new TaskException("Tree does not have root");
		}
		return p;
	}

	public int getChildCount() {
		return children.size();
	}

	public List<A_Component> getChildren() {
		List<A_Component> c = new ArrayList<>();
		children.forEach(x -> c.add(x.getComponentChild()));
		return c;
	}

	public List<Connector> getConnectorsToChildren() {
		ArrayList<Connector> temp = new ArrayList<>();
		temp.addAll(children);
		return temp;
	}

	public Connector getConnectorToParent() {
		if (hasConnectorToParent()) return parent;
		throw new TaskException("Cannot get connector to parent: ComponentBox does not have connector to parent");
	}

	public int getDescendantCount() {
		return getDescendants().size();
	}

	public List<A_Component> getDescendants() {
		ArrayList<A_Component> boxes = new ArrayList<>();
		for (A_Component child : getChildren()) {
			boxes.add(child);
			boxes.addAll(child.getDescendants());
		}
		boxes.sort(new ComponentComparator());
		return boxes;
	}

	public String getID() {
		return id;
	}

	private List<String> getTreeIDs() {
		A_Component b = this;
		try {
			while (b.hasConnectorToParent())
				b = b.getConnectorToParent().getComponentParent();
		} catch (TaskException e) {
			//Shhhh
		}
		List<A_Component> components = b.getDescendants();
		components.add(b);
		ArrayList<String> ids = new ArrayList<>();
		for (A_Component comp : components) {
			ids.add(comp.id.toLowerCase());
		}
		return ids;
	}

	public boolean hasConnectorToParent() {
		return parent != null;
	}

	@Override
	public int hashCode() {
		return id.toUpperCase().hashCode();
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setConnectorToParent(Connector connector) {
		if (connector == null) throw new TaskException("connector was null");
		if (hasConnectorToParent()) throw new TaskException("ComponentBox already has parent");
		this.parent = connector;
	}


	@Override
	public String toString() {
		StringBuilder out = new StringBuilder(String.format("ComponentBox[id='%s', isRoot=%s]", id, isRoot));
		for (Connector c : children)
			out.append("\n").append(c.toString());
		return out.toString().replaceAll("\n", "\n\t");
	}

	public static class ComponentComparator implements Comparator<A_Component> {
		@Override
		public int compare(A_Component o1, A_Component o2) {
			return o1.id.compareTo(o2.id);
		}
	}
}
