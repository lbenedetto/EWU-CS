#include "Box.h"

void Box::printShapeDetail() const {

}

double Box::computeVolume() const {
	return width * height * depth;
}

double Box::computeArea() const {
	return 2 * (width * height) + 2 * (width * depth) + 2 * (height * depth);
}
