#ifndef HW5_CYLINDER_H
#define HW5_CYLINDER_H


#include "../Shape3D.h"

class Cylinder : public Shape3D {
private:
	double radius, height;
public:
	Cylinder(double radius, double height) : radius(radius), height(height) {}

	Cylinder() : radius(1), height(1) {}

	void printShapeDetail();

	double computeArea();

	double computeVolume();

};


#endif //HW5_CYLINDER_H
