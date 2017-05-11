#ifndef HW_5_SPHERE_H
#define HW_5_SPHERE_H


#include "../Shape3D.h"

class Sphere : public Shape3D {
private:
	double radius;
public:
	Sphere(double radius) : radius(radius) {}

	Sphere() : radius(1) {}

	void printShapeDetail() const;

	double computeArea() const;

	double computeVolume() const;
};


#endif //HW_5_SPHERE_H
