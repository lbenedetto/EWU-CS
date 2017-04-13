#include "Sphere.h"

Sphere &Sphere::setRadius(double radius) {
    this->radius = radius;
    return *this;
}

Sphere &Sphere::setCenter(Point3D center) {
    this->center = center;
    return *this;
}

double Sphere::getRadius() {
    return radius;
}

Point3D &Sphere::getCenter() {
    return center;
}
