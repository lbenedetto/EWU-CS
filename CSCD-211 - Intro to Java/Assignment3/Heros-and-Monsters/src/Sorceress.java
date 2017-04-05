public class Sorceress extends Hero {
    public Sorceress() {
        //(String name, int hitPoints, int attackSpeed, int damageMax, int damageMin, double chanceToHit, double chanceToBlock)
        super("Sorceress", 75, 5, 45, 25, .7D, .43);
    }

    public void heal() {
        int hp = HeroesVersusMonsters.randInt(20, 30);
        addHitPoints(hp);
        Interface.showln(this.name + " healed [" + hp + "] points.\n" +
                "Total HP remaining is: " +
                this.hitPoints);
        Interface.showln("");
    }

    public void battleChoices(DungeonCharacter opponent) {
        super.battleChoices(opponent);
        do {
            Interface.showln("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            Interface.showln("1. Attack Opponent");
            Interface.showln("2. Heal");
            Interface.show("Choose an option: ");
            int choice;
            do {
                choice = Interface.readInt();
                switch (choice) {
                    case 1:
                        Interface.showln("You attack " + opponent.getName());
                        attack(opponent);
                        break;
                    case 2:
                        Interface.showln("You cast a healing spell on yourself");
                        heal();
                        break;
                    default:
                        Interface.show("Invalid Choice. Try again: ");
                }
            } while ((choice != 1) && (choice != 2));

            this.remainingTurns -= 1;
            if (this.remainingTurns > 0) {
                Interface.showln(this.remainingTurns + " turns remaining");
            }
        } while (this.remainingTurns > 0);
    }
}
