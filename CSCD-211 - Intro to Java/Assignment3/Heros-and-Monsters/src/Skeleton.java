public class Skeleton extends Monster {
    public Skeleton() {
        super("Skeleton", 100, 3, 50, 30, .8D, .3, 30, 50);
    }

    public void spook(DungeonCharacter opponent) {
        Interface.showln("And you rattle in your boots");
        Interface.showln("You lose your courage and your chance to hit is decreased");
        opponent.setchanceToHit(opponent.getchanceToHit() - 0.1D);
    }

    public void battleChoices(DungeonCharacter opponent) {
        if (Math.random() <= 0.3D) {
            Interface.showln(this.getName() + " rattles their bones");
            spook(opponent);
        } else {
            Interface.showln(this.getName() + " attacks you");
            attack(opponent);
        }
    }
}
