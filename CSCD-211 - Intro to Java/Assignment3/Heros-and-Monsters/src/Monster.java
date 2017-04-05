public abstract class Monster extends DungeonCharacter {
    protected double chanceToHeal;
    protected int healMin;
    protected int healMax;

    public Monster(String name, int hitPoints, int attackSpeed, int damageMax, int damageMin, double chanceToHit, double chanceToHeal, int healMin, int healMax) {
        super(name, hitPoints, attackSpeed, damageMax, damageMin, chanceToHit);
        generateName();
    }

    public void generateName() {
        if (this.name.equalsIgnoreCase("Ogre")) {
            generateOgreName();
        } else if (this.name.equalsIgnoreCase("Skeleton")) {
            generateSkeletonName();
        } else {
            generateGremlinName();
        }
    }
    public abstract void battleChoices(DungeonCharacter opponent);
    public void generateOgreName() {
        int FName = HeroesVersusMonsters.randInt(0, 5);
        String[] firstNames = new String[]{"Owen", "Oliver", "Oscar", "Omar", "Otis", "Odin"};
        name = firstNames[FName] + " the Ogre";
    }

    public void generateSkeletonName() {
        int FName = HeroesVersusMonsters.randInt(0, 5);
        String[] firstNames = new String[]{"Solomon", "Sebastian", "Stephen", "Sheldon", "Shawn", "Stanley"};
        name = firstNames[FName] + " the Skeleton";
    }

    public void generateGremlinName() {
        int FName = HeroesVersusMonsters.randInt(0, 5);
        String[] firstNames = new String[]{"Gabriel", "Gregory", "Garrett", "Graham", "Greg", "Gerald"};
        name = firstNames[FName] + " the Gremlin";
    }

}
