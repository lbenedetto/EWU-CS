public abstract class Employee {
    private String name;
    protected int salary = 40000;

    public Employee(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public abstract int getSalary();
    public abstract void reportSalary();
}
