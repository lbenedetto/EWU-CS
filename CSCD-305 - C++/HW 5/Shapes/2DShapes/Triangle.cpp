#include <iostream>
#include "Triangle.h"

double Triangle::computeArea() const {
	return (base * height) / 2;
}

void Triangle::printShapeDetail() const {
	std::cout << "Triangle, 2D Shape, base: " << base << ", height: " << height << ", Area: " << computeArea();
}
