#ifndef HW_5_BOX_H
#define HW_5_BOX_H


#include "../Shape3D.h"

class Box : public Shape3D {
private:
	double width, height, depth;
public:
	Box(double width, double height, double depth) : width(width), height(height), depth(depth) {}

	Box() : width(1), height(1), depth(1) {}

	void printShapeDetail() const;

	double computeArea() const;

	double computeVolume() const;
};


#endif //HW_5_BOX_H
