public class Gremlin extends Monster {
    public Gremlin() {
        super("Gremlin", 70, 5, 30, 15, .8D, .4D, 20, 40);
    }

    public void sabotage(DungeonCharacter opponent) {
        opponent.setDamageMax(getDamageMax() - (getDamageMin() / 4));
        Interface.showln("Your maximum damage has been decreased by 1/4 your minimum damage");
    }

    public void battleChoices(DungeonCharacter opponent) {
        if (Math.random() <= 0.3D) {
            Interface.showln(this.getName() + "sabotages your weapon");
            sabotage(opponent);
        } else {
            Interface.showln(this.getName() + " attacks you");
            attack(opponent);
        }
    }
}
