package s17cs350task1;

import javafx.geometry.Point3D;

public class Connector implements Cloneable {
	private Box childBox;
	private Box parentBox;
	private Point3D offset;

	public Connector(Box childBox, Point3D offset) throws TaskException {
		if (childBox == null) throw new TaskException("child box was null");
		if (offset == null) throw new TaskException("offset was null");
		if (childBox.isRoot()) throw new TaskException("Root box cannot be child box");
		this.childBox = childBox;
		this.offset = offset;
		childBox.setConnectorToParent(this);
	}

	public Connector clone() throws CloneNotSupportedException {
		Connector clone = (Connector) super.clone();
		clone.offset = new Point3D(offset.getX(), offset.getY(), offset.getZ());
		clone.parentBox = null;
		clone.childBox = childBox.clone();
		return clone;
	}

	public Box getChildBox() {
		return childBox;
	}

	public Point3D getOffsetFromParentBox() {
		if (hasParentBox())
			return offset;
		return null;
	}

	public Box getParentBox() {
		if (hasParentBox()) return parentBox;
		throw new TaskException("Box does not have parent box");
	}

	public void setParentBox(Box parentBox) {
		if (parentBox == null) throw new TaskException("parentBox was null");
		if (this.parentBox != null) throw new TaskException("parent Box already set");
		this.parentBox = parentBox;
	}


	public boolean hasParentBox() {
		return parentBox != null;
	}

	@Override
	public String toString() {
		return parentBox.toString() + " -> " + childBox.toString();
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
