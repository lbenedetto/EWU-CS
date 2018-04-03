#ifndef HW4_GRAPH_H
#define HW4_GRAPH_H

#include <vector>
#include <set>
#include <map>
#include "Point3D.h"

using namespace std;


class Graph {
private:
	vector<int> cityIndices;
	vector<Point3D> cityCoordinates;
	map<int, set<int>> cityConnectivity;
public:
	bool loadCityInformation(string filename);

	void Generate();

	void PrintInformation();

	void showConnectivity(int ix);
};


#endif //HW4_GRAPH_H
