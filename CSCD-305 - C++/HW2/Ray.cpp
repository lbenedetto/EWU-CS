#include <iostream>
#include "Ray.h"

Point3D &Ray::getOrigin() {
    return origin;
}

Point3D &Ray::getDirection() {
    return direction;
}

Ray &Ray::setOrigin(Point3D origin) {
    this->origin = origin;
    return *this;
}

Ray &Ray::setDirection(Point3D direction) {
    this->direction = direction;
    return *this;
}

void Ray::checkIntersection(Sphere &s) {
    double A, B, C;
    A = direction.squarePoints();
    B = origin.subtractPoints(s.center).multiplyPoints(direction);
    C = (s.radius * s.radius) - origin.subtractPoints(s.center).squarePoints();
    if (((B * B) - (A * C)) < 0) {
        std::cout << "Ray does not touch or intersect the sphere";
    } else {
        std::cout << "Ray either touches or intersects the sphere";
    }
}
