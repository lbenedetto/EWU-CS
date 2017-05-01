//Lars Benedetto, compiled with CMake
#include <iostream>
#include "ColorBlob.h"

using namespace std;

int main() {
	ColorBlob cBlob(2, 2, Color());
	cout << "cBlob = " << cBlob << endl;
	ColorBlob cBlobOne(cBlob);
	ColorBlob cBlobTwo(2, 2, Color());
	cin >> cBlobTwo;
	ColorBlob cBlobThree = cBlobOne + cBlobTwo;
	cout << "cBlobThree = " << cBlobThree << endl;
	ColorBlob cBlobFour = cBlobOne - cBlobTwo;
	cout << "cBlobFour = " << cBlobFour << endl;
	Color cColor(0.5, 0.4, 0.45);
	ColorBlob cBlobFive = cBlobOne * cColor;
	cout << "cBlobFive= " << cBlobFive << endl;
	cout << !cBlobFive << endl;
	ColorBlob cBlobSix;
	cout << "cBlobSix= " << cBlobSix << endl;
	cBlobSix = move(cBlobFive);
	ColorBlob cBlobSeven = move(cBlobOne);
	return 0;
}