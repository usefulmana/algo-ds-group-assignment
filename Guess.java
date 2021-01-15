import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 * You need to implement an algorithm to make guesses
 * for a 4-digits number in the method make_guess below
 * that means the guess must be a number between [1000-9999]
 * PLEASE DO NOT CHANGE THE NAME OF THE CLASS AND THE METHOD
 */
public class Guess {
    private static final Set<Integer> possibleAnswers = generateAllPossibleNumbers();
    private static boolean first = true;
    private static int currGuess = 0;

    public static int make_guess(int hits, int strikes) {

        int guess = 0;

        if (first) {
            // First guess
			guess = 1122;
			currGuess = guess;
            first = false;
        } else {
            // Pruning possible answers
			Iterator<Integer> integerIterator = possibleAnswers.iterator();
			while (integerIterator.hasNext()){
				int[] scores = score(integerIterator.next(), currGuess);
				if (scores[0] != strikes || scores[1] != hits) {
					integerIterator.remove();
				}
			}

            // Choose a random number
            for (Integer i : possibleAnswers) {
                guess = i;
                currGuess = guess;
                break;
            }
        }

        return guess;
    }

    /**
     * PROBLEM IS HERE? IS THIS EVEN LEGAL?
     *
     * @param target
     * @param guess
     * @return
     */
    public static int[] score(int target, int guess) {
        // IS THIS EVEN LEGAL IN THE COMPETITION?
        char des[] = Integer.toString(target).toCharArray();
        char src[] = Integer.toString(guess).toCharArray();
        int hits = 0;
        int strikes = 0;

        // process strikes
        for (int i = 0; i < 4; i++) {
            if (src[i] == des[i]) {
                strikes++;
                des[i] = 'a';
                src[i] = 'a';
            }
        }
        // process hits
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (src[i] != 'a') {
                    if (src[i] == des[j]) {
                        hits++;
                        des[j] = 'a';
                        break;
                    }
                }
            }
        }
        int[] scores = new int[2];
        scores[0] = strikes;
        scores[1] = hits;
        return scores;
    }

    public static Set<Integer> generateAllPossibleNumbers() {
        Set<Integer> numbers = new HashSet<>();
        for (int i = 1000; i < 10000; i++) {
            numbers.add(i);
        }
		System.out.printf("BIGGGGGGGGGGGGGGGG %d\n",numbers.size());
        return numbers;
    }

    private static int guessWithEntropy() {
    	// TODO INCOMPLETE
        int minEntropy = (int) Math.pow(10, 9);
        int currEntropy = 0;
        for (Integer i : possibleAnswers) {
            currEntropy += Math.log(1 / possibleAnswers.size()) * possibleAnswers.size() * (-1);

            if (currEntropy > minEntropy) {
                return currEntropy;
            }
        }
        minEntropy = currEntropy;
        return currEntropy;
    }

    private static int guessWithMinimax() {
        // TODO
        return 0;
    }

}
