#include "Cylinder.h"

double Cylinder::computeArea() const {
	return (2 * PI * radius * height) + (2 * PI * (radius * radius));
}

double Cylinder::computeVolume() const {
	return PI * (radius * radius) * height;
}

void Cylinder::printShapeDetail() const {


}
