#ifndef HW2_RAY_H
#define HW2_RAY_H


#include "Point3D.h"
#include "Sphere.h"

class Ray {

private:
    Point3D origin;
    Point3D direction;
public:
    Ray() : origin{}, direction{} {}

    Ray(Point3D origin, Point3D direction) : origin{origin}, direction{direction} {}

    Point3D &getOrigin();

    Point3D &getDirection();

    Ray &setOrigin(Point3D origin);

    Ray &setDirection(Point3D direction);

    void checkIntersection(Sphere &s);
};


#endif //HW2_RAY_H
