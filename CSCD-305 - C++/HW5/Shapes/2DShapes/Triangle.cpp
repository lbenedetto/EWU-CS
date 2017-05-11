#include <iostream>
#include "Triangle.h"

double Triangle::computeArea() {
	return (base * height) / 2;
}

void Triangle::printShapeDetail() {
	std::cout << "Triangle, 2D Shape, base: " << base
	          << ", height: " << height
	          << ", Area: " << computeArea()
	          << "." << std::endl;
}
