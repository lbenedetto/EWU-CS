#include <iostream>
#include "Shapes/Shape.h"
#include "Shapes/2DShapes/Rectangle.h"
#include "Shapes/2DShapes/Circle.h"
#include "Shapes/3DShapes/Cylinder.h"
#include "Shapes/3DShapes/Box.h"
#include "Shapes/2DShapes/Triangle.h"
#include "Shapes/3DShapes/Sphere.h"

int main() {
	Circle circle;
	Rectangle rectangle;
	Triangle triangle;
	Box box;
	Sphere sphere;
	Cylinder cylinder;

	Shape *shape[6];
	int i = 0;
	shape[i++] = &circle;
	shape[i++] = &rectangle;
	shape[i++] = &triangle;
	shape[i++] = &box;
	shape[i++] = &sphere;
	shape[i] = &cylinder;
	i = 0;
	for (Shape *s : shape) {
		std::cout << "Shape " << i << ": ";
		s->printShapeDetail();
	}
	return 0;
}