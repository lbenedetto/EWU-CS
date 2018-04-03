#ifndef HW5_TRIANGLE_H
#define HW5_TRIANGLE_H


#include "../Shape2D.h"

class Triangle : public Shape2D {
private:
	double base, height;

public:
	Triangle(double base, double height) : base(base), height(height) {}

	Triangle() : base(1), height(1) {}

	void printShapeDetail();

	double computeArea();
};


#endif //HW5_TRIANGLE_H
