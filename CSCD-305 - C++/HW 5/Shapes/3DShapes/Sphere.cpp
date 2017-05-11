#include "Sphere.h"

void Sphere::printShapeDetail() const {

}

double Sphere::computeVolume() const {
	return (4.0 / 3.0) * PI * (radius * radius * radius);
}

double Sphere::computeArea() const {
	return 4 * PI * (radius * radius);
}
