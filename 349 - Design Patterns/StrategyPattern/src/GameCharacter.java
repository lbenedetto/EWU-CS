import Guitars.Guitar;
import Solos.Solo;

class GameCharacter {
	private Solo solo;
	private Guitar guitar;
	private final String name;

	GameCharacter(String name, Solo solo, Guitar guitar) {
		this.name = name;
		this.solo = solo;
		this.guitar = guitar;
	}

	void playGuitar() {
		System.out.print(name);
		guitar.play();
	}

	void playSolo() {
		System.out.print(name);
		solo.play();
	}

	void setSolo(Solo solo) {
		this.solo = solo;
	}

	void setGuitar(Guitar guitar) {
		this.guitar = guitar;
	}
}
