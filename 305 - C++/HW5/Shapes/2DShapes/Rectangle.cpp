#include <iostream>
#include "Rectangle.h"

double Rectangle::computeArea() {
	return width * height;
}

void Rectangle::printShapeDetail() {
	std::cout << "Rectangle, 2D Shape, width: " << width
	          << ", height: " << height
	          << ", Area: " << computeArea()
	          << "." << std::endl;
}
