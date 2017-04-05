public class ListTester {
    public static void main(String[] args) {
        new GUI();
    }
    public static Integer randomInteger(){
        //Decided to take a different approach to "random" numbers
        Integer out = (Integer)(int)System.currentTimeMillis();
        out *= System.identityHashCode(out);
        out = Math.abs(out);
        out /= 655435;
        if(out==0) out = randomInteger();
        return out;
    }

    /** returns a number guaranteed to be random
     *
     * @return int
     * @see "https://xkcd.com/221/"
     */
    public static int getRandomNumber(){
        return 4; // chosen by fair dice roll.
                  // guaranteed to be random.
    }
}
