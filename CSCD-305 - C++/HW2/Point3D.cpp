#include "Point3D.h"

double Point3D::getX() {
	return x;
}

double Point3D::getY() {
	return y;
}

double Point3D::getZ() {
	return z;
}

Point3D &Point3D::setX(double x) {
	this->x = x;
	return *this;
}

Point3D &Point3D::setY(double y) {
	this->y = y;
	return *this;
}

Point3D &Point3D::setZ(double z) {
	this->z = z;
	return *this;
}

Point3D &Point3D::addPoints(const Point3D &p) {
	x += p.x;
	y += p.y;
	z += p.z;
	return *this;
}

Point3D &Point3D::subtractPoints(const Point3D &p) {
	x -= p.x;
	y -= p.y;
	z -= p.z;
	return *this;
}

double Point3D::multiplyPoints(const Point3D &p) {
	return (x * p.x) + (y * p.y) + (z * p.z);
}

double Point3D::squarePoints() {
	return (x * x) + (y * y) + (z * z);
}
