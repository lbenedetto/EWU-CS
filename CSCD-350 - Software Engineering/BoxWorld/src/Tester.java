import javafx.geometry.Point3D;
import s17cs350task1.Box;
import s17cs350task1.Connector;
import s17cs350task1.Dimension3D;

public class Tester {
	public static void main(String[] args) {
		Box box1 = new Box("Box1", new Dimension3D(1, 1, 1), true);
		Box box2 = new Box("Box2", new Dimension3D(2, 2, 2));
		Box box3 = new Box("Box3", new Dimension3D(3, 3, 3));
		Box box4 = new Box("Box4", new Dimension3D(4, 4, 4));
		Box box5 = new Box("Box5", new Dimension3D(5, 5, 5));
		Box box6 = new Box("Box6", new Dimension3D(6, 6, 6));
		connectBoxes(box1, box2);
		connectBoxes(box1, box3);
		connectBoxes(box2, box4);
		connectBoxes(box2, box5);
		connectBoxes(box3, box6);
		System.out.println(box1.toString());
		box1.getDescendantBoxes().forEach(System.out::println);
		System.out.println(box1.getAbsoluteCenterPosition());
		System.out.println(box2.getAbsoluteCenterPosition());
		System.out.println(box4.getAbsoluteCenterPosition());
		try {
			Box Box1Clone = box1.clone();
			Box1Clone.toString();
			Box box2Clone = box2.clone();
			box2Clone.toString();
		}catch (CloneNotSupportedException e){

		}
	}

	public static void connectBoxes(Box b1, Box b2) {
		Connector c1 = new Connector(b2, new Point3D(5, 5, 5));
		b1.connectChild(c1);
	}
}
