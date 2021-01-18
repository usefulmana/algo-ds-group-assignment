import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/*
 * You need to implement an algorithm to make guesses
 * for a 4-digits number in the method make_guess below
 * that means the guess must be a number between [1000-9999]
 * PLEASE DO NOT CHANGE THE NAME OF THE CLASS AND THE METHOD
 */
public class Guess {
    // Initializing necessary variables
    private static List<Integer> possibleAnswers = generateAllPossibleAnswers();
    private static List<Integer> impossibleAnswers = new LinkedList<>();
    private static int[][] counts = new int[10000][14];
    private static int[] entropy = new int[10000];
    private static int[] guessPool = new int[10000];
    private static int poolIndex = 0;
    private static boolean first = true;
    private static int currGuess = 0;

    public static int make_guess(int hits, int strikes) {

        // First guess
        int guess = 1123;
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
                Integer i = integerIterator.next();
                int[] scores = score(i, currGuess);
                // Compare scores
                if (scores[0] != strikes || scores[1] != hits) {
                    // Add to the impossible list for later use
                    impossibleAnswers.add(i);
                    // Remove if the scores do not match
                    integerIterator.remove();
                }
            }

            // Give a guess
            guess = guessWithMinimax();
            //guess = guessWithEntropy();
            // Update current guess;
            currGuess = guess;
        }
        return guess;
    }

    /**
     * A utility method to reset all static variables of the Guess class
     */
    public static void reset() {
        possibleAnswers = generateAllPossibleAnswers();
        impossibleAnswers = new LinkedList<>();
        counts = new int[10000][14];
        entropy = new int[10000];
        guessPool = new int[10000];
        poolIndex = 0;
        first = true;
        currGuess = 0;
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
     * Will generate a linked list of all possible integers within a give range from 1000 => 10000 (exclusive)
     *
     * @return a linked list of integers from 1000 => 9999
     */
    public static List<Integer> generateAllPossibleAnswers() {
        // Initialize an unordered set
        List<Integer> numbers = new LinkedList<>();
        // Add items to the set
        for (int i = 1000; i < 10000; i++) {
            numbers.add(i);
        }
        return numbers;
    }

    /**
     * Convert a bulls/cows score to a corresponding array index
     *
     * @param score: target score
     * @return: an array index
     */
    public static int convertScoreToIndex(int[] score) {
        // Calculate the sum of scores => Sum = 10*bulls + cows
        int sum = score[0] * 10 + score[1];
        int index = 0;
        switch (sum) {
            case 40:
                index = 0;
                break;
            case 30:
                index = 1;
                break;
            case 20:
                index = 2;
                break;
            case 21:
                index = 3;
                break;
            case 22:
                index = 4;
                break;
            case 10:
                index = 5;
                break;
            case 11:
                index = 6;
                break;
            case 12:
                index = 7;
                break;
            case 13:
                index = 8;
                break;
            case 0:
                index = 9;
                break;
            case 1:
                index = 10;
                break;
            case 2:
                index = 11;
                break;
            case 3:
                index = 12;
                break;
            case 4:
                index = 13;
                break;
        }
        return index;
    }

    /**
     * Guess a number using the entropy technique
     * Ref: https://en.wikipedia.org/wiki/Entropy_(information_theory)
     *
     * @return a 4-digit integer
     */
    private static int guessWithEntropy() {
        // Initialize necessary variables
        int minEntropy = (int) Math.pow(10, 9);
        int guess = 0;

        // Loop through all possible answers
        for (int i = 0; i < possibleAnswers.size(); i++) {

            // Initialize corresponding count array
            for (int j = 0; j < 14; j++) {
                counts[i][j] = 0;
            }

            // Starting counting all answers with the same score
            for (Integer possibleAnswer : possibleAnswers) {
                // Swap?
                int[] scr = score(possibleAnswer, possibleAnswers.get(i));
                // Save to the count array
                counts[i][convertScoreToIndex(scr)]++;
            }

            // Initialize corresponding entropy variable
            entropy[i] = 0;

            // Calculating entropy
            for (int j = 0; j < 14; j++) {
                int ctn = counts[i][j];
                if (ctn > 1) {
                    entropy[i] += ctn * Math.log(ctn);
                }
            }
            // If the current element less than or equal to the current min entropy
            if (entropy[i] <= minEntropy) {
                if (entropy[i] < minEntropy) {
                    // Reset pool index
                    poolIndex = 0;
                    minEntropy = entropy[i];
                }
                // Add the current elem to the guess po;;
                guessPool[poolIndex++] = possibleAnswers.get(i);
            }
        }
        // Choose the first guess from the pool
        guess = guessPool[0];
        return guess;
    }

    /**
     * Guess a number using the minimax technique
     * Ref: https://en.wikipedia.org/wiki/Mastermind_(board_game)
     *
     * @return a 4-digit integer
     */
    private static int guessWithMinimax() {

        // Initialize necessary variables
        int minimumEliminated = -1;
        int bestGuess = 0;

        // Initialize a new list with possibleAnswers' elements
        List<Integer> unused = new LinkedList<>(possibleAnswers);
        // Add impossible answers to the list
        unused.addAll(impossibleAnswers);

        // Loop through all elements
        for (Integer i : unused) {

            // Initialize a minimax table
            // This table will contain all the scores
            int[][] miniMaxTable = new int[5][5];

            // Check possible answers with the unused list elements
            for (Integer j : possibleAnswers) {
                // Calculate the scores
                // TODO SWAP i & j?
                int[] results = score(i, j);
                // Save scores
                miniMaxTable[results[1]][results[0]]++;
            }

            int mostHits = -1;

            // Loop through the table to find the most hits
            // Loop through row
            for (int[] row : miniMaxTable) {
                // Loop through columns
                for (int k : row) {
                    // Take either the most hits or the most strikes
                    mostHits = Integer.max(k, mostHits);
                }
            }

            // Calculate the score of a guess
            int scr = possibleAnswers.size() - mostHits;


            if (scr > minimumEliminated) {
                // If the score of the guess is greater than the min => it's the current best guess
                minimumEliminated = scr;
                bestGuess = i;
            }
        }
        return bestGuess;
    }

}
