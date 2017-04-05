import Guitars.*;
import Solos.*;

class GuitarHeroDemo {
	public static void main(String[] args) {
		GameCharacter player1 = new GameCharacter("Slash", new Fire(), new FenderTelecaster());
		GameCharacter player2 = new GameCharacter("Jimi Hendrix", new Smash(), new GibsonFlyingV());
		GameCharacter player3 = new GameCharacter("Angus Young", new Jump(), new GibsonSG());
		player1.playGuitar();
		player2.playGuitar();
		player3.playGuitar();
		player1.playSolo();
		player2.playSolo();
		player3.playSolo();
		player3.setGuitar(new GibsonFlyingV());
		player3.setSolo(new Fire());
		player3.playGuitar();
		player3.playSolo();
	}
}
