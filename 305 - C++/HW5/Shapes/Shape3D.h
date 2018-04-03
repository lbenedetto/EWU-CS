#ifndef HW5_SHAPE3D_H
#define HW5_SHAPE3D_H


#include "Shape2D.h"

class Shape3D : public Shape2D {
	virtual double computeVolume() = 0;
};


#endif //HW5_SHAPE3D_H
