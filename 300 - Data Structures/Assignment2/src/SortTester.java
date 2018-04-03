/**
 * @author Lars Benedetto
 * Extra credit attempted, outputting CSV file
 * I also designed my own sorting algorithm which I named Sieve Sort in this project
 * Turns out its actually just a dumb version of radix sort)
 */

import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

public class SortTester {
	public static Tracker[][][] tracker = new Tracker[5][3][4];
	public static ArrayList<LinkedList<BigInteger>> lists = new ArrayList<>(4);

	public static void main(String[] args) {
		//Generate linked lists of varying length
		populateLists();
		//Create trackers for the different sorting methods
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 3; j++)
				for (int k = 0; k < 4; k++)
					tracker[i][j][k] = new Tracker();
		//Create instances of Sorters for use in lambdas
		testSorter(new Bubble());
		testSorter(new Selection());
		testSorter(new DataInsertion());
		testSorter(new NodeInsertion());
		testSorter(new Sieve());
		saveResults();
	}

	public static void testSorter(Sorter s) {
		int i = 0;
		int type = s.sortType();
		for (LinkedList<BigInteger> list : lists) {
			LinkedList<BigInteger> newList = list.clone();
			s.sort(newList, tracker[type][0][i], "randomized");
			s.sort(newList, tracker[type][1][i], "sorted");
			LinkedList<BigInteger> listDescendingSorted = newList.cloneReverse();
			s.sort(listDescendingSorted, tracker[type][2][i], "descending sorted");
			i++;
		}
	}

	public static void saveResults() {
		PrintWriter csvFile = null;
		PrintWriter txtFile = null;
		try {
			//Clean previous output
			boolean clearedCSV;
			boolean clearedTXT;
			clearedCSV = new File("sort_results.csv").delete();
			clearedTXT = new File("sort_results.txt").delete();
			if (clearedCSV) println("Deleted previous CSV output");
			if (clearedTXT) println("Deleted previous TXT output");
			csvFile = new PrintWriter(new FileWriter("sort_results.csv", true));
			txtFile = new PrintWriter(new FileWriter("sort_results.txt", true));
		} catch (IOException e) {
			println("Something prevented the program from creating the output file");
			System.exit(-1);
		}
		outputManager(csvFile, ",");
		csvFile.close();
		outputManager(txtFile, "	");
		txtFile.close();
	}

	public static void outputManager(PrintWriter out, String separator) {
		out.println("Data Assignments");
		outputGenerator(0, out, separator);
		out.println("Total Operations");
		outputGenerator(1, out, separator);
		out.println("Time to Completion");
		outputGenerator(2, out, separator);
	}

	public static void outputGenerator(int dataType, PrintWriter out, String sep) {
		for (int i = 0; i < 3; i++) {
			out.println("List Type" + sep + "List Size" + sep + "Bubble" + sep + "Selection" + sep + "Data Insertion" + sep + "Node Insertion" + sep + "Sieve");
			String[] type = {"random" + sep, "sorted" + sep, "descending sorted" + sep};
			for (int j = 0; j < 4; j++) {
				String[] size = {"Size: 500", "Size: 1000", "Size: 5000", "Size: 10000"};
				out.print(type[i] + size[j]);
				for (int k = 0; k < 5; k++) {
					out.print(sep);
					if(dataType == 2)
						out.printf("%.9f", tracker[k][i][j].getTime());
					else
						out.print(tracker[k][i][j].getData(dataType));
				}
				out.println();
			}
		}
	}

	public static void populateLists() {
		//This method is less efficient and takes up more lines of code than my previous method
		//However, this method provides a fairer test, as lists are identical, up to the size of the previous list
		//This prevents a list from "getting lucky" and making it take more or less time than it should
		LinkedList<BigInteger> list = new LinkedList<>();
		populateList(list, 500);
		lists.add(list.clone());
		populateList(list, 500);
		lists.add(list.clone());
		populateList(list, 4000);
		lists.add(list.clone());
		populateList(list, 5000);
		lists.add(list.clone());
	}

	public static void populateList(LinkedList<BigInteger> list, int size) {
		Random random = new Random();
		for (int i = 0; i < size; i++) {
			//Adds a random BigInteger in the range [1,2^14)
			list.add(new BigInteger(14, random).add(BigInteger.ONE));
		}
	}

	public static void println(String s) {
		System.out.println(s);
	}

	public interface Sorter {
		void sort(LinkedList<BigInteger> list, Tracker t, String type);

		int sortType();
	}

	public static class Bubble implements Sorter {
		public void sort(LinkedList<BigInteger> list, Tracker t, String type) {
			println("Timing Bubble Sort on " + type + " list of size " + list.size());
			LinkedList<BigInteger> newList = list.clone();
			list.bubbleSortAndTime(t);
			println("Tracking Bubble Sort on " + type + " list of size " + list.size());
			newList.bubbleSortAndTrack(t);
		}

		public int sortType() {
			return 0;
		}
	}

	public static class Selection implements Sorter {
		public void sort(LinkedList<BigInteger> list, Tracker t, String type) {
			println("Timing Selection Sort on " + type + " list of size " + list.size());
			LinkedList<BigInteger> newList = list.clone();
			list.selectionSortAndTime(t);
			println("Tracking Selection Sort on " + type + " list of size " + list.size());
			newList.selectionSortAndTrack(t);
		}

		public int sortType() {
			return 1;
		}
	}

	public static class DataInsertion implements Sorter {
		public void sort(LinkedList<BigInteger> list, Tracker t, String type) {
			println("Timing Data Insertion Sort on " + type + " list of size " + list.size());
			LinkedList<BigInteger> newList = list.clone();
			list.dataInsertionSortAndTime(t);
			println("Tracking Data Insertion Sort on " + type + " list of size " + list.size());
			newList.dataInsertionSortAndTrack(t);
		}

		public int sortType() {
			return 2;
		}
	}

	public static class NodeInsertion implements Sorter {
		public void sort(LinkedList<BigInteger> list, Tracker t, String type) {
			println("Timing Node Insertion on " + type + " list of size " + list.size());
			LinkedList<BigInteger> newList = list.clone();
			list.nodeInsertionSortAndTime(t);
			println("Tracking Node Insertion Sort on " + type + " list of size " + list.size());
			newList.nodeInsertionSortAndTrack(t);
		}

		public int sortType() {
			return 3;
		}
	}

	public static class Sieve implements Sorter {
		public void sort(LinkedList<BigInteger> list, Tracker t, String type) {
			println("Timing Sieve Sort on " + type + " list of size " + list.size());
			LinkedList<BigInteger> newList = list.clone();
			list.sieveSortAndTime(t);
			println("Tracking Sieve Sort Sort on " + type + " list of size " + list.size());
			newList.sieveSortAndTrack(t);
		}

		public int sortType() {
			return 4;
		}
	}
}
