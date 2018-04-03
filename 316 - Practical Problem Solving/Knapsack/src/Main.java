import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

class Main {
	//As many variables as possible were moved here to reduce the overhead from the recursive solution
	private static int maxWeight = 0;
	private static Item[] items;
	private static Knapsack[][] optimalKnapsack;

	public static void main(String[] args) {
		try {
			BufferedReader file = new BufferedReader(new FileReader("backpack.in"));
			int backpacks = Integer.parseInt(file.readLine());
			for (int i = 0; i < backpacks; i++) {
				maxWeight = Integer.parseInt(file.readLine());
				int numItems = Integer.parseInt(file.readLine());
				items = new Item[numItems];
				Knapsack knapsack = new Knapsack();
				for (int j = 0; j < numItems; j++) {
					Scanner s = new Scanner(file.readLine());
					items[j] = new Item(s.nextInt(), s.nextInt());
				}
				Collections.sort(Arrays.asList(items));
				knapsack = loadBackpack(knapsack);
				System.out.println(knapsack);
				knapsack.printItems();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Knapsack loadBackpack(Knapsack knapsack) {
		//Naive solution first, can find best solution and save time
		for (Item item : items) {
			if (knapsack.actualWeight + item.weight > maxWeight) continue;
			knapsack.addItem(item);
		}
		if (knapsack.actualWeight == maxWeight) return knapsack;
		//Naive solution might not have found best option
		optimalKnapsack = new Knapsack[maxWeight + 1][items.length + 1];
		return findOptimalItems(maxWeight, 0);
	}


	private static Knapsack findOptimalItems(int w, int n) {
		if (w == 0 || n == items.length) {
			optimalKnapsack[w][n] = new Knapsack();
			return optimalKnapsack[w][n];
		}
		if (items[n].weight > w)
			return (optimalKnapsack[w][n + 1] == null) ? findOptimalItems(w, n + 1) : optimalKnapsack[w][n + 1];

		Knapsack include = (optimalKnapsack[w - items[n].weight][n + 1] == null) ?
				new Knapsack(findOptimalItems(w - items[n].weight, n + 1))
				: new Knapsack(optimalKnapsack[w - items[n].weight][n + 1]);
		include.addItem(items[n]);

		Knapsack exclude = (optimalKnapsack[w][n + 1] == null) ?
				new Knapsack(findOptimalItems(w, n + 1))
				: new Knapsack(optimalKnapsack[w][n + 1]);

		if (include.benefit > exclude.benefit) {
			optimalKnapsack[w][n] = new Knapsack(include);
			return include;
		}
		optimalKnapsack[w][n] = new Knapsack(exclude);
		return exclude;
	}

	private static class Item implements Comparable {
		final int weight;
		final int benefit;
		final double value;

		Item(int weight, int benefit) {
			this.weight = weight;
			this.benefit = benefit;
			value = (double) benefit / (double) weight;
		}

		@Override
		public int compareTo(Object o) {
			Item that = (Item) o;
			if (this.value == that.value) return 0;
			if ((this.value - that.value) < 0) return 1;
			return -1;
		}
	}

	private static class Knapsack {
		//The program could be further optimized by creating separate Knapsack and Item classes to further reduce
		//the overhead in the recursive functions by removing the unused variables.
		//For example, Knapsack only needs benefit and an array of Integers corresponding to the item it has.
		//I didn't do that because it works well enough as is, and I would have to have two different knapsack and item
		//classes. One for the naive solution and one for the dynamic solution
		int actualWeight;
		int benefit;
		final ArrayList<Item> items;

		Knapsack() {
			actualWeight = 0;
			benefit = 0;
			items = new ArrayList<>();
		}

		Knapsack(Knapsack b) {
			actualWeight = 0;
			benefit = 0;
			items = new ArrayList<>();
			b.items.forEach(this::addItem);
		}

		public String toString() {
			return "Knapsack weight: " + maxWeight +
					", Loaded weight: " + actualWeight +
					", Benefit: " + benefit;

		}

		void printItems(){
			for(Item i : items){
				System.out.println(i.weight + " " + i.benefit);
			}
		}

		void addItem(Item item) {
			items.add(item);
			actualWeight += item.weight;
			benefit += item.benefit;
		}
	}
}
