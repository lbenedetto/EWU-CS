#include <iostream>
#include "Circle.h"

void Circle::printShapeDetail() {
	std::cout << "Circle, 2D Shape, radius: " << radius
	          << ", Area: " << computeArea()
	          << "." << std::endl;
}

double Circle::computeArea() {
	return PI * (radius * radius);

}