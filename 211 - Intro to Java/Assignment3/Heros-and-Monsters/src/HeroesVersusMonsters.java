import java.util.Random;

public class HeroesVersusMonsters {
    public static void main(String[] args) {
        do {
            Hero PC = chooseHero();
            Monster NPC = getMonster();
            Interface.showln(PC.getName() + " battles " + NPC.getName());
            battle(PC, NPC);
        } while (playAgain());
    }

    public static Hero chooseHero() {
        //If the user doesn't choose one of the 3 main classes, they will be assigned the idiot class
        Interface.showln("1. Warrior");
        Interface.showln("2. Sorceress");
        Interface.showln("3. Thief");
        Interface.show("Choose a hero: ");
        int choice = Interface.readInt();
        switch (choice) {
            case 1:
                return new Warrior();
            case 2:
                return new Sorceress();
            case 3:
                return new Thief();
        }
        Hero out = new ForgetfulIdiot();
        Interface.showln("Invalid choice");
        return out;
    }

    public static Monster getMonster() {
        switch (randInt(1, 3)) {
            case 1:
                return new Ogre();
            case 2:
                return new Gremlin();
            case 3:
                return new Skeleton();
        }
        System.out.println("Invalid choice, returning Skeleton");
        return new Skeleton();
    }

    public static void battle(Hero PC, Monster NPC) {
        char quit = 'a';
        Interface.showln("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        do {
            PC.battleChoices(NPC);
            if (NPC.getHitPoints() > 0) {
                NPC.battleChoices(PC);
            }
            if (NPC.getHitPoints() <= 0) Interface.showln(PC.getName() + " stands victorious");
            else if (PC.getHitPoints() <= 0) Interface.showln(PC.getName() + " was defeated");
            if (PC.getHitPoints() <= 0 || NPC.getHitPoints() <= 0) {
                Interface.show("q to quit, anything else to continue: ");
                quit = Interface.readChar();
            }
        } while ((PC.getHitPoints() > 0) && (NPC.getHitPoints() > 0) && (quit != 'q'));
        Interface.showln("Quitting game...");
    }

    public static boolean playAgain() {
        do {
            Interface.show("Play again (y/n)? ");
            char again = Interface.readChar();
            if ((again == 'Y') || (again == 'y')) return true;
            if ((again == 'N') || (again == 'n')) return false;
            Interface.showln("Invalid choice");
        } while (true);
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static double randDouble(double min, double max) {
        Random rand = new Random();
        rand.nextDouble();
        return min + (max - min) * rand.nextDouble();
    }
}
