import observer.BadGuy;
import observer.EyeOfSauron;

public class TestSauronEye {
	public static void main(String[] args) {

		EyeOfSauron eye = new EyeOfSauron();
		BadGuy saruman = new BadGuy(eye, "Saruman");
		BadGuy witchKing = new BadGuy(eye, "Witch King");
		eye.setEnemies(1, 1, 2, 0); //hobbits, elves, dwarves, humans -- BTW, this is HORRIBLE coding style and a bad code smell
		//message should be displayed from Saruman and Angmar that they know about 1 hobbit, 1 elf, 2 dwarves

		saruman.defeated(); //Saruman is no longer registered with the Eye
		eye.setEnemies(4, 2, 2, 100);
		//only the Witch King reports on the enemies

	}//end main
}//end class