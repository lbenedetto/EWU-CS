import java.util.*;

class Graph {
	private final HashMap<Integer, SortedSet<Edge>> adjacencyList;

	Graph() {
		adjacencyList = new HashMap<>();
	}

	static Graph loadDefaultGraph() {
		Graph graph = new Graph();
		graph.addEdge(0, 1, 4);
		graph.addEdge(0, 2, 6);
		graph.addEdge(0, 3, 16);
		graph.addEdge(1, 7, 24);
		graph.addEdge(2, 3, 8);
		graph.addEdge(2, 5, 5);
		graph.addEdge(2, 7, 23);
		graph.addEdge(3, 4, 21);
		graph.addEdge(3, 5, 10);
		graph.addEdge(4, 5, 14);
		graph.addEdge(4, 6, 7);
		graph.addEdge(5, 6, 11);
		graph.addEdge(5, 7, 18);
		graph.addEdge(6, 7, 9);
		return graph;
	}

	void breadthFirstPrint(int node) {
		Queue<Integer> q = new LinkedList<>();
		HashSet<Integer> visited = new HashSet<>();
		q.add(node);
		do {
			int i = q.remove();
			visited.add(i);
			System.out.print(i);
			adjacencyList.get(i)
					.forEach(x -> {
						if (!visited.contains(x.dest)) {
							q.add(x.dest);
							visited.add(x.dest);
						}
					});
			if (!q.isEmpty()) System.out.print("->");
		} while (!q.isEmpty());

	}

	void addEdge(int src, int dest, int weight) {
		addEdge(src, new Edge(dest, weight));
		addEdge(dest, new Edge(src, weight));
	}

	private void addEdge(int src, Edge edge) {
		if (adjacencyList.containsKey(src)) {
			adjacencyList.get(src).add(edge);
		} else {
			SortedSet<Edge> edges = new TreeSet<>();
			edges.add(edge);
			adjacencyList.put(src, edges);
		}
	}

	private class Edge implements Comparable<Edge> {
		final Integer dest;
		final Integer weight;

		Edge(int dest, int weight) {
			this.dest = dest;
			this.weight = weight;
		}

		@Override
		public int compareTo(Edge e) {
			return weight - e.weight;
		}
	}

}
