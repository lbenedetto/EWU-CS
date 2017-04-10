//Lars Benedetto
//I used CLion which uses CMake
#include <iostream>

using namespace std;

int readInt(string prompt) {
	int i;
	string str;
	while (true) {
		cout << prompt;
		if (cin >> i && cin.get() == '\n') {
			if (i >= 1) {
				return i;
			}
			cout << "Number is outside of valid range" << endl;
		} else {
			cout << "Invalid number, try again" << endl;
			cin.clear();
			cin.ignore(10000, '\n');
		}
	}
}

float readFloat(string prompt, int upperBound, int lowerBound) {
	float i;
	string str;
	while (true) {
		cout << prompt;
		if (cin >> i && cin.get() == '\n') {
			if (i <= upperBound && i >= lowerBound) {
				return i;
			}
			cout << "Number does not fit within bounds" << endl;
		} else {
			cout << "Invalid number, try again" << endl;
			cin.clear();
			cin.ignore(10000, '\n');
		}
	}
}

int main() {
	string str;
	int students;
	students = readInt("Enter number of students: ");
	float *scores = new float[students];
	cout << "Enter scores for students:" << endl;
	for (int i = 0; i < students; i++) {
		scores[i] = readFloat("Enter score for student #" + to_string(i + 1) + ": ", 100, 0);
	}
	float max = 0;
	float min = 100;
	float total = 0;
	for (int i = 0; i < students; i++) {
		float curr = scores[i];
		if (curr > max) max = curr;
		if (curr < min) min = curr;
		total += curr;
	}
	cout << "Min score: " << min << endl;
	cout << "Max score: " << max << endl;
	cout << "Average score: " << total / students << endl;
	return 0;
}