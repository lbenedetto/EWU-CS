package observer;

import java.util.Observable;
import java.util.Observer;

public class BadGuy implements Observer {
	private EyeOfSauron eye;
	private String name;

	public BadGuy(EyeOfSauron eye, String name) {
		this.eye = eye;
		eye.addObserver(this);
		this.name = name;
	}

	public void defeated() {
		eye.deleteObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		int[] enemies = (int[]) arg;
		System.out.print(name + " knows about ");
		String[] enemyNamesSingular = new String[]{"hobbit", "elf", "dwarf", "human"};
		String[] enemyNamesPlural = new String[]{"hobbits", "elves", "dwarves", "humans"};
		boolean isLast;
		for (int i = 0; i < 4; i++) {
			if (enemies[i] <= 0) continue;
			isLast = true;
			for (int j = i + 1; j < 4; j++)
				if (enemies[j] > 0) isLast = false;
			if (isLast) System.out.print("and ");
			System.out.print(enemies[i] + " ");
			System.out.print(enemies[i] == 1 ? enemyNamesSingular[i] : enemyNamesPlural[i]);
			System.out.print(isLast ? ".\n" : ", ");
		}
	}
}
