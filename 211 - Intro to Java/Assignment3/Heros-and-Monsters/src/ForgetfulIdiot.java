public class ForgetfulIdiot extends Hero {
    public ForgetfulIdiot() {
        //(String name, int hitPoints, int attackSpeed, int damageMax, int damageMin, double chanceToHit, double chanceToBlock)
        super("ForgetfulIdiot", 100, 1, 10, 0, .9D, .9D);
        forgetStats();
    }

    public void forgetStats() {
        //The player forgets what their stats are and makes up some new ones to cover their incompetence
        //Stats range from the worst of any character in the game, to the best of any character in the game
        this.name = generateIdiotName();
        hitPoints = HeroesVersusMonsters.randInt(70, 200);
        //Without this line forgetStats() would be a very overpowered healing ability
        subtractHitPointsSilently(this.damageTaken);
        attackSpeed = HeroesVersusMonsters.randInt(2, 6);
        damageMax = HeroesVersusMonsters.randInt(30, 60);
        damageMin = HeroesVersusMonsters.randInt(15, 30);
        chanceToHit = HeroesVersusMonsters.randDouble(0.6D, 0.8D);
        chanceToBlock = HeroesVersusMonsters.randDouble(0.2D, 0.4D);
        Interface.showln("You are now " + this.getName() + ", a forgetful idiot with a max health of " + this.getHitPoints());
        Interface.showln("You have an attack speed of " + this.getAttackSpeed() + " and deal damage ranging from " + this.getDamageMin() + " to " + this.getDamageMax());
        Interface.showln("You have a " + (int) (this.getchanceToHit() * 100) + "% chance to hit, and a " + (int) (this.getChanceToBlock() * 100) + "% chance to block");
    }

    public void battleChoices(DungeonCharacter opponent) {
        super.battleChoices(opponent);
        do {
            Interface.showln("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            Interface.showln("1. Attack Opponent");
            Interface.showln("2. Forget stats");
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
                        Interface.showln("You can't quite remember what your stats are,");
                        Interface.showln("so you confidently make up new ones to try to save face");
                        forgetStats();
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
