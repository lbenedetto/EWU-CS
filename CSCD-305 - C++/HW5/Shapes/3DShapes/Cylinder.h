#ifndef HW_5_CYLINDER_H
#define HW_5_CYLINDER_H


#include "../Shape3D.h"

class Cylinder : public Shape3D {
private:
	double radius, height;
public:
	Cylinder(double radius, double height) : radius(radius), height(height) {}

	Cylinder() : radius(1), height(1) {}

	void printShapeDetail() const;

	double computeArea() const;

	double computeVolume() const;

};


#endif //HW_5_CYLINDER_H
