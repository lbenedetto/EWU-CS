import java.util.HashMap;
import java.util.HashSet;

class UnionFind {
	private HashMap<Integer, HashSet<Integer>> disjointedSets;

	UnionFind(int size) {
		disjointedSets = new HashMap<>();
		for (int i = 0; i < size; i++) {
			HashSet<Integer> temp = new HashSet<>();
			temp.add(i);
			disjointedSets.put(i, temp);
		}
	}

	//Determine which subset a particular element is in.
	//(Return the KEY to the hashmap that gives the ArrayList which contains it)
	int find(int x) {
		if (disjointedSets.get(x) != null) return x;
		for (int i = 0; i < disjointedSets.size(); i++) {
			HashSet<Integer> set = disjointedSets.get(i);
			if (set != null && set.contains(x)) return i;
		}
		return -1;
	}
	//Adds everything in set 2 to set 1
	boolean union(int set1, int set2) {
		if ((set1 != -1 && set2 != -1) && set1 != set2) {
			disjointedSets.get(set1).addAll(disjointedSets.get(set2));
			disjointedSets.remove(set2);
			return true;
		}
		return false;
	}
}