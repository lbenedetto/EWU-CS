import java.util.LinkedList;

public class HashTable {
	public Bucket[] buckets;
	public int size;
	public int collisions;

	public HashTable(int size) {
		this.size = size;
		buckets = new Bucket[size];
		collisions = 0;
	}

	public void add(String word) {
		AnagramPair newPair = new AnagramPair(word);
		if (buckets[newPair.hash] == null)
			buckets[newPair.hash] = new Bucket();
		for (AnagramPair p : buckets[newPair.hash]) {
			if (!p.key.equals(newPair.key)) {
				collisions++;
			}
		}
		buckets[newPair.hash].add(newPair);
	}

	public Bucket getAnagrams(AnagramPair word) {
		return buckets[word.hash];
	}

	public class Bucket extends LinkedList<AnagramPair> {
		public Bucket() {
			super();
		}
	}
}
