public abstract class DungeonCharacter {
    protected String name;
    protected int hitPoints;
    protected int attackSpeed;
    protected int damageMin;
    protected int damageMax;
    protected double chanceToHit;
    protected int damageTaken = 0;

    public DungeonCharacter(String name, int hitPoints, int attackSpeed, int damageMax, int damageMin, double chanceToHit) {
        this.name = name;
        this.hitPoints = hitPoints;
        this.attackSpeed = attackSpeed;
        this.damageMax = damageMax;
        this.damageMin = damageMin;
        this.chanceToHit = chanceToHit;
    }

    public void addHitPoints(int HP) {
        if (HP <= 0) {
            Interface.show("Can only add a positive amount of HP");
        } else {
            hitPoints += HP;
        }
    }

    public void subtractHitPoints(int dmg, boolean showLogging) {
        if (dmg < 0) {
            if (showLogging) Interface.showln("Damage amount must be positive.");
        } else if (dmg > 0) {
            this.hitPoints -= dmg;
            this.damageTaken += dmg;
            if (this.hitPoints < 0) {
                this.hitPoints = 0;
            }
            if (showLogging) Interface.showln(getName() + " hit for " + dmg + " points damage.");
            if (showLogging) Interface.showln(getName() + " now has " + getHitPoints() + " hit points remaining.");
            if (showLogging) Interface.showln("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        }
        if (this.hitPoints == 0) {
            if (showLogging) Interface.showln(this.name + " has been killed");
        }
    }

    public void attack(DungeonCharacter c) {
        boolean canAttack = Math.random() <= this.chanceToHit;
        if (canAttack) {
            int dmg = HeroesVersusMonsters.randInt(this.damageMin, this.damageMax);
            c.subtractHitPoints(dmg, true);
        } else {
            Interface.showln(getName() + "'s attack on " + c.getName() + " failed!");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public int getDamageMin() {
        return damageMin;
    }

    public void setDamageMin(int damageMin) {
        this.damageMin = damageMin;
    }

    public int getDamageMax() {
        return damageMax;
    }

    public void setDamageMax(int damageMax) {
        this.damageMax = damageMax;
    }

    public double getchanceToHit() {
        return chanceToHit;
    }

    public void setchanceToHit(double chanceToHit) {
        this.chanceToHit = chanceToHit;
    }
}
