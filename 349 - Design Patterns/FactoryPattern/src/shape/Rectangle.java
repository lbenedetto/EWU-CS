package shape;

class Rectangle extends Shape implements Comparable {
	Rectangle(String name, double height, double width) {
		super(name, height, width);
	}

	Rectangle(double height, double width) {
		super("Rectangle", height, width);
	}

	@Override
	public double calculateArea() {
		return height * width;
	}
}
