import shape.NoSuchShapeException;
import shape.Shape;
import shape.ShapeFactory;

import java.util.ArrayList;
import java.util.Collections;

public class ShapeTester {
	public static void main(String[] args) {
		ArrayList<Shape> shapes = new ArrayList<>();
		try {
			shapes.add(ShapeFactory.createShape("Circle", 5.04627));
			shapes.add(ShapeFactory.createShape("Square", 14.5));
			shapes.add(ShapeFactory.createShape("Triangle", 78, 3));
			shapes.add(ShapeFactory.createShape("Rectangle", 6, 4));
			shapes.add(ShapeFactory.createShape("Circle", 1));
			shapes.add(ShapeFactory.createShape("Square", 2));
			shapes.add(ShapeFactory.createShape("Triangle", 3, 4));
			shapes.add(ShapeFactory.createShape("Rectangle", 5, 6));
			shapes.add(ShapeFactory.createShape("Squircle", 12));
		} catch (NoSuchShapeException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Before sort: ");
		for (Shape shape : shapes) {
			System.out.println("Area of " + shape.getName() + " is " + shape.calculateArea());
		}
		Collections.sort(shapes);
		System.out.println("Before sort: ");
		for (Shape shape : shapes) {
			System.out.println("Area of " + shape.getName() + " is " + shape.calculateArea());
		}
	}
}
