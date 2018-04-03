/**
 * Keeps track of data for every sort method called.
 * There are 48 instances of this object used in this program
 */
public class Tracker {
	//Comparing data of node
	public int dataComparisons;
	//Changing data of node
	public int dataAssignments;
	//Comparing data of variables controlling loop
	public int loopControlComparisons;
	//Changing data for variables controlling loop
	public int loopControlAssignments;
	public int other;
	public double time;

	public Tracker() {
		dataComparisons = 0;
		dataAssignments = 0;
		loopControlComparisons = 0;
		loopControlAssignments = 0;
		other = 0;
		time = 0;
	}

	public int total() {
		return dataComparisons + dataAssignments +
				loopControlComparisons + loopControlAssignments + other;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public String getData(int i) {
		switch (i) {
			case 0:
				return String.valueOf(dataAssignments);
			case 1:
				return String.valueOf(total());
		}
		return "Wrong";
	}
	public double getTime(){
		return time / 1000000000.0;
	}
}
