import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Supervisors {

	public static void main(String[] args) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader("input.txt"));
		int dataSets = Integer.valueOf(in.readLine());
		for (int i = 1; i <= dataSets; i++) {
			int dataSetSize = Integer.valueOf(in.readLine());
			List<String> supervisors = new ArrayList<>();
			List<String> employees = new ArrayList<>();
			Map<String, List<String>> supervisorPrefers = new HashMap<>();
			Map<String, List<String>> employeePrefers = new HashMap<>();
			//Read supervisor names
			for (int k = 0; k < dataSetSize; k++)
				supervisors.add(in.readLine());
			//Read supervisor prefs
			for (int k = 0; k < dataSetSize; k++)
				supervisorPrefers.put(supervisors.get(k), Arrays.asList(in.readLine().split(" ")));
			//Read employee names
			for (int k = 0; k < dataSetSize; k++)
				employees.add(in.readLine());
			//Read employee prefs
			for (int k = 0; k < dataSetSize; k++)
				employeePrefers.put(employees.get(k), Arrays.asList(in.readLine().split(" ")));

			Map<String, String> matches = match(supervisors, supervisorPrefers, employeePrefers);
			System.out.println("Case " + i);
			for (Map.Entry<String, String> couple : matches.entrySet()) {
				System.out.println(couple.getKey() + " is employed by " + couple.getValue());
			}
		}

	}

	/**
	 * This uses a solution to the Stable Marriage Problem
	 */
	private static Map<String, String> match(List<String> supervisors, Map<String, List<String>> supervisorPrefers, Map<String, List<String>> employeePrefers) {
		Map<String, String> employedBy = new TreeMap<>();
		List<String> freeSupervisors = new LinkedList<>();
		freeSupervisors.addAll(supervisors);
		while (!freeSupervisors.isEmpty()) { //While there are supervisors with no employees
			String thisSupervisor = freeSupervisors.remove(0); //Remove the first supervisor from that list
			List<String> thisSupervisorPrefers = supervisorPrefers.get(thisSupervisor); //get their preferred candidate
			for (String employee : thisSupervisorPrefers) {//for every employee on the supervisors list of preferred employees
				if (employedBy.get(employee) == null) {//check if the employee is already taken
					employedBy.put(employee, thisSupervisor);//if they are not already employed, assign them to the supervisor
					break;
				} else {//If the employee is already taken, find their preferred employer
					String otherSupervisor = employedBy.get(employee);//Find who the employee is employed by
					List<String> thisEmployeePrefers = employeePrefers.get(employee);//Find the employees preferred supervisor
					if (thisEmployeePrefers.indexOf(thisSupervisor) < thisEmployeePrefers.indexOf(otherSupervisor)) {
						//If this employee prefers this supervisor to the supervisor they're employed by
						employedBy.put(employee, thisSupervisor);//Employ them by this super visor
						freeSupervisors.add(otherSupervisor);//And put their employer back in the free supervisors queue
						break;
					}//else no change...keep looking for this supervisor
				}
			}
		}
		return employedBy;
	}
}