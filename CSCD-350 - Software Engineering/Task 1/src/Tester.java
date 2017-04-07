import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;

public class Tester {
	public static void main(String[] args) {
		Box Box1 = new Box("Box1", new Dimension2D(1, 1), true);
		Box Box2 = new Box("Box2", new Dimension2D(2, 2));
		Box Box3 = new Box("Box3", new Dimension2D(3, 3));
		Box Box4 = new Box("Box4", new Dimension2D(4, 4));
		Box Box5 = new Box("Box5", new Dimension2D(5, 5));
		Box Box6 = new Box("Box6", new Dimension2D(6, 6));
		connectBoxes(Box1, Box2);
		connectBoxes(Box1, Box3);
		connectBoxes(Box2, Box4);
		connectBoxes(Box2, Box5);
		connectBoxes(Box3, Box6);
		System.out.println(Box1.toString());
		try {
			Box Box1Clone = Box1.clone();
			System.out.println(Box1Clone.toString());
			if(Box1.equals(Box1Clone)){
				if(Box1 != Box1Clone)
				System.out.println("Clone Successful");
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		Box1.getDescendantBoxes().forEach(System.out::println);
	}

	public static void connectBoxes(Box b1, Box b2) {
		Connector c1 = new Connector(b2, new Point2D(5, 5));
		c1.setParentBox(b1);
		b1.connectChild(c1);
		b2.setConnectorToParent(c1);
	}
}
