#ifndef HW5_BOX_H
#define HW5_BOX_H


#include "../Shape3D.h"

class Box : public Shape3D {
private:
	double width, height, depth;
public:
	Box(double width, double height, double depth) : width(width), height(height), depth(depth) {}

	Box() : width(1), height(1), depth(1) {}

	void printShapeDetail();

	double computeArea();

	double computeVolume();
};


#endif //HW5_BOX_H
