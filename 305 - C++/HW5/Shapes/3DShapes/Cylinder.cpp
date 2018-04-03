#include <iostream>
#include "Cylinder.h"

double Cylinder::computeArea() {
	return (2 * PI * radius * height) + (2 * PI * (radius * radius));
}

double Cylinder::computeVolume() {
	return PI * (radius * radius) * height;
}

void Cylinder::printShapeDetail() {
	std::cout << "Cylinder, 3D Shape, radius: " << radius
	          << ", height: " << height
	          << ", Area: " << computeArea()
	          << ", Volume: " << computeVolume()
	          << "." << std::endl;
}
