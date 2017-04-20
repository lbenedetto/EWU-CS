#include <iostream>
#include "Color.h"

using namespace std;

bool operator==(const Color &c1, const Color &c2) {
	return c1.r == c2.r && c1.g == c2.g && c1.b == c2.b;
}

Color operator+(const Color &c1, const Color &c2) {
	double red = min(c1.r + c2.r, 1.0);
	double green = min(c1.g + c2.g, 1.0);
	double blue = min(c1.b + c2.b, 1.0);
	return Color(red, green, blue);
}

Color operator-(const Color &c1, const Color &c2) {
	double red = max(c1.r - c2.r, 0.0);
	double green = max(c1.g - c2.g, 0.0);
	double blue = max(c1.b - c2.b, 0.0);
	return Color(red, green, blue);
}

Color operator*(const Color &c1, const Color &c2) {
	return Color(c1.r * c2.r, c1.g * c2.g, c1.b * c2.b);
}

bool operator!=(const Color &c1, const Color &c2) {
	return !(c1 == c2);
}

ostream &operator<<(ostream &stream, const Color &c) {
	stream << "[" << c.r << "," << c.g << "," << c.b << "]";
	return stream;
}

istream &operator>>(istream &stream, Color &c) {
	stream >> c.r >> c.g >> c.b;
	return stream;
}