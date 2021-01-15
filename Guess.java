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
    // Initializing necessary variables
    private static final Set<Integer> possibleAnswers = generateAllPossibleNumbers();
    private static boolean first = true;
    private static int currGuess = 0;

    public static int make_guess(int hits, int strikes) {

        // First guess
        int guess = 1234;
        // Check if this is the first guess
        if (first) {
            // Assign currGuess to guess
            currGuess = guess;
            // Disable the first flag
            first = false;
        } else {
            // Pruning possible answers
            // Remove any number from the set that would not give the same response if it (the guess) were the target.
            Iterator<Integer> integerIterator = possibleAnswers.iterator();
            while (integerIterator.hasNext()) {
                int[] scores = score(integerIterator.next(), currGuess);
                if (scores[0] != strikes || scores[1] != hits) {
                    integerIterator.remove();
                }
            }

            // Give a guess
            // Choose a random number
            // TODO Need to replace this method with something else
            // TODO Minimax or Entropy?
            for (Integer i : possibleAnswers) {
                guess = i;
                currGuess = guess;
                break;
            }
        }
        return guess;
    }

    /**
     * A method score the guess internally
     *
     * @param target: target number
     * @param guess:  the guess
     * @return: an int array with two elements => strikes and hits respectively
     */
    public static int[] score(int target, int guess) {
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

    /**
     * Will generate a possible number within a give range from 1000 => 10000 (exclusive)
     *
     * @return an ordered set of integers from 1000 => 9999
     */
    public static Set<Integer> generateAllPossibleNumbers() {
        // Initialize an unordered set
        Set<Integer> numbers = new HashSet<>();
        // Add items to the set
        for (int i = 1000; i < 10000; i++) {
            numbers.add(i);
        }
        return numbers;
    }

    private static int guessWithEntropy() {
        // TODO
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
