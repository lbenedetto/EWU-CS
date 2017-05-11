#ifndef HW_5_CIRCLE_H
#define HW_5_CIRCLE_H


#include "../Shape2D.h"

class Circle : public Shape2D {
private:
	double radius;
public:
	Circle(double radius) : radius(radius) {}

	Circle() : radius(1) {}

	void printShapeDetail() const;

	double computeArea() const;
};


#endif //HW_5_CIRCLE_H
