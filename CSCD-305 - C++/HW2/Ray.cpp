#include <iostream>
#include "Ray.h"

Point3D &Ray::getOrigin() {
	return origin;
}

Point3D &Ray::getDirection() {
	return direction;
}

Ray &Ray::setOrigin(Point3D origin) {
	this->origin = origin;
	return *this;
}

Ray &Ray::setDirection(Point3D direction) {
	this->direction = direction;
	return *this;
}

void Ray::checkIntersection(Sphere &s) {
	double A, B, C;
	A = direction.squarePoints();
	B = origin.subtractPoints(s.center).multiplyPoints(direction);
	C = origin.squarePoints() - (s.radius * s.radius);
	double ans = (B * B) - (A * C);
//	std::cout << "A: " + std::to_string(A) << std::endl;
//	std::cout << "B: " + std::to_string(B) << std::endl;
//	std::cout << "C: " + std::to_string(C) << std::endl;
//	std::cout << ans << std::endl;
	if (ans < 0)
		std::cout << "Ray does not touch or intersect the sphere" << std::endl;
	else
		std::cout << "Ray either touches or intersects the sphere" << std::endl;
}
