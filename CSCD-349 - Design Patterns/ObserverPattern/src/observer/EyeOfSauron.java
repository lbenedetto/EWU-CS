package observer;

import java.util.Observable;

public class EyeOfSauron extends Observable {
	private int[] enemies;

	public EyeOfSauron() {
		enemies = new int[4];
	}

	public void setEnemies(int hobbits, int elves, int dwarves, int humans) {
		enemies[0] = hobbits;
		enemies[1] = elves;
		enemies[2] = dwarves;
		enemies[3] = humans;
		setChanged();
		notifyObservers(enemies);
	}
}
