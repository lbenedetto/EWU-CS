#ifndef HW2_POINT3D_H
#define HW2_POINT3D_H

#include <string>

using namespace std;

class Point3D {
private :
	double x;
	double y;
	double z;
public:
	Point3D() : x{0}, y{0}, z{0} {}

	Point3D(double x, double y, double z) : x{x}, y{y}, z{z} {}

	double getX();

	double getY();

	double getZ();

	Point3D &setX(double x);

	Point3D &setY(double y);

	Point3D &setZ(double z);

	Point3D &addPoints(const Point3D &p);

	Point3D &subtractPoints(const Point3D &p);

	double multiplyPoints(const Point3D &p);

	double squarePoints();

	string toString();
};

#endif //HW2_POINT3D_H
