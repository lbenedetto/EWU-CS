#include <iostream>
#include "Circle.h"

void Circle::printShapeDetail() const {
	std::cout << "Circle, 2D Shape, radius: " << radius << ", Area: " << computeArea();
}

double Circle::computeArea() const {
	return PI * (radius * radius);

}
