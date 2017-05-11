#ifndef HW5_RECTANGLE_H
#define HW5_RECTANGLE_H


#include "../Shape2D.h"

class Rectangle : public Shape2D {
private:
	double width, height;
public:
	Rectangle(double width, double height) : width(width), height(height) {}

	Rectangle() : width(1), height(1) {}

	void printShapeDetail();

	double computeArea();
};


#endif //HW5_RECTANGLE_H
