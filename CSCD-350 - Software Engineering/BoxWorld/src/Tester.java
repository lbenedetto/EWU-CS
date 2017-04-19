import javafx.geometry.Point3D;
import s17cs350task1.Box;
import s17cs350task1.Connector;
import s17cs350task1.Dimension3D;

public class Tester {
	public static void main(String[] args) {
		Box Box1 = new Box("Box1", new Dimension3D(1, 1, 1), true);
		Box Box2 = new Box("Box2", new Dimension3D(2, 2, 2));
		Box Box3 = new Box("Box3", new Dimension3D(3, 3, 3));
		Box Box4 = new Box("Box4", new Dimension3D(4, 4, 4));
		Box Box5 = new Box("Box5", new Dimension3D(5, 5, 5));
		Box Box6 = new Box("Box6", new Dimension3D(6, 6, 6));
		connectBoxes(Box1, Box2);
		connectBoxes(Box1, Box3);
		connectBoxes(Box2, Box4);
		connectBoxes(Box2, Box5);
		connectBoxes(Box3, Box6);
		System.out.println(Box1.toString());
		Box1.getDescendantBoxes().forEach(System.out::println);
		System.out.println(Box1.getAbsoluteCenterPosition());
		System.out.println(Box2.getAbsoluteCenterPosition());
		System.out.println(Box4.getAbsoluteCenterPosition());
	}

	public static void connectBoxes(Box b1, Box b2) {
		Connector c1 = new Connector(b2, new Point3D(5, 5, 5));
		b1.connectChild(c1);
	}
}
