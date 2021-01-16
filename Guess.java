import java.util.*;

/*
 * You need to implement an algorithm to make guesses
 * for a 4-digits number in the method make_guess below
 * that means the guess must be a number between [1000-9999]
 * PLEASE DO NOT CHANGE THE NAME OF THE CLASS AND THE METHOD
 */
public class Guess {
    // Initializing necessary variables
    private static final Set<Integer> possibleAnswers = generateAllPossibleNumbers();
    private static final List<Integer> impossibleAnswers = new ArrayList<>();
    private static boolean first = true;
    private static int currGuess = 0;

    public static int make_guess(int hits, int strikes) {

        // First guess
        int guess = 1234;
        // Check if the current guess is the first guess
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
                Integer i =  integerIterator.next();
                int[] scores = score(i, currGuess);
                // Compare scores
                if (scores[0] != strikes || scores[1] != hits) {
                    // Add to impossible set for later usage
                    impossibleAnswers.add(i);
                    // Remove if the scores do not match
                    integerIterator.remove();
                }
            }

            // Give a guess
            guess = guessWithMinimax();
            // TODO Implement entropy method and compare with minimax
//            guess = guessWithEntropy();
            // Update current guess;
            currGuess = guess;
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
     * Will generate a set of all possible integers within a give range from 1000 => 10000 (exclusive)
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

    /**
     * Guess a number using the entropy technique
     * Ref: http://primepuzzle.com/tc/entropy.html
     * @return a 4-digit integer
     */
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

    /**
     * Guess a number using the minimax technique
     * Ref: https://en.wikipedia.org/wiki/Mastermind_(board_game)
     * @return a 4-digit integer
     */
    private static int guessWithMinimax() {

        // Initialize necessary variables
        int minimumEliminated = -1;
        int bestGuess = 0;

        // Initialize a new list with possibleAnswers' elements
        List<Integer> unused = new ArrayList<>(possibleAnswers);
        // Add impossible answers to the list
        unused.addAll(impossibleAnswers);

        // Loop through all elements
        for (Integer i : unused){

            // Initialize a minimax table
            // This table will contain all the scores
            int[][] miniMaxTable = new int[5][5];

            // Check possible answers with the unused list elements
            for (Integer j : possibleAnswers){
                // Calculate the scores
                int[] results = score(i, j);
                // Save scores
                miniMaxTable[results[1]][results[0]]++;
            }

            int mostHits = -1;

            // Loop through the table to find the most hits
            // Loop through row
            for (int[] row: miniMaxTable){
                // Loop through columns
                for (int k: row){
                    // Take either the most hits or the most strikes
                    mostHits = Integer.max(k, mostHits);
                }
            }

            // Calculate the score of a guess
            int scr = possibleAnswers.size() - mostHits;


            if (scr > minimumEliminated){
                // If the score of the guess is greater than the min => it's the current best guess
                minimumEliminated = scr;
                bestGuess = i;
            }
        }
        return bestGuess;
    }

}
