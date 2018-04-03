#include <iostream>
#include "Box.h"

void Box::printShapeDetail() {
	std::cout << "Box, 3D Shape, width: " << width
	          << ", height: " << height
	          << ", depth: " << depth
	          << ", Area: " << computeArea()
	          << ", Volume: " << computeVolume()
	          << "." << std::endl;
}

double Box::computeVolume() {
	return width * height * depth;
}

double Box::computeArea() {
	return 2 * (width * height) + 2 * (width * depth) + 2 * (height * depth);
}
