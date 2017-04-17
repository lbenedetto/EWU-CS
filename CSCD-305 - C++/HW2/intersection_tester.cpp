//Lars Benedetto. I used CMake to Compile
#include <iostream>
#include "Point3D.h"
#include "Sphere.h"
#include "Ray.h"

using namespace std;

double readDouble(string prompt) {
	double d;
	while (true) {
		cout << prompt;
		if (cin >> d && cin.get() == '\n') {
			return d;
		} else {
			cout << "Invalid number, try again" << endl;
			cin.clear();
			cin.ignore(10000, '\n');
		}
	}
}

double *readXYZ() {
	double *c = new double[3];
	c[0] = readDouble("  Enter X: ");
	c[1] = readDouble("  Enter Y: ");
	c[2] = readDouble("  Enter Z: ");
	return c;
}

int main() {
//	//Fail
//	Ray ray = {{0, 2, 5},
//	           {1, 0, -2}};
//	Sphere sphere = {{100,100,100}, 1};
//	ray.checkIntersection(sphere);
//	//Success
//	ray = {{0, 2, 5},
//	           {1, 0, -2}};
//	sphere = {{2,4,1}, 8};
//	ray.checkIntersection(sphere);
	cout << "Enter ray origin:" << endl;
	double *c = readXYZ();
	cout << "Enter ray direction:" << endl;
	double *c2 = readXYZ();
	Ray ray = {{c[0],  c[1],  c[2]},
	           {c2[0], c2[1], c2[2]}};
	cout << "Enter sphere center:" << endl;
	c = readXYZ();
	Sphere sphere = {{c[0], c[1], c[2]}, readDouble("Enter sphere radius: ")};
	ray.checkIntersection(sphere);
	return 0;
}