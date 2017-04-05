public class Lawyer extends Employee {
    private int stockOptions;

    public Lawyer(String name, int stockOptions) {
        super(name);
        this.stockOptions = stockOptions;
    }

    public int getStockOptions() {
        return stockOptions;
    }

    @Override
    public void reportSalary() {
        System.out.println("I am a lawyer. I get " + getSalary() + ", and I have " + getStockOptions() + " shares of stock.");
    }

    @Override
    public int getSalary() {
        return super.salary + 30000;
    }
}
