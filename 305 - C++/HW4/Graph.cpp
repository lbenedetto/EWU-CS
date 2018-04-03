#include <fstream>
#include <sstream>
#include <iostream>
#include "Graph.h"

bool Graph::loadCityInformation(string filename) {
	ifstream file(filename);
	string line;
	int cityNum = 0;
	while (getline(file, line)) {
		istringstream iss(line);
		string controlCharacter;
		iss >> controlCharacter;
		if (controlCharacter == "p") {
			double d[3];
			iss >> d[0] >> d[1] >> d[2];
			cityCoordinates.push_back({d[0], d[1], d[2]});
			cityIndices.push_back(cityNum++);
		} else if (controlCharacter == "c") {
			int c[3];
			iss >> c[0] >> c[1] >> c[2];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					if (i != j)
						cityConnectivity[c[i]].insert(c[j]);
				}
			}
		} else if (controlCharacter == "")
			continue;
		else
			return false;

	}
	return true;
}

void Graph::Generate() {

}

void Graph::PrintInformation() {
	for (auto k : cityConnectivity) {
		cout << "[" << k.first << "] ";
		for (auto v : k.second) {
			cout << v << " ";
		}
		cout << endl;
	}
}

void Graph::showConnectivity(int ix) {
	for (auto v : cityConnectivity[ix]) {
		cout << "[" << ix << "-" << v << "]: ";
		Point3D p1 = cityCoordinates[ix];
		Point3D p2 = cityCoordinates[v];
		cout << p1.distanceBetween(p2) << endl;
	}
}
