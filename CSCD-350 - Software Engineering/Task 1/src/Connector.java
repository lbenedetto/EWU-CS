import javafx.geometry.Point2D;

public class Connector implements Cloneable {
	private Box childBox;
	private Box parentBox;
	private Point2D offset;

	public Connector(Box childBox, Point2D offset) {
		this.childBox = childBox;
		this.offset = offset;
	}

	public Connector clone() throws CloneNotSupportedException {
		Connector c  = (Connector) super.clone();
		c.offset = new Point2D(offset.getX(), offset.getY());
		c.parentBox = parentBox.clone();
		c.childBox = childBox.clone();
		return c;
	}

	public Box getChildBox() {
		return childBox;
	}

	public Point2D getOffsetFromParentBox() {
		return offset;
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
		return childBox.toString() + offset.toString();
	}
}
