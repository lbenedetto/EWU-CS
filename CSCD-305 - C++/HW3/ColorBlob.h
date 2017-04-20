#ifndef HW3_COLORBLOB_H
#define HW3_COLORBLOB_H

#include <iostream>

#include "Color.h"

using namespace std;

class ColorBlob {
private:
	int width;
	int height;
	Color **data;

	void deleteData() {
		for (int i = 0; i < height; i++) {
			delete[] data[i];
		}
		delete[] data;
	}

public:
	//Destructor
	~ColorBlob() {
		if (data != nullptr) {
			deleteData();
		}
	}

	//Default constructor
	ColorBlob() : ColorBlob(1, 1, {0, 0, 0}) {}

	//Delegating constructor
	ColorBlob(int width, int height) : ColorBlob(width, height, {0, 0, 0}) {}

	//Constructor
	ColorBlob(int width_, int height_, Color color) : width(width_), height(height_) {
		data = new Color *[height];
		for (int i = 0; i < height; i++) {
			data[i] = new Color[width];
			for (int j = 0; j < width; j++) {
				data[i][j] = color;
			}
		}
	}

	//Copy constructor
	ColorBlob(const ColorBlob &blob) : width(blob.width), height(blob.height) {
		data = new Color *[height];
		for (int i = 0; i < height; i++) {
			data[i] = new Color[width];
			for (int j = 0; j < width; j++) {
				data[i][j] = blob.data[i][j];
			}
		}
	}

	//Move constructor
	ColorBlob(const ColorBlob &&blob) {
		// Copy the data pointer and its length from the
		// source object.
		data = blob.data;
		height = blob.height;
		width = blob.width;

		// Release the data pointer from the source object so that
		// the destructor does not free the memory multiple times.
//		blob.data = nullptr;
//		blob.width = 0;
//		blob.height = 0;
	}

	//Move assignment operator
	ColorBlob &operator=(ColorBlob &blob) {
		if (this != &blob) {
			// Free the existing resource.
			deleteData();
			// Copy the data pointer and its length from the
			// source object.
			data = blob.data;
			height = blob.height;
			width = blob.width;

			// Release the data pointer from the source object so that
			// the destructor does not free the memory multiple times.
			blob.data = nullptr;
			blob.width = 0;
			blob.height = 0;
		}
		return *this;
	}

	//Copy assignment operator
	ColorBlob &operator=(const ColorBlob &blob) {
		if (this != &blob) {
			deleteData();
			width = blob.width;
			height = blob.height;
			data = new Color *[height];
			copy(&blob.data[0][0], &blob.data[0][0] + height * width, &data[0][0]);
		}
		return *this;
	}

	friend bool operator==(const ColorBlob &b1, const ColorBlob &b2);

	friend ColorBlob operator+(const ColorBlob &b1, const ColorBlob &b2);

	friend ColorBlob operator-(const ColorBlob &b1, const ColorBlob &b2);

	friend ColorBlob operator*(const ColorBlob &blob, const Color &color);

	friend bool operator!(const ColorBlob &blob);

	friend ostream &operator<<(ostream &stream, const ColorBlob &blob);

	friend istream &operator>>(istream &stream, ColorBlob &blob);

	Color &operator[](const int i);

	int getHeight();

	int getWidth();

	Color **getData();
};


#endif //HW3_COLORBLOB_H
