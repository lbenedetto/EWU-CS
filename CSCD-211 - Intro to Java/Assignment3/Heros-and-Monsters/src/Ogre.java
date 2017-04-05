public class Ogre extends Monster {
    public Ogre() {
        //String name, int hitPoints, int attackSpeed, int damageMax, int damageMin, double chanceToHit, double chanceToHeal, int healMin, int healMax
        super("Ogre", 200, 2, 60, 30, .6D, .1D, 30, 60);
    }
    public void sneeze(DungeonCharacter opponent){
        int dmg = HeroesVersusMonsters.randInt(50,80);
        if(dmg <= 60){
            Interface.showln(this.getName() + " sneezes forcefully, covering you in snot");
            opponent.subtractHitPoints(dmg, true);
        }else if (dmg <= 70){
            Interface.showln(this.getName() + " sneezes violently, perforating your eardrums and covering you in snot");
            opponent.subtractHitPoints(dmg, true);
        }else if (dmg <= 80){
            Interface.showln(this.getName() + " sneezes with the force of a thousand suns.");
            Interface.showln("The shockwave knocks you backwards, shattering your bones and covering you in snot");
            opponent.subtractHitPoints(dmg, true);
        }

    }

    public void battleChoices(DungeonCharacter opponent) {
        if (Math.random() <= 0.3D) {
            Interface.showln("Something tickles " + this.getName() + "'s nose...");
            sneeze(opponent);
        } else {
            Interface.showln(this.getName() + " attacks you");
            attack(opponent);
        }
    }
}
