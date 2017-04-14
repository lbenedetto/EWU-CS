package s17cs350task1;

import javafx.geometry.Point3D;

public class Connector implements Cloneable {
	private Box childBox;
	private Box parentBox;
	private Point3D offset;

	public Connector(Box childBox, Point3D offset) {
		this.childBox = childBox;
		this.offset = offset;
		childBox.setConnectorToParent(this);
	}

	public Connector clone() throws CloneNotSupportedException {
		Connector c = (Connector) super.clone();
		c.offset = new Point3D(offset.getX(), offset.getY(), offset.getZ());
		c.childBox = childBox.clone();
		c.childBox.setConnectorToParent(c);
		return c;
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
		return null;
	}

	public void setParentBox(Box parentBox) {
		this.parentBox = parentBox;
	}


	public boolean hasParentBox() {
		return parentBox != null;
	}

	@Override
	public String toString() {
		return "->" + childBox.toString();
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
