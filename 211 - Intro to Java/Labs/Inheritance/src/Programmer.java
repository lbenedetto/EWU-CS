public class Programmer extends Employee {
    private boolean hasBusPass;

    public Programmer(String name, boolean hasBusPass) {
        super(name);
        this.hasBusPass = hasBusPass;
    }

    public boolean getBusPass() {
        return hasBusPass;
    }

    @Override
    public void reportSalary() {
        System.out.println("I am a programmer. I make " + getSalary() + " and I" +
                ((getBusPass()) ? " get a bus pass." : " do not get a bus pass."));
    }

    @Override
    public int getSalary() {
        return super.salary + 20000;
    }
}
