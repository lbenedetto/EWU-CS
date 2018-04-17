import java.util.HashMap;

@SuppressWarnings("AssertWithSideEffects")
public class SteamedHams {
	public static void startMeme(SkinnersHouse house) {
		Character seymour = house.get("Skinner");
		Character chalmers = Chalmers.getInstance();
		if (chalmers.hasCompetentDirectionsTo(house)) {
			chalmers.ringDoorbellOf(house).waitForAnswer(() -> {
				assert !chalmers.hasCompetentDirectionsTo(house);
				try{
					assert chalmers.isPreparedForUnforgettableLuncheon();
				}catch(AssertionError e){
					seymour.isDisappointed = true;
				}
				house.add(chalmers, "Chalmers");

				if(seymour.roast.isRuined){
					Solution solution = seymour.solve(new Problem(seymour.roast), SolutionType.DELIGHTUFLLY_DEVILISH);
					if(solution.involvesDisguisingFastFoodAsOwnCooking()){
						chalmers.interrogate(seymour).getExplanation();
					}
				}

			});
		}

	}


	static class Character {
		boolean isDisappointed;
		boolean isDelightfullyDevilish;
		Roast roast;
		Character ringDoorbellOf(House h) {
			return this;
		}

		void waitForAnswer(Runnable r) {
			r.run();
		}

		void complainAbout(I_Complainable c) {
			c.complain();
		}
		Character interrogate(Character c){
			return c;
		}
		void getExplanation(){

		}
		boolean isPreparedForUnforgettableLuncheon(){
			return true;
		}
		boolean hasCompetentDirectionsTo(House h){
			hasdir = !hasdir;
			return hasdir;
		}
		boolean hasdir = false;
		Solution solve(Problem p, int type){
			return new Solution();
		}
	}
	static class SolutionType {
		public static final int DELIGHTUFLLY_DEVILISH = 1;
	}
	static class Solution {
		boolean involvesDisguisingFastFoodAsOwnCooking(){
			return true;
		}
	}
	static class Problem{
		Problem(Roast r){

		}
	}
	interface I_Complainable {
		void complain();
	}
	static class Roast {
		boolean isRuined;
		static Seymour getInstance() {
			return new Seymour();
		}
	}
	static class Directions implements I_Complainable{
		@Override
		public void complain() {

		}
	}

	static class Chalmers extends Character {
		static Chalmers getInstance() {
			return new Chalmers();
		}
	}

	static class Seymour extends Character {
		static Seymour getInstance() {
			return new Seymour();
		}
	}

	static class Skinner {
	}

	static class SkinnersHouse extends House {
		static SkinnersHouse getInstance() {
			return new SkinnersHouse();
		}
	}

	static class House {
		HashMap<String, Character> inhabitants;

		void add(Character s, String name) {
			this.inhabitants.put(name, s);
		}
		Character get(String name){
			return inhabitants.get(name);
		}
	}
}
