#ifndef HW_5_SHAPE3D_H
#define HW_5_SHAPE3D_H


#include "Shape2D.h"

class Shape3D : public Shape2D {
	virtual double computeVolume() = 0;
};


#endif //HW_5_SHAPE3D_H
