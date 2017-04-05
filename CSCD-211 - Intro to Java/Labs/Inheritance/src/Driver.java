import java.util.ArrayList;

public class Driver {
    public static void main(String[] args){
        ArrayList<Employee> employees = new ArrayList<>(8);
        employees.add(new Programmer("Lars Benedetto", false));
        employees.add(new Programmer("Will E. Makit", true));
        employees.add(new Lawyer("Ivana Killmen", 11));
        employees.add(new Lawyer("Luke N. Dimm", 0));
        employees.add(new Lawyer("Eileen Dover", 100));
        employees.add(new Accountant("Bill Cheatem", 17.00));
        employees.add(new Accountant("Joe Kisonyou", 45.00));
        employees.add(new Accountant("Seymore Butts", 2.50));
        for(Employee temp : employees){
            System.out.print(temp.getName() + ": ");
            temp.reportSalary();
        }
    }
}
