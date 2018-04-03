#ifndef HW2_SPHERE_H
#define HW2_SPHERE_H

#include "Point3D.h"

class Sphere {
	friend class Ray;

private:
	Point3D center;
	double radius;
public:
	Sphere() : center{}, radius{0} {}

	Sphere(Point3D center, double radius) : center{center}, radius{radius} {}

	Point3D &getCenter();

	double getRadius();

	Sphere &setCenter(Point3D center);

	Sphere &setRadius(double radius);
};


#endif //HW2_SPHERE_H
