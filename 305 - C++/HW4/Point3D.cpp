#include <cmath>
#include "Point3D.h"

using namespace std;

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
	Point3D *p2 = new Point3D(x + p.x, y + p.y, z + p.z);
	return *p2;
}

Point3D &Point3D::subtractPoints(const Point3D &p) {
	Point3D *p2 = new Point3D(x - p.x, y - p.y, z - p.z);
	return *p2;
}

double Point3D::multiplyPoints(const Point3D &p) {
	return (x * p.x) + (y * p.y) + (z * p.z);
}

double Point3D::squarePoints() {
	return (x * x) + (y * y) + (z * z);
}

//string Point3D::toString() {
//	return "(x: " + std::to_string(x) + "y: " + to_string(y) + "z: " + to_string(z) + ")";
//}

Point3D Point3D::operator-(const Point3D &p) {
	return Point3D(x + p.x, y + p.y, z + p.z);
}

Point3D Point3D::operator+(const Point3D &p) {
	return Point3D(x + p.x, y + p.y, z + p.z);
}

double Point3D::operator*(const Point3D &p) {
	return (x * p.x) + (y * p.y) + (z * p.z);
}

double Point3D::distanceBetween(const Point3D &p) {
	return sqrt(subtractPoints(p).squarePoints());
}
