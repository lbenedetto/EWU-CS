package shape;

class Triangle extends Shape implements Comparable {
	Triangle(double height, double width) {
		super("Triangle", height, width);
	}

	@Override
	public double calculateArea() {
		return (height * width) / 2;
	}
}
