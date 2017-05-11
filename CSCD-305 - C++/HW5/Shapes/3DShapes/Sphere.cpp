#include <iostream>
#include "Sphere.h"

void Sphere::printShapeDetail() {
	std::cout << "Sphere, 3D Shape, radius: " << radius
	          << ", Area: " << computeArea()
	          << ", Volume: " << computeVolume()
	          << "." << std::endl;
}

double Sphere::computeVolume() {
	return (4.0 / 3.0) * PI * (radius * radius * radius);
}

double Sphere::computeArea() {
	return 4 * PI * (radius * radius);
}
