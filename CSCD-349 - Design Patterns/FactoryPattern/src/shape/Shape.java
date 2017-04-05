package shape;

public abstract class Shape implements Comparable {
	double height;
	double width;
	private String name;

	Shape(String n, double h, double w) {
		name = n;
		height = h;
		width = w;
	}

	public String getName() {
		return name;
	}

	public abstract double calculateArea();

	public int compareTo(Object o) {
		if (o == null) throw new NullPointerException();
		Shape that = (Shape) o;
		int c = this.name.compareTo(that.name);
		if (c == 0) {
			double v = this.calculateArea() - ((Shape) o).calculateArea();
			if (v == 0) return 0;
			if (v > 0) return 1;
			return -1;
		}
		return c;
	}
}
