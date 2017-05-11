#include <iostream>
#include "Rectangle.h"

double Rectangle::computeArea() const {
	return width * height;
}

void Rectangle::printShapeDetail() const {
	std::cout << "Rectangle, 2D Shape, width: " << width << ", height: " << height << ", Area: " << computeArea();
}
