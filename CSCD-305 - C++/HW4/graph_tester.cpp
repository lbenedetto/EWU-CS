#include <iostream>
#include "Graph.h"

using namespace std;

int main() {
	Graph g;
	bool cityInformation = g.loadCityInformation("Terrain.obj");
	if(!cityInformation){
		cerr << "No information available";
		return 0;
	}
	g.Generate();
	g.PrintInformation();
	g.showConnectivity(0);
	return 0;
}