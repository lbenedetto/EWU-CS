package shape;

public class ShapeFactory {
	public static Shape createShape(String type, double height) throws NoSuchShapeException {
		switch (type) {
			case "Square":
				return new Square(height);
			case "Circle":
				return new Circle(height);
			default:
				throw new NoSuchShapeException("No such shape: " + type);
		}
	}

	public static Shape createShape(String type, double height, double width) throws NoSuchShapeException {
		switch (type) {
			case "Triangle":
				return new Triangle(height, width);
			case "Rectangle":
				return new Rectangle(height, width);
			default:
				throw new NoSuchShapeException("No such shape: " + type);
		}
	}
}
