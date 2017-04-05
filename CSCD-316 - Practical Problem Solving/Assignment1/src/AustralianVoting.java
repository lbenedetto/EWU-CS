import javax.xml.bind.ValidationException;
import java.io.*;
import java.util.ArrayList;

/**
 * @author Lars Benedetto
 * Problem: Australian Voting
 * Edge Cases:
 *      Ties between two or more candidates
 *      Small amounts of candidates and ballots
 *      Large (maximum) amount of candidates and ballots
 *
 * Did not safeguard program against malformed input files,
 * or against reading more than 1000 ballots
 */
// At the top of your solution include comments that include your name, which problem you solved, and the edge cases that your ROBUST test file checks for.
//https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=29&page=show_problem&problem=1083
public class AustralianVoting {
	private static PrintWriter out;
	public static void main(String[] args) throws ValidationException {
		String infilename = "";
		if (args.length != 0)
			infilename = args[0];
		if (infilename.isEmpty()) {
			infilename = "input.txt";
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader(infilename));
			out = new PrintWriter("output.txt");
			String line;
			int count = Integer.parseInt(in.readLine());
			in.readLine();
			for (int i = 0; i < count; i++) {
				//Create the array of Candidates
				int cand = Integer.parseInt(in.readLine());
				if(cand > 20) throw new ValidationException("Too many candidates");
				Candidate[] candidates = new Candidate[cand];
				for (int j = 0; j < cand; j++) {
					line = in.readLine();
					if (line.length() > 80)
						throw new ValidationException("Candidate name was too long\n" + line);
					candidates[j] = new Candidate(line);
				}
				//Create the array of Ballots
				ArrayList<Ballot> ballots = new ArrayList<>();
				line = in.readLine();
				do {
					ballots.add(new Ballot(line));
					line = in.readLine();
				} while (line != null && !line.isEmpty());
				//Number of ballots is the total number of votes, so ballots.size will be used
				//Count the ballots
				boolean winnerFound = false;
				while (!winnerFound) {
					//Count or Recount votes
					for (Candidate c : candidates) {
						c.votes = 0;
					}
					for (Ballot b : ballots) {
						count(candidates, b, 0);
					}
					//Find high and low
					int highest = 0;
					int lowest = 1001;
					for (Candidate c : candidates) {
						if (!c.droppedOut) {
							if (c.votes < lowest)
								lowest = c.votes;
							if (c.votes > highest)
								highest = c.votes;
						}
					}
					//Check for winner
					if (highest / ballots.size() > .5 || highest == lowest) {
						winnerFound = true;
						for (Candidate c : candidates) {
							if (!c.droppedOut) {
								if (c.votes == highest) {
									output(c.name);
								}
							}
						}
						output("");
					}
					if (!winnerFound) {
						//Eliminate candidates
						for (Candidate c : candidates) {
							if (!c.droppedOut) {
								if (c.votes == lowest) {
									//output("Eliminating candidate " + c.name);
									c.droppedOut = true;
								}
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			output("Couldn't open file");
			e.printStackTrace();
		} catch (IOException e) {
			output("Ya dun fucked up");
			e.printStackTrace();
		}
		out.close();
		//Generator.generateResults(20,1000);
	}

	private static void count(Candidate[] candidates, Ballot b, int k) {
		if (candidates[b.votes[k]].droppedOut) {
			count(candidates, b, k + 1);
		} else {
			candidates[b.votes[k]].votes++;
		}

	}
	private static void output(String s){
		System.out.println(s);
		out.println(s);
	}
	private static class Candidate {
		String name;
		int votes;
		boolean droppedOut;

		Candidate(String name) {
			this.name = name;
			votes = 0;
			droppedOut = false;
		}

		//Debugging
		@Override
		public String toString() {
			if(droppedOut)
				return "Dead";
			return name + votes;
		}
	}

	private static class Ballot {
		int[] votes;

		Ballot(String in) {
			String[] temp = in.split(" ");
			votes = new int[temp.length];
			for (int i = 0; i < temp.length; i++) {
				votes[i] = Integer.parseInt(temp[i]) - 1;
			}
		}
	}

}
