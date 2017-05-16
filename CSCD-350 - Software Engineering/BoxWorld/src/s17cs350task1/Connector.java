package s17cs350task1;


public class Connector implements Cloneable {
	private A_Component childBox;
	private A_Component parentBox;
	private Point3D offset;

	public Connector(ComponentBox childBox, Point3D offset) throws TaskException {
		if (childBox == null) throw new TaskException("child box was null");
		if (offset == null) throw new TaskException("offset was null");
		if (childBox.isRoot()) throw new TaskException("Root box cannot be child box");
		this.childBox = childBox;
		this.offset = offset;
		childBox.setConnectorToParent(this);
	}

	public Connector clone() throws CloneNotSupportedException {
		Connector clone = (Connector) super.clone();
		clone.offset = offset.clone();
		clone.parentBox = null;
		clone.childBox = childBox.clone();
		return clone;
	}

	public A_Component getComponentChild() {
		return childBox;
	}

	public A_Component getComponentParent() {
		if (hasComponentParent()) return parentBox;
		throw new TaskException("Component does not have parent box");
	}

	public Point3D getOffsetFromParent() {
		if (hasComponentParent())
			return offset;
		return null;
	}

	public boolean hasComponentParent() {
		return parentBox != null;
	}


	public void setComponentParent(A_Component parentBox) {
		if (parentBox == null) throw new TaskException("parentBox was null");
		if (this.parentBox != null) throw new TaskException("parent Component already set");
		this.parentBox = parentBox;
	}

	@Override
	public String toString() {
		boolean parent = (parentBox != null && parentBox.getConnectorsToChildren().contains(this));
		return String.format("Connector[GoodParent=%s, offset=%s] -> %s", parent, offset, childBox);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Connector connector = (Connector) o;
		return childBox.equals(connector.childBox) &&
				childBox.equals(connector.childBox) &&
				offset.equals(connector.offset);
	}
}
