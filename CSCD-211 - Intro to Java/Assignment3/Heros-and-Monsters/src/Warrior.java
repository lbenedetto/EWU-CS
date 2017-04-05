public class Warrior extends Hero {
    public Warrior() {
        //(String name, int hitPoints, int attackSpeed, int damageMax, int damageMin, double chanceToHit, double chanceToBlock)
        super("Warrior", 125, 4, 60, 35, .8D, .2D);
    }

    public void crushingBlow(DungeonCharacter c) {
        if (Math.random() <= .4D) {
            int dmg = HeroesVersusMonsters.randInt(75, 175);
            Interface.showln(this.name + " lands a crushing blow for " + dmg + " damage!");
            c.subtractHitPoints(dmg, true);
        } else {
            Interface.showln(this.name + " failed to land a crushing blow");
        }
    }

    public void battleChoices(DungeonCharacter opponent) {
        super.battleChoices(opponent);
        do {
            Interface.showln("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            Interface.showln("1. Attack Opponent");
            Interface.showln("2. Crushing Blow on Opponent");
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
                        Interface.showln("You attempt to land a crushing blow on " + opponent.getName());
                        crushingBlow(opponent);
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
