public class Accountant extends Employee {
    private double parkingAllowance;

    public Accountant(String name, double p) {
        super(name);
        parkingAllowance = p;
    }

    public double getParkingAllowance() {
        return parkingAllowance;
    }

    @Override
    public void reportSalary() {
        System.out.println("I am an accountant. I make " + getSalary() + " plus a parking allowance of " + getParkingAllowance());
    }

    @Override
    public int getSalary() {
        return super.salary + 0;
    }
}
