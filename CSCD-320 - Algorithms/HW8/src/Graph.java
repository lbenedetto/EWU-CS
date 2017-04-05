import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.SortedSet;
import java.util.TreeSet;

class Graph {
	private final HashMap<Integer, SortedSet<Edge>> adjacencies;
	private final HashMap<Integer, Node> nodes;

	Graph() {
		adjacencies = new HashMap<>();
		nodes = new HashMap<>();
	}

	static Graph loadDefaultGraph() {
		Graph graph = new Graph();
		graph.addEdge(1, 2, 9);
		graph.addEdge(1, 6, 14);
		graph.addEdge(1, 7, 15);
		graph.addEdge(2, 3, 24);
		graph.addEdge(3, 8, 19);
		graph.addEdge(3, 5, 2);
		graph.addEdge(4, 3, 6);
		graph.addEdge(4, 8, 6);
		graph.addEdge(5, 4, 11);
		graph.addEdge(5, 8, 16);
		graph.addEdge(6, 3, 18);
		graph.addEdge(6, 5, 30);
		graph.addEdge(6, 7, 5);
		graph.addEdge(7, 5, 20);
		graph.addEdge(7, 8, 44);
		return graph;
	}

	void printDijkstras(int src) {
		adjacencies.keySet().forEach(x -> nodes.put(x, new Node(x)));
		Node source = nodes.get(src);
		source.minDistance = 0;
		PriorityQueue<Node> pq = new PriorityQueue<>();
		pq.add(source);
		while (!pq.isEmpty()) {
			Node curr = pq.poll();
			for (Edge e : curr.adjacencies2) {
				Node dest = nodes.get(e.dest);
				int weight = e.weight;
				int distanceThroughCurr = curr.minDistance + weight;
				if (distanceThroughCurr < dest.minDistance) {
					pq.remove(dest);
					dest.minDistance = distanceThroughCurr;
					dest.previous = curr;
					pq.add(dest);
				}
			}
		}
		int j = 1;
		for (int target : adjacencies.keySet()) {
			Node n = nodes.get(target);
			System.out.printf("Path %d) vertex %d to %d, %14s, length %d\n", j++, src, target, n.getPathString(), n.minDistance);
		}
	}

	void addEdge(int src, int dest, int weight) {
		Edge edge = new Edge(src, dest, weight);
		if (adjacencies.containsKey(src)) {
			adjacencies.get(src).add(edge);
		} else {
			SortedSet<Edge> edges = new TreeSet<>();
			edges.add(edge);
			adjacencies.put(src, edges);
		}
		if(!adjacencies.containsKey(dest)){
			SortedSet<Edge> edges = new TreeSet<>();
			edges.add(edge);
			adjacencies.put(dest, edges);
		}
	}

	class Node implements Comparable<Node> {
		int value;
		Edge[] adjacencies2;
		int minDistance = Integer.MAX_VALUE;
		Node previous;

		Node(int value) {
			this.value = value;
			SortedSet<Edge> temp = adjacencies.get(value);
			adjacencies2 = temp.toArray(new Edge[temp.size()]);
		}

		@Override
		public int compareTo(Node other) {
			return minDistance - other.minDistance;
		}

		String getPathString() {
			String out = "";
			for (Node n = this; n != null; n = n.previous)
				out += ">-" + n.value;
			out = out.substring(2, out.length());
			return new StringBuilder(out).reverse().toString();
		}

	}

	private class Edge implements Comparable<Edge> {
		final int src;
		final int dest;

		final Integer weight;

		Edge(int src, int dest, int weight) {
			this.src = src;
			this.dest = dest;
			this.weight = weight;
		}

		@Override
		public int compareTo(Edge e) {
			return weight - e.weight;
		}


	}
}
