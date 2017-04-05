package shape;

class Circle extends Shape implements Comparable{
	Circle(double diameter) {
		super("Circle", diameter, diameter);
	}

	@Override
	public double calculateArea() {
		return height * width * Math.PI;
	}
}
