#ifndef HW5_SPHERE_H
#define HW5_SPHERE_H


#include "../Shape3D.h"

class Sphere : public Shape3D {
private:
	double radius;
public:
	Sphere(double radius) : radius(radius) {}

	Sphere() : radius(1) {}

	void printShapeDetail();

	double computeArea();

	double computeVolume();
};


#endif //HW5_SPHERE_H
