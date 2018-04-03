public class Thief extends Hero {
    public Thief() {
        //(String name, int hitPoints, int attackSpeed, int damageMax, int damageMin, double chanceToHit, double chanceToBlock)
        super("Thief", 75, 6, 40, 20, .8, .4);
    }

    public void surpriseAttack(DungeonCharacter c) {
        if (Math.random() <= .4D) {
            if (Math.random() <= .2D) {
                Interface.showln(c.getName() + " saw you and blocked your attack!");
            } else {
                Interface.showln("Surprise attack succeeded");
                Interface.showln(this.name + " gets an additional turn.");
                this.remainingTurns += 1;
                attack(c);
            }
        } else {
            attack(c);
        }
    }

    public void battleChoices(DungeonCharacter opponent) {
        super.battleChoices(opponent);
        do {
            Interface.showln("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            Interface.showln("1. Attack Opponent");
            Interface.showln("2. Surprise Attack");
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
                        Interface.showln("You attempt a surprise attack on " + opponent.getName());
                        surpriseAttack(opponent);
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
