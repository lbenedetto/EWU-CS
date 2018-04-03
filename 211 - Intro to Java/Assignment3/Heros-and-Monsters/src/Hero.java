public abstract class Hero extends DungeonCharacter {
    protected double chanceToBlock;
    protected int remainingTurns;

    public Hero(String name, int hitPoints, int attackSpeed, int damageMax, int damageMin, double chanceToHit, double chanceToBlock) {
        super(name, hitPoints, attackSpeed, damageMax, damageMin, chanceToHit);
        this.chanceToBlock = chanceToBlock;
        readName();
    }

    public void readName() {
        if (this.name.equalsIgnoreCase("ForgetfulIdiot")) {
            name = generateIdiotName();
        } else {
            Interface.show("Enter character name: ");
            name = Interface.readString();
        }
    }

    public boolean defend() {
        return Math.random() <= this.chanceToBlock;
    }

    public void subtractHitPoints(int hitPoints) {
        if (defend()) {
            Interface.showln(this.name + " BLOCKED the attack!");
        } else {
            super.subtractHitPoints(hitPoints, true);
        }
    }

    public String generateIdiotName() {
        int FName = HeroesVersusMonsters.randInt(0, 34);
        int LName = HeroesVersusMonsters.randInt(0, 13);
        String[] firstNames = new String[]{"Jethro", "Obadiah", "Eli", "Enoch", "Amos", "Lloyd", "Otis", "Virgil", "Ace", "Verne",
                "Duke", "Silas", "Wyatt", "Harlan", "Roy", "Wilfred", "Roscoe", "Beau", "Orville", "Quentin", "Merle", "Lester",
                "Arlo", "Gomer", "Hoyt", "Wade", "Abner", "Burl", "Cyrus", "Winslow", "Cletus", "Earl", "Tobias", "Abel", "Jonah"};
        String[] lastNames = new String[]{"Montgomery", "Jackson", "Davenport", "Lee", "Sterling", "Holliston", "Grayson",
                "Stafford", "Stansfield", "Holt", "Hayes", "Coleman", "Broderick", "Colton"};
        return firstNames[FName] + " " + lastNames[LName];
    }

    public void subtractHitPointsSilently(int dmg) {
        super.subtractHitPoints(dmg, false);
    }

    public void battleChoices(DungeonCharacter opponent) {
        this.remainingTurns = (this.attackSpeed / opponent.getAttackSpeed());
        if (this.remainingTurns == 0) {
            this.remainingTurns += 1;
        }
        Interface.showln("Number of turns this round is: " + this.remainingTurns);
    }

    public double getChanceToBlock() {
        return chanceToBlock;
    }

    public void setChanceToBlock(double chanceToBlock) {
        this.chanceToBlock = chanceToBlock;
    }

}
